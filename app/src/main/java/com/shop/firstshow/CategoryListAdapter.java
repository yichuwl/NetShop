package com.shop.firstshow;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author victory
 * @time 2018/6/27
 * @about
 */
public class CategoryListAdapter extends BaseAdapter {
    Context mContext;

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public void setCheckedPosition(int checkedPosition) {
        this.checkedPosition = checkedPosition;
    }

    int checkedPosition = 0;

    List<String> dataList = new ArrayList<>();

    public CategoryListAdapter(Context context, List<String> dataList) {
        mContext = context;
        this.dataList = dataList;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.text, null);
        TextView text = convertView.findViewById(R.id.text_only);
        text.setTextSize(17);
        text.setMinHeight(60);
        text.setGravity(Gravity.CENTER);
        if (position == checkedPosition) {
            text.setTextColor(mContext.getResources().getColor(R.color.dark_red));
        } else {
            text.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        text.setText(dataList.get(position));
        return convertView;
    }
}
