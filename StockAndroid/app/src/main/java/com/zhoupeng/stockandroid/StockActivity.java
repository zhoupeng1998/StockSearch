package com.zhoupeng.stockandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;
import com.zhoupeng.stockandroid.bean.Latest;
import com.zhoupeng.stockandroid.bean.NewsEntry;
import com.zhoupeng.stockandroid.bean.Profile;
import com.zhoupeng.stockandroid.bean.Stockhold;
import com.zhoupeng.stockandroid.data.DataService;
import com.zhoupeng.stockandroid.data.FavoriteService;
import com.zhoupeng.stockandroid.data.QueryService;
import com.zhoupeng.stockandroid.data.TransactionService;
import com.zhoupeng.stockandroid.jsinterface.InfoInterface;
import com.zhoupeng.stockandroid.jsinterface.InsightsInterface;
import com.zhoupeng.stockandroid.jsinterface.PortfolioInterface;
import com.zhoupeng.stockandroid.utils.DimConverter;
import com.zhoupeng.stockandroid.utils.NewsRecyclerViewAdapter;
import com.zhoupeng.stockandroid.utils.VPAdapter;

import java.util.Observable;
import java.util.Observer;

public class StockActivity extends AppCompatActivity implements Observer {

    private String symbol;
    private String companyName;
    private Double price;
    private Double delta;
    private Double dp;

    private boolean isDataLoaded;
    private boolean isFavorite;
    private RequestQueue requestQueue;

    // local data
    private Latest latestData;
    private Stockhold stockhold;

    private Toolbar toolbar;
    private ProgressBar progressbar;

    private VPAdapter vpAdapter;
    private TabLayout tabLayout;
    private ViewPager2 chartsViewPager;

    private ScrollView contentView;
    private TextView testTextView;
    private Button testButton;
    private Button tradeButton;

    private ImageView summaryLogo;
    private TextView summaryTicker;
    private TextView summaryPrice;
    private TextView summaryCompanyName;
    private TextView summaryDelta;
    private ImageView summaryTrendImage;

    private WebView portfolioWebView;
    private WebView infoWebView;
    private WebView insightsWebView;
    private PortfolioInterface portfolioInterface;
    private InfoInterface infoInterface;
    private InsightsInterface insightsInterface;
    private RecyclerView newsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        Intent intent = getIntent();
        this.symbol = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        this.isFavorite = false;

        this.requestQueue = Volley.newRequestQueue(this);
        DataService.getInstance().addObserver(this);

        // Toolbar
        this.toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(this.symbol);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // transaction manager
        this.stockhold = TransactionService.getInstance().getStockholdByTicker(this.symbol);

        // charts view pager
        int[] titles = {R.drawable.chart_line, R.drawable.clock_time_three};
        this.chartsViewPager = (ViewPager2) findViewById(R.id.chartsViewPager);
        this.tabLayout = (TabLayout) findViewById(R.id.chartsTab);
        this.vpAdapter = new VPAdapter(this);
        this.vpAdapter.setTicker(symbol);
        this.chartsViewPager.setAdapter(vpAdapter);
        new TabLayoutMediator(tabLayout, chartsViewPager, (tab, position) -> {
            Drawable drawable = getResources().getDrawable(titles[position]);
            tab.setIcon(drawable);
        }).attach();

        // elements
        this.progressbar = (ProgressBar) findViewById(R.id.my_progressbar);
        this.contentView = (ScrollView) findViewById(R.id.contentView);
        this.tradeButton = (Button) findViewById(R.id.tradeButton);

        this.summaryLogo = (ImageView) findViewById(R.id.summaryLogo);
        this.summaryTicker = (TextView) findViewById(R.id.summaryTicker);
        this.summaryCompanyName = (TextView) findViewById(R.id.summaryCompanyName);
        this.summaryPrice = (TextView) findViewById(R.id.summaryPrice);
        this.summaryDelta = (TextView) findViewById(R.id.summaryDelta);
        this.summaryTrendImage = (ImageView) findViewById(R.id.summaryTrendImage);

        this.portfolioWebView = (WebView) findViewById(R.id.portfolioWebView);
        this.infoWebView = (WebView) findViewById(R.id.infoWebView);
        this.insightsWebView = (WebView) findViewById(R.id.insightsWebView);

