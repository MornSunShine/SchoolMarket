package com.main.maomorn.tools.tagcloudlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.main.maomorn.schoolmarket.R;

import java.util.List;

/**
 * @author fyales
 * @since 8/26/15.
 */
public class TagBaseAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;

    public TagBaseAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tools_tagview, null);
            holder = new ViewHolder();
            holder.tagText = (TextView) convertView.findViewById(R.id.tag);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        final String text = getItem(position);
        holder.tagText.setText(text);
        return convertView;
    }

    static class ViewHolder {
        TextView tagText;
    }
}
