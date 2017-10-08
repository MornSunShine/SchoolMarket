package com.main.maomorn.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.main.maomorn.schoolmarket.R;
import com.main.maomorn.until.Utils;

import java.security.InvalidParameterException;

/**
 * 拉动更新视图
 * Created by MaoMorn on 2017/3/5.
 */

public class PullToRefreshView extends ViewGroup {

    private static final int DRAG_MAX_DISTANCE = 120;//拖拽的最大距离
    private static final float DRAG_RATE = 0.5f;//拖拽的速率
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    public static final int STYLE_SUN = 0;

    public static final int MAX_OFFSET_ANIMATION_DURATION = 700;//刷新动画持续的最长时间

    private static final int INVALID_POINTER = -1;

    private View mTarget;//目标视图
    private ImageView mRefreshView;//刷新视图
    private Interpolator mDecelerateInterpolator;//动画变化率
    private int mTouchSlop;//达到滑动刷新要求的最小距离
    private int mTotalDragDistance;//总拖拽距离
    private BaseRefreshView mBaseRefreshView;//基础刷新视图
    private float mCurrentDragPercent;//当前拖拽程度
    private int mCurrentOffsetTop;//当前距顶部偏移值
    private boolean mRefreshing;//更新状态
    private int mActivePointerId;
    private boolean mIsBeingDragged;//拖拽状态
    private float mInitialMotionY;
    private int mFrom;
    private float mFromDragPercent;
    private boolean mNotify;
    private OnRefreshListener mListener;

    private int mTargetPaddingTop;
    private int mTargetPaddingBottom;
    private int mTargetPaddingRight;
    private int mTargetPaddingLeft;

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public PullToRefreshView(Context context) {
        this(context, null);
    }

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*属性设置*/
        TypedArray a = context.
                obtainStyledAttributes(attrs, R.styleable.RefreshView);
        final int type = a.getInteger(R.styleable.RefreshView_type, STYLE_SUN);
        a.recycle();//更新属性
        /*变量赋值*/
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mTotalDragDistance = Utils.dpToPixel(context, DRAG_MAX_DISTANCE);

        mRefreshView = new ImageView(context);

        setRefreshStyle(type);

        addView(mRefreshView);

