package com.shop.shopchart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.lljjcoder.style.citythreelist.ProvinceActivity;
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

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mBackAddress;
    protected EditText mEdtNameAddr;
    protected EditText mEdtPhonenumberAddr;
    protected EditText mEdtAddrAddr;
    protected Button mBtnSaveAddr;
    public static final String ADDR_INFO = "addrInfo";
    protected TextView mTxtAddrAddr;
    protected CheckBox ckbSdefault;
    private CityPickerView mPickerView;
    protected String userId = "";
    protected String changeData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_address);
        mPickerView = new CityPickerView();
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
        Intent intent = getIntent();
        changeData = intent.getStringExtra("addr");
        initView();
        initChangeData();
        initCityData();
        initPickerView();
    }

    Map<String, Object> object;

    private void initChangeData() {
        if (StringUtils.isNotEmpty(changeData)) {
            object = GsonUtils.parseJsonObject(changeData);
            String addrCity = object.get("ADDR_CITY") + "";
            String addrDetial = object.get("ADDR_DETAILS") + "";
            String userName = object.get("ADDR_REALNAME") + "";
            String phone = object.get("ADDR_PHONE") + "";
            String isDefault = object.get("IS_DEFAULT") + "";
            mEdtNameAddr.setText(userName);
            mEdtPhonenumberAddr.setText(phone);
            mEdtAddrAddr.setText(addrDetial);
            mTxtAddrAddr.setText(addrCity);
            if ("1".equals(isDefault)) {
                ckbSdefault.setChecked(true);
            }
        }
    }

    private void initPickerView() {
        CityConfig cityConfig = new CityConfig.Builder()
                .title("选择地址")//标题
                .titleTextSize(18)//标题文字大小
                .titleTextColor("#585858")//标题文字颜  色
                .titleBackgroundColor("#E9E9E9")//标题栏背景色
                .confirTextColor("#585858")//确认按钮文字颜色
                .confirmText("确定")//确认按钮文字
                .confirmTextSize(16)//确认按钮文字大小
                .cancelTextColor("#585858")//取消按钮文字颜色
                .cancelText("取消")//取消按钮文字
                .cancelTextSize(16)//取消按钮文字大小
                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)//显示类，只显示省份一级，显示省市两级还是显示省市区三级
                .showBackground(true)//是否显示半透明背景
                .visibleItemsCount(7)//显示item的数量
                .province("浙江省")//默认显示的省份
                .city("杭州市")//默认显示省份下面的城市
                .district("滨江区")//默认显示省市下面的区县数据
                .provinceCyclic(true)//省份滚轮是否可以循环滚动
                .cityCyclic(true)//城市滚轮是否可以循环滚动
                .districtCyclic(true)//区县滚轮是否循环滚动
                .drawShadows(false)//滚轮不显示模糊效果
                .setLineColor("#03a9f4")//中间横线的颜色
                .setLineHeigh(5)//中间横线的高度
                .setShowGAT(true)//是否显示港澳台数据，默认不显示
                .build();

