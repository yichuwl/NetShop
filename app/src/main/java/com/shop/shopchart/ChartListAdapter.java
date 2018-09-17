package com.shop.shopchart;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;

import com.shop.R;
import com.shop.util.MeasureListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/6/27
 * @about
 */
public class ChartListAdapter extends BaseAdapter {
    Context mContext;
    List<Map<String, String>> dataList;

    Map<Integer, Boolean> groupCheckMap = new HashMap<>();

    public ChartListAdapter(Context context, List<Map<String, String>> list) {
        mContext = context;
        dataList = list;
        for (int i = 0; i < list.size(); i++) {
            groupCheckMap.put(i, false);
        }
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

    boolean isOnBind = false;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_group_chart, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
       // final ChartGoodsListAdapter adapter = new ChartGoodsListAdapter(mContext, null,position);
       /* viewHolder.mChildList.setAdapter(adapter);
        viewHolder.checkButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                groupCheckMap.put(position, isChecked);
                adapter.setGroupCheck(viewHolder.checkButton);
            }
        });*/
        isOnBind = true;
        viewHolder.checkButton.setChecked(groupCheckMap.get(position));
        isOnBind = false;
        return convertView;
    }

    class ViewHolder {
        CheckBox checkButton;
        ListView mChildList;

        public ViewHolder(View rootView) {
            checkButton = rootView.findViewById(R.id.rdb_group_chart);
            mChildList = rootView.findViewById(R.id.list_child_chart);
        }
    }
}
