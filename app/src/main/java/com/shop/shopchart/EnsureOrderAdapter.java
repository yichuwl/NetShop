package com.shop.shopchart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.goods.GoodsShowActivity;
import com.shop.util.GlideUtils;
import com.shop.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/6/27
 * @about
 */
public class EnsureOrderAdapter extends RecyclerView.Adapter<EnsureOrderAdapter.GoodsVH> {
    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }


    public EnsureOrderAdapter(Context context) {
        mContext = context;
    }

    List<Map<String, Object>> dataList = new ArrayList<>();
    Context mContext;

    public void setClickListener(View.OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    View.OnClickListener mClickListener;

    @NonNull
    @Override
    public GoodsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GoodsVH(View.inflate(mContext, R.layout.item_chartgoods_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsVH holder, int position) {
        Map<String, Object> map = dataList.get(position);

        String goodsPicUrl = map.get("GOODS_PIC") + "";
        if (StringUtils.isNotEmpty(goodsPicUrl)) {
            if (goodsPicUrl.contains(",")) {
                goodsPicUrl = goodsPicUrl.split(",")[0];
            }
        }
        GlideUtils.loadImageOrGif(mContext, Consts.ROOT_URL + goodsPicUrl, holder.mImageView);
        holder.titleTxt.setText(map.get("GOODS_NAME") + "");
        holder.numTxt.setText("x " + map.get("GOODS_NUM"));
        holder.modelTxt.setText(map.get("GOODS_ATTRIBUTE") + "");
        holder.priceTxt.setText(map.get("GOODS_PRICE") + "");

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class GoodsVH extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;
        ImageView mImageView;
        TextView priceTxt, minusTxt, plusTxt, numTxt, titleTxt, modelTxt;
        Button removeBtn;

        public GoodsVH(View rootView) {
            super(rootView);
            mCheckBox = rootView.findViewById(R.id.checkbox_good_chart);
            mImageView = rootView.findViewById(R.id.image_item_good_chart);
            titleTxt = rootView.findViewById(R.id.txt_title_item_goods);
            priceTxt = rootView.findViewById(R.id.text_chartgoods_money);
            minusTxt = rootView.findViewById(R.id.text_minus_goodschart);
            plusTxt = rootView.findViewById(R.id.text_plus_goodschart);
            numTxt = rootView.findViewById(R.id.text_num_goodschart);
            removeBtn = rootView.findViewById(R.id.btn_remove_goodschart);
            modelTxt = rootView.findViewById(R.id.txt_model_item_goods);
            mCheckBox.setVisibility(View.GONE);
            minusTxt.setVisibility(View.GONE);
            plusTxt.setVisibility(View.GONE);
            removeBtn.setVisibility(View.GONE);
        }
    }
}
