package com.shop.firstshow;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.shop.Consts;
import com.shop.MainActivity;
import com.shop.R;
import com.shop.goods.GoodsListActivity;
import com.shop.goods.GoodsSearchActivity;
import com.shop.goods.GoodsShowActivity;
import com.shop.goods.TimeToByListActivity;
import com.shop.util.FactoryUtils;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.PreferenceUtil;
import com.shop.util.StringUtils;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.shop.goods.GoodsListActivity.GOOD_ID;

/**
 * @author victory
 * @time 2018/6/25
 * @about 首页页面
 */

public class ShouYeFragment extends Fragment implements View.OnClickListener {

    protected View rootView;
    protected RecyclerView mRecyFirstshow;
    protected SwipeRefreshLayout mSwipeFirstshowlist;
    protected ImageView imgSearch;
    protected EditText edtSearch;
    protected Button btnSearch;
    protected LinearLayout linearSearchbar;
    private VirtualLayoutManager mLayoutManager;
    private FirstShowBannerAdapter mBannerAdapter;
    private GridLayoutHelper mCategoryHelper;
    private AdsAdapter mAdsAdapter;
    private FirstShowCatoryAdapter mCatoryAdapter;
    private TimeToByAdapter mTimeToByAdapter;
    private ThemeTobyAdapter mThemeTobyAdapter;
    private MayBeLikeAdapter mMayBeLikeAdapter;
    private FactoryUtils.OnItemClickListenerFactory categoryIdClick;

    public static ShouYeFragment newInstance() {

        Bundle args = new Bundle();

        ShouYeFragment fragment = new ShouYeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static final int BANNER_WHAT = 0;
    public static final int CATEGORY_WHAT = 1;
    public static final int ADS_WHAT = 2;
    public static final int TIME_WHAT = 3;
    public static final int THEME_WHAT = 4;
    public static final int MAYLIKE_WHAT = 5;
    public static final int COUPON_WHAT = 6;
    String userId = "";


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case BANNER_WHAT:
                    notifyDataSet(mBannerAdapter);
                    break;
                case CATEGORY_WHAT:
                    notifyDataSet(mCatoryAdapter);
                    break;
                case ADS_WHAT:
                    notifyDataSet(mAdsAdapter);
                    break;
                case TIME_WHAT:
                    notifyDataSet(mTimeToByAdapter);
                    break;
                case MAYLIKE_WHAT:
                    notifyDataSet(mMayBeLikeAdapter);
                    break;
                case THEME_WHAT:
                    notifyDataSet(mThemeTobyAdapter);
                    break;
                case COUPON_WHAT:
                    if (StringUtils.isNotEmpty(userId)) {
                        String string = msg.getData().getString("coupondata");
                        GetCouponPopWindow popWindow = new GetCouponPopWindow(getActivity(), string);
                        // popWindow.setCouponDataStr(string);
                        popWindow.showAtLocation(mSwipeFirstshowlist, Gravity.CENTER, 0, 0);
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_shouye, null);
        initView(contentView);
        userId = (String) PreferenceUtil.getParam(PreferenceUtil.USERID, "");
        initCouponListData();
        return contentView;
    }

    TextView txtSearch;