        setWillNotDraw(false);
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
    }

    /**
     * 设置更新样式
     *
     * @param type 样式的枚举值
     */
    public void setRefreshStyle(int type) {
        setRefreshing(false);
        switch (type) {
            case STYLE_SUN:
                mBaseRefreshView = new SunRefreshView(getContext(), this);
                break;
            default:
                throw new InvalidParameterException("Type does not exist");
        }
        mRefreshView.setImageDrawable(mBaseRefreshView);
    }

    /**
     * 设置更新视图(程序)的内间距
     *
     * @param left   左内间距
     * @param top    上内间距
     * @param right  右内间距
     * @param bottom 下内间距
     */
    public void setRefreshViewPadding(int left, int top, int right, int bottom) {
        mRefreshView.setPadding(left, top, right, bottom);
    }

    /**
     * 获取总拖拽距离
     *
     * @return 总拖拽距离
     */
    public int getTotalDragDistance() {
        return mTotalDragDistance;
    }

    /**
     * 目标视图尺寸重设
     * @param widthMeasureSpec  测量宽度
     * @param heightMeasureSpec 测量高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ensureTarget();
        if (mTarget == null)
            return;

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(//宽度有界限
                getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(//高度有界限
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
        mRefreshView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 确认目标
     */
    private void ensureTarget() {
        if (mTarget != null)
            return;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mRefreshView) {
                    mTarget = child;
                    mTargetPaddingBottom = mTarget.getPaddingBottom();
                    mTargetPaddingLeft = mTarget.getPaddingLeft();
                    mTargetPaddingRight = mTarget.getPaddingRight();
                    mTargetPaddingTop = mTarget.getPaddingTop();
                    return;//得到目标则确认退出
                }
            }
        }
    }

    /**
     * 触摸拦截活动
     * @param ev 活动对象
     * @return 是否继续下传触摸事件,true-拦截不下传,false-继续向子控件下传
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!isEnabled() || canChildScrollUp() || mRefreshing) {
            return false;
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setTargetOffsetTop(0, true);
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionY == -1) {
                    return false;
                }
                mInitialMotionY = initialMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                final float yDiff = y - mInitialMotionY;
                if (yDiff > mTouchSlop && !mIsBeingDragged) {
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }
        return mIsBeingDragged;
    }

    /**
     * 触摸事件处理
     * @param ev 活动对象
     * @return 是否重复处理触摸事件，true-传递至父控件重复处理,false-不向上传递
     */
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {

        if (!mIsBeingDragged) {
            return super.onTouchEvent(ev);
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float yDiff = y - mInitialMotionY;
                final float scrollTop = yDiff * DRAG_RATE;
                mCurrentDragPercent = scrollTop / mTotalDragDistance;
                if (mCurrentDragPercent < 0) {
                    return false;
                }
                float boundedDragPercent = Math.min(1f, Math.abs(mCurrentDragPercent));
                float extraOS = Math.abs(scrollTop) - mTotalDragDistance;
                float slingshotDist = mTotalDragDistance;
                float tensionSlingshotPercent = Math.max(0,
                        Math.min(extraOS, slingshotDist * 2) / slingshotDist);
                float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                        (tensionSlingshotPercent / 4), 2)) * 2f;
                float extraMove = (slingshotDist) * tensionPercent / 2;
                int targetY = (int) ((slingshotDist * boundedDragPercent) + extraMove);

                mBaseRefreshView.setPercent(mCurrentDragPercent, true);
                setTargetOffsetTop(targetY - mCurrentOffsetTop, true);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN:
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                mIsBeingDragged = false;
                if (overScrollTop > mTotalDragDistance) {
                    setRefreshing(true, true);
                } else {
                    mRefreshing = false;
                    animateOffsetToStartPosition();
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
        }
        return true;
    }

    /**
     *由开始处偏移的动画设置
     */
    private void animateOffsetToStartPosition() {
        mFrom = mCurrentOffsetTop;
        mFromDragPercent = mCurrentDragPercent;
        long animationDuration = Math.abs((long) (MAX_OFFSET_ANIMATION_DURATION * mFromDragPercent));

        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(animationDuration);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToStartPosition.setAnimationListener(mToStartListener);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToStartPosition);
    }

    /**
     *到达指定偏移位置的刷新动画设置
     */
    private void animateOffsetToCorrectPosition() {
        mFrom = mCurrentOffsetTop;
        mFromDragPercent = mCurrentDragPercent;

        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(MAX_OFFSET_ANIMATION_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToCorrectPosition);

        if (mRefreshing) {
            mBaseRefreshView.start();
            if (mNotify) {
                if (mListener != null) {
                    mListener.onRefresh();
                }
            }
        } else {
            mBaseRefreshView.stop();
            animateOffsetToStartPosition();
        }
        mCurrentOffsetTop = mTarget.getTop();
        mTarget.setPadding(mTargetPaddingLeft, mTargetPaddingTop, mTargetPaddingRight, mTotalDragDistance);
    }

    /**
     *
     */
    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    /**
     *
     */
    private final Animation mAnimateToCorrectPosition = new Animation() {
        /**
         *实现动画的变换，在动画结束前会不断调用
         *
         * @param interpolatedTime 动画运行的时间(0.0-1.0)
         * @param t 当前的变换效果的属性值
         */
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop;
            int endTarget = mTotalDragDistance;
            targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mTarget.getTop();

            mCurrentDragPercent = mFromDragPercent - (mFromDragPercent - 1.0f) * interpolatedTime;
            mBaseRefreshView.setPercent(mCurrentDragPercent, false);

            setTargetOffsetTop(offset, false /* requires update */);
        }
    };

    /**
     *
     * @param interpolatedTime
     */
    private void moveToStart(float interpolatedTime) {
        int targetTop = mFrom - (int) (mFrom * interpolatedTime);
        float targetPercent = mFromDragPercent * (1.0f - interpolatedTime);
        int offset = targetTop - mTarget.getTop();

        mCurrentDragPercent = targetPercent;
        mBaseRefreshView.setPercent(mCurrentDragPercent, true);
        mTarget.setPadding(mTargetPaddingLeft, mTargetPaddingTop, mTargetPaddingRight, mTargetPaddingBottom + targetTop);
        setTargetOffsetTop(offset, false);
    }

    /**
     *
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing) {
        if (mRefreshing != refreshing) {
            setRefreshing(refreshing, false /* notify */);
        }
    }

    /**
     *
     * @param refreshing
     * @param notify
     */
    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                mBaseRefreshView.setPercent(1f, true);
                animateOffsetToCorrectPosition();
            } else {
                animateOffsetToStartPosition();
            }
        }
    }

    /**
     *
     */
    private Animation.AnimationListener mToStartListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        /**
         *
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            mBaseRefreshView.stop();
            mCurrentOffsetTop = mTarget.getTop();
        }
    };

    /**
     *
     * @param ev
     */
    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    /**
     * 获取触摸点纵坐标
     * @param ev 事件对象
     * @param activePointerId 活动手势的id
     * @return 手势的纵坐标
     */
    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    /**
     * 设置偏移距离和更新权限
     *
     * @param offset         偏移距离
     * @param requiresUpdate 是否更新
     */
    private void setTargetOffsetTop(int offset, boolean requiresUpdate) {
        mTarget.offsetTopAndBottom(offset);
        mBaseRefreshView.offsetTopAndBottom(offset);
        mCurrentOffsetTop = mTarget.getTop();
        if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
            invalidate();
        }
    }

    /**
     *子控件是否能滑动
     * @return 子控件滑动权限，true-可以，false-不可以
     */
    private boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    /**
     * 决定目标视图在ViewGroup中的位置
     * @param changed 是否改变位置
     * @param l 子控件左上角距离ViewGroup左上角水平距离
     * @param t 子控件左上角距离ViewGroup左上角竖直距离
     * @param r 子控件右下角距离ViewGroup左上角水平距离
     * @param b 子控件右下角距离ViewGroup左上角竖直距离
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        ensureTarget();
        if (mTarget == null)
            return;

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

        mTarget.layout(left, top + mCurrentOffsetTop, left + width - right, top + height - bottom + mCurrentOffsetTop);
        mRefreshView.layout(left, top, left + width - right, top + height - bottom);
    }

    /**
     * 设置刷新监听器
     * @param listener 刷新监听器对象
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 刷新监听器
     */
    public interface OnRefreshListener {
        void onRefresh();//刷新行为
    }

}
