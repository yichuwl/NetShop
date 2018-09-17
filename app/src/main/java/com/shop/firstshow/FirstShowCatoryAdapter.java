package com.shop.firstshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.shop.Consts;
import com.shop.R;
import com.shop.util.FactoryUtils;
import com.shop.util.GlideUtils;

import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/6/26
 * @about
 */
public class FirstShowCatoryAdapter extends DelegateAdapter.Adapter<FirstShowCatoryAdapter.CategoryVH> {

    Context mContext;
    LayoutHelper mLayoutHelper;

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    List<Map<String, Object>> dataList;

    interface SetCatoryClickListener {
        void OnCatoryItemClick(int position);
    }

    public void setItemClickListenerFactory(FactoryUtils.OnItemClickListenerFactory itemClickListenerFactory) {
        this.itemClickListenerFactory = itemClickListenerFactory;
    }

    FactoryUtils.OnItemClickListenerFactory itemClickListenerFactory;

    public FirstShowCatoryAdapter(Context context, LayoutHelper layoutHelper, List<Map<String, Object>> dataList) {
        mContext = context;
        mLayoutHelper = layoutHelper;
        this.dataList = dataList;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.item_category_firstshowlist, null);
        return new CategoryVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, final int position) {
        if (position < dataList.size()) {
            Map<String, Object> map = dataList.get(position);
            holder.mTextView.setText(map.get("CATEGORY_NAME") + "");
            GlideUtils.loadCirleImageOrGif(mContext, Consts.ROOT_URL + map.get("CATEGORY_IMG"), holder.mImageView);
        } else {
            holder.mImageView.setImageResource(R.mipmap.all);
            holder.mTextView.setText(mContext.getString(R.string.all));
        }
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListenerFactory.OnClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    class CategoryVH extends RecyclerView.ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        LinearLayout mLinearLayout;

        public CategoryVH(View itemView) {
            super(itemView);
            mLinearLayout = itemView.findViewById(R.id.linear_category);
            mTextView = itemView.findViewById(R.id.txt_item_categoryfirstlist);
            mImageView = itemView.findViewById(R.id.img_item_categoryfirstlist);
        }
    }
}
