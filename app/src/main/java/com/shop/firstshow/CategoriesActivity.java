package com.shop.firstshow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;


import com.shop.R;
import com.shop.util.GsonUtils;
import com.shop.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener {

    protected ListView mListCategory;
    protected FrameLayout mFrameCategorycontent;
    protected ImageView mImageBackCategory;
    private CategoryFragment mFragment;
    List<String> categorySonListData = new ArrayList<>();
    List<String> categoryTitleList = new ArrayList<>();
    List<Map<String, String>> categoryListData = new ArrayList<>();
    private CategoryListAdapter mAdapter;
    private String mDataStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_categories);
        Intent intent = getIntent();
        mDataStr = intent.getStringExtra(ShouYeFragment.CATEGORYSTR);
        initView();
        initData();
    }

    private void initData() {
        if (StringUtils.isNotEmpty(mDataStr)) {
            parseDataStr();
            mAdapter.setDataList(categoryTitleList);
            mAdapter.setCheckedPosition(0);
            mAdapter.notifyDataSetChanged();

        } else {
            // 网络请求
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPosition == 0 && mFragment != null) {
            mFragment.setDataStr(categorySonListData.get(0));
            mFragment.setTitleTxt(categoryTitleList.get(0));
        }
    }

    int checkPosition = 0;

    private void parseDataStr() {
        List<Map<String, Object>> maps = GsonUtils.parseArrayGson(mDataStr);
        categoryListData.clear();
        Log.i("category", mDataStr + "----");
        for (int i = 0; i < maps.size(); i++) {
            Map<String, Object> map = maps.get(i);
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("CATEGORY_NAME", map.get("CATEGORY_NAME") + "");
            tempMap.put("CATEGORY_IMG", map.get("CATEGORY_IMG") + "");
            tempMap.put("SON", map.get("SON") + "");
            tempMap.put("CATEGORY_ID", map.get("CATEGORY_ID") + "");
            categoryListData.add(tempMap);
            categoryTitleList.add(map.get("CATEGORY_NAME") + "");
            categorySonListData.add(map.get("SON") + "");
        }
    }

    private void initView() {
        mListCategory = (ListView) findViewById(R.id.list_category);
        mFrameCategorycontent = (FrameLayout) findViewById(R.id.frame_categorycontent);
        mFragment = CategoryFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_categorycontent, mFragment)
                .show(mFragment)
                .commit();
        mListCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // todo 传递不同的参数到fragment中
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //mListCategory.setSelection(0);
            }
        });
        mAdapter = new CategoryListAdapter(this, categoryTitleList);
        mListCategory.setAdapter(mAdapter);
        mImageBackCategory = (ImageView) findViewById(R.id.image_back_category);
        mImageBackCategory.setOnClickListener(CategoriesActivity.this);
        mListCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 进入商品列表页
                checkPosition = 0;
                mFragment.setDataStr(categorySonListData.get(position));
                mFragment.setTitleTxt(categoryTitleList.get(position));
                mAdapter.setCheckedPosition(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.image_back_category) {
            finish();
        }
    }
}
