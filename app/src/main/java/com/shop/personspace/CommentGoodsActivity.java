package com.shop.personspace;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shop.Consts;
import com.shop.R;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;
import com.shop.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CommentGoodsActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mBackTop;
    protected TextView mTitleTop;
    protected EditText mEdtCommentcontent;
    protected Button mBtnCommitComment;
    protected RatingBar mRatingbar;
    String userName = "";
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:
                    String message = msg.getData().getString("message");
                    Map<String, Object> map = GsonUtils.parseJsonObject(message);
                    if ("1".equals(map.get("RESULT") + "")) {
                        Toast.makeText(CommentGoodsActivity.this, map.get("MESSAGE") + "", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CommentGoodsActivity.this, map.get("MESSAGE") + "", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_comment_goods);
        userName = (String) PreferenceUtil.getParam(PreferenceUtil.USERNAME, "");
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            finish();
        } else if (view.getId() == R.id.btn_commit_comment) {
            float rating = mRatingbar.getRating();
            String content = mEdtCommentcontent.getText().toString();
            if (StringUtils.isEmpty(content)) {
                Toast.makeText(this, "请填写评论内容", Toast.LENGTH_SHORT).show();
                return;
            }
            if (rating == 0f) {
                rating = 3;
            }
            try {
                commitCommentData(content, rating);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void commitCommentData(String content, float rating) throws JSONException {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("RATE_USERNAME", userName);
        jsonRequest.put("RATE_SCORE", rating);
        jsonRequest.put("RATE_CONTENT", content);
        jsonRequest.put("RATE_GOODS", "be622566a0fe42cb8fc7cb5588d8332a");
        //jsonRequest.put("RATE_IP","");
        OkUtils.getOkUtilsInstance().setNewClient().httpPostJson(jsonRequest, Consts.ROOT_URL + Consts.ADD_GOODS_COMMENT, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (StringUtils.isNotEmpty(string)) {
                    Log.e("addcomment", string);
                    //if ("1".equals(map.get("RESULT") + "")) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString("message", string);
                    message.setData(data);
                    message.what = 0;
                    mHandler.sendMessage(message);
                    // }
                }
            }
        });
    }

    private void initView() {
        mBackTop = (ImageView) findViewById(R.id.back_top);
        mBackTop.setOnClickListener(CommentGoodsActivity.this);
        mTitleTop = (TextView) findViewById(R.id.title_top);
        mTitleTop.setText(getString(R.string.new_comment));
        mEdtCommentcontent = (EditText) findViewById(R.id.edt_commentcontent);
        mBtnCommitComment = (Button) findViewById(R.id.btn_commit_comment);
        mBtnCommitComment.setOnClickListener(CommentGoodsActivity.this);
        mRatingbar = (RatingBar) findViewById(R.id.ratingbar);
    }
}
