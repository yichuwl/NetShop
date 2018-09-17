package com.shop.shopchart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.Consts;
import com.shop.R;
import com.shop.customize.pullrefresh.PullToRefershView;
import com.shop.customize.pullrefresh.PullToRefreshBase;
import com.shop.sign.SigninAndUpActivity;
import com.shop.util.FactoryUtils;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;
import com.shop.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author victory
 * @time 2018/6/25
 * @about 购物车页面
 */

public class ShoppingChartFragment extends Fragment implements View.OnClickListener {
    protected View rootView;
    protected ListView mListGoodschart;
    protected CheckBox mRdbSelectallChart;
    protected TextView mTxtTopayMoney;
    protected TextView mTxtPrivilege;
    protected Button mBtnChartPay;
    protected TextView mTxtClearallChart;
    List<Map<String, Object>> goodsDataList = new ArrayList<>();
    private ChartGoodsListAdapter mListAdapter;
    private String userName, userId;

    public static ShoppingChartFragment newInstance() {

        Bundle args = new Bundle();

        ShoppingChartFragment fragment = new ShoppingChartFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_chart, null);
        userName = PreferenceUtil.getParam(PreferenceUtil.USERNAME, "") + "";
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
        initView(contentView);
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //initData();
        // goodsAccount = 0f;
        // mTxtTopayMoney.setText("0.0");
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (StringUtils.isEmpty(userName))
        /*userName = PreferenceUtil.getParam(PreferenceUtil.USERNAME, "") + "";
        refershView.doPullRefreshing(true, 500);*/
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            refershView.onPullDownRefreshComplete();
            refershView.onPullUpRefreshComplete();
            switch (what) {
                case 0:
                    if (goodsDataList.size() > 0) {
                        refershView.onPullDownRefreshComplete();
                        goToSignTxt.setVisibility(View.GONE);
                        refershView.setVisibility(View.VISIBLE);
                        refershView.setPullRefreshEnabled(true);
                    } else {
                        refershView.onPullDownRefreshComplete();
                        //refershView.setVisibility(View.GONE);
                        //goToSignTxt.setVisibility(View.VISIBLE);
                        goToSignTxt.setText(getString(R.string.chartisempty));
                        // refershView.setVisibility(View.GONE);
                    }
                    mListAdapter.setDataList(goodsDataList);
                    mListAdapter.notifyDataSetChanged();
                    // goodsAccount = 0;
                    mTxtTopayMoney.setText("0.0");
                    // Toast.makeText(getActivity(), getString(R.string.pleasedolater), Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    refershView.onPullDownRefreshComplete();
                    //refershView.setVisibility(View.GONE);
                    //goToSignTxt.setVisibility(View.VISIBLE);
                    goToSignTxt.setText(getString(R.string.chartisempty));
                    goToSignTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            refershView.doPullRefreshing(true, 500);
                        }
                    });
                    break;
            }
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            userName = PreferenceUtil.getParam(PreferenceUtil.USERNAME, "") + "";
            //if (StringUtils.isNotEmpty(userName))
            refershView.doPullRefreshing(true, 500);
        }
    }

    private void initData() {
        if (TextUtils.isEmpty(userName)) {
            goToSignTxt.setText(getString(R.string.gotosign));
            goToSignTxt.setVisibility(View.VISIBLE);
            if (goodsDataList.size() > 0) {
                goodsDataList.clear();
                mListAdapter.setDataList(goodsDataList);
                mListAdapter.notifyDataSetChanged();
            }
            //refershView.setVisibility(View.GONE);
            refershView.onPullUpRefreshComplete();
            refershView.onPullDownRefreshComplete();
        } else {
            OkUtils okUtils = OkUtils.getOkUtilsInstance();
            HashMap<String, String> params = new HashMap<>();
            params.put("USERNAME", userId);
            String url = okUtils.getUrl(Consts.ROOT_URL + Consts.GET_CHART_LIST, params);
            Log.e("usercharturl", url);
            okUtils.httpGet(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mHandler.sendEmptyMessage(1);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //if (Consts.RESPONSE_OK_STATE.equals(response.message())) {
                    String string = response.body().string();
                    Log.e("userchart", string);
                    goodsDataList.clear();
                    checkgroup.clear();
                    if (!TextUtils.isEmpty(string)) {
                        List<Map<String, Object>> mapList = GsonUtils.parseArrayGson(string);
                        if (mapList != null && mapList.size() > 0) {
                            goodsDataList.clear();
                            goodsDataList.addAll(mapList);
                            for (int i = 0; i < goodsDataList.size(); i++) {
                                checkgroup.put(i, false);
                            }
                            mHandler.sendEmptyMessage(0);

                        } else {
                            mHandler.sendEmptyMessage(1);

                        }
                    } else {
                        mHandler.sendEmptyMessage(1);
                    }
                    // }
                }
            });
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_chart_pay) {
            // todo pay
            Set<Integer> indexSet = checkgroup.keySet();
            List<Map<String, Object>> checkedList = new ArrayList<>();
            for (int index :
                    indexSet) {
                Boolean aBoolean = checkgroup.get(index);
                if (aBoolean)
                    checkedList.add(goodsDataList.get(index));
            }
            if (checkedList.size() <= 0) {
                Toast.makeText(getActivity(), "请选择商品", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), EnsureOrderActivity.class);
                try {
                    JSONArray jsonArray = GsonUtils.listToArray(checkedList);
                    intent.putExtra(EnsureOrderActivity.ENSURE_BY_GOODS, jsonArray.toString());
                    intent.putExtra(EnsureOrderActivity.FROM, EnsureOrderActivity.CHART);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        } else if (view.getId() == R.id.txt_clearall_chart) {
            if (goodsDataList.size() > 0) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("清空购物车？")
                        .setPositiveButton("清空", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearShopChart();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("再想想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        } else if (view.getId() == R.id.txt_gotosign) {
            if (getString(R.string.chartisempty).equals(goToSignTxt.getText().toString()))
                return;
            Intent intent = new Intent(getActivity(), SigninAndUpActivity.class);
            startActivity(intent);
        }
    }

    private void clearShopChart() {
        OkUtils okUtils = OkUtils.getOkUtilsInstance();
        HashMap<String, String> params = new HashMap<>();
        params.put("USERNAME", userId);
        String url = okUtils.getUrl(Consts.ROOT_URL + Consts.REMOVE_CHART_GOODS, params);
        okUtils.setNewClient().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getString(R.string.pleasedolater), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //if (Consts.RESPONSE_OK_STATE.equals(response.message())) {
                String string = response.body().string();
                if (!TextUtils.isEmpty(string)) {
                    Map<String, Object> map = GsonUtils.parseJsonObject(string);
                    if ("1".equals(map.get("RESULT") + "")) {
                        goodsDataList.clear();
                        mHandler.sendEmptyMessage(0);
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), getString(R.string.pleasedolater), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
            //}
        });
    }

    HashMap<Integer, Boolean> checkgroup = new HashMap<>();
    TextView goToSignTxt;
    PullToRefershView refershView;

    private void initView(View rootView) {
        goToSignTxt = rootView.findViewById(R.id.txt_gotosign);
        goToSignTxt.setOnClickListener(this);
        refershView = (PullToRefershView) rootView.findViewById(R.id.list_goodschart);
        refershView.getRefreshView().setVisibility(View.GONE);
        refershView.setPullLoadEnabled(true);
        refershView.setPullRefreshEnabled(true);
        initRefreshView();
        mListGoodschart = refershView.getListView();
        mListAdapter = new ChartGoodsListAdapter(getActivity(), goodsDataList);
        initAdapter();
        mListGoodschart.setAdapter(mListAdapter);
        mRdbSelectallChart = (CheckBox) rootView.findViewById(R.id.rdb_selectall_chart);
        mTxtTopayMoney = (TextView) rootView.findViewById(R.id.txt_topay_money);
        mTxtPrivilege = (TextView) rootView.findViewById(R.id.txt_privilege);
        mBtnChartPay = (Button) rootView.findViewById(R.id.btn_chart_pay);
        mBtnChartPay.setOnClickListener(ShoppingChartFragment.this);
        mTxtClearallChart = (TextView) rootView.findViewById(R.id.txt_clearall_chart);
        mTxtClearallChart.setOnClickListener(ShoppingChartFragment.this);
        mRdbSelectallChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAllChecked = isChecked;
                int num = 0;
                for (int i = 0; i < checkgroup.size(); i++) {
                    Boolean aBoolean = checkgroup.get(i);
                    if (aBoolean) {
                        num++;
                    }
                }
                if (num < checkgroup.size()) {
                    if (isChecked) {
                        for (int i = 0; i < goodsDataList.size(); i++) {
                            checkgroup.put(i, true);
                        }
                        mListAdapter.setGoodsCheckMap(checkgroup);
                        mListAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (!isChecked) {
                        for (int i = 0; i < goodsDataList.size(); i++) {
                            checkgroup.put(i, false);
                        }
                        mListAdapter.setGoodsCheckMap(checkgroup);
                        mListAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }

    private void initRefreshView() {
        refershView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<LinearLayout>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<LinearLayout> refreshView) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<LinearLayout> refreshView) {

            }
        });
    }

    boolean isAllChecked = false;
    float goodsAccount = 0;

    private void initAdapter() {
        mListAdapter.setmInterfaceUtils(new FactoryUtils.OnChartChangeListener() {
            @Override
            public void onMinusNumListener(int position) {
                String num = "" + goodsDataList.get(position).get("GOODS_NUM"); //todo 数量
                Integer value = Integer.valueOf(num);
                if (value > 1) {
                    goodsDataList.get(position).put("GOODS_NUM", (value - 1) + "");
                    mListAdapter.notifyDataSetChanged();
                }

                String goodsPrice = goodsDataList.get(position).get("GOODS_PRICE") + "";
                if (checkgroup.get(position) && StringUtils.isNotEmpty(goodsPrice)) { //  && goodsAccount > 0f
                    goodsAccount -= Float.valueOf(goodsPrice);
                    mTxtTopayMoney.setText(goodsAccount + "");
                }
            }

            @Override
            public void onPlusNumListener(int position) {
                String num = "" + goodsDataList.get(position).get("GOODS_NUM"); //todo 数量
                Integer value = Integer.valueOf(num);
                goodsDataList.get(position).put("GOODS_NUM", (value + 1) + "");
                mListAdapter.notifyDataSetChanged();
                String goodsPrice = goodsDataList.get(position).get("GOODS_PRICE") + "";
                if (checkgroup.get(position) && StringUtils.isNotEmpty(goodsPrice)) {
                    goodsAccount += Float.valueOf(goodsPrice);
                    mTxtTopayMoney.setText(goodsAccount + "");
                }
            }

            @Override
            public void onRemoveGoodsListener(final int position) {
                OkUtils okUtils = OkUtils.getOkUtilsInstance();
                HashMap<String, String> params = new HashMap<>();
                params.put("SHOPPINGCAR_ID", goodsDataList.get(position).get("SHOPPINGCAR_ID") + "");
                params.put("USERNAME", userId);
                String url = okUtils.getUrl(Consts.ROOT_URL + Consts.REMOVE_CHART_GOODS, params);
                okUtils.httpGet(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), getString(R.string.pleasedolater), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //if (Consts.RESPONSE_OK_STATE.equals(response.message())) {
                        String string = response.body().string();
                        if (!TextUtils.isEmpty(string)) {
                            Map<String, Object> map = GsonUtils.parseJsonObject(string);
                            if ("1".equals(map.get("RESULT") + "")) {
                                goodsDataList.remove(position);
                                mHandler.sendEmptyMessage(0);
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(), getString(R.string.pleasedolater), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                    //}
                });
            }

            @Override
            public void onCheckedChangeListener(int position, boolean isChecked) {
                checkgroup.put(position, isChecked);
                int checkedNum = 0;
                for (int i = 0; i < checkgroup.size(); i++) {
                    boolean checked = checkgroup.get(i);
                    if (checked) {
                        checkedNum++;
                    }
                }

                if (checkedNum == checkgroup.size() && !mRdbSelectallChart.isChecked()) {
                    mRdbSelectallChart.setChecked(true);
                } else if (checkedNum < checkgroup.size() && mRdbSelectallChart.isChecked()) {
                    mRdbSelectallChart.setChecked(false);
                }
                mListAdapter.setGoodsCheckMap(checkgroup);
                mListAdapter.notifyDataSetChanged();
                Map<String, Object> map = goodsDataList.get(position);
                String priceStr = map.get("GOODS_PRICE") + "";
                int num = Integer.valueOf(mListAdapter.getMpMap().get(position).getText().toString());
                if (isChecked) {
                    if (StringUtils.isNotEmpty(priceStr)) {
                        float goodsPrice = Float.valueOf(priceStr);
                        goodsAccount += goodsPrice * num;
                        Log.e("goodprice0", goodsPrice + "---" + num);
                        mTxtTopayMoney.setText(goodsAccount + "");
                    }

                } else {
                    if (StringUtils.isNotEmpty(priceStr)) {
                        float goodsPrice = Float.valueOf(priceStr);
                        goodsAccount -= goodsPrice * num;
                        Log.e("goodprice0", goodsPrice + "---" + num);
                        if (goodsAccount <= 0f) {
                            mTxtTopayMoney.setText("0.0");
                        } else
                            mTxtTopayMoney.setText(goodsAccount + "");
                    }
                }
            }
        });
    }
}
