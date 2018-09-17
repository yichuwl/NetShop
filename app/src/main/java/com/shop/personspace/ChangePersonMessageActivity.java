package com.shop.personspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.Consts;
import com.shop.R;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;
import com.shop.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangePersonMessageActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView backTop;
    protected TextView titleTop;
    protected TextView txtSignin;
    protected TextInputEditText edtSignUsername;
    protected TextInputLayout edtlayoutSignUsername;
    protected TextInputEditText edtSignUserphone;
    protected TextInputLayout edtlayoutSignUserphone;
    protected TextInputEditText edtSignNickname;
    protected TextInputLayout edtlayoutSignNickname;
    protected TextInputEditText edtSignEmail;
    protected TextInputLayout edtlayoutSignEmail;
    protected TextInputEditText edtSignPassword;
    protected TextInputLayout edtlayoutSignPassword;
    protected TextInputEditText edtSignSurepassword;
    protected TextInputLayout edtlayoutSignSurepassword;
    protected LinearLayout linearSignin;
    protected Button btnSignin;
    protected TextView txtNoaccount;
    protected ScrollView activitySigninAndUp;
    String url = Consts.ROOT_URL + Consts.SAVE_USER_DATA_CHANGE;
    String userName, phone, email, nickName, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_change_person_message);
        Intent intent = getIntent();
        String value = intent.getStringExtra("userData");
        parseData(value);
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
        Log.e("userdata", userName + "---" + email + "---" + phone + "--" + nickName);
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
            // todo 修改个人信息
            try {
                JSONObject jsonRequest = new JSONObject();
                userDataMap.put("EMAIL", edtSignEmail.getText().toString().trim());
                userDataMap.put("PHONE", edtSignUserphone.getText().toString().trim());
                userDataMap.put("NAME", edtSignNickname.getText().toString().trim());
                //jsonRequest.put("USERNAME", edtSignUsername.getText().toString().trim());
                // jsonRequest.put("EMAIL", edtSignEmail.getText().toString().trim());
                /// jsonRequest.put("PHONE", edtSignUserphone.getText().toString().trim());
                //jsonRequest.put("ROLE_NAME", edtSignNickname.getText().toString().trim());
                //jsonRequest.put("USER_ID", userId);
                JSONObject jsonObject = GsonUtils.mapToArray(userDataMap);
                jsonRequest.put("USERDATA", jsonObject.toString());
                OkUtils.getOkUtilsInstance().setNewClient().httpPostJson(jsonRequest, url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String string = response.body().string();
                        Log.i("changeuserdata", string);
                        // if (Consts.RESPONSE_OK_STATE.equals(response.message())) {
                        if (!TextUtils.isEmpty(string)) {
                            Map<String, Object> map = GsonUtils.parseJsonObject(string);
                            if ("1".equals(map.get("RESULT") + "")) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ChangePersonMessageActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ChangePersonMessageActivity.this, "修改失败，请稍后重试", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }
                    }
                    //}
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        backTop = (ImageView) findViewById(R.id.back_top);
        backTop.setOnClickListener(ChangePersonMessageActivity.this);
        titleTop = (TextView) findViewById(R.id.title_top);
        titleTop.setText(getString(R.string.change_personal_data));
        txtSignin = (TextView) findViewById(R.id.txt_signin);
        edtSignUsername = (TextInputEditText) findViewById(R.id.edt_sign_username);
        edtlayoutSignUsername = (TextInputLayout) findViewById(R.id.edtlayout_sign_username);
        edtlayoutSignUsername.setVisibility(View.GONE);
        edtSignUserphone = (TextInputEditText) findViewById(R.id.edt_sign_userphone);
        edtlayoutSignUserphone = (TextInputLayout) findViewById(R.id.edtlayout_sign_userphone);
        edtSignNickname = (TextInputEditText) findViewById(R.id.edt_sign_nickname);
        edtlayoutSignNickname = (TextInputLayout) findViewById(R.id.edtlayout_sign_nickname);
        edtSignEmail = (TextInputEditText) findViewById(R.id.edt_sign_email);
        edtlayoutSignEmail = (TextInputLayout) findViewById(R.id.edtlayout_sign_email);
        edtSignPassword = (TextInputEditText) findViewById(R.id.edt_sign_password);
        edtlayoutSignPassword = (TextInputLayout) findViewById(R.id.edtlayout_sign_password);
        edtSignSurepassword = (TextInputEditText) findViewById(R.id.edt_sign_surepassword);
        edtlayoutSignSurepassword = (TextInputLayout) findViewById(R.id.edtlayout_sign_surepassword);
        linearSignin = (LinearLayout) findViewById(R.id.linear_signin);
        btnSignin = (Button) findViewById(R.id.btn_signin);
        btnSignin.setOnClickListener(ChangePersonMessageActivity.this);
        txtNoaccount = (TextView) findViewById(R.id.txt_noaccount);
        activitySigninAndUp = (ScrollView) findViewById(R.id.activity_signin_and_up);
        txtSignin.setVisibility(View.GONE);
        txtNoaccount.setVisibility(View.GONE);
        edtlayoutSignPassword.setVisibility(View.GONE);
        edtlayoutSignNickname.setVisibility(View.VISIBLE);
        edtlayoutSignEmail.setVisibility(View.VISIBLE);
        edtlayoutSignUserphone.setVisibility(View.VISIBLE);
        btnSignin.setText(getString(R.string.btn_update_data));
        edtSignUsername.setText(StringUtils.isEmpty(userName) ? "" : userName);
        edtSignUserphone.setText(StringUtils.isEmpty(phone) ? "" : phone);
        edtSignEmail.setText(StringUtils.isEmpty(email) ? "" : email);
        edtSignNickname.setText(StringUtils.isEmpty(nickName) ? "" : nickName);
    }
}
