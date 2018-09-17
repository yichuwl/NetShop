package com.shop.firstshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.shop.Consts;
import com.shop.R;
import com.shop.util.FactoryUtils;
import com.shop.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/6/26
 * @about
 */
public class MayBeLikeAdapter extends DelegateAdapter.Adapter<MayBeLikeAdapter.MayBeLikeVH> {

    Context mContext;
    LayoutHelper mLayoutHelper;

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    List<Map<String, Object>> dataList = new ArrayList<>();

    public void setOnItemClickListenerFactory(FactoryUtils.OnItemClickListenerFactory onItemClickListenerFactory) {
        this.onItemClickListenerFactory = onItemClickListenerFactory;
    }

    FactoryUtils.OnItemClickListenerFactory onItemClickListenerFactory;

    public MayBeLikeAdapter(Context context, LayoutHelper layoutHelper) {
        mContext = context;
        mLayoutHelper = layoutHelper;
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public MayBeLikeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = View.inflate(mContext, R.layout.item_goods_list, null);
        return new MayBeLikeVH(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull MayBeLikeVH holder, final int position) {
        Map<String, Object> map = dataList.get(position);
        String goodsName = map.get("GOODS_NAME") + "";
        String picUrl = Consts.ROOT_URL + map.get("GOODS_PIC");
        String price = map.get("GOODS_PRICE") + "";
        holder.txtPrice.setText(price);
        holder.txtTitle.setText(goodsName);
        GlideUtils.loadImageOrGif(mContext, Consts.ROOT_URL + picUrl, holder.imgGoods);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListenerFactory.OnClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MayBeLikeVH extends RecyclerView.ViewHolder {
        ImageView imgGoods;
        TextView txtTitle;
        TextView txtPrice;
        RelativeLayout layout;

        public MayBeLikeVH(View itemView) {
            super(itemView);
            imgGoods = itemView.findViewById(R.id.img_item_maylike);
            txtTitle = itemView.findViewById(R.id.txt_title_item_maylike);
            txtPrice = itemView.findViewById(R.id.txt_price_item_list);
            layout = itemView.findViewById(R.id.rela_item_good_list);
        }
    }
}
