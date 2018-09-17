package com.shop.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shop.R;
import com.shop.util.StringUtils;

public class GoodsSearchActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView mImgSearch;
    protected EditText mEdtSearch;
    protected Button mBtnSearch;
    protected LinearLayout mLinearSearchbar;
    public static final String SEARCH_CONTENT = "searchcontent";
    public static final String FROM = "from";
    public static final String FROM_LIST = "list";
    public static final String FROM_SHOUYE = "shouye";
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_goods_search);
        from = getIntent().getStringExtra(FROM);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_search) {
            String searchContent = mEdtSearch.getText().toString().trim();
            if (StringUtils.isEmpty(searchContent)) {
                searchContent = mEdtSearch.getHint().toString();
            }
            if (FROM_LIST.equals(from)) {
                Intent data = new Intent();
                data.putExtra(SEARCH_CONTENT, searchContent);
                setResult(RESULT_OK, data);
            } else if (FROM_SHOUYE.equals(from)) {
                Intent data = new Intent();
                data.putExtra(SEARCH_CONTENT, searchContent);
                data.setClass(GoodsSearchActivity.this, GoodsListActivity.class);
                startActivity(data);
            }
            finish();
        } else if (view.getId() == R.id.img_search) {
            finish();
        }
    }

    private void initView() {
        mImgSearch = (ImageView) findViewById(R.id.img_search);
        mImgSearch.setVisibility(View.VISIBLE);
        mImgSearch.setImageResource(R.mipmap.back);
        mImgSearch.setOnClickListener(GoodsSearchActivity.this);
        mEdtSearch = (EditText) findViewById(R.id.edt_search);
        mEdtSearch.requestFocus();
        mEdtSearch.setHint(getString(R.string.default_searchword));
        mBtnSearch = (Button) findViewById(R.id.btn_search);
        mBtnSearch.setVisibility(View.VISIBLE);
        mBtnSearch.setOnClickListener(GoodsSearchActivity.this);
        mLinearSearchbar = (LinearLayout) findViewById(R.id.linear_searchbar);
    }
}
