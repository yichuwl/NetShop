package com.shop.firstshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.shop.Consts;
import com.shop.R;
import com.shop.util.FactoryUtils;
import com.shop.util.GlideUtils;
import com.shop.util.GsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/6/26
 * @about
 */
public class ThemeTobyAdapter extends DelegateAdapter.Adapter<ThemeTobyAdapter.ThemeTobyViewHolder> {

    Context mContext;
    LayoutHelper mLayoutHelper;

    public void setMoreThemeClick(FactoryUtils.OnItemClickListenerFactory moreThemeClick) {
        this.moreThemeClick = moreThemeClick;
    }

    FactoryUtils.OnItemClickListenerFactory moreThemeClick;

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    List<String> imageUrlList = new ArrayList<>();

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    List<Map<String, Object>> dataList;

    public ThemeTobyAdapter(Context context, LayoutHelper layoutHelper, List<Map<String, Object>> dataList) {
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
    public ThemeTobyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ThemeTobyViewHolder holder = new ThemeTobyViewHolder(View.inflate(mContext, R.layout.item_theme_firstshow, null));
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeTobyViewHolder holder, final int position) {
        Map<String, Object> themeMap = dataList.get(position);
        String categoryName = themeMap.get("CATEGORY_NAME") + "";
        String sonArray = themeMap.get("SON") + "";
        final String themeId = themeMap.get("CATEGORY_ID") + "";
        List<Map<String, Object>> sonMapList = GsonUtils.parseArrayGson(sonArray);
        holder.title.setText(categoryName);
        if (imageUrlList.size() > position) {
            GlideUtils.loadImageOrGif(mContext, Consts.ROOT_URL + imageUrlList.get(position), holder.bigImage);
        }
        holder.seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreThemeClick.OnClickListener(position);
            }
        });

        holder.bigImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        for (int i = 0; i < (sonMapList.size() <= 6 ? sonMapList.size() : 6); i++) {
            final Map<String, Object> map = sonMapList.get(i);
            holder.txtList.get(i).setText(map.get("CATEGORY_NAME") + "");
            GlideUtils.loadImageOrGif(mContext, Consts.ROOT_URL + map.get("CATEGORY_IMG"), holder.imgList.get(i));
            holder.linearList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moreThemeClick.OnSubTHemeClickListener(map.get("CATEGORY_ID") + "");
                }
            });
        }
        if (sonMapList.size() < 6) {
            for (int i = sonMapList.size(); i < holder.txtList.size(); i++) {
                holder.linearList.get(i).setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ThemeTobyViewHolder extends RecyclerView.ViewHolder {
        List<TextView> txtList = new ArrayList<>();
        List<ImageView> imgList = new ArrayList<>();
        List<LinearLayout> linearList = new ArrayList<>();
        TextView title, seemore, txtSub1, txtSub2, txtSub3, txtSub4, txtSub5, txtSub6;
        ImageView bigImage, imageSub1, imageSub2, imageSub3, imageSub4, imageSub5, imageSub6;
        LinearLayout mLinearLayout1, mLinearLayout2, mLinearLayout3, mLinearLayout4, mLinearLayout5, mLinearLayout6;

        public ThemeTobyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title_theme);
            seemore = itemView.findViewById(R.id.txt_seemore_theme);
            bigImage = itemView.findViewById(R.id.image_theme);
            txtSub1 = itemView.findViewById(R.id.txt_themesub1);
            txtSub2 = itemView.findViewById(R.id.txt_themesub2);
            txtSub3 = itemView.findViewById(R.id.txt_themesub3);
            txtSub4 = itemView.findViewById(R.id.txt_themesub4);
            txtSub5 = itemView.findViewById(R.id.txt_themesub5);
            txtSub6 = itemView.findViewById(R.id.txt_themesub6);
            imageSub1 = itemView.findViewById(R.id.image_subtheme1);
            imageSub2 = itemView.findViewById(R.id.image_subtheme2);
            imageSub3 = itemView.findViewById(R.id.image_subtheme3);
            imageSub4 = itemView.findViewById(R.id.image_subtheme4);
            imageSub5 = itemView.findViewById(R.id.image_subtheme5);
            imageSub6 = itemView.findViewById(R.id.image_subtheme6);
            mLinearLayout1 = itemView.findViewById(R.id.linear1_theme);
            mLinearLayout2 = itemView.findViewById(R.id.linear2_theme);
            mLinearLayout3 = itemView.findViewById(R.id.linear3_theme);
            mLinearLayout4 = itemView.findViewById(R.id.linear4_theme);
            mLinearLayout5 = itemView.findViewById(R.id.linear5_theme);
            mLinearLayout6 = itemView.findViewById(R.id.linear6_theme);
            txtList.add(txtSub1);
            txtList.add(txtSub2);
            txtList.add(txtSub3);
            txtList.add(txtSub4);
            txtList.add(txtSub5);
            txtList.add(txtSub6);
            imgList.add(imageSub1);
            imgList.add(imageSub2);
            imgList.add(imageSub3);
            imgList.add(imageSub4);
            imgList.add(imageSub5);
            imgList.add(imageSub6);
            linearList.add(mLinearLayout1);
            linearList.add(mLinearLayout2);
            linearList.add(mLinearLayout3);
            linearList.add(mLinearLayout4);
            linearList.add(mLinearLayout5);
            linearList.add(mLinearLayout6);
        }
    }

}
