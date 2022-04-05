package com.zhoupeng.stockandroid.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zhoupeng.stockandroid.R;
import com.zhoupeng.stockandroid.bean.NewsEntry;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter {

    private SimpleDateFormat dateFormat;

    private Context context;
    private List<NewsEntry> newsEntries;

    public NewsRecyclerViewAdapter(Context context, List<NewsEntry> newsEntries) {
        this.context = context;
        this.newsEntries = newsEntries;
        this.dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
    }

    @NonNull
    @Override
    public NewsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_news, parent, false);
        NewsRecyclerViewHolder viewHolder = new NewsRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsRecyclerViewHolder newsHolder = (NewsRecyclerViewHolder) holder;
        NewsEntry newsEntry = newsEntries.get(position + 1);
        Long timeDiff = System.currentTimeMillis() - newsEntry.getDatetime() * 1000;
        Long timeLapse = Math.max(1, timeDiff / 3600000);
        newsHolder.getNewsSourceTextView().setText(newsEntry.getSource());
        newsHolder.getNewsDateTextView().setText(timeLapse.toString() + " hours ago");
        newsHolder.getNewsTitleTextView().setText(newsEntry.getHeadline());
        newsHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewsDialog(newsEntry);
            }
        });

        ImageView newsImage = newsHolder.getNewsImageView();
        Picasso.get().load(newsEntry.getImage()).resize(
                DimConverter.Dp2Pix(120, context),
                DimConverter.Dp2Pix(120, context)).
                into(newsImage);
    }

    @Override
    public int getItemCount() {
        return newsEntries.size() - 1;
    }

    public void startNewsDialog(NewsEntry newsEntry) {
        Dialog newsDialog = new Dialog(this.context);
        newsDialog.setContentView(R.layout.dialog_news);
        newsDialog.setTitle("News");
        newsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView newsSource = (TextView) newsDialog.findViewById(R.id.newsSource);
        TextView newsDateTime = (TextView) newsDialog.findViewById(R.id.newsDateTime);
        TextView newsHeadline = (TextView) newsDialog.findViewById(R.id.newsHeadline);
        TextView newsDescription = (TextView) newsDialog.findViewById(R.id.newsDescription);

        ImageView newsChromeButton = (ImageView) newsDialog.findViewById(R.id.newsChromeButton);
        ImageView newsTwitterButton = (ImageView) newsDialog.findViewById(R.id.newsTwitterButton);
        ImageView newsFacebookButton = (ImageView) newsDialog.findViewById(R.id.newsFacebookButton);

        newsSource.setText(newsEntry.getSource());
        newsDateTime.setText(dateFormat.format(newsEntry.getDatetime() * 1000));
        newsHeadline.setText(newsEntry.getHeadline());
        newsDescription.setText(newsEntry.getSummary());

        newsChromeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsEntry.getUrl()));
                context.startActivity(intent);
            }
        });

        newsTwitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String twitterURI = null;
                try {
                    twitterURI = "https://twitter.com/intent/tweet?text=" +
                            URLEncoder.encode(newsEntry.getHeadline(), "UTF-8") +
                            "&url=" +
                            URLEncoder.encode(newsEntry.getUrl(), "UTF-8");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterURI));
                    context.startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        newsFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String facebookURI = "https://www.facebook.com/sharer/sharer.php?u=" +
                            URLEncoder.encode(newsEntry.getUrl(), "UTF-8") +
                            "&amp;src=sdkpreparse";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookURI));
                    context.startActivity(intent);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        newsDialog.show();
    }
}
