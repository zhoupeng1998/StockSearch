package com.zhoupeng.stockandroid.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhoupeng.stockandroid.R;

public class NewsRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView newsSourceTextView;
    private TextView newsDateTextView;
    private TextView newsTitleTextView;
    private ImageView newsImageView;

    public NewsRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        this.newsSourceTextView = (TextView) itemView.findViewById(R.id.newsSourceTextView);
        this.newsDateTextView = (TextView) itemView.findViewById(R.id.newsDateTextView);
        this.newsTitleTextView = (TextView) itemView.findViewById(R.id.newsTitleTextView);
        this.newsImageView = (ImageView) itemView.findViewById(R.id.newsImageView);
    }

    public TextView getNewsSourceTextView() {
        return newsSourceTextView;
    }

    public TextView getNewsDateTextView() {
        return newsDateTextView;
    }

    public TextView getNewsTitleTextView() {
        return newsTitleTextView;
    }

    public ImageView getNewsImageView() {
        return newsImageView;
    }
}
