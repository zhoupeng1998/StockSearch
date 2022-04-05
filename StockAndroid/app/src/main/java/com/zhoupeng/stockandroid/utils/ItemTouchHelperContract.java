package com.zhoupeng.stockandroid.utils;

public interface ItemTouchHelperContract {
    void onRowMoved(int fromPosition, int toPosition);
    void onRowSelected(PortfolioRecyclerViewHolder viewHolder);
    void onRowClear(PortfolioRecyclerViewHolder viewHolder);
    void onItemRemove(int position);
}
