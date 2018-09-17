package com.shop.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.R;
import com.shop.shopchart.EnsureOrderActivity;

public class SelectPayTypeActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mBackTop;
    protected TextView mTitleTop;
    protected RadioButton mRdbWechatPay;
    protected RadioButton mRdbZhifubaoPay;
    protected Button mBtnSuretopay;
    protected RadioGroup mRdgPaytype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_select_pay_type);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            finish();
        } else if (view.getId() == R.id.btn_suretopay) {
            int checkedRadioButtonId = mRdgPaytype.getCheckedRadioButtonId();
            Intent data = new Intent();
            switch (checkedRadioButtonId) {
                case R.id.rdb_wechat_pay:
                    data.putExtra("payType", EnsureOrderActivity.WECHAT_PAY);
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                case R.id.rdb_zhifubao_pay:
                    data.putExtra("payType", EnsureOrderActivity.ZHIFUBAO_PAY);
                    setResult(RESULT_OK, data);
                    finish();
                    break;
                default:
                    Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void initView() {
        mBackTop = (ImageView) findViewById(R.id.back_top);
        mBackTop.setOnClickListener(SelectPayTypeActivity.this);
        mTitleTop = (TextView) findViewById(R.id.title_top);
        mTitleTop.setText(getString(R.string.select_pay_type));
        mRdbWechatPay = (RadioButton) findViewById(R.id.rdb_wechat_pay);
        mRdbZhifubaoPay = (RadioButton) findViewById(R.id.rdb_zhifubao_pay);
        mBtnSuretopay = (Button) findViewById(R.id.btn_suretopay);
        mBtnSuretopay.setOnClickListener(SelectPayTypeActivity.this);
        mRdgPaytype = (RadioGroup) findViewById(R.id.rdg_paytype);
        initRdgroup();
    }

    private void initRdgroup() {
        mRdgPaytype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });
    }
}
