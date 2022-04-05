package com.zhoupeng.stockandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zhoupeng.stockandroid.MainActivity;
import com.zhoupeng.stockandroid.R;
import com.zhoupeng.stockandroid.StockActivity;
import com.zhoupeng.stockandroid.bean.Stockhold;

import java.util.Collections;
import java.util.List;

public class PortfolioRecyclerViewAdapter extends RecyclerView.Adapter implements ItemTouchHelperContract {

    private Context context;
    private List<Stockhold> stockholdList;

    public PortfolioRecyclerViewAdapter(Context context, List<Stockhold> stockholdList) {
        this.context = context;
        this.stockholdList = stockholdList;
    }

    @NonNull
    @Override
    public PortfolioRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_portfolio, parent, false);
        PortfolioRecyclerViewHolder viewHolder = new PortfolioRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PortfolioRecyclerViewHolder portfolioHolder = (PortfolioRecyclerViewHolder) holder;
        Stockhold stockhold = stockholdList.get(position);
        portfolioHolder.getEntryTicker().setText(stockhold.getTicker());
        portfolioHolder.getEntryDescription().setText(stockhold.getQuantity().toString() + " shares");
        portfolioHolder.getEntryAmount().setText(String.format("$%.2f", stockhold.getMarketValue()));
        Double change = stockhold.getChange() * stockhold.getQuantity();
        Double changePercent = change / stockhold.getTotalCost();
        System.out.println(change);
        System.out.println(changePercent);
        if (change >= 0.01) {
            Drawable drawable = context.getDrawable(R.drawable.trending_up);
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, Color.GREEN);
            portfolioHolder.getEntryTrendImage().setImageDrawable(wrappedDrawable);
            portfolioHolder.getEntryDelta().setTextColor(Color.GREEN);
        } else if (change <= -0.01) {
            Drawable drawable = context.getDrawable(R.drawable.trending_down);
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, Color.RED);
            portfolioHolder.getEntryTrendImage().setImageDrawable(wrappedDrawable);
            portfolioHolder.getEntryDelta().setTextColor(Color.RED);
        }

        if (change < 0.01 && change > -0.01) {
            System.out.println("zero");
            changePercent = 0.0;
        }
        portfolioHolder.getEntryDelta().setText(
                "$" + String.format("%.2f", change) + "(" + String.format("%.2f", changePercent) + "%)"
        );

        portfolioHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StockActivity.class);
                intent.putExtra(MainActivity.EXTRA_MESSAGE, stockhold.getTicker());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stockholdList.size();
    }

    public List<Stockhold> getStockholdList() {
        return stockholdList;
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(stockholdList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(stockholdList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(PortfolioRecyclerViewHolder viewHolder) {

    }

    @Override
    public void onRowClear(PortfolioRecyclerViewHolder viewHolder) {

    }

    @Override
    public void onItemRemove(int position) {

    }
}
