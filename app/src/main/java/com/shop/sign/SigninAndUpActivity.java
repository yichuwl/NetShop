package com.shop.sign;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.shop.BaseActivity;
import com.shop.Consts;
import com.shop.MainActivity;
import com.shop.MyApplication;
import com.shop.R;
import com.shop.util.GsonUtils;
import com.shop.util.MD5Utils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SigninAndUpActivity extends BaseActivity implements View.OnClickListener {

    protected TextView mTxtSignin;
    protected TextInputEditText mEdtSignUserphone;
    protected TextInputLayout mEdtlayoutSignUserphone;
    protected TextInputEditText mEdtSignPassword;
    protected TextInputLayout mEdtlayoutSignPassword;
    protected Button mBtnSignin;
    protected ScrollView mActivitySigninAndUp;
    protected TextInputEditText mEdtSignUsername;
    protected TextInputLayout mEdtlayoutSignUsername;
    protected TextView mTxtNoaccount;
    protected TextInputEditText edtSignSurepassword;
    protected TextInputLayout edtlayoutSignSurepassword;
    protected LinearLayout linearSignin;
    protected TextInputEditText edtSignEmail;
    protected TextInputLayout edtlayoutSignEmail;
    protected TextInputEditText edtSignNickname;
    protected TextInputLayout edtlayoutSignNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_signin_and_up);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_signin) {
            if (mEdtlayoutSignUserphone.getVisibility() == View.GONE) {
                // 登陆
                try {
                    JSONObject jsonRequest = new JSONObject();
                    jsonRequest.put("USERNAME", mEdtSignUsername.getText().toString());
                    jsonRequest.put("PASSWORD", mEdtSignPassword.getText().toString());
                    jsonRequest.put("IP", getIPAddress(this));
                    OkUtils.getOkUtilsInstance().httpPostJson(jsonRequest, Consts.ROOT_URL + Consts.SIGN_IN, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String string = response.body().string();
                            Log.i("login", string);
                            //  if (Consts.RESPONSE_OK_STATE.equals(response.message())) {
                            if (!TextUtils.isEmpty(string)) {
                                Map<String, Object> map = GsonUtils.parseJsonObject(string);
                                String state = map.get("RESULT") + "";
                                final String message = map.get("MESSAGE") + "";
                                if ("1".equals(map.get("RESULT") + "")) {
                                    PreferenceUtil.putParam(PreferenceUtil.USERNAME, mEdtSignUsername.getText().toString().trim());
                                    PreferenceUtil.putParam(PreferenceUtil.USERPHONE, mEdtSignUserphone.getText().toString().trim());
                                    String pwd = mEdtSignPassword.getText().toString().trim();
                                    pwd = MD5Utils.md5(pwd);
                                    PreferenceUtil.putParam(PreferenceUtil.USERPWD, pwd);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SigninAndUpActivity.this, message, Toast.LENGTH_SHORT).show();
                                            SigninAndUpActivity.this.finish();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SigninAndUpActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            //}
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // 注册
                try {
                    JSONObject jsonRequest = new JSONObject();
                    jsonRequest.put("PHONE", mEdtSignUserphone.getText().toString());
                    jsonRequest.put("PASSWORD", mEdtSignPassword.getText().toString());
                    jsonRequest.put("USERNAME", mEdtSignUsername.getText().toString());
                    jsonRequest.put("EMAIL", edtSignEmail.getText().toString());
                    jsonRequest.put("NAME", edtSignNickname.getText().toString());
                    jsonRequest.put("PASSWORD1", edtSignSurepassword.getText().toString());
                    jsonRequest.put("IP", getIPAddress(this));
                    OkUtils.getOkUtilsInstance().httpPostJson(jsonRequest, Consts.ROOT_URL + Consts.SIGN_UP, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String string = response.body().string();
                            Log.i("login", string);
                            // if (Consts.RESPONSE_OK_STATE.equals(response.message())) {
                            if (!TextUtils.isEmpty(string)) {
                                Map<String, Object> map = GsonUtils.parseJsonObject(string);
                                String state = map.get("RESULT") + "";
                                final String message = map.get("MESSAGE") + "";
                                if ("1".equals(map.get("RESULT") + "")) {
                                    PreferenceUtil.putParam(PreferenceUtil.USERNAME, mEdtSignUsername.getText().toString().trim());
                                    PreferenceUtil.putParam(PreferenceUtil.USERPHONE, mEdtSignUserphone.getText().toString().trim());
                                    String pwd = mEdtSignPassword.getText().toString().trim();
                                    pwd = MD5Utils.md5(pwd);
                                    PreferenceUtil.putParam(PreferenceUtil.USERPWD, pwd);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SigninAndUpActivity.this, message, Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SigninAndUpActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            // }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (view.getId() == R.id.txt_noaccount) {
            if (getString(R.string.noaccount_signin).equals(((TextView) view).getText())) {
                mTxtSignin.setText(getString(R.string.signin));
                edtlayoutSignNickname.setVisibility(View.VISIBLE);
                mEdtlayoutSignUsername.setVisibility(View.VISIBLE);
                edtlayoutSignSurepassword.setVisibility(View.VISIBLE);
                edtlayoutSignEmail.setVisibility(View.VISIBLE);
                mBtnSignin.setText(getString(R.string.signin));
                mTxtNoaccount.setText(getString(R.string.signup));
            } else {
                mTxtSignin.setText(getString(R.string.signup));
                mEdtlayoutSignUsername.setVisibility(View.VISIBLE);
                mEdtlayoutSignUserphone.setVisibility(View.GONE);
                mEdtlayoutSignUsername.setHint(getString(R.string.phoneorname));
                edtlayoutSignNickname.setVisibility(View.GONE);
                edtlayoutSignSurepassword.setVisibility(View.GONE);
                edtlayoutSignEmail.setVisibility(View.GONE);
                mBtnSignin.setText(getString(R.string.signup));
                mTxtNoaccount.setText(getString(R.string.noaccount_signin));
            }

        }
    }

    private void initView() {
        mTxtSignin = (TextView) findViewById(R.id.txt_signin);
        mEdtSignUserphone = (TextInputEditText) findViewById(R.id.edt_sign_userphone);
        mEdtlayoutSignUserphone = (TextInputLayout) findViewById(R.id.edtlayout_sign_userphone);
        mEdtSignPassword = (TextInputEditText) findViewById(R.id.edt_sign_password);
        mEdtlayoutSignPassword = (TextInputLayout) findViewById(R.id.edtlayout_sign_password);
        mBtnSignin = (Button) findViewById(R.id.btn_signin);
        mBtnSignin.setOnClickListener(SigninAndUpActivity.this);
        mActivitySigninAndUp = (ScrollView) findViewById(R.id.activity_signin_and_up);
        mEdtSignUserphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEdtSignUserphone.getText().length() > 11) {
                    mEdtlayoutSignUserphone.setHintTextAppearance(R.style.HintErrorAppearance);
                    mEdtlayoutSignUserphone.setError(getString(R.string.phonenumber_error_text));
                } else {
                    mEdtlayoutSignUserphone.setHintTextAppearance(R.style.HintErrorAppearance);
                    mEdtlayoutSignUserphone.setError("");
                }
            }
        });

        mEdtSignPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEdtSignPassword.getText().length() < 6) {
                    mEdtlayoutSignPassword.setError(getString(R.string.passworderror_text));
                } else {
                    mEdtlayoutSignPassword.setError("");
                }
            }
        });
        mEdtSignUsername = (TextInputEditText) findViewById(R.id.edt_sign_username);
        mEdtlayoutSignUsername = (TextInputLayout) findViewById(R.id.edtlayout_sign_username);
        mTxtNoaccount = (TextView) findViewById(R.id.txt_noaccount);
        mTxtNoaccount.setOnClickListener(SigninAndUpActivity.this);
        edtSignSurepassword = (TextInputEditText) findViewById(R.id.edt_sign_surepassword);
        edtlayoutSignSurepassword = (TextInputLayout) findViewById(R.id.edtlayout_sign_surepassword);
        linearSignin = (LinearLayout) findViewById(R.id.linear_signin);
        edtSignEmail = (TextInputEditText) findViewById(R.id.edt_sign_email);
        edtlayoutSignEmail = (TextInputLayout) findViewById(R.id.edtlayout_sign_email);
        edtSignNickname = (TextInputEditText) findViewById(R.id.edt_sign_nickname);
        edtlayoutSignNickname = (TextInputLayout) findViewById(R.id.edtlayout_sign_nickname);
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
