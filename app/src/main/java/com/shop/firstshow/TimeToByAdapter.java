package com.shop.firstshow;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.shop.Consts;
import com.shop.R;
import com.shop.util.GlideUtils;
import com.shop.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/6/26
 * @about
 */
public class TimeToByAdapter extends DelegateAdapter.Adapter {

    Context mContext;
    LayoutHelper mLayoutHelper;

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    List<Map<String, Object>> data = new ArrayList<>();

    public void setItemClickListener(TimeToByItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    TimeToByItemClickListener mItemClickListener;

    interface TimeToByItemClickListener {
        void onItemClick(int position);
    }

    public TimeToByAdapter(Context context, LayoutHelper layoutHelper) {
        mContext = context;
        mLayoutHelper = layoutHelper;
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new TimeTobyViewHolder(View.inflate(mContext, R.layout.item_goods_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (data.size() > position) {
            TimeTobyViewHolder viewHolder = (TimeTobyViewHolder) holder;
            viewHolder.imgGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });
            Map<String, Object> map = data.get(position);
            String goodsPic = map.get("GOODS_PIC") + "";
            if (StringUtils.isNotEmpty(goodsPic)) {
                if (goodsPic.contains(",")) {
                    goodsPic = goodsPic.substring(0, goodsPic.indexOf(","));
                }
                GlideUtils.loadImageOrGif(mContext, Consts.ROOT_URL + goodsPic, viewHolder.imgGoods);
            }
            viewHolder.txtPrice.setText(map.get("GOODS_PRICE") + "");
            viewHolder.txtTitle.setText(map.get("GOODS_NAME") + "");
        }
    }

    @Override
    public int getItemCount() {
        if (data.size() < 3) {
            return data.size();
        }
        return 3;
    }

    class TimeTobyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGoods;
        TextView txtTitle;
        TextView txtPrice;

        public TimeTobyViewHolder(View itemView) {
            super(itemView);
            imgGoods = itemView.findViewById(R.id.img_item_maylike);
            txtTitle = itemView.findViewById(R.id.txt_title_item_maylike);
            txtPrice = itemView.findViewById(R.id.txt_price_item_list);
        }
    }
}