    private void initView(View rootView) {
        mRecyFirstshow = (RecyclerView) rootView.findViewById(R.id.recy_firstshow);
        mSwipeFirstshowlist = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_firstshowlist);
        mSwipeFirstshowlist.setColorSchemeColors(getContext().getResources().getColor(R.color.main_color));
        initSwipeRefresh();
        initRecycler();
        imgSearch = (ImageView) rootView.findViewById(R.id.img_search);
        txtSearch = (TextView) rootView.findViewById(R.id.txt_search);
        linearSearchbar = (LinearLayout) rootView.findViewById(R.id.linear_shouye_searchbar);
        txtSearch.setOnClickListener(ShouYeFragment.this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    boolean isFirstStart = true;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstStart) {
            isFirstStart = false;
            try {
                getBannerData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Map<String, Object>> mayLikeDataList = new ArrayList<>();

    private void getMayLikeData() {
        OkUtils.getOkUtilsInstance().setNewClient().httpGet(Consts.ROOT_URL + Consts.RECOMMEND_GOODS_LIST, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeFirstshowlist.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // if (Consts.RESPONSE_OK_STATE.equals(response.message())) {
                String string = response.body().string();
                if (!TextUtils.isEmpty(string)) {

                    Log.i("goodslistmaylike", string);
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(string);
                    int m = 0;
                    if (mayLikeDataList.size() == maps.size()) {
                        for (int i = 0; i < maps.size(); i++) {
                            if ((mayLikeDataList.get(i).get("GOODS_ID") + "").equals
                                    ((maps.get(i).get("GOODS_ID") + ""))) {
                                m = i;
                            }
                        }
                    }
                    if (m < maps.size() - 1) {
                        mayLikeDataList.clear();
                        mayLikeDataList.addAll(maps);
                        mMayBeLikeAdapter.setDataList(mayLikeDataList);
                        mHandler.sendEmptyMessage(MAYLIKE_WHAT);
                    }

                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeFirstshowlist.setRefreshing(false);
                    }
                });

                //  }
            }
        });
    }

    private void getThemeData() {
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put(Consts.ADS_PARMA_KEY, Consts.FENLEI_PARAM);
            OkUtils.getOkUtilsInstance().setNewClient().httpPostJson(jsonRequest, Consts.ROOT_URL + Consts.ADS_FIRST_SHOW, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getTimeToByData();
                    getMayLikeData();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // if (Consts.RESPONSE_OK_STATE == response.message()) {
                    String string = response.body().string();
                    Log.i("themeimageurl", string);
                    if (!TextUtils.isEmpty(string)) {

                        List<Map<String, Object>> maps = GsonUtils.parseArrayGson(string);
                        int m = 0;
                        for (int i = 0; i < maps.size(); i++) {
                            if (themeImageUrlList.size() == maps.size() &&
                                    themeImageUrlList.get(i).equals(maps.get(i).get("ADVERTISE_PIC") + "")) {
                                m = i;
                            }
                            // themeImageUrlList.add(maps.get(i).get("ADVERTISE_PIC") + "");
                        }
                        if (m < maps.size() - 1) {
                            themeImageUrlList.clear();
                            for (int i = 0; i < maps.size(); i++) {
                                themeImageUrlList.add(maps.get(i).get("ADVERTISE_PIC") + "");
                            }
                            mThemeTobyAdapter.setImageUrlList(themeImageUrlList);
                            mThemeTobyAdapter.setDataList(categoryDataList);
                            mHandler.sendEmptyMessage(THEME_WHAT);
                        }
                    }
                    //}
                    getTimeToByData();
                    getMayLikeData();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getTimeToByData() {
        OkUtils okUtils = OkUtils.getOkUtilsInstance().setNewClient();
        okUtils.httpGet(Consts.ROOT_URL + Consts.GET_TIME_TO_BY, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.i("timeheaderdata", string);
                if (StringUtils.isNotEmpty(string)) {
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(string);
                    if (maps.size() > 0) {
                        final Map<String, Object> map = maps.get(0);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getGoodsList(map);

                            }
                        });
                    }

                } else {

                }
            }
        });
    }

    private void getGoodsList(final Map<String, Object> map) {
        OkUtils okUtils = OkUtils.getOkUtilsInstance();
        HashMap<String, String> params = new HashMap<>();
        params.put("SECKILL_ID", map.get("SECKILL_ID") + "");
        String url = okUtils.getUrl(Consts.ROOT_URL + Consts.GET_TIME_TO_BY_GOODS_LIST, params);
        okUtils.setNewClient().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("timegoodslist", "--" + string);
                if (StringUtils.isNotEmpty(string)) {
                    final List<Map<String, Object>> goodsDataList = GsonUtils.parseArrayGson(string);
                    // mHandler.sendEmptyMessage(2);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTimeToByAdapter.setItemClickListener(new TimeToByAdapter.TimeToByItemClickListener() {

                                @Override
                                public void onItemClick(int position) {
                                    if ("1".equals(map.get("ISNOTSTART") + "")) {
                                        startActivity(new Intent(getActivity(), TimeToByListActivity.class));
                                    } else {
                                        Map<String, Object> map = goodsDataList.get(position);
                                        Intent intent = new Intent(getActivity(), GoodsShowActivity.class);
                                        intent.putExtra(GoodsListActivity.GOOD_ID, map.get("GOODS_ID") + "");
                                        intent.putExtra(GoodsShowActivity.FROM, GoodsShowActivity.FROM_TIME_LIST);
                                        startActivity(intent);
                                    }
                                }
                            });

                        }
                    });
                    mTimeToByAdapter.setData(goodsDataList);
                    mHandler.sendEmptyMessage(TIME_WHAT);
                }
            }
        });
    }

    private void getAdsData() throws JSONException {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put(Consts.ADS_PARMA_KEY, Consts.TIME_PARAM);
        OkUtils.getOkUtilsInstance().setNewClient().httpPostJson(jsonRequest, Consts.ROOT_URL + Consts.ADS_FIRST_SHOW, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    getCategoryData();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // if (Consts.RESPONSE_OK_STATE.equals(response.message())) {
                String data = response.body().string();
                if (!TextUtils.isEmpty(data)) {
                    Log.e("timedata", data);
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(data);
                    if (!adsUrl.equals(maps.get(0).get("ADVERTISE_PIC") + "")) {
                        adsUrl = (String) maps.get(0).get("ADVERTISE_PIC");
                        mAdsAdapter.setUrl(adsUrl);
                        mHandler.sendEmptyMessage(ADS_WHAT);
                    }
                }
                //}
                try {
                    getCategoryData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    List<Map<String, Object>> themeSubMapList = new ArrayList<>();
    String categoryStr = "";

    private void getCategoryData() throws JSONException {
        OkUtils.getOkUtilsInstance().httpGet(Consts.ROOT_URL + Consts.CATEGORY_FIRST_SHOW, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getThemeData();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message = response.message();
                // if ("OK".equals(message)) {
                categoryStr = response.body().string();
                if (!TextUtils.isEmpty(categoryStr)) {
                    try {
                        if (!categoryStr.equals(GsonUtils.listToArray(categoryDataList).toString())) {
                            categoryDataList.clear();
                            Log.i("category", categoryStr + "----");
                            List<Map<String, Object>> maps = GsonUtils.parseArrayGson(categoryStr);
                            for (int i = 0; i < maps.size(); i++) {
                                Map<String, Object> map = maps.get(i);
                                Map<String, Object> tempMap = new HashMap<>();
                                tempMap.put("CATEGORY_NAME", map.get("CATEGORY_NAME") + "");
                                tempMap.put("CATEGORY_IMG", map.get("CATEGORY_IMG") + "");
                                tempMap.put("SON", map.get("SON") + "");
                                tempMap.put("CATEGORY_ID", map.get("CATEGORY_ID") + "");
                                categoryDataList.add(tempMap);

                            }
                            mHandler.sendEmptyMessage(CATEGORY_WHAT);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //}
                getThemeData();
            }
        });
    }

    private void getBannerData() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.ADS_PARMA_KEY, Consts.BANNER_PARAM);
        //jsonObject.put("ADV_NUM", 0);
        OkUtils.getOkUtilsInstance().setNewClient().httpPostJson(jsonObject, Consts.ROOT_URL + Consts.ADS_FIRST_SHOW, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    getAdsData();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message = response.message();
                String data = response.body().string();
                Log.i("bannerurl", message + "---" + data);
//                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                if ("OK".equals(message)) {
                    if (!TextUtils.isEmpty(data)) {
                        int m = 0;
                        List<Map<String, Object>> maps = GsonUtils.parseArrayGson(data);
                        for (int i = 0; i < maps.size(); i++) {
                            if (bannerImageUrls.size() == maps.size() &&
                                    bannerImageUrls.get(i).equals(maps.get(i).get("ADVERTISE_PIC") + "")) {
                                m = i;
                            }
                            // bannerImageUrls.add(maps.get(i).get("ADVERTISE_PIC") + "");
                        }

                        if (m < maps.size() - 1) {
                            bannerImageUrls.clear();
                            for (int i = 0; i < maps.size(); i++) {
                                bannerImageUrls.add(maps.get(i).get("ADVERTISE_PIC") + "");
                            }
                            mHandler.sendEmptyMessage(BANNER_WHAT);
                        }
                        // mBannerAdapter.setImageUrls(bannerImageUrls);
                        //mBannerAdapter.notifyDataSetChanged();
                    }
                }
                try {
                    getAdsData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void notifyDataSet(DelegateAdapter.Adapter mAdapter) {
        while (mRecyFirstshow != null && mRecyFirstshow.isComputingLayout()) {

        }
        mAdapter.notifyDataSetChanged();
    }

    List<String> bannerImageUrls = new ArrayList<>();
    List<Map<String, Object>> categoryDataList = new ArrayList<>();
    List<String> themeImageUrlList = new ArrayList<>();
    String adsUrl = "";

    private void initRecycler() {
        mRecyFirstshow.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        mLayoutManager = new VirtualLayoutManager(getActivity());
        mRecyFirstshow.setLayoutManager(mLayoutManager);
       /* mRecyFirstshow.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
                outRect.set(3, 3, 3, 3);
            }
        });*/
        DelegateAdapter delegateAdapter = new DelegateAdapter(mLayoutManager, false);
        mRecyFirstshow.setAdapter(delegateAdapter);
        List<DelegateAdapter.Adapter> adapters = new LinkedList<>();
        LinearLayoutHelper bannerHelper = new LinearLayoutHelper();
        bannerHelper.setItemCount(1);
        mBannerAdapter = new FirstShowBannerAdapter(getActivity(), bannerHelper);
        mBannerAdapter.setImageUrls(bannerImageUrls);
        initBannerItemClickListener(mBannerAdapter);
        adapters.add(mBannerAdapter);

        mCategoryHelper = new GridLayoutHelper(5);
        mCategoryHelper.setMargin(10, 10, 10, 5);
        //mCategoryHelper.setAutoExpand(true);
        // mCategoryHelper.setHGap(3);
        mCategoryHelper.setAspectRatio(5f);
        //mCategoryHelper.setGap(10);
        /*float[] weights = {1, 1, 1, 1, 1};
        mCategoryHelper.setWeights(weights);*/
        mCatoryAdapter = new FirstShowCatoryAdapter(getContext(), mCategoryHelper, categoryDataList);
        initCategorySeeMoreClick();
        mCatoryAdapter.setItemClickListenerFactory(categoryIdClick);
        adapters.add(mCatoryAdapter);

        LinearLayoutHelper adsHelper = new LinearLayoutHelper();
        adsHelper.setMargin(10, 0, 10, 0);
        adsHelper.setItemCount(1);
        mAdsAdapter = new AdsAdapter(getContext(), adsHelper, adsUrl);
        adapters.add(mAdsAdapter);

        LinearLayoutHelper timeHeaderHelper = new LinearLayoutHelper();
        timeHeaderHelper.setItemCount(1);
        timeHeaderHelper.setMarginTop(8);
        TimeHeaderAdapter timeHeaderAdapter = new TimeHeaderAdapter(getContext(), timeHeaderHelper, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TimeToByListActivity.class));
            }
        });
        adapters.add(timeHeaderAdapter);

        GridLayoutHelper timeTobyHelper = new GridLayoutHelper(3);
        //float[] timeWeights = {1, 1, 1};
        // timeTobyHelper.setWeights(timeWeights);
        //timeTobyHelper.setAutoExpand(true);
        timeTobyHelper.setMargin(5, 0, 5, 5);
        //timeTobyHelper.setAspectRatio(3f);
        //timeTobyHelper.setv(3);
        timeTobyHelper.setAutoExpand(true);
        mTimeToByAdapter = new TimeToByAdapter(getContext(), timeTobyHelper);
        initTimeToByItemClickListener(mTimeToByAdapter);
        adapters.add(mTimeToByAdapter);


        LinearLayoutHelper themeHelper = new LinearLayoutHelper();
        //adsHelper.setItemCount(1);
        mThemeTobyAdapter = new ThemeTobyAdapter(getContext(), themeHelper, categoryDataList);
        mThemeTobyAdapter.setMoreThemeClick(categoryIdClick);
        adapters.add(mThemeTobyAdapter);

        LinearLayoutHelper maylikeheadHelper = new LinearLayoutHelper();
        maylikeheadHelper.setItemCount(1);
        maylikeheadHelper.setMarginTop(8);
        maylikeheadHelper.setMarginBottom(2);
        MayLikeHeaderAdapter headerAdapter = new MayLikeHeaderAdapter(getActivity(), maylikeheadHelper);
        adapters.add(headerAdapter);
        GridLayoutHelper matbeLikeHelper = new GridLayoutHelper(2);
        /*float[] mayWeight = {1, 1};
        matbeLikeHelper.setWeights(mayWeight);*/
        matbeLikeHelper.setHGap(1);
        // matbeLikeHelper.setAspectRatio(2f);
        mMayBeLikeAdapter = new MayBeLikeAdapter(getContext(), matbeLikeHelper);
        mMayBeLikeAdapter.setDataList(mayLikeDataList);
        mMayBeLikeAdapter.setOnItemClickListenerFactory(new FactoryUtils.OnItemClickListenerFactory() {
            @Override
            public void OnClickListener(int position) {
                Intent intent = new Intent(getActivity(), GoodsShowActivity.class);
                intent.putExtra(GOOD_ID, mayLikeDataList.get(position).get("GOODS_ID") + "");
                intent.putExtra(GoodsShowActivity.FROM, GoodsShowActivity.FROM_NOMAL_LIST);
                startActivity(intent);
            }

            @Override
            public void OnSubTHemeClickListener(String categoryId) {

            }
        });
        adapters.add(mMayBeLikeAdapter);
        delegateAdapter.addAdapters(adapters);

       /* LinearLayoutHelper footerHelper = new LinearLayoutHelper();
        footerHelper.setItemCount(1);
        mBannerAdapter = new FirstShowBannerAdapter(getActivity(), bannerHelper);
        adapters.add(mBannerAdapter);
        delegateAdapter.addAdapters(adapters);*/
    }

    public static final String CATEGORYSTR = "categoryStr";

    private void initCategorySeeMoreClick() {
        categoryIdClick = new FactoryUtils.OnItemClickListenerFactory() {
            @Override
            public void OnClickListener(int position) {
                if (position < categoryDataList.size()) {
                    Intent intent = new Intent(getActivity(), GoodsListActivity.class);
                    intent.putExtra(Consts.PARAM_GOODS_LIST_THEME_FIRST, categoryDataList.get(position).get("CATEGORY_ID") + "");
                    intent.putExtra(Consts.PARAM_GOODS_LIST_PARAM, "SUPER_ID");
                    startActivity(intent);
                } else if (position == categoryDataList.size()) {
                    Intent intent = new Intent(getActivity(), CategoriesActivity.class);
                    intent.putExtra(CATEGORYSTR, categoryStr);
                    startActivity(intent);
                }
            }

            @Override
            public void OnSubTHemeClickListener(String categoryId) {
                Intent intent = new Intent(getActivity(), GoodsListActivity.class);
                intent.putExtra(Consts.PARAM_GOODS_LIST_THEME_FIRST, categoryId);
                intent.putExtra(Consts.PARAM_GOODS_LIST_PARAM, "GOODS_CATEGORY");
                startActivity(intent);
            }
        };
    }

    private void initTimeToByItemClickListener(TimeToByAdapter timeToByAdapter) {
        timeToByAdapter.setItemClickListener(new TimeToByAdapter.TimeToByItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (0 == position) {
                    Toast.makeText(getActivity(), "timetoby查看更多", Toast.LENGTH_SHORT).show();
                } else if (position > 2) {
                    Toast.makeText(getActivity(), "timetoby图片", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initBannerItemClickListener(FirstShowBannerAdapter bannerAdapter) {
        bannerAdapter.setBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                // todo 点击banneritem 下标从1开始
                Toast.makeText(getActivity(), "点击了banner第" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSwipeRefresh() {
        mSwipeFirstshowlist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // todo 网络请求
                try {
                    getBannerData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_search) {
            Intent intent = new Intent(getActivity(), GoodsSearchActivity.class);
            intent.putExtra(GoodsSearchActivity.FROM, GoodsSearchActivity.FROM_SHOUYE);
            startActivity(intent);
        }
    }

    private void initCouponListData() {
        OkUtils okUtils = OkUtils.getOkUtilsInstance();
        String url = Consts.ROOT_URL + Consts.GET_COUPON_LIST;
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
                Log.i("couponlistfirst", string);
                if (StringUtils.isNotEmpty(string) && (!"[]".equals(string))) {
                    Message message = new Message();
                    Bundle data = new Bundle();
                    data.putString("coupondata", string);
                    message.setData(data);
                    message.what = COUPON_WHAT;
                    mHandler.sendMessage(message);
                }
            }
        });
    }
}
