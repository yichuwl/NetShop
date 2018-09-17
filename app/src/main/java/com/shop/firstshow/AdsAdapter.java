package com.shop.firstshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.shop.Consts;
import com.shop.R;
import com.shop.util.GlideUtils;

/**
 * @author victory
 * @time 2018/6/26
 * @about
 */
public class AdsAdapter extends DelegateAdapter.Adapter<AdsAdapter.AdsViewHolder> {

    Context mContext;
    LayoutHelper mLayoutHelper;

    public void setUrl(String url) {
        this.url = url;
    }

    String url;

    public AdsAdapter(Context context, LayoutHelper layoutHelper, String url) {
        mContext = context;
        mLayoutHelper = layoutHelper;
        this.url = url;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public AdsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View conTentView = View.inflate(mContext, R.layout.image, null);
        return new AdsViewHolder(conTentView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsViewHolder holder, int position) {
        GlideUtils.loadImageOrGif(mContext, Consts.ROOT_URL + url, holder.adsImag);
        holder.adsImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了广告", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class AdsViewHolder extends RecyclerView.ViewHolder {
        ImageView adsImag;

        public AdsViewHolder(View itemView) {
            super(itemView);
            adsImag = itemView.findViewById(R.id.image_only);
            //adsImag.setPadding(20, 5, 20, 5);
        }
    }
}
