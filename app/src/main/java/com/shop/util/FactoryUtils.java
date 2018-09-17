package com.shop.util;

import java.util.List;
import java.util.Map;

/**
 * Created by victoryf on 2018-06-30.
 */
public class FactoryUtils {
    public static interface OnItemClickListenerFactory {
        void OnClickListener(int position);

        void OnSubTHemeClickListener(String categoryId);
    }

    public static interface OnChartChangeListener {
        void onMinusNumListener(int position);

        void onPlusNumListener(int position);

        void onRemoveGoodsListener(int position);

        void onCheckedChangeListener(int position, boolean isChecked);
    }

    public static interface TimeToByListListener {
        void onByNowClickListener(int position);
    }

    public static interface OnGoodsSelectModelListener {
        void getSelectedModelData(Map<String, Object> selectedData);
    }

    public static interface OnAddrItemClickListener {
        void onDeleteClickListener(int position);

        void onChangeClickListener(int position);
    }

    public static abstract class OnOrderListItemClick {
        public void onBtnOperateClick(int position) {
        }

        public abstract void onSeeOrderDetailsClick(int position);

        public void onBtnCancelClick(int position){};
    }
}
