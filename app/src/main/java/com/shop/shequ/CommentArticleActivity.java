package com.shop.shequ;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.Consts;
import com.shop.R;
import com.shop.customize.pullrefresh.PullToRefershView;
import com.shop.customize.pullrefresh.PullToRefreshBase;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;
import com.shop.util.StringUtils;

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

public class CommentArticleActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mBackTop;
    protected TextView mTitleTop;
    protected PullToRefershView mListComment;
    protected EditText mEdtCommentarticle;
    protected Button mBtnUpcommentarticle;
    protected TextView mTxtCommentNewsReminder;
    private ListView mListView;
    private String articleId;
    OkUtils okUtils;
    private String userName;
    private String commentContent;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            mListComment.onPullDownRefreshComplete();
            mListComment.onPullUpRefreshComplete();
            switch (what) {
                case 0:

                    mTxtCommentNewsReminder.setVisibility(View.GONE);
                    mAdapter.setDataList(commentList);
                    mAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    initCommentListData();
                    Toast.makeText(CommentArticleActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    mEdtCommentarticle.setText("");
                    break;
                case 2:
                    Toast.makeText(CommentArticleActivity.this, "评论失败，请稍候重试", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    mTxtCommentNewsReminder.setVisibility(View.VISIBLE);
                    mTxtCommentNewsReminder.setClickable(true);
                    mTxtCommentNewsReminder.setText(getString(R.string.nodata_clicktorefresh));
                    break;
            }
        }
    };
    private SheQuCommentListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_comment_article);
        Intent intent = getIntent();
        articleId = intent.getStringExtra(SheQuArticleActivity.ARTICELID);
        userName = (String) PreferenceUtil.getParam(PreferenceUtil.USERNAME, "");
        initView();
        okUtils = OkUtils.getOkUtilsInstance();
        initCommentListData();
    }

    private void initCommentListData() {
        mTxtCommentNewsReminder.setText(getString(R.string.loading));
        mTxtCommentNewsReminder.setClickable(false);
        HashMap<String, String> params = new HashMap<>();
        params.put("NEWS_ID", articleId);
        String url = okUtils.getUrl(Consts.ROOT_URL + Consts.GET_COMMENT_NEWS, params);
        okUtils.setNewClient().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(3);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("newscomment", "----" + string);
                if (StringUtils.isNotEmpty(string)) {
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(string);
                    if (maps != null && maps.size() > 0) {
                        commentList.clear();
                        commentList.addAll(maps);
                        mHandler.sendEmptyMessage(0);
                    } else {
                        mHandler.sendEmptyMessage(3);
                    }
                } else {
                    mHandler.sendEmptyMessage(3);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            onBackPressed();
        } else if (view.getId() == R.id.btn_upcommentarticle) {
            try {
                commentContent = mEdtCommentarticle.getText().toString();
                if (StringUtils.isNotEmpty(commentContent))
                    commitComment();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (view.getId() == R.id.txt_comment_news_reminder) {
            initCommentListData();
        }
    }

    private void commitComment() throws JSONException {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("NEWSRATE_USERNAME", userName);
        jsonRequest.put("NEWSRATE_CONTENT", commentContent);
        jsonRequest.put("NEWS_ID", articleId);
        okUtils.setNewClient().httpPostJson(jsonRequest, Consts.ROOT_URL + Consts.ADD_COMMENT_NEWS, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("addnewscomment", "---" + string);
                if (StringUtils.isNotEmpty(string)) {
                    Map<String, Object> map = GsonUtils.parseJsonObject(string);
                    if ("1".equals(map.get("RESULT") + "")) {
                        mHandler.sendEmptyMessage(1);
                    } else {
                        mHandler.sendEmptyMessage(2);
                    }
                }
            }
        });
    }

    List<Map<String, Object>> commentList = new ArrayList<>();

    private void initView() {
        mBackTop = (ImageView) findViewById(R.id.back_top);
        mBackTop.setOnClickListener(CommentArticleActivity.this);
        mTitleTop = (TextView) findViewById(R.id.title_top);
        mTitleTop.setText(getString(R.string.shequ_comment_title));
        mListComment = (PullToRefershView) findViewById(R.id.list_comment);
        mListComment.setPullLoadEnabled(true);
        mListComment.setPullRefreshEnabled(true);
        mListComment.getRefreshView().setVisibility(View.GONE);
        mListComment.setPullRefreshEnabled(false);
        mListView = mListComment.getListView();
        mAdapter = new SheQuCommentListAdapter(CommentArticleActivity.this, commentList);
        mListView.setAdapter(mAdapter);
        mEdtCommentarticle = (EditText) findViewById(R.id.edt_commentarticle);
        mBtnUpcommentarticle = (Button) findViewById(R.id.btn_upcommentarticle);
        mBtnUpcommentarticle.setOnClickListener(CommentArticleActivity.this);

        if (StringUtils.isEmpty(userName)) {
            mBtnUpcommentarticle.setVisibility(View.GONE);
            mEdtCommentarticle.setHint(getString(R.string.commit_aftersign));
        }
        mTxtCommentNewsReminder = (TextView) findViewById(R.id.txt_comment_news_reminder);
        mTxtCommentNewsReminder.setOnClickListener(CommentArticleActivity.this);
        mListComment.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<LinearLayout>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<LinearLayout> refreshView) {
                initCommentListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<LinearLayout> refreshView) {

            }
        });
    }
}
