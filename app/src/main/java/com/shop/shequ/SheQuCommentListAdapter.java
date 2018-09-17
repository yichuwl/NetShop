package com.shop.shequ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shop.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/7/4
 * @about
 */
public class SheQuCommentListAdapter extends BaseAdapter {

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    List<Map<String, Object>> dataList = new ArrayList<>();
    Context mContext;

    public SheQuCommentListAdapter(Context context, List<Map<String, Object>> data) {
        mContext = context;
        dataList = data;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list, null, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Map<String, Object> map = dataList.get(position);
        viewHolder.mTxtUsernameCommentlist.setText(map.get("NEWSRATE_USERNAME") + "");
        viewHolder.mTxtContentCommentlist.setText(map.get("NEWSRATE_CONTENT") + "");
        viewHolder.dateTxt.setText(map.get("NEWSRATE_ADDTIME") + "");
        return view;
    }

    static class ViewHolder {
        protected TextView mTxtUsernameCommentlist;
        protected TextView mTxtContentCommentlist, dateTxt;

        ViewHolder(View rootView) {
            initView(rootView);
        }

        private void initView(View rootView) {
            mTxtUsernameCommentlist = (TextView) rootView.findViewById(R.id.txt_username_commentlist);
            mTxtContentCommentlist = (TextView) rootView.findViewById(R.id.txt_content_commentlist);
            dateTxt = rootView.findViewById(R.id.txt_commentdate);
        }
    }
}
