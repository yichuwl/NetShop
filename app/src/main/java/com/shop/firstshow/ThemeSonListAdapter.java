package com.shop.firstshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shop.R;
import com.shop.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/6/29
 * @about
 */
public class ThemeSonListAdapter extends RecyclerView.Adapter<ThemeSonListAdapter.SonViewHolder> {
    Context mContext;

    public ThemeSonListAdapter(Context context) {
        mContext = context;
    }

    public void setSonDataList(List<Map<String, Object>> sonDataList) {
        this.sonDataList = sonDataList;
    }

    List<Map<String, Object>> sonDataList = new ArrayList<>();

    @NonNull
    @Override
    public SonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SonViewHolder(View.inflate(mContext, R.layout.item_category_firstshowlist, null));
    }

    @Override
    public void onBindViewHolder(@NonNull SonViewHolder holder, int position) {
        Map<String, Object> map = sonDataList.get(position);
        String name = map.get("CATEGORY_NAME") + "";
        String imgUrl = map.get("CATEGORY_IMG") + "";
        holder.text.setText(name);
        GlideUtils.loadCirleImageOrGif(mContext, imgUrl, holder.img);
    }

    @Override
    public int getItemCount() {
        return sonDataList.size();
    }

    class SonViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView text;

        public SonViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_item_recy_themefirstlist);
            text = itemView.findViewById(R.id.txt_item_recy_themefirstlist);
        }
    }
}
