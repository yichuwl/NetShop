package com.shop.personspace;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.customize.pullrefresh.PullToRefershView;
import com.shop.customize.pullrefresh.PullToRefreshBase;
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

public class ToBeReceiveActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView backTop;
    protected TextView titleTop;
    protected ListView listWuliu;
    protected PullToRefershView mPullToRefershView;
    protected OrderListAdapter mAdapter;
    String url = Consts.ROOT_URL + Consts.GET_ORDER_LIST;
    String userName, userId;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            mPullToRefershView.onPullDownRefreshComplete();
            mPullToRefershView.onPullUpRefreshComplete();
            switch (what) {
                case 0:
                    mAdapter.setDataList(dataList);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_to_be_pay_list);
        userName = (String) PreferenceUtil.getParam(PreferenceUtil.USERNAME, "");
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
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
        params.put("status", Consts.TORECEIVESTATE);
        url = okUtils.getUrl(this.url, params);
        okUtils.setNewClient().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("toreceivelist", string);
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
        backTop.setOnClickListener(ToBeReceiveActivity.this);
        titleTop = (TextView) findViewById(R.id.title_top);
        titleTop.setText(getString(R.string.tobereceive));
        mPullToRefershView = (PullToRefershView) findViewById(R.id.list_wuliu);
        listWuliu = mPullToRefershView.getListView();
        mPullToRefershView.setPullLoadEnabled(true);
        mPullToRefershView.setPullRefreshEnabled(true);
        mPullToRefershView.getRefreshView().setVisibility(View.GONE);
        ColorDrawable divider = new ColorDrawable();
        divider.setColor(getResources().getColor(R.color.gray_transparent));
        listWuliu.setDivider(divider);
        listWuliu.setDividerHeight(8);
        mAdapter = new OrderListAdapter(this, dataList, Consts.TO_BE_RECEIVE_ORDER);
        listWuliu.setAdapter(mAdapter);
        initPullToRefresh();
    }

    private void initPullToRefresh() {
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
                intent.setClass(ToBeReceiveActivity.this, OrderDetailActivity.class);
                try {
                    intent.putExtra(ToBePayListActivity.ORDER_DATA_KEY, GsonUtils.mapToArray(dataList.get(position)).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }
}
