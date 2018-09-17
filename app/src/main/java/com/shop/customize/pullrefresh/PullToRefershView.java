package com.shop.customize.pullrefresh;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;

import com.shop.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PullToRefershView extends PullToRefreshBase<LinearLayout> implements OnScrollListener {

    private LinearLayout lin;
    /**
     * ListView
     */
    private ListView mListView;
    private ExpandableListView expListView;
    /**
     * EditText
     */
    private EditText edtSearch;
    /**
     * TextView
     */
    private GetId getId;
    private TextView txtNoFind, tv_refresh;
    private ImageView searchImg;
    private LinearLayout dateSelect, ll_refresh;
    private Spinner spYear, spMonth, spDay, spYearend, spMonthend, spDayend, sp_refresh;
    /**
     * 用于滑到底部自动加载的Footer
     */
    private LoadingLayout mLoadMoreFooterLayout;
    /**
     * 滚动的监听器
     */
    private OnScrollListener mScrollListener;

    public PullToRefershView(Context context) {
        this(context, null);
    }

    public PullToRefershView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefershView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected LinearLayout createRefreshableView(Context context, AttributeSet attrs) {
        lin = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.content, null, true);
        edtSearch = (EditText) lin.findViewById(R.id.content_edt_seatch);
        txtNoFind = (TextView) lin.findViewById(R.id.content_nofind);
        mListView = (ListView) lin.findViewById(R.id.content_list);
        expListView = (ExpandableListView) lin.findViewById(R.id.exp_list);
        searchImg = (ImageView) lin.findViewById(R.id.basedata_select_btn_serach);
        mListView.setOnScrollListener(this);
        dateSelect = (LinearLayout) lin.findViewById(R.id.date_select);
        spYear = (Spinner) lin.findViewById(R.id.sp_year);
        spMonth = (Spinner) lin.findViewById(R.id.sp_month);
        spDay = (Spinner) lin.findViewById(R.id.sp_day);
        spYearend = (Spinner) lin.findViewById(R.id.sp_yearend);
        spMonthend = (Spinner) lin.findViewById(R.id.sp_monthend);
        spDayend = (Spinner) lin.findViewById(R.id.sp_dayend);
        ll_refresh = (LinearLayout) lin.findViewById(R.id.ll_refresh);
        sp_refresh = (Spinner) lin.findViewById(R.id.sp_refresh);
        tv_refresh = (TextView) lin.findViewById(R.id.tv_refresh);
        return lin;
    }

    public ListView getListView() {
        return mListView;
    }

    public LinearLayout getDateSelect() {
        return dateSelect;
    }

    public ExpandableListView getExpListView() {
        mListView.setVisibility(View.GONE);
        expListView.setVisibility(View.VISIBLE);
        return expListView;
    }

    public void setLl_refreshVisiblely(boolean visiblely) {
        if (visiblely) {
            ll_refresh.setVisibility(VISIBLE);
        } else {
            ll_refresh.setVisibility(GONE);
        }
    }

    public EditText getEditText() {
        return edtSearch;
    }

    public Spinner getSp_refresh() {
        return sp_refresh;
    }

    public TextView getTextView() {
        return txtNoFind;
    }

    public TextView getTv_refresh() {
        return tv_refresh;
    }

    public ImageView getShImageView() {
        return searchImg;
    }

    public View getRefreshView() {
        return lin.findViewById(R.id.refresh_rootview);
    }

    /**
     * 设置是否有更多数据的标志
     *
     * @param hasMoreData true表示还有更多的数据，false表示没有更多数据了
     */
    public void setHasMoreData(boolean hasMoreData) {
        if (!hasMoreData) {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
            }
            LoadingLayout footerLoadingLayout = getFooterLoadingLayout();
            if (null != footerLoadingLayout) {
                footerLoadingLayout.setState(ILoadingLayout.State.NO_MORE_DATA);
            }
        }
    }

    /**
     * 设置滑动的监听器
     *
     * @param l 监听器
     */
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    @Override
    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    @Override
    protected void startLoading() {
        super.startLoading();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(ILoadingLayout.State.REFRESHING);
        }
    }

    @Override
    public void onPullUpRefreshComplete() {
        super.onPullUpRefreshComplete();

        if (null != mLoadMoreFooterLayout) {
            mLoadMoreFooterLayout.setState(ILoadingLayout.State.RESET);
        }
    }

    @Override
    public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
        super.setScrollLoadEnabled(scrollLoadEnabled);

        if (scrollLoadEnabled) {
            // 设置Footer
            if (null == mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
            }
            if (null == mLoadMoreFooterLayout.getParent()) {
                mListView.addFooterView(mLoadMoreFooterLayout, null, false);
            }
            mLoadMoreFooterLayout.show(true);
        } else {
            if (null != mLoadMoreFooterLayout) {
                mLoadMoreFooterLayout.show(false);
            }
        }
    }

    @Override
    public LoadingLayout getFooterLoadingLayout() {
        if (isScrollLoadEnabled()) {
            return mLoadMoreFooterLayout;
        }
        return super.getFooterLoadingLayout();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (isScrollLoadEnabled() && hasMoreData()) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
                if (isReadyForPullUp()) {
                    startLoading();
                }
            }
        }
        if (null != mScrollListener) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (null != mScrollListener) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
        return new RotateLoadingLayout(context);
    }

    /**
     * 表示是否还有更多数据
     *
     * @return true表示还有更多数据
     */
    private boolean hasMoreData() {
        if ((null != mLoadMoreFooterLayout) && (mLoadMoreFooterLayout.getState() == ILoadingLayout.State.NO_MORE_DATA)) {
            return false;
        }
        return true;
    }

    /**
     * 判断第一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isFirstItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }
        int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;
        if (mostTop >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断最后一个child是否完全显示出来
     *
     * @return true完全显示出来，否则false
     */
    private boolean isLastItemVisible() {
        final Adapter adapter = mListView.getAdapter();

        if (null == adapter || adapter.isEmpty()) {
            return true;
        }

        final int lastItemPosition = adapter.getCount() - 1;
        final int lastVisiblePosition = mListView.getLastVisiblePosition();

        /**
         * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
         * internally uses a FooterView which messes the positions up. For me we'll just subtract
         * one to account for it and rely on the inner condition which checks getBottom().
         */
        if (lastVisiblePosition >= lastItemPosition - 1) {
            final int childIndex = lastVisiblePosition - mListView.getFirstVisiblePosition();
            final int childCount = mListView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mListView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= mListView.getBottom();
            }
        }
        return false;
    }

    public Spinner getSpYear() {
        return spYear;
    }

    public Spinner getSpMonth() {
        return spMonth;
    }

    public Spinner getSpDay() {
        return spDay;
    }

    public Spinner getSpDayend() {
        return spDayend;
    }

    public Spinner getSpYearend() {
        return spYearend;
    }

    public Spinner getSpMonthend() {
        return spMonthend;
    }

    public void dateSelectItemclick(Context context, final Spinner spYear, Spinner spMonth, Spinner spDay, boolean end) {
        final boolean[] fist = {true};
        ArrayList<String> dataYear = new ArrayList<String>();
        ArrayList<String> dataMonth = new ArrayList<String>();
        final ArrayList<String> dataDay = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        if (end) {
            cal.add(Calendar.DAY_OF_YEAR, +1);
        }
        for (int i = 0; i < 40; i++) {
            dataYear.add("" + (cal.get(Calendar.YEAR) - 20 + i));
        }
        ArrayAdapter<String> adapterSpYear = new ArrayAdapter<String>(context, R.layout.spinner_pulltorefresh, dataYear);
        adapterSpYear.setDropDownViewResource(R.layout.sort_item);
        spYear.setAdapter(adapterSpYear);
        spYear.setSelection(20);// 默认选中今年
        // 12个月
        for (int i = 1; i <= 12; i++) {
            dataMonth.add("" + (i < 10 ? "0" + i : i));
        }
        ArrayAdapter<String> adapterSpMonth = new ArrayAdapter<String>(context, R.layout.spinner_pulltorefresh, dataMonth);
        adapterSpMonth.setDropDownViewResource(R.layout.sort_item);
        spMonth.setAdapter(adapterSpMonth);
        spMonth.setSelection(cal.get(Calendar.MONTH));
        final ArrayAdapter<String> adapterSpDay = new ArrayAdapter<String>(context, R.layout.spinner_pulltorefresh, dataDay);
        for (int i = 1; i <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dataDay.add("" + (i < 10 ? "0" + i : i));
        }
        adapterSpDay.setDropDownViewResource(R.layout.sort_item);
        spDay.setAdapter(adapterSpDay);
        spDay.setSelection(cal.get(Calendar.DAY_OF_MONTH) - 1);
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (!fist[0]) {
                    dataDay.clear();
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, Integer.valueOf(spYear.getSelectedItem().toString()));
                    cal.set(Calendar.MONTH, arg2);
                    int dayofm = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    for (int i = 1; i <= dayofm; i++) {
                        dataDay.add("" + (i < 10 ? "0" + i : i));
                    }
                    adapterSpDay.notifyDataSetChanged();
                }
                fist[0] = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void Sp_refreshClick(List<Map<String, Object>> list, Context context, Spinner spinner) {
        List<String> refresh = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id", 0l);
        map.put("name", "全部");
        map.put("tag", "");
        map.put("code", "");
        refresh.add("全部");
        final List<Map<String, Object>> list1 = new ArrayList<>();
        list1.add(map);
        for (Map<String, Object> o : list) {
            list1.add(o);
            refresh.add(TextUtils.isEmpty(o.get("name") + "") ? "" : o.get("name") + "");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_pulltorefresh, refresh);
        adapter.setDropDownViewResource(R.layout.sort_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (null != getId) {
                    getId.getId(list1.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public interface GetId {
        void getId(Map<String, Object> map);
    }

    public void setGetId(GetId getId) {
        this.getId = getId;
    }
}
