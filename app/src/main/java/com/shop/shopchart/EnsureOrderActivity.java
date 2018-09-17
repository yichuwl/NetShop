package com.shop.shopchart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.shop.Consts;
import com.shop.R;
import com.shop.goods.AddrListActivity;
import com.shop.goods.SelectPayTypeActivity;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;
import com.shop.util.StringUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EnsureOrderActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mImageBackOrder;
    protected RecyclerView mRecyOrder;
    protected TextView mTextOrderMoney;
    protected Button mBtnEnsuretopay;
    protected TextView mTxtAddrShow;
    protected Spinner couponSpinner;
    protected TextView textOrderWuliumoney;
    private EnsureOrderAdapter mOrderAdapter;
    protected String addrTxt;
    public static final String ENSURE_BY_GOODS = "ensureby";
    public static final String FROM = "from";
    public static final String GOODS_SHOW = "goodsshow";
    public static final String CHART = "chart";
    private String mFronFlag;
    private String mDataString;
    private String mIsTimeGoods;
    public static final int REQUEST_PAY_TYPE = 0x234;
    public static final int WECHAT_PAY = 0x112;
    public static final int ZHIFUBAO_PAY = 0x113;
    public static final int ORDERCOUNT = 0x114;
    public static final int ADDRLIST = 0x115;
    public static final int ADDRLISTREQUEST = 0x116;
    public static final int SAVE_ORDER = 0x117;
    public static final int STARTZHIFUBAO = 0x118;
    public static final int ZHIFUBAORESULT = 0x119;
    List<Map<String, Object>> maps = new ArrayList<>();
    String addrStr = "";
    Map<String, Object> orderResultData = new HashMap<>();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case ORDERCOUNT:
                    Bundle data = msg.getData();
                    String string = data.getString(ORDERCOUNT + "");
                    Map<String, Object> map = GsonUtils.parseJsonObject(string);
                    if ("0".equals(map.get("result") + "")) {
                        Toast.makeText(EnsureOrderActivity.this, map.get("message") + "", Toast.LENGTH_SHORT).show();
                    } else {
                        String totalCount = map.get("ORDER_TOTAL") + "";
                        mTextOrderMoney.setText("￥ " + totalCount);
                        String defaultConponId = map.get("CHECKCOUPON_ID") + "";
                        String couponlist = map.get("COUPONLIST").toString();
                        maps = GsonUtils.parseArrayGson(couponlist);
                        spinnerAdapter.setSpinnerList(maps);
                        spinnerAdapter.notifyDataSetChanged();
                        for (int i = 0; i < maps.size(); i++) {
                            if (defaultConponId.equals(maps.get(i).get("COUPON_ID"))) {
                                couponSpinner.setSelection(i + 1);
                                break;
                            }
                        }
                        String wuliuMoney = map.get("FREIGHT_PRICE") + "";
                        textOrderWuliumoney.setText("运费：" + wuliuMoney);
                    }

                    break;
                case ADDRLIST:
                    Bundle bundle = msg.getData();
                    addrStr = bundle.getString("addr");
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(addrStr);
                    int j = 0;
                    for (int i = 0; i < maps.size(); i++) {
                        j++;
                        Map<String, Object> object = maps.get(i);
                        if ("1".equals(object.get("IS_DEFAULT") + "")) {
                            String addrCity = object.get("ADDR_CITY") + "";
                            String addrDetial = object.get("ADDR_DETAILS") + "";
                            String userName = object.get("ADDR_REALNAME") + "";
                            String phone = object.get("ADDR_PHONE") + "";
                            addrId = object.get("ADDRESS_ID") + "";
                            String s = new StringBuilder().append(addrCity).append(" ").append(addrDetial)
                                    .append("\n").append(userName).append("   ").append(phone).toString();
                            mTxtAddrShow.setText(s);
                            break;
                        }
                    }
                    if (j == maps.size() && maps.size() > 0) {
                        Map<String, Object> object = maps.get(0);
                        String addrCity = object.get("ADDR_CITY") + "";
                        String addrDetial = object.get("ADDR_DETAILS") + "";
                        String userName = object.get("ADDR_REALNAME") + "";
                        String phone = object.get("ADDR_PHONE") + "";
                        addrId = object.get("ADDRESS_ID") + "";
                        String s = new StringBuilder().append(addrCity).append(" ").append(addrDetial)
                                .append("\n").append(userName).append("   ").append(phone).toString();
                        mTxtAddrShow.setText(s);
                    }
                    break;
                case SAVE_ORDER:
                    Bundle data1 = msg.getData();
                    String saveOrder = data1.getString("saveOrder");
                    if (StringUtils.isNotEmpty(saveOrder)) {
                        orderResultData = GsonUtils.parseJsonObject(saveOrder);
                        if (!"1".equals(orderResultData.get("result") + "")) {
                            Toast.makeText(EnsureOrderActivity.this, "提交失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivityForResult(new Intent(EnsureOrderActivity.this, SelectPayTypeActivity.class), REQUEST_PAY_TYPE);
                        }
                    } else {
                        Toast.makeText(EnsureOrderActivity.this, "提交失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case STARTZHIFUBAO:
                    initZhiFuBaoPay();
                    break;
                case ZHIFUBAORESULT:
                    String s = msg.obj.toString();
                    Log.e("zhifubaoresult", s);
                    break;
            }
        }
    };
    String userId = "";
    String attributeId = "";
    String price = "";
    String goodsNum = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_ensure_order);
        Intent intent = getIntent();
        mFronFlag = intent.getStringExtra(FROM);
        mDataString = intent.getStringExtra(ENSURE_BY_GOODS);
        attributeId = intent.getStringExtra("goodsAttributeId");
        price = intent.getStringExtra("price");
        goodsNum = intent.getStringExtra("goodsnum");
        initView();
        addrTxt = (String) PreferenceUtil.getParam(PreferenceUtil.ADDR, "");
        if (StringUtils.isEmpty(addrTxt)) {
            mTxtAddrShow.setText(getString(R.string.new_addr));
        } else {
            mTxtAddrShow.setText(addrTxt);
        }
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
        initAddrData();
        initData();
    }

    private void initAddrData() {
        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        String url = Consts.ROOT_URL + Consts.GET_ADDR_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("USER_ID", userId);
        url = okUtils.getUrl(url, params);
        okUtils.httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("addrlist", string);
                if (StringUtils.isNotEmpty(string)) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString("addr", string);
                    message.setData(data);
                    message.what = ADDRLIST;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    List<Map<String, Object>> dataList = new ArrayList<>();

    private void initData() {
        if (GOODS_SHOW.equals(mFronFlag)) {
            Map<String, Object> map = GsonUtils.parseJsonObject(mDataString);
            map.put("GOODS_PRICE", price);
            map.put("GOODS_NUM", goodsNum);
            dataList.add(map);
            mOrderAdapter.setDataList(dataList);
            mOrderAdapter.notifyDataSetChanged();
            // mOrderAdapter.setIsTimeGoods(mIsTimeGoods);
            goodsIdParam.append(map.get("GOODS_ID") + "");
            attributeIdParam.append(attributeId);
            goodsCount.append(map.get("GOODS_NUM") + "");
        } else if (CHART.equals(mFronFlag)) {
            List<Map<String, Object>> maps = GsonUtils.parseArrayGson(mDataString);
            dataList.addAll(maps);
            mOrderAdapter.setDataList(dataList);
            mOrderAdapter.notifyDataSetChanged();

           /* if (maps != null && maps.size() > 0)
                dataList.addAll(maps);*/
            if (dataList.size() == 1) {
                Map<String, Object> map = dataList.get(0);
                goodsIdParam.append(map.get("GOODS_ID") + "");
                attributeIdParam.append(map.get("GOODS_ATTRIBUTE_ID") + "");
                goodsCount.append(map.get("GOODS_NUM") + "");
            } else {
                for (int i = 0; i < dataList.size(); i++) {
                    Map<String, Object> map = dataList.get(i);
                    goodsIdParam.append(map.get("GOODS_ID") + ",");
                    attributeIdParam.append(map.get("GOODS_ATTRIBUTE_ID") + ",");
                    goodsCount.append(map.get("GOODS_NUM") + ",");
                }
            }
        }
        httpGetData();
    }

    StringBuilder goodsIdParam = new StringBuilder();
    StringBuilder attributeIdParam = new StringBuilder();
    StringBuilder goodsCount = new StringBuilder();
    String couponId = "";

    private void httpGetData() {
        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        String url = Consts.ROOT_URL + Consts.ORDER_TOTAL;
        HashMap<String, String> params = new HashMap<>();
        params.put("GOODS_ID", goodsIdParam.toString());
        params.put("GOODS_COUNT", goodsCount.toString());
        params.put("ATTRIBUTE_DETAIL_ID", attributeIdParam.toString());
        params.put("USER_ID", userId);
        params.put("COUPON_ID", couponId);
        url = okUtils.getUrl(url, params);
        okUtils.httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("ensureorder", string);
                if (StringUtils.isNotEmpty(string)) {
                    Message message = new Message();
                    message.what = ORDERCOUNT;
                    Bundle data = new Bundle();
                    data.putString(ORDERCOUNT + "", string);
                    message.setData(data);
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    String addrId = "3440fdcc3b8346e1bc54907006168997";

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.image_back_order) {
            finish();
        } else if (view.getId() == R.id.btn_ensuretopay) {
            // todo 支付
            if (StringUtils.isEmpty(addrId)) {
                Toast.makeText(this, getString(R.string.please_new_addr), Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                saveOrder();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(EnsureOrderActivity.this, SelectPayTypeActivity.class);
            // startActivityForResult(intent, REQUEST_PAY_TYPE);
        } else if (view.getId() == R.id.txt_addr_show) {
            startActivityForResult(new Intent(EnsureOrderActivity.this, AddrListActivity.class), 0x1);
        }
    }

    private void saveOrder() throws JSONException {
        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("ADDRESS_ID", addrId);
        jsonRequest.put("GOODS_ID", goodsIdParam.toString());
        jsonRequest.put("ATTRIBUTE_DETAIL_ID", attributeIdParam.toString());
        jsonRequest.put("GOODS_COUNT", goodsCount.toString());
        jsonRequest.put("USER_ID", userId);
        jsonRequest.put("COUPON_ID", couponId);
        okUtils.httpPostJson(jsonRequest, Consts.ROOT_URL + Consts.SAVE_ORDER, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("saveorder", string);
                Message message = new Message();
                Bundle data = new Bundle();
                data.putString("saveOrder", string);
                message.setData(data);
                message.what = SAVE_ORDER;
                mHandler.sendMessage(message);
            }
        });
    }

    SpinnerAdapter spinnerAdapter;

    private void initView() {
        mImageBackOrder = (ImageView) findViewById(R.id.image_back_order);
        mImageBackOrder.setOnClickListener(EnsureOrderActivity.this);
        mRecyOrder = (RecyclerView) findViewById(R.id.recy_order);
        mRecyOrder.setLayoutManager(new LinearLayoutManager(this));
        mOrderAdapter = new EnsureOrderAdapter(this);
        mRecyOrder.setAdapter(mOrderAdapter);
        mTextOrderMoney = (TextView) findViewById(R.id.text_order_money);
        mBtnEnsuretopay = (Button) findViewById(R.id.btn_ensuretopay);
        mBtnEnsuretopay.setOnClickListener(EnsureOrderActivity.this);
        mOrderAdapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(EnsureOrderActivity.this, AddressActivity.class);
                startActivityForResult(intent, 0x1);*/
            }
        });
        mTxtAddrShow = (TextView) findViewById(R.id.txt_addr_show);
        mTxtAddrShow.setOnClickListener(EnsureOrderActivity.this);
        couponSpinner = (Spinner) findViewById(R.id.coupon_spinner);
        spinnerAdapter = new SpinnerAdapter();
        spinnerAdapter.setSpinnerList(maps);
        couponSpinner.setAdapter(spinnerAdapter);
        couponSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    couponId = (String) maps.get(position - 1).get("COUPON_ID");
                httpGetData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        textOrderWuliumoney = (TextView) findViewById(R.id.text_order_wuliumoney);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            // TODO 赋值
            //initAddrData();
            String addr = data.getStringExtra("addr");
            if (StringUtils.isNotEmpty(addr)) {
                Map<String, Object> object = GsonUtils.parseJsonObject(addr);
                String addrCity = object.get("ADDR_CITY") + "";
                String addrDetial = object.get("ADDR_DETAILS") + "";
                String userName = object.get("REALNAME") + "";
                String phone = object.get("PHONE") + "";
                addrId = object.get("ADDRESS_ID") + "";
                String s = new StringBuilder().append(addrCity).append(" ").append(addrDetial)
                        .append("\n").append(userName).append("   ").append(phone).toString();
                mTxtAddrShow.setText(s);
            }
        } else if (requestCode == REQUEST_PAY_TYPE && resultCode == RESULT_OK) {
            int payType = data.getIntExtra("payType", 0);
            switch (payType) {
                case WECHAT_PAY:
                    // initWeChatPay();
                    break;
                case ZHIFUBAO_PAY:
                    prepareForZhiFuBaoPay();
                    //initZhiFuBaoPay();
                    break;
            }
        }
    }

    static String string;

    public void prepareForZhiFuBaoPay() {

        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        okUtils.httpGet(Consts.ROOT_URL + Consts.ALIPAY_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                string = response.body().string();
                Log.i("alipay", string);
                if (StringUtils.isNotEmpty(string)) {
                    // int i = string.indexOf("&");
                    orderInfo = string;//string.substring(i + 1, string.length());
                    mHandler.sendEmptyMessage(STARTZHIFUBAO);
                }
            }
        });
    }

    String partnerId, prepayId, nonceStr, timeStamp, sign;

    private IWXAPI iwxapi; //微信支付api

    private void initWeChatPay() {
        iwxapi = WXAPIFactory.createWXAPI(this, null); //初始化微信api
        iwxapi.registerApp(Consts.WECHAT_APP_ID); //注册appid  appid可以在开发平台获取

        Runnable payRunnable = new Runnable() {  //这里注意要放在子线程
            @Override
            public void run() {
                PayReq request = new PayReq(); //调起微信APP的对象
                //下面是设置必要的参数，也就是前面说的参数,这几个参数从何而来请看上面说明
                request.appId = Consts.WECHAT_APP_ID;
                request.partnerId = partnerId;
                request.prepayId = prepayId;
                request.packageValue = "Sign=WXPay";
                request.nonceStr = nonceStr;
                request.timeStamp = timeStamp;
                request.sign = sign;
                iwxapi.sendReq(request);//发送调起微信的请求
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    static String orderInfo = "";   // 订单信息
    public static final String ZHIFUBAO_APP_ID = "2018070460502323";
    public static final String ZHIFUBAO_NOTIFYURL = "";

    private void appendZhiFuBaoParams() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = dateFormat.format(date);
        StringBuilder stringBuilder = new StringBuilder();
        orderInfo = stringBuilder.append("app_id=").append(ZHIFUBAO_APP_ID)
                .append("&method=alipay.trade.app.pay&")
                .append("format=").append("").append("charset=utf-8&")
                .append("sign=").append("").append("&timestamp=")
                .append(timeStamp).append("&version=1.0&")
                .append("notify_url=").append(ZHIFUBAO_NOTIFYURL)
                .append("&bizcontent=").toString();
    }

    private void initZhiFuBaoPay() {
        //appendZhiFuBaoParams();
        Log.e("zhifubaoparam", orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(EnsureOrderActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = ZHIFUBAORESULT;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    class SpinnerAdapter extends BaseAdapter {
        public void setSpinnerList(List<Map<String, Object>> spinnerList) {
            this.spinnerList = spinnerList;
        }

        List<Map<String, Object>> spinnerList = new ArrayList<>();

        @Override
        public int getCount() {
            return spinnerList.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            if (position != 0)
                return spinnerList.get(position);
            else
                return 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(EnsureOrderActivity.this, R.layout.text, null);
            TextView textView = convertView.findViewById(R.id.text_only);
            if (position == 0) {
                textView.setText("请选择优惠券");
            } else {
                Map<String, Object> map = spinnerList.get(position - 1);
                String couponPrice = map.get("COUPON_PRICE") + "";
                couponPrice = StringUtils.isEmpty(couponPrice) ? "" : couponPrice;
                if (StringUtils.isEmpty(couponPrice))
                    textView.setText(map.get("COUPON_NAME") + "");
                else
                    textView.setText(map.get("COUPON_NAME") + ":  " + couponPrice + "元");
            }
            return convertView;
        }
    }
}
