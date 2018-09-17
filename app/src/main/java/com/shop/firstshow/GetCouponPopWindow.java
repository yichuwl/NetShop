package com.shop.firstshow;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.shop.Consts;
import com.shop.R;
import com.shop.personspace.CouponAdapter;
import com.shop.personspace.CouponListActivity;
import com.shop.sign.SigninAndUpActivity;
import com.shop.util.GsonUtils;
import com.shop.util.MeasureListUtil;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;
import com.shop.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author victory
 * @time 2018/7/4
 * @about
 */
public class GetCouponPopWindow extends PopupWindow implements View.OnClickListener {

    private String userId;
    protected ImageView mClosePop;
    protected ListView mListCouponFirstshow;
    protected Button mBtnGetcouponFirst;
    Context mContext;
    private CouponAdapter mAdapter;
    List<Map<String, Object>> couponList = new ArrayList<>();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*if (couponList.size() > 0) {
                mAdapter.setPriceList(couponList);
                mAdapter.notifyDataSetChanged();
            } else if (GetCouponPopWindow.this.isShowing())*/
            // GetCouponPopWindow.this.dismiss();
            int what = msg.what;
            switch (what) {
                case 0:
                    Toast.makeText(mContext, "领取成功", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(mContext, "无法领取", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    public void setCouponDataStr(String couponDataStr) {
        this.couponDataStr = couponDataStr;
    }

    String couponDataStr = "";

    public GetCouponPopWindow(Context context, String data) {
        super();
        this.mContext = context;
        couponDataStr = data;
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
        View contentView = View.inflate(context, R.layout.couponlist_firstshow, null);
        this.setContentView(contentView);
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.gray_transparent)));
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        initView(contentView);
        initCouponListData();
    }

    List<Map<String, Object>> maps = new ArrayList<>();

    private void initCouponListData() {
        if (StringUtils.isNotEmpty(couponDataStr)) {
            Log.e("coupondata", couponDataStr);
            maps = GsonUtils.parseArrayGson(couponDataStr);
            mAdapter = new CouponAdapter(mContext);
            mAdapter.setmListener(new CouponAdapter.SaveCouponClickLisnter() {
                @Override
                public void onClickListener(int position) {
                    saveCoupon(position);
                }
            });
            mAdapter.setPriceList(maps);
            mAdapter.setLogin(StringUtils.isNotEmpty(userId));
            mListCouponFirstshow.setAdapter(mAdapter);
            //mAdapter.notifyDataSetChanged();
            MeasureListUtil.setListViewHeightBasedOnChildren(mListCouponFirstshow);
        }
    }

    private void initView(View view) {
        mClosePop = (ImageView) view.findViewById(R.id.close_pop);
        mClosePop.setOnClickListener(GetCouponPopWindow.this);
        mListCouponFirstshow = (ListView) view.findViewById(R.id.list_coupon_firstshow);
       /* mBtnGetcouponFirst = (Button) view.findViewById(R.id.btn_getcoupon_first);
        mBtnGetcouponFirst.setOnClickListener(GetCouponPopWindow.this);*/
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.close_pop) {
            this.dismiss();
        } else if (view.getId() == R.id.btn_getcoupon_first) {
            //saveCoupon();
        }
    }

    private void saveCoupon(int position) {
        if (StringUtils.isEmpty(userId)) {
            Toast.makeText(mContext, "请登录", Toast.LENGTH_SHORT).show();
            return;
        }
        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        String url = Consts.ROOT_URL + Consts.SAVE_COUPON;
        HashMap<String, String> params = new HashMap<>();
        params.put("COUPON_ID", maps.get(position).get("COUPON_ID") + "");
        params.put("USER_ID", userId);
        url = okUtils.getUrl(url, params);
        okUtils.httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (StringUtils.isNotEmpty(string)) {
                    Log.i("savecoupon", string);
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(string);
                    //couponList.addAll(maps);
                    mHandler.sendEmptyMessage(0);

                } else
                    mHandler.sendEmptyMessage(1);
            }
        });
    }
}
