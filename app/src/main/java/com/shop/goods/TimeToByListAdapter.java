package com.shop.goods;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.util.FactoryUtils;
import com.shop.util.GlideUtils;
import com.shop.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/7/3
 * @about
 */
public class TimeToByListAdapter extends BaseAdapter {

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    List<Map<String, Object>> dataList = new ArrayList<>();
    Context mContext;

    public void setNotStart(boolean notStart) {
        isNotStart = notStart;
    }

    boolean isNotStart = true;

    public void setListener(FactoryUtils.TimeToByListListener listener) {
        mListener = listener;
    }

    FactoryUtils.TimeToByListListener mListener;

    public TimeToByListAdapter(Context context, List<Map<String, Object>> data) {
        dataList = data;
        mContext = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Map<String, Object> map = dataList.get(position);
        View view = convertView;
        ViewHolder viewHolder = null;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timetoby_list, null, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTxtTitle.setText(StringUtils.isNotEmpty(map.get("GOODS_NAME") + "") ? (map.get("GOODS_NAME") + "") : "");
        viewHolder.mTextMoney.setText(StringUtils.isNotEmpty(map.get("GOODS_PRICE") + "") ? (map.get("GOODS_PRICE") + "") : "");
        viewHolder.mTxtMarketprice.setText("现存量" +
                (StringUtils.isNotEmpty(map.get("GOODS_NUM") + "") ?
                        (map.get("GOODS_NUM") + "") : ""));
        String goodsPic = map.get("GOODS_PIC") + "";
        if (StringUtils.isNotEmpty(goodsPic)) {
            if (goodsPic.contains(",")) {
                goodsPic = goodsPic.substring(0, goodsPic.indexOf(","));
            }
            GlideUtils.loadImageOrGif(mContext, Consts.ROOT_URL + goodsPic, viewHolder.mImage);
        }

        if (isNotStart) {
            viewHolder.mBtn.setClickable(false);
            viewHolder.mBtn.setText(mContext.getText(R.string.shopping_later));
            viewHolder.mBtn.setTextColor(mContext.getResources().getColor(R.color.dark_red));
        }
        viewHolder.mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onByNowClickListener(position);
            }
        });
        return view;
    }

    static class ViewHolder {
        protected ImageView mImage;
        protected TextView mTxtTitle;
        protected TextView mTxtMarketprice;
        protected TextView mTextMoney;
        protected Button mBtn;

        ViewHolder(View rootView) {
            initView(rootView);
        }

        private void initView(View rootView) {
            mImage = (ImageView) rootView.findViewById(R.id.image_item_time_list);
            mTxtTitle = (TextView) rootView.findViewById(R.id.txt_title_item_time_list);
            mTxtMarketprice = (TextView) rootView.findViewById(R.id.txt_marketprice);
            mTextMoney = (TextView) rootView.findViewById(R.id.text_money_item_timelist);
            mBtn = (Button) rootView.findViewById(R.id.btn_bynow_timelist);
        }
    }
}
