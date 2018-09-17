package com.shop.personspace;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.util.GsonUtils;
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

public class CouponListActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mBackTop;
    protected TextView mTitleTop;
    //protected TextView mTxtLoading;
    protected ListView mListCoupon;
    protected TextView mTxtCouponReminder;
    protected TabLayout tabCouponState;
    private CouponAdapter mAdapter;
    private String userName, userId;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:
                    if (couponList.size() > 0) {
                        mTxtCouponReminder.setVisibility(View.GONE);
                    } else {
                        mHandler.sendEmptyMessage(1);
                    }

                    mAdapter.setPriceList(couponList);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    mTxtCouponReminder.setText(getString(R.string.nodata_clicktorefresh));
                    mTxtCouponReminder.setClickable(true);
                    mTxtCouponReminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tabSelection == 0)
                                initData("0");
                            else if (tabSelection == 1)
                                initData("1");
                            else if (tabSelection == 2)
                                initData("-1");
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_coupon_list);
        userName = (String) PreferenceUtil.getParam(PreferenceUtil.USERNAME, "");
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
        initView();
        if (StringUtils.isNotEmpty(userName)) {
            initData("0");
        } else {
            //mTxtCouponReminder.setVisibility(View.VISIBLE);
            mTxtCouponReminder.setText(getString(R.string.not_signin));
        }
    }

    private void initData(String couponState) {
        // couponList.clear();
        mTxtCouponReminder.setText(R.string.loading);
        mTxtCouponReminder.setClickable(false);
        OkUtils okUtils = OkUtils.getOkUtilsInstance();
        HashMap<String, String> params = new HashMap<>();
        params.put("USER_ID", userId);
        params.put("COUPON_STATE", couponState);
        String url = okUtils.getUrl(Consts.ROOT_URL + Consts.GET_USER_COUPON_LIST, params);
        okUtils.setNewClient().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("couponlist", "---" + string);
                if (StringUtils.isNotEmpty(string)) {
                    couponList.clear();
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(string);
                    //if (maps != null && maps.size() > 0) {
                    couponList.addAll(maps);
                    mHandler.sendEmptyMessage(0);
                    // } else {

                    // }
                } else {

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            onBackPressed();
        }
    }

    private void initView() {
        mBackTop = (ImageView) findViewById(R.id.back_top);
        mBackTop.setOnClickListener(CouponListActivity.this);
        mTitleTop = (TextView) findViewById(R.id.title_top);
        mTitleTop.setText(getString(R.string.txt_coupon));
        //mTxtLoading = (TextView) findViewById(R.id.txt_loading);
        mListCoupon = (ListView) findViewById(R.id.list_coupon);
        initListView();
        mTxtCouponReminder = (TextView) findViewById(R.id.txt_coupon_reminder);
        tabCouponState = (TabLayout) findViewById(R.id.tab_coupon_state);
        initTab();
    }

    int tabSelection;

    private void initTab() {
        //tab居中显示
        // tabCouponState.setTabGravity(TabLayout.GRAVITY_CENTER);
        //tab的字体选择器,默认黑色,选择时红色
        tabCouponState.setTabTextColors(Color.BLACK, getResources().getColor(R.color.main_color));
        //tab的下划线颜色,默认是粉红色
        tabCouponState.setSelectedTabIndicatorColor(getResources().getColor(R.color.main_color));

        String[] tabTxt = {"未使用", "已使用", "已过期"};
        for (int i = 0; i < tabTxt.length; i++) {
            tabCouponState.addTab(tabCouponState.newTab().setText(tabTxt[i]));
        }
        tabCouponState.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tabSelection = position;
                if (tabSelection == 0)
                    initData("0");
                else if (tabSelection == 1)
                    initData("1");
                else if (tabSelection == 2)
                    initData("-1");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabCouponState.getTabAt(0).select();
    }

    List<Map<String, Object>> couponList = new ArrayList<>();

    private void initListView() {
        mAdapter = new CouponAdapter(this);

        mListCoupon.setAdapter(mAdapter);
    }
}
