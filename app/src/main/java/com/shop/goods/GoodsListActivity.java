package com.shop.goods;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.StringUtils;
import com.shop.util.SwipeRefreshUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GoodsListActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mBackTop;
    protected TextView mTitleTop;
    protected RecyclerView mRecyGoodlist;
    protected SwipeRefreshLayout swipeGoodslist;
    protected ImageView imgRight;
    protected Button mBtnSortbyprice;
    protected ImageView mBtnSortMore;
    private GridLayoutManager mGridLayoutManager;
    private GoodsListAdapter mAdapter;
    protected String themeId = "";
    protected String paramKey = "";
    public static final int REQUEST_SEARCH = 0x112;
    String orderFlag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_goods_list);
        Intent intent = getIntent();
        themeId = intent.getStringExtra(Consts.PARAM_GOODS_LIST_THEME_FIRST);
        paramKey = intent.getStringExtra(Consts.PARAM_GOODS_LIST_PARAM);
        searchWords = intent.getStringExtra(GoodsSearchActivity.SEARCH_CONTENT);
        initView();
        swipeGoodslist.setRefreshing(true);
    }

    List<Map<String, Object>> dataList = new ArrayList<>();

    private void initData() {
        String url = Consts.ROOT_URL + Consts.THEME_SEEMORE_GOODS_LIST;
        HashMap<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(themeId)) {
            params.put(paramKey, themeId);
        }
        if (StringUtils.isNotEmpty(searchWords)) {
            params.put("keywords", searchWords);
        }
        if ("DESC".equals(orderFlag)){
            params.put("ORDERFLAG","GOODS_PRICE");
            params.put("SORTSTRING",orderFlag);
        }else if ("asc".equals(orderFlag)){
            params.put("ORDERFLAG","GOODS_PRICE");
        }

        url = OkUtils.getOkUtilsInstance().getUrl(url, params);
        Log.e("goodslisturl", url);
        OkUtils.getOkUtilsInstance().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                SwipeRefreshUtils.setRefreshState(GoodsListActivity.this, swipeGoodslist, false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //if (Consts.RESPONSE_OK_STATE.equals(response.message())) {
                String string = response.body().string();
                if (!TextUtils.isEmpty(string)) {
                    dataList.clear();
                    Log.i("goodslist", string);
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(string);
                    dataList.addAll(maps);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (refreshView.getVisibility() == View.VISIBLE)
                                refreshView.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                            swipeGoodslist.setRefreshing(false);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (refreshView.getVisibility() == View.GONE)
                                refreshView.setVisibility(View.VISIBLE);
                        }
                    });
                }
                // }
                SwipeRefreshUtils.setRefreshState(GoodsListActivity.this, swipeGoodslist, false);
            }
        });
    }

    boolean priceUp = true; // true--- low to up

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_top) {
            finish();
        } else if (view.getId() == R.id.img_right) {
            // todo 跳转至搜索
            Intent intent = new Intent(GoodsListActivity.this, GoodsSearchActivity.class);
            intent.putExtra(GoodsSearchActivity.FROM, GoodsSearchActivity.FROM_LIST);
            startActivityForResult(intent, REQUEST_SEARCH);
        } else if (view.getId() == R.id.btn_sortbyprice) {
            if (priceUp) {
                priceUp = false;
                mBtnSortbyprice.setText(getString(R.string.pricelowtohigh));
                orderFlag = "asc";
                mBtnSortbyprice.setTextColor(getResources().getColor(R.color.main_color));
            } else {
                priceUp = true;
                orderFlag = "DESC";
                mBtnSortbyprice.setText(getString(R.string.pricehightolow));
            }
            initData();
            // todo search
        } else if (view.getId() == R.id.btn_sort_more) {

        }
    }

    RelativeLayout refreshView;

    private void initView() {
        mBackTop = (ImageView) findViewById(R.id.back_top);
        mBackTop.setOnClickListener(GoodsListActivity.this);
        mTitleTop = (TextView) findViewById(R.id.title_top);
        mTitleTop.setText(getString(R.string.list));
        mRecyGoodlist = (RecyclerView) findViewById(R.id.recy_goodlist);
        swipeGoodslist = (SwipeRefreshLayout) findViewById(R.id.swipe_goodslist);
        swipeGoodslist.setColorSchemeResources(R.color.main_color);
        refreshView = findViewById(R.id.refresh_goodslist_view);
        initRecy();
        initSwipeRefresh();
        initData();
        swipeGoodslist.setRefreshing(true);
        imgRight = (ImageView) findViewById(R.id.img_right);
        imgRight.setOnClickListener(GoodsListActivity.this);
        imgRight.setVisibility(View.VISIBLE);
        imgRight.setImageResource(R.mipmap.searchicon);
        mBtnSortbyprice = (Button) findViewById(R.id.btn_sortbyprice);
        mBtnSortbyprice.setOnClickListener(GoodsListActivity.this);
        mBtnSortMore = (ImageView) findViewById(R.id.btn_sort_more);
        mBtnSortMore.setOnClickListener(GoodsListActivity.this);
    }

    int lastShowItemPosition = 0;

    private void initSwipeRefresh() {
        swipeGoodslist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // loading
                initData();
            }
        });
    }

    public static final String GOOD_ID = "goodid";

    private void initRecy() {
        mRecyGoodlist.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(4, 4, 4, 4);
            }
        });

        mGridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyGoodlist.setLayoutManager(mGridLayoutManager);
        mAdapter = new GoodsListAdapter(this, dataList);
        mAdapter.setClickListener(new GoodsListAdapter.OnItemClickListener() {
            @Override
            public void clickListener(int position) {
                Intent intent = new Intent(GoodsListActivity.this, GoodsShowActivity.class);
                intent.putExtra(GOOD_ID, dataList.get(position).get("GOODS_ID") + "");
                intent.putExtra(GoodsShowActivity.FROM, GoodsShowActivity.FROM_NOMAL_LIST);
                startActivity(intent);
            }
        });
        mRecyGoodlist.setAdapter(mAdapter);
        mRecyGoodlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastShowItemPosition >= 20
                        && (lastShowItemPosition + 1) == mAdapter.getItemCount()) {
                    // todo loadmore
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastShowItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    String searchWords = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SEARCH && resultCode == RESULT_OK) {
            searchWords = data.getStringExtra(GoodsSearchActivity.SEARCH_CONTENT);
            initData(); // todo searchurl
        }
    }
}
