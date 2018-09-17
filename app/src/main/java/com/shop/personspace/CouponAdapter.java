package com.shop.personspace;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.shop.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/7/5
 * @about
 */
public class CouponAdapter extends BaseAdapter {
    Context mContext;

    public interface SaveCouponClickLisnter {
        void onClickListener(int position);
    }

    public void setmListener(SaveCouponClickLisnter mListener) {
        this.mListener = mListener;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    boolean isLogin = true;

    SaveCouponClickLisnter mListener;

    public CouponAdapter(Context context) {
        mContext = context;
    }

    public void setPriceList(List<Map<String, Object>> priceList) {
        this.priceList = priceList;
    }

    List<Map<String, Object>> priceList = new ArrayList<>();

    @Override
    public int getCount() {
        return priceList.size();
    }

    @Override
    public Object getItem(int position) {
        return priceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.item_couponlist, null);
        final Button saveCouponBtn = convertView.findViewById(R.id.btn_getcoupon_first);
        if (mContext instanceof CouponListActivity || (!isLogin))
            saveCouponBtn.setVisibility(View.GONE);
        saveCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCouponBtn.setText("已领取");
                saveCouponBtn.setClickable(false);
                saveCouponBtn.setBackgroundColor(mContext.getResources().getColor(R.color.darkyellow));
                mListener.onClickListener(position);
            }
        });
        TextView priceTxt = convertView.findViewById(R.id.txt_couponprice);
        TextView userTime = convertView.findViewById(R.id.txt_couponusertime);
        TextView userRule = convertView.findViewById(R.id.txt_couponrule);
        Map<String, Object> map = priceList.get(position);
        priceTxt.setText(map.get("COUPON_PRICE") + "");
        userRule.setText(map.get("COUPON_NAME") + "");
        userTime.setText(map.get("STARTTIME") + " - " + map.get("ENDTIME"));
        return convertView;
    }
}
