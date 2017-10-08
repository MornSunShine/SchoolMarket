package com.main.maomorn.schoolmarket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.maomorn.adapter.guesslove.GuessLoveAdapter;
import com.main.maomorn.bean.LoveBean;
import com.main.maomorn.tools.BannerLayout;
import com.main.maomorn.tools.ColorArcProgressBar;
import com.main.maomorn.tools.ExpandableTextView;
import com.main.maomorn.tools.SearchBar;
import com.main.maomorn.tools.materialratingbar.MaterialRatingBar;
import com.main.maomorn.tools.tagcloudlayout.TagBaseAdapter;
import com.main.maomorn.tools.tagcloudlayout.TagCloudLayout;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by MaoMorn on 2017-04-21.
 */

public class GoodsDetailsAct extends AppCompatActivity {
    private LoveBean loveBean;

    private SearchBar searchBar;
    private BannerLayout banner;
    private ImageView love;
    private TextView loveNum;
    private TextView goodsName;
    private TextView goodsPrice;
    private ExpandableTextView goodsIntroduction;
    private MaterialRatingBar ratingBar;
    private ColorArcProgressBar browseNum;

    private TagCloudLayout mContainer;
    private TagBaseAdapter mAdapter;
    private List<String> mList;

    private GridView mGridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_details);
        initView();
        setBannerBar();

        mList = new ArrayList<>();
        mList.add("潮流");
        mList.add("高品质");
        mList.add("好用");
        mAdapter = new TagBaseAdapter(this,mList);
        mContainer.setAdapter(mAdapter);

        mContainer.setItemClickListener(new TagCloudLayout.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                Toast.makeText(GoodsDetailsAct.this,mList.get(position),Toast.LENGTH_SHORT).show();
            }
        });

        goodsIntroduction.setText("学姐大三的时候买上了 跟朋友玩了没有超过10次 现在学姐要毕业啦 所以想转让 没有任何质量问题 可以现场验货" +
                " 学姐大三的时候买上了 跟朋友玩了没有超过10次 现在学姐要毕业啦 所以想转让 没有任何质量问题 可以现场验货 " +
                "学姐大三的时候买上了 跟朋友玩了没有超过10次 现在学姐要毕业啦 所以想转让 没有任何质量问题 可以现场验货");
        initListener();
    }

    private void initView(){
        loveBean=new LoveBean(220,true);
        searchBar=(SearchBar)findViewById(R.id.search_bar);
        banner=(BannerLayout)findViewById(R.id.banner);
        browseNum=(ColorArcProgressBar)findViewById(R.id.browse_number);
        goodsName=(TextView)findViewById(R.id.goods_name);
        goodsPrice=(TextView)findViewById(R.id.goods_price);
        goodsIntroduction=(ExpandableTextView)findViewById(R.id.goods_introduction);
        ratingBar=(MaterialRatingBar)findViewById(R.id.ratingbar);

        love=(ImageView)findViewById(R.id.love_img);
        loveNum=(TextView)findViewById(R.id.love_num);

        mContainer = (TagCloudLayout) findViewById(R.id.container);

        mGridView=(GridView)findViewById(R.id.guess_love_container);
        mGridView.setAdapter(new GuessLoveAdapter(this));

        loveNum.setText(""+loveBean.getNumLove());
        if(loveBean.isFocusedLove()){
            love.setImageResource(R.drawable.icon_love_filled);
        }else{
            love.setImageResource(R.drawable.icon_love_empty);
        }
    }

    private void setBannerBar(){
        final List<Integer> resIds = new ArrayList<>();
        resIds.add(R.drawable.banner1);
        resIds.add(R.drawable.banner2);
        resIds.add(R.drawable.banner3);
        resIds.add(R.drawable.banner4);
        banner.setViewResIds(resIds);
        //添加监听事件
        banner.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(GoodsDetailsAct.this, String.valueOf(position), LENGTH_SHORT).show();
            }
        });
    }

    private void initListener(){
        browseNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseNum.setTargetValues(50f,1000);
            }
        });
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loveBean.isFocusedLove()){
                    love.setImageResource(R.drawable.icon_love_empty);
                    loveBean.changeLoveState();
                    loveNum.setText(""+loveBean.getNumLove());
                }else{
                    love.setImageResource(R.drawable.icon_love_filled);
                    loveBean.changeLoveState();
                    loveNum.setText(""+loveBean.getNumLove());
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

}
