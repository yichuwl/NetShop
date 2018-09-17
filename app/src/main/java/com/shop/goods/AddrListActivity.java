package com.shop.goods;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shop.Consts;
import com.shop.R;
import com.shop.shopchart.AddressActivity;
import com.shop.util.FactoryUtils;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;
import com.shop.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddrListActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView backTop;
    protected TextView titleTop;
    protected TextView add;
    protected ListView listAddrlist;
    private AddrListAdapter adapter;
    String userId = "";
    private AlertDialog mAlertDialog;
    public static final int ADDRLIST = 0x12;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (mAlertDialog != null && mAlertDialog.isShowing()) {
                mAlertDialog.dismiss();
            }
            switch (what) {
                case 0:

                    break;
                case 1:
                    Toast.makeText(AddrListActivity.this, "操作失败，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case ADDRLIST:
                    String addrStr = msg.getData().getString("addr");
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(addrStr);
                    if (maps != null && maps.size() > 0) {
                        dataList.addAll(maps);
                        adapter.setDataList(dataList);
                        adapter.notifyDataSetChanged();
                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_addr_list);
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
        initView();
        initAddrData();
    }

    private void initAddrData() {
        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        String url = Consts.ROOT_URL + Consts.GET_ADDR_LIST;
        HashMap<String, String> params = new HashMap<>();
        params.put("USER_ID", userId);
        url = okUtils.getUrl(url, params);
        okUtils.httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("addrlist", string);
                if (StringUtils.isNotEmpty(string)) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString("addr", string);
                    message.setData(data);
                    message.what = ADDRLIST;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAddrData();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            finish();
        } else if (view.getId() == R.id.add) {
            startActivity(new Intent(AddrListActivity.this, AddressActivity.class));
        }
    }

    private void initView() {
        backTop = (ImageView) findViewById(R.id.back_top);
        backTop.setOnClickListener(AddrListActivity.this);
        titleTop = (TextView) findViewById(R.id.title_top);
        titleTop.setText(getString(R.string.addr_manage));
        add = (TextView) findViewById(R.id.add);
        add.setOnClickListener(AddrListActivity.this);
        listAddrlist = (ListView) findViewById(R.id.list_addrlist);
        initListView();
    }

    List<Map<String, Object>> dataList = new ArrayList<>();
    Map<String, Object> deleteMap = null;

    private void initListView() {
        adapter = new AddrListAdapter(this, dataList);
        listAddrlist.setAdapter(adapter);
        adapter.setItemClickListener(new FactoryUtils.OnAddrItemClickListener() {
            @Override
            public void onDeleteClickListener(int position) {
                if (dataList.size() > position) {
                    deleteMap = dataList.get(position);
                    mAlertDialog = new AlertDialog.Builder(AddrListActivity.this)
                            .setMessage("确定删除？")
                            .setNegativeButton(getString(R.string.cancle), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAddr(deleteMap);
                                }
                            }).create();
                    mAlertDialog.show();
                }
            }

            @Override
            public void onChangeClickListener(int position) {
                Intent intent = new Intent();
                intent.setClass(AddrListActivity.this, AddressActivity.class);
                Map<String, Object> map = dataList.get(position);
                try {
                    JSONObject jsonObject = GsonUtils.mapToArray(map);
                    intent.putExtra("addr", jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });

        listAddrlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                Map<String, Object> map = dataList.get(position);
                try {
                    JSONObject jsonObject = GsonUtils.mapToArray(map);
                    data.putExtra("addr", jsonObject.toString());
                    setResult(RESULT_OK, data);
                    AddrListActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void deleteAddr(Map<String, Object> map) {
        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        HashMap<String, String> params = new HashMap<>();
        //params.put("USER_ID", userId);
        params.put("ADDRESS_ID", map.get("ADDRESS_ID") + "");
        String url = okUtils.getUrl(Consts.ROOT_URL + Consts.DELETE_ADDR, params);
        okUtils.httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (StringUtils.isNotEmpty(string)) {
                    Map<String, Object> map = GsonUtils.parseJsonObject(string);
                    if ("1".equals(map.get("RESULT") + ""))
                        mHandler.sendEmptyMessage(0);
                    else
                        mHandler.sendEmptyMessage(1);
                } else
                    mHandler.sendEmptyMessage(1);
            }
        });
    }
}
