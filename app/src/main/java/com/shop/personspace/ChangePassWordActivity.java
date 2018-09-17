package com.shop.personspace;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.Consts;
import com.shop.R;
import com.shop.util.GsonUtils;
import com.shop.util.MD5Utils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePassWordActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView backTop;
    protected TextView titleTop;
    protected TextInputEditText edtChangeOldpassword;
    protected TextInputLayout edtlayoutChangeOldpassword;
    protected TextInputEditText edtChangeNewpassword;
    protected TextInputLayout edtlayoutChangeNewpassword;
    protected TextInputEditText edtChangeSurepassword;
    protected TextInputLayout edtlayoutChangeSurepassword;
    protected Button btnSignin;
    String userDataStr = "", userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_change_pass_word);
        userDataStr = getIntent().getStringExtra("PASSWORD");
        parseData(userDataStr);
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
        initView();
    }

    Map<String, Object> userDataMap = new HashMap<>();

    private void parseData(String value) {
        userDataMap = GsonUtils.parseJsonObject(value);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            finish();
        } else if (view.getId() == R.id.btn_signin) {
            String newPassWord = edtChangeNewpassword.getText().toString().trim();
            String repeatNew = edtChangeSurepassword.getText().toString().trim();
            String oldPassWord = edtChangeOldpassword.getText().toString().trim();
            if (userDataMap.get("PASSWORD").equals(MD5Utils.md5(oldPassWord))) {
                if (TextUtils.isEmpty(newPassWord)) {
                    Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!newPassWord.equals(repeatNew)) {
                    Toast.makeText(this, "重复密码输入与新密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        upDateChange(newPassWord);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(this, "旧密码输入错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void upDateChange(String newPwd) throws JSONException {
        JSONObject jsonRequest = new JSONObject();
        userDataMap.put("PASSWORD", newPwd);
        jsonRequest.put("USER_DATA", GsonUtils.mapToArray(userDataMap).toString());
        OkUtils.getOkUtilsInstance().setNewClient().httpPostJson(jsonRequest, Consts.ROOT_URL + Consts.SAVE_USER_DATA_CHANGE, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("changepassword", string);
                // if (Consts.RESPONSE_OK_STATE == response.message()) {
                if (!TextUtils.isEmpty(string)) {
                    Map<String, Object> map = GsonUtils.parseJsonObject(string);
                    if ("1".equals(map.get("RESULT") + "")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangePassWordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangePassWordActivity.this, "修改失败，请稍后重试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                //  }
            }
        });
    }

    private void initView() {
        backTop = (ImageView) findViewById(R.id.back_top);
        backTop.setOnClickListener(ChangePassWordActivity.this);
        titleTop = (TextView) findViewById(R.id.title_top);
        titleTop.setText(getString(R.string.txt_change_password));
        edtChangeOldpassword = (TextInputEditText) findViewById(R.id.edt_change_oldpassword);
        edtlayoutChangeOldpassword = (TextInputLayout) findViewById(R.id.edtlayout_change_oldpassword);
        edtChangeNewpassword = (TextInputEditText) findViewById(R.id.edt_change_newpassword);
        edtlayoutChangeNewpassword = (TextInputLayout) findViewById(R.id.edtlayout_change_newpassword);
        edtChangeSurepassword = (TextInputEditText) findViewById(R.id.edt_change_surepassword);
        edtlayoutChangeSurepassword = (TextInputLayout) findViewById(R.id.edtlayout_change_surepassword);
        btnSignin = (Button) findViewById(R.id.btn_signin);
        btnSignin.setOnClickListener(ChangePassWordActivity.this);
    }
}
