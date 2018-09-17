package com.shop.personspace;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.sign.SigninAndUpActivity;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;
import com.shop.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author victory
 * @time 2018/6/25
 * @about 个人中心页面
 */

public class WodeFragment extends Fragment implements View.OnClickListener {
    protected ImageView mImgPersonavastar;
    protected TextView mTxtWodeUserSign;
    protected TextView mTxtWodeSeeMoreOrder;
    protected TextView mTxtWodeTobepay;
    protected TextView mTxtWodeTobesend;
    protected TextView mTxtWodeTobereceive;
    protected TextView mTxtWodeTobecomment;
    protected RelativeLayout mRelaCoupon;
    protected RelativeLayout mRelaAboutus;
    protected TextView mTxtVip;
    protected ImageView imgPersonavastar;
    protected TextView txtWodeUserSign;
    protected TextView txtWodeSeeMoreOrder;
    protected TextView txtWodeTobepay;
    protected TextView txtWodeTobesend;
    protected TextView txtWodeTobereceive;
    protected TextView txtWodeTobecomment;
    protected RelativeLayout relaCoupon;
    protected TextView txtVip;
    protected RelativeLayout relaVip;
    protected RelativeLayout relaChangeuserdata;
    protected RelativeLayout relaChangepassword;
    protected RelativeLayout relaAboutus;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:
                    String number = userDataMap.get("NUMBER") + "";
                    mTxtWodeUserSign.setText(userDataMap.get("USERNAME") + "");
                    PreferenceUtil.putParam(PreferenceUtil.USERNAME, userDataMap.get("USERNAME") + "");
                    PreferenceUtil.putParam(PreferenceUtil.USERPHONE, userDataMap.get("PHONE"));
                    PreferenceUtil.putParam(PreferenceUtil.USERID, userDataMap.get("USER_ID") + "");
                    if (TextUtils.isEmpty(number) || "null".equals(number))
                        return;
                    mTxtVip.setText(number);
                    break;
            }
        }
    };

    public static WodeFragment newInstance() {

        Bundle args = new Bundle();

        WodeFragment fragment = new WodeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_wode, null);
        initContentView(contentView);
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (StringUtils.isEmpty(userName))
            userName = (String) PreferenceUtil.getParam(PreferenceUtil.USERNAME, "");
        if (!TextUtils.isEmpty(userName)) {
            mTxtWodeUserSign.setText(userName);
            getHttpUserData();
            mRelaCoupon.setVisibility(View.VISIBLE);
            relaChangepassword.setVisibility(View.VISIBLE);
            relaChangeuserdata.setVisibility(View.VISIBLE);
            relaVip.setVisibility(View.VISIBLE);
        } else {
            mTxtWodeUserSign.setText(getString(R.string.signuporin));
            mRelaCoupon.setVisibility(View.GONE);
            relaChangepassword.setVisibility(View.GONE);
            relaChangeuserdata.setVisibility(View.GONE);
            relaVip.setVisibility(View.GONE);
        }
    }

    String userName = "";

    private void initContentView(View rootView) {
        mImgPersonavastar = (ImageView) rootView.findViewById(R.id.img_personavastar);
        mImgPersonavastar.setOnClickListener(WodeFragment.this);
        mTxtWodeUserSign = (TextView) rootView.findViewById(R.id.txt_wode_user_sign);
        userName = (String) PreferenceUtil.getParam(PreferenceUtil.USERNAME, "");
        mTxtWodeUserSign.setOnClickListener(WodeFragment.this);
        mTxtWodeSeeMoreOrder = (TextView) rootView.findViewById(R.id.txt_wode_see_more_order);
        mTxtWodeSeeMoreOrder.setOnClickListener(WodeFragment.this);
        mTxtWodeTobepay = (TextView) rootView.findViewById(R.id.txt_wode_tobepay);
        mTxtWodeTobepay.setOnClickListener(WodeFragment.this);
        mTxtWodeTobesend = (TextView) rootView.findViewById(R.id.txt_wode_tobesend);
        mTxtWodeTobesend.setOnClickListener(WodeFragment.this);
        mTxtWodeTobereceive = (TextView) rootView.findViewById(R.id.txt_wode_tobereceive);
        mTxtWodeTobereceive.setOnClickListener(WodeFragment.this);
        mTxtWodeTobecomment = (TextView) rootView.findViewById(R.id.txt_wode_tobecomment);
        mTxtWodeTobecomment.setOnClickListener(WodeFragment.this);
        mRelaCoupon = (RelativeLayout) rootView.findViewById(R.id.rela_coupon);
        mRelaCoupon.setOnClickListener(WodeFragment.this);
        mRelaAboutus = (RelativeLayout) rootView.findViewById(R.id.rela_aboutus);
        mRelaAboutus.setOnClickListener(WodeFragment.this);
        mTxtVip = rootView.findViewById(R.id.txt_vip);
        relaVip = rootView.findViewById(R.id.rela_vip);
        relaChangeuserdata = (RelativeLayout) rootView.findViewById(R.id.rela_changeuserdata);
        relaChangeuserdata.setOnClickListener(WodeFragment.this);
        relaChangepassword = (RelativeLayout) rootView.findViewById(R.id.rela_changepassword);
        relaChangepassword.setOnClickListener(WodeFragment.this);
    }

    @Override
    public void onStop() {
        super.onStop();
        userName = "";
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_personavastar) {

        } else if (view.getId() == R.id.txt_wode_user_sign) {
            if (!TextUtils.isEmpty(userName)) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.reminder))
                        .setMessage(getString(R.string.sure_exit))
                        .setPositiveButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(PreferenceUtil.getParam(PreferenceUtil.USERNAME, "") + "")) {
                            PreferenceUtil.putParam(PreferenceUtil.USERNAME, "");
                            PreferenceUtil.putParam(PreferenceUtil.USERPHONE, "");
                            PreferenceUtil.putParam(PreferenceUtil.USERPWD, "");
                            startActivity(new Intent(getActivity(), SigninAndUpActivity.class));
                        }
                    }
                }).show();
            } else {
                Intent intent = new Intent(getActivity(), SigninAndUpActivity.class);
                startActivity(intent);
                // getActivity().finish();
            }
        } else if (view.getId() == R.id.txt_wode_see_more_order) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), ToBePayListActivity.class);
            intent.putExtra("all", "1");
            startActivity(intent);
        } else if (view.getId() == R.id.txt_wode_tobepay) {
            startActivity(new Intent(getActivity(), ToBePayListActivity.class));
        } else if (view.getId() == R.id.txt_wode_tobesend) {
            startActivity(new Intent(getActivity(), ToBeSendActivity.class));
        } else if (view.getId() == R.id.txt_wode_tobereceive) {
            startActivity(new Intent(getActivity(), ToBeReceiveActivity.class));
        } else if (view.getId() == R.id.txt_wode_tobecomment) {
            startActivity(new Intent(getActivity(), ToCommentActivity.class));
        } else if (view.getId() == R.id.rela_coupon) {
            startActivity(new Intent(getActivity(), CouponListActivity.class));
        } else if (view.getId() == R.id.rela_aboutus) {

        } else if (view.getId() == R.id.rela_changeuserdata) {
            Intent intent = new Intent();
            //Bundle value = new Bundle();
            /*value.putString("USERNAME", userDataMap.get("USERNAME") + "");
            value.putString("EMAIL", userDataMap.get("EMAIL") + "");
            value.putString("PHONE", userDataMap.get("PHONE") + "");
            value.putString("ROLE_NAME", userDataMap.get("NAME") + "");*/
            intent.putExtra("userData", userDataStr);
            intent.setClass(getActivity(), ChangePersonMessageActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.rela_changepassword) {
            Intent intent = new Intent();
            intent.putExtra("PASSWORD", userDataStr);
            intent.setClass(getActivity(), ChangePassWordActivity.class);
            startActivity(intent);
        }
    }

    Map<String, Object> userDataMap = new HashMap<>();
    String userDataStr = "";

    public void getHttpUserData() {
        OkUtils okUtils = OkUtils.getOkUtilsInstance();
        HashMap<String, String> params = new HashMap<>();
        params.put("USERNAME", userName);
        String url = okUtils.getUrl(Consts.ROOT_URL + Consts.GET_USER_DATA, params);
        okUtils.setNewClient().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //  if (Consts.RESPONSE_OK_STATE == response.message()) {
                userDataStr = response.body().string();
                Log.e("userdata", userDataStr);
                if (!TextUtils.isEmpty(userDataStr)) {
                    userDataMap = GsonUtils.parseJsonObject(userDataStr);
                    mHandler.sendEmptyMessage(0);
                }
                //}
            }
        });
    }
}
