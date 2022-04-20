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
import com.zhoupeng.stockandroid.bean.FavoriteItem;
import com.zhoupeng.stockandroid.data.FavoriteService;

import java.util.Collections;
import java.util.List;

public class FavoriteRecyclerViewAdapter extends RecyclerView.Adapter implements ItemTouchHelperContract {

    private Context context;
    private List<FavoriteItem> favoriteList;

    public FavoriteRecyclerViewAdapter(Context context, List<FavoriteItem> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
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
        PortfolioRecyclerViewHolder favoriteHolder = (PortfolioRecyclerViewHolder) holder;
        FavoriteItem favoriteItem = favoriteList.get(position);
        favoriteHolder.getEntryTicker().setText(favoriteItem.getTicker());
        favoriteHolder.getEntryDescription().setText(favoriteItem.getCompanyName());
        favoriteHolder.getEntryAmount().setText(String.format("$%.2f", favoriteItem.getPrice()));
        if (favoriteItem.getDp() > 0) {
            Drawable drawable = context.getDrawable(R.drawable.trending_up);
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, Color.GREEN);
            favoriteHolder.getEntryTrendImage().setImageDrawable(wrappedDrawable);
            favoriteHolder.getEntryDelta().setTextColor(Color.GREEN);
        } else if (favoriteItem.getDp() < 0) {
            Drawable drawable = context.getDrawable(R.drawable.trending_down);
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, Color.RED);
            favoriteHolder.getEntryTrendImage().setImageDrawable(wrappedDrawable);
            favoriteHolder.getEntryDelta().setTextColor(Color.RED);
        }
        favoriteHolder.getEntryDelta().setText(
                "$" + String.format("%.2f", favoriteItem.getDelta()) + "(" + String.format("%.2f", favoriteItem.getDp()) + "%)"
        );
        favoriteHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StockActivity.class);
                intent.putExtra(MainActivity.EXTRA_MESSAGE, favoriteItem.getTicker());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public List<FavoriteItem> getFavoriteList() {
        return favoriteList;
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(favoriteList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(favoriteList, i, i - 1);
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
        String ticker = favoriteList.get(position).getTicker();
        FavoriteService.getInstance().removeFromFavorite(ticker);
    }
}
