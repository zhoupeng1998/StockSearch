package com.zhoupeng.stockandroid.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.zhoupeng.stockandroid.HistoryChartFragment;
import com.zhoupeng.stockandroid.HourChartFragment;
import com.zhoupeng.stockandroid.R;

import java.util.ArrayList;

public class VPAdapter extends FragmentStateAdapter {

    private String ticker;
    //private String[] titles = {"Hour", "History"};
    private int[] titles = {R.drawable.ic_baseline_star_24, R.drawable.ic_baseline_star_border_24};

    public VPAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        HourChartFragment hourChartFragment = new HourChartFragment();
        HistoryChartFragment historyChartFragment = new HistoryChartFragment();
        hourChartFragment.setTicker(ticker);
        historyChartFragment.setTicker(ticker);
        switch (position) {
            case 0:
                return hourChartFragment;
            default:
                return historyChartFragment;
        }
        /*
        switch (position) {
            case 0:
                return new HourChartFragment();
            case 1:
                return new HistoryChartFragment();
        }
        return new HourChartFragment();
         */
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}