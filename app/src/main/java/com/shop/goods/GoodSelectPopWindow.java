package com.shop.goods;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.AutoFlowLayout;
import com.example.library.FlowAdapter;
import com.shop.Consts;
import com.shop.R;
import com.shop.util.FactoryUtils;
import com.shop.util.GlideUtils;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by victoryf on 2018-06-28.
 */
public class GoodSelectPopWindow extends PopupWindow implements View.OnClickListener {

    protected ImageView imageThumbGoodselect;
    protected TextView txtGoodstitleWindow;
    protected TextView txtGoodsrmbWindow;
    protected TextView txtGoodspriceWindow;
    protected AutoFlowLayout autoflowGoodsselect;
    protected LinearLayout linearModel;
    protected TextView txtMinusGoodssel;
    protected TextView txtNumGoodssel;
    protected TextView txtPlusGoodssel;
    protected Button btnTochart;
    protected Button btnBynow;
    protected ImageView mImgClosewindow;
    protected Button mBtnGoodsselectSure;
    GoodsShowActivity mContext;
    private FlowAdapter adapter;
    String goodId = "";

    public void setSelectModelListener(FactoryUtils.OnGoodsSelectModelListener selectModelListener) {
        this.selectModelListener = selectModelListener;
    }

    FactoryUtils.OnGoodsSelectModelListener selectModelListener;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 0:
                    itemViewId.clear();
                    autoflowGoodsselect.removeAllViews();
                    autoflowGoodsselect.setAdapter(adapter);
                    break;
            }
        }
    };

    String defaultAttributeId = "";

    public GoodSelectPopWindow(GoodsShowActivity context, String goodsId, String defaultAttrId) {
        super();
        this.mContext = context;
        defaultAttributeId = defaultAttrId;
        View contentView = View.inflate(context, R.layout.popwindow_goods_select, null);
        this.setContentView(contentView);
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.gray_transparent)));
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        goodId = goodsId;
        initView(contentView);
    }

    public void setGoodName(String goodName, String picUrl) {
        txtGoodstitleWindow.setText(goodName);
        if (StringUtils.isNotEmpty(picUrl))
            GlideUtils.loadImageOrGif(mContext, picUrl, imageThumbGoodselect);
    }

    public void setModelData(List<Map<String, Object>> list) {
        modelList = list;
        for (int i = 0; i < modelList.size(); i++) {
            Map<String, Object> map = modelList.get(i);
            if (Integer.valueOf(map.get("ATTRIBUTENUM") + "") > 0) {
                modelDataList.add(map.get("ATTRIBUTE") + "");
            }
            if (defaultAttributeId.equals(map.get("ATTRIBUTE_ID") + "")) {
                selectedPosition = i;
                mAttrNum.setText("库存：" + modelList.get(i).get("ATTRIBUTENUM"));
                String seckillprice = modelList.get(i).get("SECKILLPRICE") + "";
                if (StringUtils.isNotEmpty(seckillprice)) {
                    Float value = Float.valueOf(seckillprice);
                    if (value > 0f) {
                        txtGoodspriceWindow.setText(seckillprice);
                        modelList.get(i).put("ATTRIBUTEPRICE", seckillprice);
                    } else {
                        txtGoodspriceWindow.setText(modelList.get(i).get("ATTRIBUTEPRICE") + "");
                    }
                } else
                    txtGoodspriceWindow.setText(modelList.get(i).get("ATTRIBUTEPRICE") + "");
            }
        }
        mHandler.sendEmptyMessage(0);
    }

    private List<Map<String, Object>> modelList = new ArrayList<>();

    TextView mAttrNum;

    private void initView(View rootView) {
        imageThumbGoodselect = (ImageView) rootView.findViewById(R.id.image_thumb_goodselect);
        txtGoodstitleWindow = (TextView) rootView.findViewById(R.id.txt_goodstitle_window);
        txtGoodsrmbWindow = (TextView) rootView.findViewById(R.id.txt_goodsrmb_window);
        txtGoodspriceWindow = (TextView) rootView.findViewById(R.id.txt_goodsprice_window);
        autoflowGoodsselect = (AutoFlowLayout) rootView.findViewById(R.id.autoflow_goodsselect);
        linearModel = (LinearLayout) rootView.findViewById(R.id.linear_model);
        txtMinusGoodssel = (TextView) rootView.findViewById(R.id.txt_minus_goodssel);
        txtMinusGoodssel.setOnClickListener(GoodSelectPopWindow.this);
        txtNumGoodssel = (TextView) rootView.findViewById(R.id.txt_num_goodssel);
        txtPlusGoodssel = (TextView) rootView.findViewById(R.id.txt_plus_goodssel);
        txtPlusGoodssel.setOnClickListener(GoodSelectPopWindow.this);
        initAutoFlow();
        mImgClosewindow = (ImageView) rootView.findViewById(R.id.img_closewindow);
        mImgClosewindow.setOnClickListener(GoodSelectPopWindow.this);
        mBtnGoodsselectSure = (Button) rootView.findViewById(R.id.btn_goodsselect_sure);
        mBtnGoodsselectSure.setOnClickListener(GoodSelectPopWindow.this);
        mAttrNum = rootView.findViewById(R.id.txt_goodsnum_window);
    }

    List<String> modelDataList = new ArrayList<>();
    List<Integer> itemViewId = new ArrayList<>();

    private void initAutoFlow() {
        autoflowGoodsselect.setHorizontalSpace(8);
        adapter = new FlowAdapter(modelDataList) {
            @Override
            public View getView(int position) {
                View item = View.inflate(mContext, R.layout.text, null);
                TextView tvAttrTag = (TextView) item.findViewById(R.id.text_only);
                tvAttrTag.setMinWidth(40);
                if (position == selectedPosition) {
                    tvAttrTag.setBackgroundColor(mContext.getResources().getColor(R.color.main_bg_transparent_red));
                } else
                    tvAttrTag.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_gray_corner));
                tvAttrTag.setText(modelDataList.get(position));
                itemViewId.add(tvAttrTag.getId());
                return item;
            }
        };
        //autoflowGoodsselect.setAdapter(adapter);
        autoflowGoodsselect.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int i, View view) {
                selectedPosition = i;
                mAttrNum.setText("库存：" + modelList.get(i).get("ATTRIBUTENUM"));
                txtGoodspriceWindow.setText(modelList.get(i).get("ATTRIBUTEPRICE") + "");
                autoflowGoodsselect.clearViews();
                autoflowGoodsselect.setAdapter(adapter);
            }
        });
    }

    int selectedPosition = -1;

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_minus_goodssel) {
            if (selectedPosition == -1) {
                Toast.makeText(mContext, "请选择规格型号", Toast.LENGTH_SHORT).show();
                return;
            }
            int num = Integer.valueOf(txtNumGoodssel.getText().toString());
            if (num > 1) {
                txtNumGoodssel.setText(String.valueOf(num - 1));
            }
        } else if (view.getId() == R.id.txt_plus_goodssel) {
            if (selectedPosition == -1) {
                Toast.makeText(mContext, "请选择规格型号", Toast.LENGTH_SHORT).show();
                return;
            }
            int num = Integer.valueOf(txtNumGoodssel.getText().toString());
            int attributenum = Integer.valueOf(modelList.get(selectedPosition).get("ATTRIBUTENUM") + "");
            if (num >= 0 && num < attributenum) {  //  与库存做对比
                txtNumGoodssel.setText(String.valueOf(num + 1));
            }
        } else if (view.getId() == R.id.img_closewindow) {
            this.dismiss();
        } else if (view.getId() == R.id.btn_goodsselect_sure) {
            // todo 数据传回
            if (selectedPosition == -1) {
                Toast.makeText(mContext, mContext.getText(R.string.select_model), Toast.LENGTH_SHORT).show();
            } else {
                Map<String, Object> map = modelList.get(selectedPosition);
                String attributenum = map.get("ATTRIBUTENUM") + "";
                if (StringUtils.isEmpty(attributenum) || Integer.valueOf(attributenum) < 1) {
                    Toast.makeText(mContext, "该型号库存不足", Toast.LENGTH_SHORT).show();
                    return;
                }
                map.put("ATTRIBUTENUM", txtNumGoodssel.getText().toString());
                selectModelListener.getSelectedModelData(map);
            }
        }
    }
}
