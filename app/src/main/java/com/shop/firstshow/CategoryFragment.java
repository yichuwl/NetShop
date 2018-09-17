package com.shop.firstshow;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shop.Consts;
import com.shop.R;
import com.shop.goods.GoodsListActivity;
import com.shop.util.GsonUtils;
import com.shop.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author victory
 * @time 2018/6/27
 * @about
 */
public class CategoryFragment extends Fragment {

    protected View rootView;
    protected RecyclerView mRecyFragmentCategory;
    private GridLayoutManager mLayoutManager;
    private CategoryGridAdapter mAdapter;
    List<Map<String, Object>> dataList = new ArrayList<>();

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
        if (StringUtils.isNotEmpty(dataStr)) {
            List<Map<String, Object>> maps = GsonUtils.parseArrayGson(dataStr);
            dataList.clear();
            dataList.addAll(maps);
            mAdapter.setDataList(dataList);
            mAdapter.notifyDataSetChanged();
        }
    }

    String dataStr = "";

    public void setTitleTxt(String titleTxt) {
        this.titleTxt = titleTxt;
        title.setText("------- "+titleTxt+" -------");
    }

    String titleTxt = "";

    public static CategoryFragment newInstance() {

        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categorylist, null);
        initView(rootView);
        return rootView;
    }

    TextView title;

    private void initView(View rootView) {
        title = rootView.findViewById(R.id.title_categorysub);
        mRecyFragmentCategory = (RecyclerView) rootView.findViewById(R.id.recy_fragment_category);
        mLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        mRecyFragmentCategory.setLayoutManager(mLayoutManager);
        mRecyFragmentCategory.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.set(2, 10, 2, 2);
            }
        });
        mAdapter = new CategoryGridAdapter(getActivity(), dataList, new CategoryGridAdapter.OnitemClickListener() {
            @Override
            public void onClick(int position) {
                if (dataList.size() > position) {
                    Map<String, Object> map = dataList.get(position);
                    String categoryId = map.get("CATEGORY_ID") + "";
                    Intent intent = new Intent(getActivity(), GoodsListActivity.class);
                    intent.putExtra(Consts.PARAM_GOODS_LIST_THEME_FIRST, categoryId);
                    intent.putExtra(Consts.PARAM_GOODS_LIST_PARAM, "GOODS_CATEGORY");
                    startActivity(intent);
                }
            }
        });
        mRecyFragmentCategory.setAdapter(mAdapter);
    }
}
