package com.shop.goods;

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
 * @time 2018/6/29
 * @about
 */
public class CommentListAdapter extends BaseAdapter {
    public CommentListAdapter(Context context) {
        mContext = context;
    }

    Context mContext;

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    List<Map<String, Object>> dataList = new ArrayList<>();

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
        Map<String, Object> map = dataList.get(position);
        View view = convertView;
        ViewHolder viewHolder = null;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_list, null, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTxtUsernameCommentlist.setText(map.get("RATE_USERNAME") + "");
        viewHolder.mTxtContentCommentlist.setText(map.get("RATE_CONTENT") + "");
        float rateScore = Float.valueOf(map.get("RATE_SCORE") + "");
        StringBuilder builder = new StringBuilder(); //.append(mContext.getString(R.string.commentrate))
        if (rateScore < 3f) {
            builder.append(mContext.getString(R.string.badcomment));
        } else if (rateScore < 5f) {
            builder.append(mContext.getString(R.string.middlecomment));
        } else {
            builder.append(mContext.getString(R.string.goodcomment));
        }
        builder.append("   ");
        String rateAddtime = builder.append(map.get("RATE_ADDTIME")).toString();
        viewHolder.dateTxt.setText(rateAddtime);
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
