package com.shop.personspace;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.shop.Consts;
import com.shop.R;
import com.shop.customize.pullrefresh.PullToRefershView;
import com.shop.customize.pullrefresh.PullToRefreshBase;
import com.shop.goods.SelectPayTypeActivity;
import com.shop.shopchart.EnsureOrderActivity;
import com.shop.util.FactoryUtils;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;
import com.shop.util.StringUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ToBePayListActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView backTop;
    protected TextView titleTop;
    protected ListView listWuliu;
    protected PullToRefershView mPullToRefershView;
    protected TextView txtOrderReminder;
    private OrderListAdapter mAdapter;
    private String url = Consts.ROOT_URL + Consts.GET_ORDER_LIST;
    private String userName, userId;
    public static final String ORDER_DATA_KEY = "orderdata";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            mPullToRefershView.onPullDownRefreshComplete();
            mPullToRefershView.onPullUpRefreshComplete();
            switch (what) {
                case 0:
                    if (dataList.size() > 0) {
                        mAdapter.setDataList(dataList);
                        mAdapter.notifyDataSetChanged();

                    }
                    break;
                case EnsureOrderActivity.STARTZHIFUBAO:
                    initZhiFuBaoPay();
                    break;
                case EnsureOrderActivity.ZHIFUBAORESULT:
                    String s = msg.obj.toString();
                    Log.e("zhifubaoresult", s);
                    break;
                case 2:
                    Toast.makeText(ToBePayListActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    mPullToRefershView.doPullRefreshing(true, 500);
                    break;
                case 3:
                    Toast.makeText(ToBePayListActivity.this, "操作失败，请重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private String mAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_to_be_pay_list);
        userName = (String) PreferenceUtil.getParam(PreferenceUtil.USERNAME, "");
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
        Intent intent = getIntent();
        mAll = intent.getStringExtra("all");
        initView();
        if (StringUtils.isNotEmpty(userName)) {
            mPullToRefershView.doPullRefreshing(true, 500);
        } else {

        }

    }

    private void initData() {
        OkUtils okUtils = OkUtils.getOkUtilsInstance();
        HashMap<String, String> params = new HashMap<>();
        params.put("USER_ID", userId);
        if (!"1".equals(mAll))
            params.put("status", Consts.TOPAYSTATE);
        url = okUtils.getUrl(this.url, params);
        okUtils.setNewClient().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("topaylist", string);
                if (StringUtils.isNotEmpty(string)) {
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(string);
                    //if (maps != null && maps.size() > 0) {
                    dataList.clear();
                    dataList.addAll(maps);
                    mHandler.sendEmptyMessage(0);
                    // }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            finish();
        }
    }

    List<Map<String, Object>> dataList = new ArrayList<>();

    private void initView() {
        backTop = (ImageView) findViewById(R.id.back_top);
        backTop.setOnClickListener(ToBePayListActivity.this);
        titleTop = (TextView) findViewById(R.id.title_top);
        if ("1".equals(mAll))
            titleTop.setText("全部订单");
        else
            titleTop.setText(getString(R.string.tobepay));
        mPullToRefershView = (PullToRefershView) findViewById(R.id.list_wuliu);
        mPullToRefershView.setPullLoadEnabled(false);
        mPullToRefershView.setPullRefreshEnabled(true);
        listWuliu = mPullToRefershView.getListView();
        mPullToRefershView.getRefreshView().setVisibility(View.GONE);
        ColorDrawable divider = new ColorDrawable();
        divider.setColor(getResources().getColor(R.color.gray_transparent));
        listWuliu.setDivider(divider);
        listWuliu.setDividerHeight(8);
        mAdapter = new OrderListAdapter(this, dataList, Consts.TO_BE_PAY_ORDER);
        listWuliu.setAdapter(mAdapter);
        initPullToRefresh();
        txtOrderReminder = (TextView) findViewById(R.id.txt_order_reminder);
    }

    private void initPullToRefresh() {
        mPullToRefershView.setPullRefreshEnabled(true);
        mPullToRefershView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<LinearLayout>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<LinearLayout> refreshView) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<LinearLayout> refreshView) {

            }
        });
        mAdapter.setItemClick(new FactoryUtils.OnOrderListItemClick() {

            @Override
            public void onSeeOrderDetailsClick(int position) {
                Intent intent = new Intent();
                intent.setClass(ToBePayListActivity.this, OrderDetailActivity.class);
                try {
                    String value = GsonUtils.mapToArray(dataList.get(position)).toString();
                    intent.putExtra(ORDER_DATA_KEY, value);
                    Log.e("data", position + "---" + value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                startActivity(intent);
                //Toast.makeText(ToBePayListActivity.this, "seedetails", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBtnOperateClick(int position) {
                super.onBtnOperateClick(position);
                //  topay
                startActivityForResult(new Intent(ToBePayListActivity.this, SelectPayTypeActivity.class), EnsureOrderActivity.REQUEST_PAY_TYPE);
            }

            @Override
            public void onBtnCancelClick(final int position) {
                super.onBtnCancelClick(position);
                new AlertDialog.Builder(ToBePayListActivity.this)
                        .setMessage("确定取消该订单？")
                        .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                cancleOrder(position);
                            }
                        }).setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }

    private void cancleOrder(int position) {
        Map<String, Object> objectMap = dataList.get(position);
        String orderId = objectMap.get("order_id") + "";
        // todo cancelorder
        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        HashMap<String, String> params = new HashMap<>();
        params.put("status", "99");
        params.put("order_id", orderId);
        String url = okUtils.getUrl(Consts.ROOT_URL + Consts.CANCEL_ORDER, params);
        okUtils.httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(3);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (StringUtils.isNotEmpty(string)) {
                    Map<String, Object> map = GsonUtils.parseJsonObject(string);
                    if ("1".equals(map.get("result") + "")) {
                        mHandler.sendEmptyMessage(2);
                        return;
                    }
                }
                mHandler.sendEmptyMessage(3);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EnsureOrderActivity.REQUEST_PAY_TYPE && resultCode == RESULT_OK) {
            int payType = data.getIntExtra("payType", 0);
            switch (payType) {
                case EnsureOrderActivity.WECHAT_PAY:
                    // initWeChatPay();
                    break;
                case EnsureOrderActivity.ZHIFUBAO_PAY:
                    prepareForZhiFuBaoPay();
                    //initZhiFuBaoPay();
                    break;
            }
        }
    }

    String orderInfo;

    public void prepareForZhiFuBaoPay() {

        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        okUtils.httpGet(Consts.ROOT_URL + Consts.ALIPAY_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("alipay", string);
                if (StringUtils.isNotEmpty(string)) {
                    // int i = string.indexOf("&");
                    orderInfo = string;//string.substring(i + 1, string.length());
                    mHandler.sendEmptyMessage(EnsureOrderActivity.STARTZHIFUBAO);
                }
            }
        });
    }

    private void initZhiFuBaoPay() {
        //appendZhiFuBaoParams();
        Log.e("zhifubaoparam", orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ToBePayListActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = EnsureOrderActivity.ZHIFUBAORESULT;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
