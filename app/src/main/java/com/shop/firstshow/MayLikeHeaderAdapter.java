package com.shop.firstshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.shop.R;

/**
 * @author victory
 * @time 2018/6/27
 * @about
 */
public class MayLikeHeaderAdapter extends DelegateAdapter.Adapter<MayLikeHeaderAdapter.MayLikeHeaderVH> {

    Context mContext;
    LayoutHelper mLayoutHelper;

    public MayLikeHeaderAdapter(Context context, LayoutHelper layoutHelper) {
        mContext = context;
        mLayoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {

        return mLayoutHelper;
    }

    @NonNull
    @Override
    public MayLikeHeaderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MayLikeHeaderVH(View.inflate(mContext, R.layout.twotext_image, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MayLikeHeaderVH holder, int position) {
        holder.rightTxt.setText(mContext.getString(R.string.txt_maylikeheader));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class MayLikeHeaderVH extends RecyclerView.ViewHolder {
        TextView leftTxt, rightTxt;
        ImageView image;

        public MayLikeHeaderVH(View itemView) {
            super(itemView);
            leftTxt = itemView.findViewById(R.id.text_lefttop);
            leftTxt.setVisibility(View.GONE);
            rightTxt = itemView.findViewById(R.id.text_righttop);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//添加相应的规则
            params.addRule(RelativeLayout.CENTER_IN_PARENT, R.id.rela);
            rightTxt.setLayoutParams(params);
            rightTxt.setGravity(Gravity.CENTER);
            rightTxt.setTextColor(mContext.getResources().getColor(R.color.main_color));
            rightTxt.setTextSize(18);
            image = itemView.findViewById(R.id.img_bottom);
            image.setVisibility(View.GONE);
        }
    }
}