//设置自定义的属性配置
        mPickerView.setConfig(cityConfig);
        mPickerView.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                super.onSelected(province, city, district);
                StringBuilder cityData = new StringBuilder().append(province.getName()).append(" ")
                        .append(city.getName()).append(" ").append(district.getName());
                mTxtAddrAddr.setText(cityData);
            }
        });
    }

    private void initCityData() {
        mPickerView.init(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_address) {
            finish();
        } else if (view.getId() == R.id.btn_save_addr) {
            String name = mEdtNameAddr.getText().toString().trim();
            String phoneNumber = mEdtPhonenumberAddr.getText().toString().trim();
            String addr = mTxtAddrAddr.getText().toString();
            if (StringUtils.isEmpty(name)) {
                Toast.makeText(this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else if (StringUtils.isEmpty(phoneNumber)) {
                Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                return;
            } else if (StringUtils.isEmpty(addr) || getString(R.string.select_addr).equals(addr)) {
                Toast.makeText(this, "地址不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            // 保存返回数据
          /*  String addrDetial = mEdtAddrAddr.getText().toString().trim();
            String addrInfo = new StringBuilder().append(addr)
                    .append(StringUtils.isEmpty(addrDetial) ? "" : addrDetial).append("\n")
                    .append(name).append("  ").append(phoneNumber).toString();*/
            // PreferenceUtil.putParam(PreferenceUtil.ADDR, addrInfo);
            try {
                saveAddr();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //  finish();

        } else if (view.getId() == R.id.txt_addr_addr) {
            mPickerView.showCityPicker();
        }
    }

    Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:
                    /*Intent data = new Intent();
                    data.putExtra(ADDR_INFO, "");
                    setResult(RESULT_OK, data);*/
                    Toast.makeText(AddressActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 1:
                    Toast.makeText(AddressActivity.this, "操作失败，请重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void saveAddr() throws JSONException {
        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("ADDR_PHONE", mEdtPhonenumberAddr.getText().toString());
        jsonRequest.put("ADDR_REALNAME", mEdtNameAddr.getText().toString());
        jsonRequest.put("ADDR_DETAILS", mEdtAddrAddr.getText().toString());
        jsonRequest.put("ADDR_CITY", mTxtAddrAddr.getText().toString());
        jsonRequest.put("USER_ID", userId);
        jsonRequest.put("IS_DEFAULT", ckbSdefault.isChecked() ? "1" : "0");
        if (StringUtils.isNotEmpty(changeData)) {
            jsonRequest.put("ADDRESS_ID", object.get("ADDRESS_ID") + "");
            okUtils.httpPostJson(jsonRequest, Consts.ROOT_URL + Consts.EDIT_ADDR, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mHander.sendEmptyMessage(1);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    Log.e("changeaddr", string);
                    if (StringUtils.isNotEmpty(string)) {
                        Map<String, Object> map = GsonUtils.parseJsonObject(string);
                        if ("1".equals(map.get("RESULT") + ""))
                            mHander.sendEmptyMessage(0);
                        else
                            mHander.sendEmptyMessage(1);
                    } else
                        mHander.sendEmptyMessage(1);
                }
            });
        } else {

            okUtils.httpPostJson(jsonRequest, Consts.ROOT_URL + Consts.SAVE_ADDR, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    mHander.sendEmptyMessage(1);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string = response.body().string();
                    Log.i("saveaddr", string);
                    if (StringUtils.isNotEmpty(string)) {
                        Map<String, Object> map = GsonUtils.parseJsonObject(string);
                        if ("1".equals(map.get("RESULT") + ""))
                            mHander.sendEmptyMessage(0);
                        else
                            mHander.sendEmptyMessage(1);
                    } else
                        mHander.sendEmptyMessage(1);

                }
            });
        }
    }

    private void initView() {
        mBackAddress = (ImageView) findViewById(R.id.back_address);
        mBackAddress.setOnClickListener(AddressActivity.this);
        mEdtNameAddr = (EditText) findViewById(R.id.edt_name_addr);
        mEdtPhonenumberAddr = (EditText) findViewById(R.id.edt_phonenumber_addr);
        mEdtAddrAddr = (EditText) findViewById(R.id.edt_addr_addr);
        mBtnSaveAddr = (Button) findViewById(R.id.btn_save_addr);
        mBtnSaveAddr.setOnClickListener(AddressActivity.this);
        mTxtAddrAddr = (TextView) findViewById(R.id.txt_addr_addr);
        mTxtAddrAddr.setOnClickListener(AddressActivity.this);
        ckbSdefault = (CheckBox) findViewById(R.id.ckb_sdefault);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProvinceActivity.RESULT_DATA) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return;
                }
                //省份结果
                CityBean province = data.getParcelableExtra("province");
                //城市结果
                CityBean city = data.getParcelableExtra("city");
                //区域结果
                CityBean area = data.getParcelableExtra("area");

            }
        }
    }
}
