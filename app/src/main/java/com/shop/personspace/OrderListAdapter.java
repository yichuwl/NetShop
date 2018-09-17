package com.shop.personspace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.util.FactoryUtils;
import com.shop.util.GlideUtils;
import com.shop.util.GsonUtils;
import com.shop.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/7/2
 * @about
 */
public class OrderListAdapter extends BaseAdapter {
    protected Context mContext;
    protected int orderStateFlag;

    public void setItemClick(FactoryUtils.OnOrderListItemClick itemClick) {
        this.itemClick = itemClick;
    }

    FactoryUtils.OnOrderListItemClick itemClick;

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    List<Map<String, Object>> dataList = new ArrayList<>();

    public OrderListAdapter(Context context, List<Map<String, Object>> dataList, int flag) {
        mContext = context;
        orderStateFlag = flag;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Map<String, Object> map = dataList.get(position);
        orderStateFlag = (int) map.get("status");
        View view = convertView;
        ViewHolder viewHolder = null;
        if (view == null || !(view.getTag() instanceof ViewHolder)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderlist_wode, null, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.mTxtOrdernum.setText("订单编号：" + map.get("order_id") + "");
        viewHolder.mTxtOrderCount.setText(map.get("order_total") + "");
        viewHolder.mTxtOrderSeedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onSeeOrderDetailsClick(position);
            }
        });
        if (orderStateFlag == Consts.TO_BE_PAY_ORDER) {
            viewHolder.mBtnOrderOperator.setText(mContext.getText(R.string.gotopay));
            viewHolder.mBtnOrderOperator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onBtnOperateClick(position);
                }
            });
            viewHolder.mBtnOrderOperator.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_item_autoflow_corner));
            viewHolder.mTxtCancel.setVisibility(View.VISIBLE);
            viewHolder.mTxtCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onBtnCancelClick(position);
                }
            });
        } else if (orderStateFlag == Consts.TO_BE_COMMENT) {
            viewHolder.mTxtCancel.setVisibility(View.GONE);
            viewHolder.mBtnOrderOperator.setVisibility(View.VISIBLE);
            viewHolder.mBtnOrderOperator.setText("待评价");
             viewHolder.mBtnOrderOperator.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else if (orderStateFlag == Consts.TO_BE_SEND_ORDER) {
            viewHolder.mTxtCancel.setVisibility(View.GONE);
            viewHolder.mBtnOrderOperator.setVisibility(View.VISIBLE);
            viewHolder.mBtnOrderOperator.setText("待发货");
        } else if (orderStateFlag == Consts.TO_BE_RECEIVE_ORDER) {
            viewHolder.mTxtCancel.setVisibility(View.GONE);
            viewHolder.mBtnOrderOperator.setVisibility(View.VISIBLE);
            viewHolder.mBtnOrderOperator.setText(mContext.getText(R.string.check_wuliu));
            viewHolder.mBtnOrderOperator.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_item_autoflow_corner));
        }else if (orderStateFlag == Consts.TO_PAY_BACK){
            viewHolder.mTxtCancel.setVisibility(View.GONE);
            viewHolder.mBtnOrderOperator.setVisibility(View.VISIBLE);
            viewHolder.mBtnOrderOperator.setText("待退款");
            viewHolder.mBtnOrderOperator.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else if (orderStateFlag == Consts.PAY_BACK){
            viewHolder.mTxtCancel.setVisibility(View.GONE);
            viewHolder.mBtnOrderOperator.setVisibility(View.VISIBLE);
            viewHolder.mBtnOrderOperator.setText("退款成功");
            viewHolder.mBtnOrderOperator.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else if (orderStateFlag == Consts.TRADE_CLOSE){
            viewHolder.mTxtCancel.setVisibility(View.GONE);
            viewHolder.mBtnOrderOperator.setVisibility(View.VISIBLE);
            viewHolder.mBtnOrderOperator.setText("交易关闭");
            viewHolder.mBtnOrderOperator.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else if (orderStateFlag == Consts.TRADE_SUCESS){
            viewHolder.mTxtCancel.setVisibility(View.GONE);
            viewHolder.mBtnOrderOperator.setVisibility(View.VISIBLE);
            viewHolder.mBtnOrderOperator.setText("交易成功");
            viewHolder.mBtnOrderOperator.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        String goodsDetails = map.get("orderdetail") + "";
        viewHolder.mGoodsContent.removeAllViews();
        if (StringUtils.isNotEmpty(goodsDetails)) {
            List<Map<String, Object>> goodsMaps = GsonUtils.parseArrayGson(goodsDetails);
            for (int i = 0; i < goodsMaps.size(); i++) {
                Map<String, Object> goodMap = goodsMaps.get(i);
                View goodsView = View.inflate(mContext, R.layout.item_goods_orderlist, null);
                ImageView mImageItemGoodOrder = (ImageView) goodsView.findViewById(R.id.image_item_good_order);
                GlideUtils.loadImageOrGif(mContext, Consts.ROOT_URL + goodMap.get("goods_pic"), mImageItemGoodOrder);
                TextView mTxtTitleItemOrder = (TextView) goodsView.findViewById(R.id.txt_title_item_order);
                mTxtTitleItemOrder.setText(goodMap.get("goods_name") + "");
                TextView mTxtModelItemOrder = (TextView) goodsView.findViewById(R.id.txt_model_item_order);
                mTxtModelItemOrder.setText(goodMap.get("attribute_detail_name") + "");
                TextView mTextOrdergoodsMoney = (TextView) goodsView.findViewById(R.id.text_ordergoods_money);
                mTextOrdergoodsMoney.setText(goodMap.get("goods_price") + "");
                TextView mTextNumGoodsorder = (TextView) goodsView.findViewById(R.id.text_num_goodsorder);
                mTextNumGoodsorder.setText("x " + goodMap.get("goods_count") + "");
                TextView mBtnGoodsOperator = (TextView) goodsView.findViewById(R.id.btn_goods_order_operator);
                if (orderStateFlag == Consts.TO_BE_COMMENT) {
                    mBtnGoodsOperator.setVisibility(View.VISIBLE);
                    mBtnGoodsOperator.setText(mContext.getText(R.string.tobecomment));
                    mBtnGoodsOperator.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_item_autoflow_corner));
                }

                viewHolder.mGoodsContent.addView(goodsView);
            }
        }
        return view;
    }

    static class ViewHolder {
        protected TextView mTxtOrdernum, mTxtOrderCount, mTxtCancel;
        protected TextView mTxtOrderSeedetail;
        protected LinearLayout mGoodsContent;
        protected TextView mBtnOrderOperator;

        ViewHolder(View rootView) {
            initView(rootView);
        }

        private void initView(View rootView) {
            mTxtOrdernum = (TextView) rootView.findViewById(R.id.txt_ordernum);
            mTxtOrderSeedetail = (TextView) rootView.findViewById(R.id.txt_order_seedetail);
            mGoodsContent = (LinearLayout) rootView.findViewById(R.id.goods_content);
            mBtnOrderOperator = (TextView) rootView.findViewById(R.id.btn_order_operator);
            mTxtOrderCount = rootView.findViewById(R.id.txt_order_count);
            mTxtCancel = rootView.findViewById(R.id.btn_order_cancel);
        }
    }
}

/* protected ImageView mImageItemGoodOrder;
        protected TextView mTxtTitleItemOrder;
        protected TextView mTxtModelItemOrder;
        protected TextView mTextOrdergoodsMoney;
        protected TextView mTextNumGoodsorder;
        protected TextView mBtnOrderOperator;*/
/**/
