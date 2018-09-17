package com.shop.firstshow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.shop.R;

/**
 * Created by victoryf on 2018-06-29.
 */
public class TimeHeaderAdapter extends DelegateAdapter.Adapter<TimeHeaderAdapter.TimeHeaderVH> {
    Context mContect;
    LayoutHelper mHelper;
    View.OnClickListener mClickListener;

    public TimeHeaderAdapter(Context context, LayoutHelper helper, View.OnClickListener listener) {
        mContect = context;
        mClickListener = listener;
        mHelper = helper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }

    @NonNull
    @Override
    public TimeHeaderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TimeHeaderVH(View.inflate(mContect, R.layout.twotext_image, null));
    }

    @Override
    public void onBindViewHolder(@NonNull TimeHeaderVH holder, int position) {
        holder.leftTxt.setText(mContect.getString(R.string.txt_timetoby));
        holder.rightTxt.setText(mContect.getString(R.string.txt_seemore));
        holder.rightTxt.setOnClickListener(mClickListener);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class TimeHeaderVH extends RecyclerView.ViewHolder {
        TextView leftTxt, rightTxt;
        ImageView image;

        public TimeHeaderVH(View itemView) {
            super(itemView);
            leftTxt = itemView.findViewById(R.id.text_lefttop);
            leftTxt.setTextColor(mContect.getResources().getColor(R.color.main_pay_bg_red));
            leftTxt.setTextSize(18);
            rightTxt = itemView.findViewById(R.id.text_righttop);
            image = itemView.findViewById(R.id.img_bottom);
            image.setVisibility(View.GONE);
        }
    }
}
