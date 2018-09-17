package com.shop.firstshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shop.Consts;
import com.shop.R;
import com.shop.util.GlideUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

/**
 * @author victory
 * @time 2018/6/26
 * @about
 */
public class FirstShowBannerAdapter extends DelegateAdapter.Adapter<FirstShowBannerAdapter.BannerVH> {
    Context mContext;
    LayoutHelper mLayoutHelper;

    public void setBannerListener(OnBannerListener bannerListener) {
        mBannerListener = bannerListener;
    }

    OnBannerListener mBannerListener;

    public FirstShowBannerAdapter(Context context, LayoutHelper layoutHelper) {
        mContext = context;
        mLayoutHelper = layoutHelper;
    }

    @NonNull
    @Override
    public BannerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View bannerView = View.inflate(mContext, R.layout.item_banner_firstshowlist, null);

        return new BannerVH(bannerView);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerVH holder, int position) {
        holder.mBanner.setImages(imageUrls);
        holder.mBanner.start();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    List<String> imageUrls;

    class BannerVH extends RecyclerView.ViewHolder {

        Banner mBanner;

        public BannerVH(View itemView) {
            super(itemView);
            mBanner = itemView.findViewById(R.id.banner_image);
            mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR);
            GlideImageLoader imageLoader = new GlideImageLoader();
            mBanner.setImageLoader(imageLoader);
            mBanner.setOnBannerListener(mBannerListener);
        }
    }

    class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            GlideUtils.loadImageOrGif(context, Consts.ROOT_URL + path, imageView);

        }
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {

        return mLayoutHelper;
    }
}
