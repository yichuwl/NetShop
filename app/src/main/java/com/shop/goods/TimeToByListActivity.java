package com.shop.goods;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.util.FactoryUtils;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TimeToByListActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mBackTop;
    protected TextView mTitleTop;
    protected ImageView mImgRight;
    protected TextView mTxtTimePeriod;
    protected TextView mTxtTimeState;
    protected TextView mTxtTimeHour;
    protected TextView mTxtTimeMinute;
    protected TextView mTxtTimeSecond;
    protected ListView mListTimetoby;
    protected TextView mTxtNodata;
    long countDownsecond = 0;
    private String mIsNotStart;
    private boolean isNotStart = true;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:
                    if (mTxtNodata.getVisibility() == View.VISIBLE) {
                        mTxtNodata.setVisibility(View.GONE);
                    }
                    StringBuilder timePeriod = new StringBuilder()
                            .append(headerDataMap.get("START_TIME"))
                            .append(" - ").append(headerDataMap.get("END_TIME"));
                    mTxtTimePeriod.setText(timePeriod);
                    String countdown = headerDataMap.get("COUNTDOWN") + "";
                    if (StringUtils.isNotEmpty(countdown)) {
                        countDownsecond = Long.valueOf(countdown);
                        initTime(countdown);
                        mTxtTimeHour.setText(getLongTxt(hour));
                        mTxtTimeMinute.setText(getLongTxt(minute));
                        mTxtTimeSecond.setText(getLongTxt(second));
                        startCountDownTimer();
                        mCountDownTimer.start();
                    }
                    mIsNotStart = headerDataMap.get("ISNOTSTART") + "";
                    getGoodsList(headerDataMap.get("SECKILL_ID") + "");
                    if ("1".equals(headerDataMap.get("ISNOTSTART") + "")) {
                        mTxtTimeState.setText(getString(R.string.shopping_later));
                        isNotStart = true;
                    } else {
                        isNotStart = false;
                        mTxtTimeState.setText(getString(R.string.shooping_now));
                    }
                    break;
                case 1:
                    mTxtNodata.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mTxtNodata.setVisibility(View.GONE);
                    mGoodsListAdapter.setNotStart(isNotStart);
                    mGoodsListAdapter.setDataList(goodsDataList);
                    mGoodsListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private TimeToByListAdapter mGoodsListAdapter;

    private void getGoodsList(String id) {
        OkUtils okUtils = OkUtils.getOkUtilsInstance();
        HashMap<String, String> params = new HashMap<>();
        params.put("SECKILL_ID", id);
        String url = okUtils.getUrl(Consts.ROOT_URL + Consts.GET_TIME_TO_BY_GOODS_LIST, params);
        okUtils.setNewClient().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("timegoodslist", "--" + string);
                if (StringUtils.isNotEmpty(string)) {
                    goodsDataList = GsonUtils.parseArrayGson(string);
                    mHandler.sendEmptyMessage(2);
                }
            }
        });
    }

    long hour, minute, second;
    String hourStr = "0";
    String minuteStr = "0";
    String secondStr = "0";

    private void initTime(String countdown) {
        Long seconds = Long.valueOf(countdown);
        hour = seconds / 3600;
        minute = (seconds % 3600) / 60;
        second = seconds % 60;
    }

    private void initTime(long countdown) {
        hour = countdown / 3600;
        minute = (countdown % 3600) / 60;
        second = countdown % 60;
    }

    private String getLongTxt(long num) {
        String text = "0";
        if (num >= 10) {
            text = num + "";
        } else if (num < 10) {
            text += num;
        }
        return text;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    CountDownTimer mCountDownTimer = null;

    void startCountDownTimer() {
        mCountDownTimer = new CountDownTimer((countDownsecond * 1000l) + 500l, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //if (millisUntilFinished > 0l) {
                Log.i("countdown", millisUntilFinished + "--");
                initTime(millisUntilFinished / 1000);
                hourStr = getLongTxt(hour);
                minuteStr = getLongTxt(minute);
                secondStr = getLongTxt(second);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTxtTimeHour.setText(hourStr);
                        mTxtTimeMinute.setText(minuteStr);
                        mTxtTimeSecond.setText(secondStr);
                    }
                });

            }

            @Override
            public void onFinish() {
                initHeaderData();
            }
        };
    }

    /**
     * } else if (millisUntilFinished == 0l) {
     * initTime(millisUntilFinished / 1000);
     * hourStr = getLongTxt(hour);
     * minuteStr = getLongTxt(minute);
     * secondStr = getLongTxt(second);
     * runOnUiThread(new Runnable() {
     *
     * @param savedInstanceState
     * @Override public void run() {
     * mTxtTimeHour.setText(hourStr);
     * mTxtTimeMinute.setText(minuteStr);
     * mTxtTimeSecond.setText(secondStr);
     * initHeaderData();
     * }
     * });
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_time_to_by_list);
        initView();
        initHeaderData();
    }

    Map<String, Object> headerDataMap = new HashMap<>();

    private void initHeaderData() {
        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        okUtils.httpGet(Consts.ROOT_URL + Consts.GET_TIME_TO_BY, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("timeheaderdata", string);
                if (StringUtils.isNotEmpty(string)) {
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(string);
                    if (maps.size() > 0)
                        headerDataMap = maps.get(0);
                    mHandler.sendEmptyMessage(0);
                } else {
                    mHandler.sendEmptyMessage(1);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            finish();
        } else if (view.getId() == R.id.txt_nodata) {
            initHeaderData();
        }
    }

    private void initView() {
        mBackTop = (ImageView) findViewById(R.id.back_top);
        mBackTop.setOnClickListener(TimeToByListActivity.this);
        mTitleTop = (TextView) findViewById(R.id.title_top);
        mTitleTop.setText(getString(R.string.txt_timetoby));
        mTxtTimePeriod = (TextView) findViewById(R.id.txt_time_period);
        mTxtTimeState = (TextView) findViewById(R.id.txt_time_state);
        mTxtTimeHour = (TextView) findViewById(R.id.txt_time_hour);
        mTxtTimeMinute = (TextView) findViewById(R.id.txt_time_minute);
        mTxtTimeSecond = (TextView) findViewById(R.id.txt_time_second);
        mListTimetoby = (ListView) findViewById(R.id.list_timetoby);
        mImgRight = (ImageView) findViewById(R.id.img_right);
        mTxtNodata = (TextView) findViewById(R.id.txt_nodata);
        mTxtNodata.setOnClickListener(TimeToByListActivity.this);
        initListView();
    }

    List<Map<String, Object>> goodsDataList = new ArrayList<>();

    private void initListView() {

        mGoodsListAdapter = new TimeToByListAdapter(this, goodsDataList);
        mGoodsListAdapter.setListener(new FactoryUtils.TimeToByListListener() {
            @Override
            public void onByNowClickListener(int position) {
                if (goodsDataList.size() > position) {
                    Map<String, Object> map = goodsDataList.get(position);
                    Intent intent = new Intent(TimeToByListActivity.this, GoodsShowActivity.class);
                    intent.putExtra(GoodsListActivity.GOOD_ID, map.get("GOODS_ID") + "");
                    intent.putExtra(GoodsShowActivity.FROM, GoodsShowActivity.FROM_TIME_LIST);
                    startActivity(intent);
                }
            }
        });
        mListTimetoby.setAdapter(mGoodsListAdapter);
    }
}
