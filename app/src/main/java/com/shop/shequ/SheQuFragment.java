package com.shop.shequ;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.util.GsonUtils;
import com.shop.util.OkUtils;
import com.shop.util.SwipeRefreshUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author victory
 * @time 2018/6/25
 * @about 社区页面
 */

public class SheQuFragment extends Fragment {

    protected View rootView;
    protected TextView txtTitleShequ;
    protected RecyclerView recyShequ;
    protected SwipeRefreshLayout swipeShequlist;
    private ShequContentListAdapter mListAdapter;
    private LinearLayoutManager mManager;
    public static final String ARTICLE_CONTENT_TAG = "articlecontent";

    public static SheQuFragment newInstance() {

        Bundle args = new Bundle();

        SheQuFragment fragment = new SheQuFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_shequ, null);
        initContentView(contentView);
        return contentView;
    }

    RecyclerView mRecyclerView;
    List<String> articleList = new ArrayList<>();

    private void initContentView(View view) {
        swipeShequlist = (SwipeRefreshLayout) view.findViewById(R.id.swipe_shequlist);
        swipeShequlist.setColorSchemeColors(getContext().getResources().getColor(R.color.main_color));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recy_shequ);
        mManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mListAdapter = new ShequContentListAdapter(getContext());
        mRecyclerView.setAdapter(mListAdapter);
        initRecySetting();
        initSwipeListener();
        initDataList();
        swipeShequlist.setRefreshing(true);

    }

    List<Map<String, Object>> dataList = new ArrayList<>();
    List<String> articleContentList = new ArrayList<>();

    private void initDataList() {
        String url = Consts.ROOT_URL + Consts.SHEQU_ARTICLE_LIST;
        Log.i("shequlisturl", url);
        OkUtils.getOkUtilsInstance().httpGet(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                SwipeRefreshUtils.setRefreshState(getActivity(), swipeShequlist, false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (swipeShequlist.isRefreshing()){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            swipeShequlist.setRefreshing(false);
                        }
                    });
                }

                // if (Consts.RESPONSE_OK_STATE.equals(response.message())) {
                String string = response.body().string();

                if (!TextUtils.isEmpty(string)) {
                    Log.i("shequlist", string);
                    List<Map<String, Object>> maps = GsonUtils.parseArrayGson(string);
                    dataList.clear();
                    articleContentList.clear();
                    for (int i = 0; i < maps.size(); i++) {
                        Map<String, Object> tempMap = new HashMap<>();
                        Map<String, Object> map = maps.get(i);
                        tempMap.put("TITLE", map.get("TITLE"));
                        tempMap.put("SHORTCONTENT", map.get("SHORTCONTENT"));
                        tempMap.put("IMAGE", map.get("IMAGE"));
                        tempMap.put("NEWS_ID", map.get("NEWS_ID"));
                        dataList.add(tempMap);

                        String content = map.get("CONTENT") + "";
                        articleContentList.add(content);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListAdapter.setDatalist(dataList);
                            mListAdapter.notifyDataSetChanged();
                        }
                    });
                }
                //  }
                SwipeRefreshUtils.setRefreshState(getActivity(), swipeShequlist, false);
            }
        });
    }

    private void initSwipeListener() {
        swipeShequlist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // todo loading data
                initDataList();

            }
        });
    }

    private void initRecySetting() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(4, 4, 4, 4);
            }
        });
        mListAdapter.setClickListener(new ShequContentListAdapter.OnItemClickListener() {
            @Override
            public void itemClickListener(int position) {
                Intent intent = new Intent(getActivity(), SheQuArticleActivity.class);
                intent.putExtra(ARTICLE_CONTENT_TAG, articleContentList.get(position));
                intent.putExtra(SheQuArticleActivity.ARTICELID, dataList.get(position).get("NEWS_ID") + "");
                intent.putExtra("title", dataList.get(position).get("TITLE") + "");
                /*if (articleList.size() > 20) {
                    if (position != articleList.size() - 1) {
                        startActivity(intent);
                    }
                } else {*/
                startActivity(intent);
                //  }
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastShowItemPosition >= 20
                        && (lastShowItemPosition + 1) == mListAdapter.getItemCount()) {
                    // todo loadmore
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastShowItemPosition = mManager.findLastVisibleItemPosition();
            }
        });
    }

    int lastShowItemPosition = 0;
}
