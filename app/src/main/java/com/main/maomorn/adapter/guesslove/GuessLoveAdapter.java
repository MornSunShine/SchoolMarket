package com.main.maomorn.adapter.guesslove;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.main.maomorn.schoolmarket.R;

import java.util.List;

/**
 * Created by MaoMorn on 2017-04-30.
 */

public class GuessLoveAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<GuessLoveItem> mList;

    public GuessLoveAdapter(Context context){
        mContext=context;
    }

    public GuessLoveAdapter(int[] imgRes, Context context){
        mContext=context;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GuessLoveItem temp;
        if(null==convertView){
            convertView=new GuessLoveItem(mContext);
            temp=(GuessLoveItem) convertView;
            convertView.setTag(temp);
        }else{
            temp=(GuessLoveItem)convertView.getTag();
        }
        temp.setGoodsImage(R.drawable.background);
        return convertView;
    }
}
