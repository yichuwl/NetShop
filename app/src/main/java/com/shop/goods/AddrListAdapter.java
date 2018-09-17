package com.shop.goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shop.R;
import com.shop.util.FactoryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by victoryf on 2018-07-06.
 */
public class AddrListAdapter extends BaseAdapter {
    Context mContext;

    public void setDataList(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }

    List<Map<String, Object>> dataList = new ArrayList<>();

    public AddrListAdapter(Context context, List<Map<String, Object>> data) {
        mContext = context;
        dataList = data;
    }

    public void setItemClickListener(FactoryUtils.OnAddrItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    FactoryUtils.OnAddrItemClickListener itemClickListener;

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
        Map<String, Object> object = dataList.get(position);
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addr_list, null, false);
        ViewHolder viewHolder = new ViewHolder(convertView);
        String addrCity = object.get("ADDR_CITY") + "";
        String addrDetial = object.get("ADDR_DETAILS") + "";
        String userName = object.get("ADDR_REALNAME") + "";
        String phone = object.get("ADDR_PHONE") + "";
        viewHolder.txtNameAddrlist.setText(userName);
        viewHolder.txtPhoneAddrlist.setText(phone);
        viewHolder.txtDetailAddrlist.setText(addrCity + " " + addrDetial);
        if ("1".equals(object.get("IS_DEFAULT"))) {
            viewHolder.txtDefaultAddrlist.setVisibility(View.VISIBLE);
        } else
            viewHolder.txtDefaultAddrlist.setVisibility(View.GONE);
        viewHolder.imgChangeAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onChangeClickListener(position);
            }
        });
        viewHolder.imgDeleteAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onDeleteClickListener(position);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        protected TextView txtNameAddrlist, txtPhoneAddrlist, txtDetailAddrlist, txtDefaultAddrlist;
        protected ImageView imgDeleteAddr;
        protected ImageView imgChangeAddr;

        ViewHolder(View rootView) {
            initView(rootView);
        }

        private void initView(View rootView) {
            txtNameAddrlist = (TextView) rootView.findViewById(R.id.txt_addr_name_addrlist);
            txtPhoneAddrlist = (TextView) rootView.findViewById(R.id.txt_addr_phone_addrlist);
            txtDetailAddrlist = (TextView) rootView.findViewById(R.id.txt_addr_addrdetail_addrlist);
            txtDefaultAddrlist = (TextView) rootView.findViewById(R.id.txt_isdefault);
            imgDeleteAddr = (ImageView) rootView.findViewById(R.id.img_delete_addr);
            imgChangeAddr = (ImageView) rootView.findViewById(R.id.img_change_addr);
        }
    }
}
