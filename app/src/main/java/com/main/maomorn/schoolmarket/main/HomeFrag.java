package com.main.maomorn.schoolmarket.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.main.maomorn.schoolmarket.R;
import com.main.maomorn.tools.BannerLayout;
import com.main.maomorn.tools.DIYScrollView;
import com.main.maomorn.tools.SearchBar;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by MaoMorn on 2017-04-11.
 */

public class HomeFrag extends Fragment {
    private SearchBar mSearchBar;
    private DIYScrollView mScrollView;
    private BannerLayout mBannerLayout;

    private TextView title1;
    private ImageView module1_1;
    private ImageView module1_2;
    private ImageView module1_3;
    private ImageView module1_4;

    private TextView title2;
    private ImageView module2_1;
    private ImageView module2_2;
    private ImageView module2_3;
    private ImageView module2_4;

    private TextView title3;
    private ImageView module3_1;
    private ImageView module3_2;
    private ImageView module3_3;
    private ImageView module3_4;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_home, container, false);
        initView(view);




        setBannerBar();

        initListener();


        return view;
    }

    private void setBannerBar(){
        final List<Integer> resIds = new ArrayList<>();
        resIds.add(R.drawable.banner1);
        resIds.add(R.drawable.banner2);
        resIds.add(R.drawable.banner3);
        resIds.add(R.drawable.banner4);
        mBannerLayout.setViewResIds(resIds);
        //添加监听事件
        mBannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getActivity(), String.valueOf(position), LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view){
        mSearchBar = (SearchBar) view.findViewById(R.id.search_bar);
        mScrollView=(DIYScrollView)view.findViewById(R.id.scrollView);
        mBannerLayout = (BannerLayout) view.findViewById(R.id.banner);

        title1=(TextView)view.findViewById(R.id.module1_title);
        module1_1=(ImageView)view.findViewById(R.id.module1_1);
        module1_2=(ImageView)view.findViewById(R.id.module1_2);
        module1_3=(ImageView)view.findViewById(R.id.module1_3);
        module1_4=(ImageView)view.findViewById(R.id.module1_4);

        title2=(TextView)view.findViewById(R.id.module2_title);
        module2_1=(ImageView)view.findViewById(R.id.module2_1);
        module2_2=(ImageView)view.findViewById(R.id.module2_2);
        module2_3=(ImageView)view.findViewById(R.id.module2_3);
        module2_4=(ImageView)view.findViewById(R.id.module2_4);

        title3=(TextView)view.findViewById(R.id.module3_title);
        module3_1=(ImageView)view.findViewById(R.id.module3_1);
        module3_2=(ImageView)view.findViewById(R.id.module3_2);
        module3_3=(ImageView)view.findViewById(R.id.module3_3);
        module3_4=(ImageView)view.findViewById(R.id.module3_4);
    }

    private void initListener(){

        title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        title3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module3_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        module3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
