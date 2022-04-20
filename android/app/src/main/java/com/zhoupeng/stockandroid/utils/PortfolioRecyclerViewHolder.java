package com.zhoupeng.stockandroid.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhoupeng.stockandroid.R;

public class PortfolioRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView entryTicker;
    private TextView entryAmount;
    private TextView entryDescription;
    private TextView entryDelta;
    private ImageView entryTrendImage;

    public PortfolioRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        this.entryTicker = (TextView) itemView.findViewById(R.id.entryTicker);
        this.entryAmount = (TextView) itemView.findViewById(R.id.entryAmount);
        this.entryDescription = (TextView) itemView.findViewById(R.id.entryDescription);
        this.entryDelta = (TextView) itemView.findViewById(R.id.entryDelta);
        this.entryTrendImage = (ImageView) itemView.findViewById(R.id.entryTrendImage);
    }

    public TextView getEntryTicker() {
        return entryTicker;
    }

    public TextView getEntryAmount() {
        return entryAmount;
    }

    public TextView getEntryDescription() {
        return entryDescription;
    }

    public TextView getEntryDelta() {
        return entryDelta;
    }

    public ImageView getEntryTrendImage() {
        return entryTrendImage;
    }
}
