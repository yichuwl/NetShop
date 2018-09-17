package com.shop.shequ;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shop.R;
import com.shop.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * @author victory
 * @time 2018/6/25
 * @about
 */

public class ShequContentListAdapter extends RecyclerView.Adapter {

    public void setClickListener(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    Context mContext;

    public ShequContentListAdapter(Context context) {
        mContext = context;
    }

    OnItemClickListener mClickListener;

    public void setDatalist(List<Map<String, Object>> datalist) {
        this.datalist = datalist;
    }

    List<Map<String, Object>> datalist = new ArrayList<>();

    public interface OnItemClickListener {
        void itemClickListener(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        ImageView imageContent;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title_item_shequ);
            content = itemView.findViewById(R.id.txt_content_item_shequ);
            imageContent = itemView.findViewById(R.id.img_content_item_shequ);
            cardView = itemView.findViewById(R.id.card_articlelist);
        }
    }

    class FooterVH extends RecyclerView.ViewHolder {
        TextView footerTxt;

        public FooterVH(View itemView) {
            super(itemView);
            footerTxt = itemView.findViewById(R.id.text_only);
            footerTxt.setText(mContext.getString(R.string.upformore));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  if (2 == viewType) {
        View itemContentView = View.inflate(mContext, R.layout.item_shequlist, null);
        return new ViewHolder(itemContentView);
      /*  } else {
            return new FooterVH(View.inflate(mContext, R.layout.text, null));
        }*/
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder sheQuHolder = (ViewHolder) holder;
            Map<String, Object> map = datalist.get(position);
            sheQuHolder.title.setText(map.get("TITLE") + "");
            sheQuHolder.content.setText(map.get("SHORTCONTENT") + "");
            String imgUrl = map.get("IMAGE") + "";
            if ((!TextUtils.isEmpty(imgUrl))) {
                Log.i("articleimgurl", imgUrl);
                GlideUtils.loadImageOrGif(mContext, imgUrl, ((ViewHolder) holder).imageContent);
            }
            ((ViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.itemClickListener(position);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (datalist.size() > 20 && position == datalist.size() - 1) {
            return 1;
        }
        return 2;
    }

    @Override
    public int getItemCount() {
        return datalist == null ? 0 : datalist.size();
    }
}
