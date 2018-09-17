package com.shop.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by victoryf on 2018-06-30.
 */
public class SwipeRefreshUtils {
    public static void setRefreshState(Activity mcontext, final SwipeRefreshLayout refreshLayout, final boolean refreshState) {
        mcontext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (refreshState) {
                    if (refreshLayout.isRefreshing())
                        return;
                    else
                        refreshLayout.setRefreshing(true);
                } else {
                    if (refreshLayout.isRefreshing())
                        refreshLayout.setRefreshing(refreshState);
                }

            }
        });
    }
}
