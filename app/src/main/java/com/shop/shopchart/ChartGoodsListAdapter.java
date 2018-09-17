package com.shop.shopchart;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.shop.R;
import com.shop.util.FactoryUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author victory
 * @time 2018/6/27
 * @about
 */
public class ChartGoodsListAdapter extends BaseAdapter {

    Context mContext;

    public void setGoodsCheckMap(Map<Integer, Boolean> goodsCheckMap) {
        this.goodsCheckMap = goodsCheckMap;
    }

    Map<Integer, Boolean> goodsCheckMap = new HashMap<>();

    public void setmInterfaceUtils(FactoryUtils.OnChartChangeListener mInterfaceUtils) {
        this.mInterfaceUtils = mInterfaceUtils;
    }

    FactoryUtils.OnChartChangeListener mInterfaceUtils;

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
        for (int i = 0; i < dataList.size(); i++) {
            goodsCheckMap.put(i, false);
        }
        mpMap.clear();
    }

    List<Map<String, Object>> dataList;

    public Map<Integer, TextView> getMpMap() {
        return mpMap;
    }

    Map<Integer, TextView> mpMap = new HashMap<>();

    public ChartGoodsListAdapter(Context context, List<Map<String, Object>> data) {
        mContext = context;
        dataList = data;
        for (int i = 0; i < dataList.size(); i++) {
            goodsCheckMap.put(i, false);
        }
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

    ViewHolder holder = null;
    String num;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Map<String, Object> map = dataList.get(position);
       if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_chartgoods_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.priceTxt.setText(map.get("GOODS_PRICE") + "");
        holder.numTxt.setText(map.get("GOODS_NUM") + "");
        holder.titleTxt.setText(map.get("GOODS_NAME") + "");
        holder.modelTxt.setText(map.get("GOODS_ATTRIBUTE") + "");
        HashMap<Integer, TextView> hashMap = new HashMap<>();
        mpMap.put(position, holder.numTxt);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mInterfaceUtils.onCheckedChangeListener(position, isChecked);
                goodsCheckMap.put(position, isChecked);
            }
        });
        holder.mCheckBox.setChecked(goodsCheckMap.get(position));
        holder.minusTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* num = holder.numTxt.getText().toString();
                Integer value = Integer.valueOf(num);*/
               /* if (value > 1) {
                    int num = value - 1;
                    mpMap.get(position).setText(String.valueOf(num));*/
                    // todo update data
                    mInterfaceUtils.onMinusNumListener(position);
                //}
            }
        });
        holder.plusTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* num = holder.numTxt.getText().toString();
                Integer value = Integer.valueOf(num);
                int num = value + 1;
                mpMap.get(position).setText(String.valueOf(num));*/
                // todo update data
                mInterfaceUtils.onPlusNumListener(position);
            }
        });
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo update data
                mInterfaceUtils.onRemoveGoodsListener(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        CheckBox mCheckBox;
        ImageView mImageView;
        TextView priceTxt, minusTxt, plusTxt, numTxt, titleTxt, modelTxt;
        Button removeBtn;

        public ViewHolder(View rootView) {
            mCheckBox = rootView.findViewById(R.id.checkbox_good_chart);
            mImageView = rootView.findViewById(R.id.image_item_good_chart);
            titleTxt = rootView.findViewById(R.id.txt_title_item_goods);
            priceTxt = rootView.findViewById(R.id.text_chartgoods_money);
            minusTxt = rootView.findViewById(R.id.text_minus_goodschart);
            plusTxt = rootView.findViewById(R.id.text_plus_goodschart);
            numTxt = rootView.findViewById(R.id.text_num_goodschart);
            removeBtn = rootView.findViewById(R.id.btn_remove_goodschart);
            modelTxt = rootView.findViewById(R.id.txt_model_item_goods);
        }
    }
}
