package com.shop.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shop.R;

/**
 * @author victory
 * @time 2018/6/25
 * @about 图片解析和加载 工具类
 */

public class GlideUtils {

    public static void loadImageOrGif(Context context,Object url, ImageView imageView){
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round);
        Glide.with(context).load(url)
                .apply(requestOptions).into(imageView);
    }

    public static void loadImageOrGif(Context context,Object url, ImageView imageView,int width,int height){
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .override(width,height);
        Glide.with(context).load(url)
                .apply(requestOptions).into(imageView);
    }

    public static void loadCirleImageOrGif(Context context,Object url, ImageView imageView,int width,int height){
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .circleCrop()
                .override(width,height);
        Glide.with(context).load(url)
                .apply(requestOptions).into(imageView);
    }
    public static void loadCirleImageOrGif(Context context,Object url, ImageView imageView){
        RequestOptions requestOptions = new RequestOptions().placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round)
                .circleCrop();
        Glide.with(context).load(url)
                .apply(requestOptions).into(imageView);
    }
}
