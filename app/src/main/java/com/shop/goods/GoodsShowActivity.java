package com.shop.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.Consts;
import com.shop.MainActivity;
import com.shop.R;
import com.shop.shopchart.EnsureOrderActivity;
import com.shop.util.FactoryUtils;
import com.shop.util.GlideUtils;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;
import com.shop.util.StringUtils;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GoodsShowActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mBackTop;
    protected TextView mTitleTop;
    protected Banner mBannerGoodsshow;
    protected TextView mTxtTitleGoodsshow;
    protected TextView mTxtPriceGoodsshow;
    protected TextView mGuildprice;
    protected TextView mTxtSelectmodel;
    protected TextView mTxtSeecomment;
    protected LinearLayout mContentGoodsdetail;
    protected Button mBtnTochart;
    protected Button mBtnBynow;
    protected LinearLayout mLinearGoodsshow;
    protected ImageView mImgRight;
    private GoodSelectPopWindow mSelectPopWindow;
    private String goodsId;
    public static final String FROM = "from";
    public static final String FROM_NOMAL_LIST = "nomallist";
    public static final String FROM_TIME_LIST = "timelist"; // 抢购列表
    String defaultAttribute = "";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:
                    mTxtTitleGoodsshow.setText(map.get("GOODS_NAME") + "");
                    mTxtPriceGoodsshow.setText(map.get("GOODS_MINPRICE") + "");
                    mGuildprice.setText(StringUtils.isEmpty(map.get("MARKET_PRICE") + "") ? "" : map.get("MARKET_PRICE") + "");
                    defaultAttribute = map.get("GOODS_MINATTRIBUTEID") + "";
                    String goodsPicUrl = map.get("GOODS_PIC") + "";
                    if (StringUtils.isNotEmpty(goodsPicUrl)) {
                        if (goodsPicUrl.contains(",")) {
                            String[] split = goodsPicUrl.split(",");
                            for (int i = 0; i < split.length; i++) {
                                imageUrls.add(split[i]);
                            }
                        } else {
                            imageUrls.add(goodsPicUrl);
                        }
                        mBannerGoodsshow.setImages(imageUrls);
                        mBannerGoodsshow.start();
                        initDetailsImage();
                    }
                    break;
            }
        }
    };

    private void initDetailsImage() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;
        String detailspic = (String) map.get("GOODS_DETAILSPIC");
        List<String> detailPicList = new ArrayList<>();
        if (StringUtils.isNotEmpty(detailspic)) {
            if (detailspic.contains(",")) {
                String[] split = detailspic.split(",");
                for (int i = 0; i < split.length; i++) {
                    detailPicList.add(split[i]);
                }
            } else {
                detailPicList.add(detailspic);
            }
            for (int i = 0; i < detailPicList.size(); i++) {
                ImageView imageView = new ImageView(GoodsShowActivity.this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setMaxHeight(screenWidth*3);
                imageView.setMaxWidth(screenWidth);
                imageView.setAdjustViewBounds(true);
                GlideUtils.loadImageOrGif(GoodsShowActivity.this, Consts.ROOT_URL + detailPicList.get(i), imageView);
                mContentGoodsdetail.addView(imageView);
            }
        }
    }

    private String userName, userId;
    private String mFromState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_goods_show);
        Intent intent = getIntent();
        goodsId = intent.getStringExtra(GoodsListActivity.GOOD_ID);
        mFromState = intent.getStringExtra(FROM);
        userName = (String) PreferenceUtil.getParam(PreferenceUtil.USERNAME, "");
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
        initView();
        initData();
    }

    Map<String, Object> map = new HashMap<>();

    int showPopFlag = 0;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            onBackPressed();
        } else if (view.getId() == R.id.txt_selectmodel) {
            showPopFlag = 1;
            if (mSelectPopWindow != null)
                mSelectPopWindow = null;
            mSelectPopWindow = new GoodSelectPopWindow(this, map.get("GOODS_ID") + "", defaultAttribute);
            mSelectPopWindow.showAtLocation(mLinearGoodsshow, Gravity.BOTTOM, 0, 0);
        } else if (view.getId() == R.id.txt_seecomment) {
            Intent intent = new Intent(GoodsShowActivity.this, GoodsCommentActivity.class);
            intent.putExtra("GOODS_ID", map.get("GOODS_ID") + "");
            startActivity(intent);
        } else if (view.getId() == R.id.btn_tochart) {
            showPopFlag = 2;
            if (mSelectPopWindow == null) {
                mSelectPopWindow = new GoodSelectPopWindow(this, map.get("GOODS_ID") + "", defaultAttribute);
                mSelectPopWindow.setModelData(modelList);
                mSelectPopWindow.setGoodName(map.get("GOODS_NAME") + "", imageUrls.size() > 0 ? imageUrls.get(0) : "");
            }
            mSelectPopWindow.showAtLocation(mLinearGoodsshow, Gravity.BOTTOM, 0, 0);
            settingPopWindow();
        } else if (view.getId() == R.id.btn_bynow) {
            showPopFlag = 3;
            if (mSelectPopWindow == null) {
                mSelectPopWindow = new GoodSelectPopWindow(this, map.get("GOODS_ID") + "", defaultAttribute);
                mSelectPopWindow.setModelData(modelList);
                mSelectPopWindow.setGoodName(map.get("GOODS_NAME") + "", imageUrls.size() > 0 ? imageUrls.get(0) : "");
            }
            mSelectPopWindow.showAtLocation(mLinearGoodsshow, Gravity.BOTTOM, 0, 0);
            settingPopWindow();
        } else if (view.getId() == R.id.img_right) {
            Intent intent = new Intent(GoodsShowActivity.this, MainActivity.class);
            intent.putExtra("tochart", 1);
            startActivity(intent);
        }
    }

    List<Map<String, Object>> modelList = new ArrayList<>();

    private void initModelData() {
        OkUtils okUtils = OkUtils.getOkUtilsInstance();
        HashMap<String, String> params = new HashMap<>();
        params.put("GOODS_ID", goodsId);
        String url = okUtils.getUrl(Consts.ROOT_URL + Consts.GET_GOODS_ATTRIBUTES, params);
        okUtils.setNewClient().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // if (Consts.RESPONSE_OK_STATE.equals(response.message())) {
                String string = response.body().string();
                Log.i("attribute", string);
                if (!TextUtils.isEmpty(string)) {
                    modelList = GsonUtils.parseArrayGson(string);
                }
            }
            //}
        });
    }

    Map<String, Object> selectedGoods = new HashMap<>();

    private void settingPopWindow() {
        mSelectPopWindow.setSelectModelListener(new FactoryUtils.OnGoodsSelectModelListener() {
            @Override
            public void getSelectedModelData(Map<String, Object> selectedData) {
                mSelectPopWindow.dismiss();
                selectedGoods = selectedData;
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(GoodsShowActivity.this, getString(R.string.please_signin), Toast.LENGTH_SHORT).show();
                } else {
                    if (showPopFlag == 2) {
                        // 加入购物车 todo
                        try {
                            addGoodsToChart();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (showPopFlag == 3) {
                        // 直接购买 todo
                        Intent intent = new Intent(GoodsShowActivity.this, EnsureOrderActivity.class);
                        intent.putExtra(EnsureOrderActivity.ENSURE_BY_GOODS, string);
                        intent.putExtra(EnsureOrderActivity.FROM, EnsureOrderActivity.GOODS_SHOW);
                        intent.putExtra("goodsAttributeId", selectedGoods.get("ATTRIBUTE_ID") + "");
                        intent.putExtra("price", selectedGoods.get("ATTRIBUTEPRICE") + "");
                        intent.putExtra("goodsnum", selectedGoods.get("ATTRIBUTENUM") + "");
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void addGoodsToChart() throws JSONException {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("GOODS_ID", selectedGoods.get("GOODS_ID"));
        jsonRequest.put("GOODS_NUM", selectedGoods.get("ATTRIBUTENUM"));
        jsonRequest.put("GOODS_NAME", map.get("GOODS_NAME"));
        jsonRequest.put("GOODS_PRICE", selectedGoods.get("ATTRIBUTEPRICE"));
        jsonRequest.put("GOODS_ATTRIBUTE", selectedGoods.get("ATTRIBUTE"));
        jsonRequest.put("GOODS_ATTRIBUTE_ID", selectedGoods.get("ATTRIBUTE_ID"));
        jsonRequest.put("USERNAME", userId);
        OkUtils.getOkUtilsInstance().setNewClient().httpPostJson(jsonRequest, Consts.ROOT_URL + Consts.ADD_CHART_GOODS, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoodsShowActivity.this, getString(R.string.pleasedolater), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("addtochart", string);
                if (!TextUtils.isEmpty(string)) {
                    Map<String, Object> map = GsonUtils.parseJsonObject(string);
                    if ("1".equals(map.get("RESULT") + "")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GoodsShowActivity.this, getString(R.string.addchart_sucess), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GoodsShowActivity.this, getString(R.string.pleasedolater), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    private void initView() {
        mBackTop = (ImageView) findViewById(R.id.back_top);
        mBackTop.setOnClickListener(GoodsShowActivity.this);
        mTitleTop = (TextView) findViewById(R.id.title_top);
        mTitleTop.setText(getString(R.string.goodsshow));
        mBannerGoodsshow = (Banner) findViewById(R.id.banner_goodsshow);
        mTxtTitleGoodsshow = (TextView) findViewById(R.id.txt_title_goodsshow);
        mTxtPriceGoodsshow = (TextView) findViewById(R.id.txt_price_goodsshow);
        mGuildprice = (TextView) findViewById(R.id.guildprice);
        mGuildprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        mTxtSelectmodel = (TextView) findViewById(R.id.txt_selectmodel);
        mTxtSelectmodel.setOnClickListener(GoodsShowActivity.this);
        mTxtSeecomment = (TextView) findViewById(R.id.txt_seecomment);
        mTxtSeecomment.setOnClickListener(GoodsShowActivity.this);
        mContentGoodsdetail = (LinearLayout) findViewById(R.id.content_goodsdetail);
        mBtnTochart = (Button) findViewById(R.id.btn_tochart);
        mBtnTochart.setOnClickListener(GoodsShowActivity.this);
        mBtnBynow = (Button) findViewById(R.id.btn_bynow);
        mBtnBynow.setOnClickListener(GoodsShowActivity.this);
        mLinearGoodsshow = (LinearLayout) findViewById(R.id.linear_goodsshow);
        initBannerShow();
       /* if (FROM_TIME_LIST.equals(mFromState)) {
            mBtnTochart.setVisibility(View.GONE);
            mBtnBynow.setText(getString(R.string.by_now));
        }*/
        mImgRight = (ImageView) findViewById(R.id.img_right);
        mImgRight.setOnClickListener(GoodsShowActivity.this);
        mImgRight.setVisibility(View.VISIBLE);
        mImgRight.setImageResource(R.mipmap.chart_icon);
    }

    String string;

    private void initData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("GOODS_ID", goodsId);
        String url = OkUtils.getOkUtilsInstance().getUrl(Consts.ROOT_URL + Consts.GET_SINGLE_GOODS, params);
        OkUtils.getOkUtilsInstance().setNewClient().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                string = response.body().string();
                Log.i("goods", string);
                // if (Consts.RESPONSE_OK_STATE == response.message()) {
                if (!TextUtils.isEmpty(string)) {
                    map = GsonUtils.parseJsonObject(string);
                    mHandler.sendEmptyMessage(0);
                    initModelData();
                }
            }
            // }
        });
    }

    List<String> imageUrls = new ArrayList<>();

    private void initBannerShow() {
        mBannerGoodsshow.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                GlideUtils.loadImageOrGif(context, Consts.ROOT_URL + path, imageView);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
