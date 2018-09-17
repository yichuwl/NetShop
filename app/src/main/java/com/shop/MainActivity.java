package com.shop;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shop.firstshow.GetCouponPopWindow;
import com.shop.firstshow.ShouYeFragment;
import com.shop.personspace.WodeFragment;
import com.shop.shequ.SheQuFragment;
import com.shop.shopchart.ShoppingChartFragment;
import com.shop.util.OkUtils;
import com.shop.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    protected FrameLayout mFrameMainshow;
    protected TabLayout mTabMain;
    protected ShouYeFragment mShouYeFragment;
    protected SheQuFragment mSheQuFragment;
    protected ShoppingChartFragment mChartFragment;
    protected WodeFragment mWodeFragment;
    public static final String SHOUYE_TAG = "shouye";
    public static final String SHEQU_TAG = "shequ";
    public static final String CHART_TAG = "chart";
    public static final String WODE_TAG = "wode";
    private String[] tabTxt;
    int tochart;
    private int[] tabImag = {R.drawable.maintab_selector_00, R.drawable.maintab_selector_01,
            R.drawable.maintab_selector_02, R.drawable.maintab_selector_03};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        tabTxt = getResources().getStringArray(R.array.main_bottomtab_txt);
        initView();
        initFrameContent();
        initBottomTab();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        mFrameMainshow = (FrameLayout) findViewById(R.id.frame_mainshow);
        mTabMain = (TabLayout) findViewById(R.id.tab_main);
        for (int i = 0; i < tabTxt.length; i++) {
            View tabContentView = View.inflate(this, R.layout.item_mainbottomtab_layout, null);
            ((ImageView) tabContentView.findViewById(R.id.img_item_maintab)).setImageResource(tabImag[i]);
            ((TextView) tabContentView.findViewById(R.id.txt_item_maintab)).setText(tabTxt[i]);

            mTabMain.addTab(mTabMain.newTab().setCustomView(tabContentView));
        }
    }

    List<Fragment> fragmentList = new ArrayList<>();

    private void initFrameContent() {
        mShouYeFragment = ShouYeFragment.newInstance();
        mSheQuFragment = SheQuFragment.newInstance();
        mChartFragment = ShoppingChartFragment.newInstance();
        mWodeFragment = WodeFragment.newInstance();
        fragmentList.add(mShouYeFragment);
        fragmentList.add(mSheQuFragment);
        fragmentList.add(mChartFragment);
        fragmentList.add(mWodeFragment);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_mainshow, mShouYeFragment)
                .add(R.id.frame_mainshow, mSheQuFragment)
                .add(R.id.frame_mainshow, mChartFragment)
                .add(R.id.frame_mainshow, mWodeFragment)
                .hide(mSheQuFragment).hide(mChartFragment)
                .hide(mWodeFragment)
                .show(mShouYeFragment)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        tochart = intent.getIntExtra("tochart", 0);
        tabSelection = 2;
        setFragmentSelectChange(tabSelection, 0);
        tochart = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();

        setFragmentSelectChange(tabSelection, 0);
    }

    int tabSelection = 0;

    private void initBottomTab() {
        mTabMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                setFragmentSelectChange(position, tabSelection);
                tabSelection = position;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabMain.getTabAt(tabSelection).select();
    }

    void setFragmentSelectChange(int selectedPosition, int unSelectedPosition) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.hide(mShouYeFragment)
                .hide(mSheQuFragment)
                .hide(mChartFragment)
                .hide(mWodeFragment);
        transaction.show(fragmentList.get(selectedPosition)).commit();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:

                    break;
                default:
                    break;
            }
        }
    };

}