        this.newsRecyclerView = (RecyclerView) findViewById(R.id.newsRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        this.newsRecyclerView.setLayoutManager(gridLayoutManager);

        // back & search button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        contentView.setVisibility(View.GONE);
        QueryService.getInstance().queryAll(this.requestQueue, this.symbol);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stock_menu, menu);
        MenuItem favoriteButton = menu.findItem(R.id.action_favorite);
        if (FavoriteService.getInstance().isFavorite(this.symbol)) {
            isFavorite = true;
            favoriteButton.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_24));
        } else {
            isFavorite = false;
            favoriteButton.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_border_24));

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            if (isFavorite) {
                isFavorite = false;
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_border_24));
                FavoriteService.getInstance().removeFromFavorite(this.symbol);
                Toast.makeText(StockActivity.this, this.symbol + " removed from favorites", Toast.LENGTH_LONG).show();
            } else {
                isFavorite = true;
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_star_24));
                FavoriteService.getInstance().addToFavorite(this.symbol, this.companyName, this.price, this.delta, this.dp);
                Toast.makeText(StockActivity.this, this.symbol + " added to favorites", Toast.LENGTH_LONG).show();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (DataService.getInstance().isDataInvalid()) {
            setInvalidData();
            Toast.makeText(StockActivity.this, "INVALID", Toast.LENGTH_LONG).show();
        } else if (!isDataLoaded){
            isDataLoaded = true;
            setValidData();
        }
        if (observable instanceof DataService) {
        }
    }

    public void startStockPage() {
        Intent intent = new Intent(this, StockActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, "TSLA");
        startActivity(intent);
    }

    public void setInvalidData() {
        this.progressbar.setVisibility(View.GONE);
    }

    public void setValidData() {
        setProfile();
        setLatest();
        setPortfolio();
        setInfo();
        this.progressbar.setVisibility(View.GONE);
        this.contentView.setVisibility(View.VISIBLE);
        setInsights();
        setNews();
    }

    public void setProfile() {
        Profile profile = DataService.getInstance().getProfileData();
        Picasso.get().load(profile.getLogo()).resize(80, 80).into(summaryLogo);
        this.summaryTicker.setText(profile.getTicker());
        this.summaryCompanyName.setText(profile.getName());
        this.companyName = profile.getName();
    }

    public void setLatest() {
        this.latestData = DataService.getInstance().getLatestData();
        Latest latest = this.latestData;
        this.price = latest.getC();
        this.delta = latest.getD();
        this.dp = latest.getDp();
        this.summaryPrice.setText("$" + String.format("%.2f", latest.getC()));
        this.summaryDelta.setText("$" + String.format("%.2f", latest.getD()) + "(" + String.format("%.2f", latest.getDp()) + "%)");
        if (latest.getDp() > 0) {
            Drawable drawable = getResources().getDrawable(R.drawable.trending_up);
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, Color.GREEN);
            this.summaryTrendImage.setImageDrawable(wrappedDrawable);
            this.summaryDelta.setTextColor(Color.GREEN);
        } else if (latest.getDp() < 0) {
            Drawable drawable = getResources().getDrawable(R.drawable.trending_down);
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, Color.RED);
            this.summaryTrendImage.setImageDrawable(wrappedDrawable);
            this.summaryDelta.setTextColor(Color.RED);
        }
        this.stockhold.setPrice(latest.getC());
    }

    public void setPortfolio() {
        this.portfolioInterface = new PortfolioInterface(this.stockhold);
        this.portfolioWebView.getSettings().setJavaScriptEnabled(true);
        this.portfolioWebView.setWebChromeClient(new WebChromeClient());
        this.portfolioWebView.addJavascriptInterface(this.portfolioInterface, "PortfolioInterface");
        this.portfolioWebView.loadUrl("file:///android_asset/stock_portfolio.html");
    }

    public void setInfo() {
        this.infoInterface = new InfoInterface(this, this.symbol,
                DataService.getInstance().getProfileData(),
                DataService.getInstance().getLatestData(),
                DataService.getInstance().getPeersListData());
        this.infoWebView.getSettings().setJavaScriptEnabled(true);
        this.infoWebView.getSettings().setUseWideViewPort(true);
        this.infoWebView.setWebChromeClient(new WebChromeClient());
        this.infoWebView.addJavascriptInterface(this.infoInterface, "InfoInterface");
        this.infoWebView.loadUrl("file:///android_asset/stock_info.html");
    }

    public void updateInfo() {
        this.infoWebView.loadUrl("file:///android_asset/stock_info.html");
    }

    public void setInsights() {
        this.insightsInterface = new InsightsInterface(this.symbol,
                DataService.getInstance().getProfileData().getName(),
                DataService.getInstance().getSocialData(),
                DataService.getInstance().getRecommendationListData(),
                DataService.getInstance().getEarningsListData());
        this.insightsWebView.getSettings().setJavaScriptEnabled(true);
        this.insightsWebView.setWebChromeClient(new WebChromeClient());
        this.insightsWebView.addJavascriptInterface(this.insightsInterface, "InsightsInterface");
        this.insightsWebView.loadUrl("file:///android_asset/insights_info.html");
    }

    public void setNews() {
        // top news
        RelativeLayout topNewsLayout = (RelativeLayout) findViewById(R.id.topNewsLayout);
        View view = getLayoutInflater().inflate(R.layout.topitem_news, null);
        topNewsLayout.addView(view);
        NewsEntry topNews = DataService.getInstance().getNewsListData().get(0);
        Long timeDiff = System.currentTimeMillis() - topNews.getDatetime() * 1000;
        Long timeLapse = Math.max(1, timeDiff / 3600000);
        TextView topnewsSourceTextView = (TextView) view.findViewById(R.id.topnewsSourceTextView);
        TextView topnewsDateTextView = (TextView) view.findViewById(R.id.topnewsDateTextView);
        TextView topnewsTitleTextView = (TextView) view.findViewById(R.id.topnewsTitleTextView);
        ImageView topnewsImageView = (ImageView) view.findViewById(R.id.topnewsImageView);
        topnewsSourceTextView.setText(topNews.getSource());
        topnewsDateTextView.setText(timeLapse.toString() + " hours ago");
        topnewsTitleTextView.setText(topNews.getHeadline());
        Picasso.get().load(topNews.getImage()).resize(
                DimConverter.Dp2Pix(630, this),
                DimConverter.Dp2Pix(300, this)
        ).into(topnewsImageView);
        // news list
        NewsRecyclerViewAdapter newsViewAdapter = new NewsRecyclerViewAdapter(StockActivity.this,
                DataService.getInstance().getNewsListData());
        this.newsRecyclerView.setAdapter(newsViewAdapter);
    }

    // Trading feature
    public void testTradeButton(View view) {
        Double balance = TransactionService.getInstance().getBalance();
        Double localPrice = this.price;
        final Integer[] shares = {0};
        final Double[] estimate = {0.0};

        Dialog transactionDialog = new Dialog(StockActivity.this);
        transactionDialog.setContentView(R.layout.dialog_transaction);
        transactionDialog.setTitle("ZP");
        transactionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText transactionInputBox = transactionDialog.findViewById(R.id.transactionInputBox);
        TextView transactionTitleText = transactionDialog.findViewById(R.id.transactionTitleText);
        TextView transactionPreviewText = transactionDialog.findViewById(R.id.transactionPreviewText);
        TextView transactionInfoText = transactionDialog.findViewById(R.id.transactionInfoText);

        transactionTitleText.setText("Trade " + this.companyName + " shares");
        transactionInfoText.setText("$" + String.format("%.2f", balance) + " to buy " + this.symbol);
        transactionPreviewText.setText("0*$" + String.format("%.2f", this.price) + "/share = 0.00");

        transactionDialog.show();

        Button buyButton = (Button) transactionDialog.findViewById(R.id.doneButton);
        Button sellButton = (Button) transactionDialog.findViewById(R.id.sellButton);

        transactionInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String editableString = editable.toString();
                if (editableString.length() == 0) {
                    shares[0] = 0;
                } else {
                    shares[0] = Integer.parseInt(editable.toString());
                }
                estimate[0] = localPrice * shares[0];
                transactionPreviewText.setText(
                        shares[0].toString() + "*$" + String.format("%.2f", localPrice) +
                        "/share = " + String.format("%.2f", estimate[0]));
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tryBuyStock(shares[0], estimate[0])) {
                    transactionDialog.dismiss();
                    showTransactionComplete("bought", symbol, shares[0]);
                }
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trySellStock(shares[0], estimate[0])) {
                    transactionDialog.dismiss();
                    showTransactionComplete("sold", symbol, shares[0]);
                }
            }
        });
    }

    public boolean tryBuyStock(Integer shares, Double estimate) {
        if (estimate <= 0) {
            Toast.makeText(StockActivity.this, "Cannot buy non-positive shares", Toast.LENGTH_LONG).show();
            return false;
        }
        if (estimate > TransactionService.getInstance().getBalance()) {
            Toast.makeText(StockActivity.this, "Not enough money to buy", Toast.LENGTH_LONG).show();
            return false;
        }
        this.stockhold = TransactionService.getInstance().buyStockByTicker(this.symbol, shares, this.price);
        this.portfolioInterface.setPortfolioInterface(this.stockhold);
        this.portfolioWebView.loadUrl("file:///android_asset/stock_portfolio.html");
        return true;
    }

    public boolean trySellStock(Integer shares, Double estimate) {
        if (shares <= 0) {
            Toast.makeText(StockActivity.this, "Cannot sell non-positive shares", Toast.LENGTH_LONG).show();
            return false;
        }
        if (shares > this.stockhold.getQuantity()) {
            Toast.makeText(StockActivity.this, "Not enough shares to sell", Toast.LENGTH_LONG).show();
            return false;
        }
        this.stockhold = TransactionService.getInstance().sellStockByTicker(this.symbol, shares, this.price);
        this.portfolioInterface.setPortfolioInterface(this.stockhold);
        this.portfolioWebView.loadUrl("file:///android_asset/stock_portfolio.html");
        return true;
    }

    public void showTransactionComplete(String mode, String ticker, Integer shares) {
        Dialog successDialog = new Dialog(StockActivity.this);
        successDialog.setContentView(R.layout.dialog_transaction_complete);
        successDialog.setTitle("ZP");
        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView transactionSuccessText = successDialog.findViewById(R.id.transactionSuccessText);
        Button doneButton = successDialog.findViewById(R.id.doneButton);

        transactionSuccessText.setText("You have successfully " + mode + "\n" +
                shares.toString() + " shares of " + ticker);

        successDialog.show();

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                successDialog.dismiss();
            }
        });
    }
}