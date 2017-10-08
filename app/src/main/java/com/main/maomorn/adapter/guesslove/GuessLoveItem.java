package com.main.maomorn.adapter.guesslove;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.main.maomorn.schoolmarket.R;

/**
 * Created by MaoMorn on 2017-04-30.
 */

public class GuessLoveItem extends RelativeLayout{
    private Context mContext;
    private ImageView mImg;
    private TextView mTitle;
    private TextView mContent;
    private TextView mPrice;

    public GuessLoveItem(Context context) {
        this(context,null);
    }

    public GuessLoveItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initView();
    }
    private void initView(){
        View view= LayoutInflater.
                from(mContext).inflate(R.layout.tools_loveitem,this,true);
        mImg=(ImageView)view.findViewById(R.id.item_img);
        mTitle=(TextView)view.findViewById(R.id.item_title);
        mContent=(TextView)view.findViewById(R.id.item_introduction);
        mPrice=(TextView)view.findViewById(R.id.price);
    }

    public void setGoodsImage(int imgRes){
        mImg.setImageResource(imgRes);
    }
    public void setTiTle(CharSequence title){
        mTitle.setText(title);
    }

    public void setPrice(CharSequence price){
        mPrice.setText(price);
    }

    public void setContent(CharSequence content){
        mContent.setText(content);
    }
}
