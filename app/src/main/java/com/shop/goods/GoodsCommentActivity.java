package com.shop.goods;

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

public class GoodsCommentActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mBackTop;
    protected TextView mTitleTop;
    protected ListView mListComment;
    protected PullToRefershView mRefershView;
    private CommentListAdapter mAdapter;
    private String mGOODSId;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            mRefershView.onPullUpRefreshComplete();
            mRefershView.onPullDownRefreshComplete();
            switch (what) {
                case 1:
                    if (reminder.getVisibility() == View.VISIBLE)
                        reminder.setVisibility(View.GONE);
                    mAdapter.setDataList(commentList);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 0:
                    if (commentList.size() <= 0 && reminder.getVisibility() == View.GONE) {
                        reminder.setVisibility(View.VISIBLE);
                        reminder.setClickable(true);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_comment);
        mGOODSId = getIntent().getStringExtra("GOODS_ID");
        initView();
        // mRefershView.doPullRefreshing(true, 500);
        initDataList();
        mRefershView.setPullRefreshEnabled(false);
    }

    List<Map<String, Object>> commentList = new ArrayList<>();

    private void initDataList() {
        reminder.setText(getString(R.string.loading));
        reminder.setClickable(false);
        OkUtils okUtils = OkUtils.getOkUtilsInstance();
        String url = Consts.ROOT_URL + Consts.GET_GOODS_COMMENT_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("GOODS_ID", "be622566a0fe42cb8fc7cb5588d8332a");
        url = okUtils.getUrl(url, params);
        okUtils.setNewClient().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("goodscommentlist", "----" + string);
                if (StringUtils.isNotEmpty(string)) {
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(string);
                    if (maps != null && maps.size() > 0) {
                        commentList.clear();
                        commentList.addAll(maps);
                        mHandler.sendEmptyMessage(1);
                    } else {
                        mHandler.sendEmptyMessage(0);
                    }
                } else {
                    mHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            onBackPressed();
        } else if (view.getId() == R.id.txt_comment_news_reminder) {
            initDataList();
        }
    }

    TextView reminder;

    private void initView() {
        reminder = findViewById(R.id.txt_comment_news_reminder);
        mBackTop = (ImageView) findViewById(R.id.back_top);
        mBackTop.setOnClickListener(GoodsCommentActivity.this);
        mTitleTop = (TextView) findViewById(R.id.title_top);
        mTitleTop.setText(getString(R.string.comment));
        mRefershView = (PullToRefershView) findViewById(R.id.list_comment);
        mRefershView.setPullRefreshEnabled(true);
        mRefershView.setPullLoadEnabled(true);
        mRefershView.getRefreshView().setVisibility(View.GONE);
        mListComment = mRefershView.getListView();
        mRefershView.setPullLoadEnabled(false);
        mRefershView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<LinearLayout>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<LinearLayout> refreshView) {
                initDataList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<LinearLayout> refreshView) {

            }
        });

        mAdapter = new CommentListAdapter(GoodsCommentActivity.this);
        mListComment.setAdapter(mAdapter);
    }
}
