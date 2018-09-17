package com.shop.personspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.util.GlideUtils;
import com.shop.util.GsonUtils;
import com.shop.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mBackTop;
    protected TextView mTitleTop;
    protected ImageView mImgRight;
    protected TextView mTxtAddrOrderdetail;
    protected LinearLayout mGoodContentOrderdetail;
    protected TextView mBtnOperatorOrderdetail;
    protected TextView mTxtMoneyOrderdetail;
    protected TextView mTxtOrderdetail;
    protected TextView mCouponOrderdetail;
    private String mData;
    private Map<String, Object> dataMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_order_detail);
        Intent intent = getIntent();
        mData = intent.getStringExtra(ToBePayListActivity.ORDER_DATA_KEY);
        Log.e("mdata", mData);
        if (StringUtils.isNotEmpty(mData))
            dataMap = GsonUtils.parseJsonObject(mData);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            finish();
        } else if (view.getId() == R.id.txt_addr_orderdetail) {

        } else if (view.getId() == R.id.btn_operator_orderdetail) {

        }
    }

    private void initView() {
        mBackTop = (ImageView) findViewById(R.id.back_top);
        mBackTop.setOnClickListener(OrderDetailActivity.this);
        mTitleTop = (TextView) findViewById(R.id.title_top);
        mTitleTop.setText(getString(R.string.orderdetail));
        mImgRight = (ImageView) findViewById(R.id.img_right);
        mTxtAddrOrderdetail = (TextView) findViewById(R.id.txt_addr_orderdetail);
        String address = dataMap.get("address") + "\n" + dataMap.get("addr_realname") + "  "
                + dataMap.get("addr_phone");
        mTxtAddrOrderdetail.setText(address);
        mTxtAddrOrderdetail.setOnClickListener(OrderDetailActivity.this);
        mGoodContentOrderdetail = (LinearLayout) findViewById(R.id.good_content_orderdetail);
        mBtnOperatorOrderdetail = (TextView) findViewById(R.id.btn_operator_orderdetail);
        mBtnOperatorOrderdetail.setOnClickListener(OrderDetailActivity.this);
        mTxtMoneyOrderdetail = (TextView) findViewById(R.id.txt_money_orderdetail);
        mTxtMoneyOrderdetail.setText(dataMap.get("order_total") + "");
        mTxtOrderdetail = (TextView) findViewById(R.id.txt_orderdetail);
        String orderid = "订单编号：" + dataMap.get("order_id") + "\n";
        String addTime = "订单时间：" + dataMap.get("addtime");
        mTxtOrderdetail.setText(orderid+addTime);
        initContentDetail();
        mCouponOrderdetail = (TextView) findViewById(R.id.coupon_orderdetail);
        mCouponOrderdetail.setText("优惠金额：" + dataMap.get("coupon_price"));
    }

    private void initContentDetail() {
        String goodsDetails = dataMap.get("orderdetail") + "";
        if (StringUtils.isNotEmpty(goodsDetails)) {
            List<Map<String, Object>> goodsMaps = GsonUtils.parseArrayGson(goodsDetails);
            for (int i = 0; i < goodsMaps.size(); i++) {
                Map<String, Object> goodMap = goodsMaps.get(i);
                View goodsView = View.inflate(this, R.layout.item_goods_orderlist, null);
                ImageView mImageItemGoodOrder = (ImageView) goodsView.findViewById(R.id.image_item_good_order);
                GlideUtils.loadImageOrGif(this, Consts.ROOT_URL + goodMap.get("goods_pic"), mImageItemGoodOrder);
                TextView mTxtTitleItemOrder = (TextView) goodsView.findViewById(R.id.txt_title_item_order);
                mTxtTitleItemOrder.setText(goodMap.get("goods_name") + "");
                TextView mTxtModelItemOrder = (TextView) goodsView.findViewById(R.id.txt_model_item_order);
                mTxtModelItemOrder.setText(goodMap.get("attribute_detail_name") + "");
                TextView mTextOrdergoodsMoney = (TextView) goodsView.findViewById(R.id.text_ordergoods_money);
                mTextOrdergoodsMoney.setText(goodMap.get("goods_price") + "");
                TextView mTextNumGoodsorder = (TextView) goodsView.findViewById(R.id.text_num_goodsorder);
                mTextNumGoodsorder.setText("x " + goodMap.get("goods_count") + "");
                TextView mBtnGoodsOperator = (TextView) goodsView.findViewById(R.id.btn_goods_order_operator);

                mGoodContentOrderdetail.addView(goodsView);
            }
        }
    }
}
