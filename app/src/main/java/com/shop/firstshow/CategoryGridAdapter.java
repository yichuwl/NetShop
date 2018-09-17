package com.shop.firstshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.ViewHolder> {

    Context mContext;

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    List<Map<String, Object>> dataList = new ArrayList<>();

    public CategoryGridAdapter(Context context, List<Map<String, Object>> data, OnitemClickListener listener) {
        mContext = context;
        dataList = data;
        this.mListener = listener;
    }

    interface OnitemClickListener {
        void onClick(int position);
    }

    OnitemClickListener mListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_grid_categoryfragment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Map<String, Object> map = dataList.get(position);
        String categoryName = map.get("CATEGORY_NAME") + "";
        String imgUrl = Consts.ROOT_URL + map.get("CATEGORY_IMG");
        holder.mTextView.setText(categoryName);
        GlideUtils.loadCirleImageOrGif(mContext, imgUrl, holder.mImageView);
        holder.mlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mTextView;
        LinearLayout mlinear;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_item_grid_category);
            mTextView = itemView.findViewById(R.id.txt_item_grid_category);
            mlinear = itemView.findViewById(R.id.linear_subcategory);
        }
    }
}
