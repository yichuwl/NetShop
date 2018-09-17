package com.shop.goods;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/6/27
 * @about
 */
public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.ViewHolder> {
    Context mContext;
    List<Map<String, Object>> dataList = new ArrayList<>();

    interface OnItemClickListener {
        void clickListener(int position);
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    OnItemClickListener clickListener;

    public GoodsListAdapter(Context context, List<Map<String, Object>> data) {
        mContext = context;
        dataList = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.item_goods_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Map<String, Object> map = dataList.get(position);
        String goodsName = map.get("GOODS_NAME") + "";
        String picUrl = Consts.ROOT_URL + map.get("GOODS_PIC");
        String price = map.get("GOODS_MINPRICE") + "";
        holder.txtPrice.setText(price);
        holder.txtTitle.setText(goodsName);
        GlideUtils.loadImageOrGif(mContext, picUrl, holder.imgGoods);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.clickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgGoods;
        TextView txtTitle;
        TextView txtPrice;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            imgGoods = itemView.findViewById(R.id.img_item_maylike);
            txtTitle = itemView.findViewById(R.id.txt_title_item_maylike);
            txtPrice = itemView.findViewById(R.id.txt_price_item_list);
            layout = itemView.findViewById(R.id.rela_item_good_list);
        }
    }
}
