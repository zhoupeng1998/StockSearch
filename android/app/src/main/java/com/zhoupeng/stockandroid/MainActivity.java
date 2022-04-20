package com.zhoupeng.stockandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.zhoupeng.stockandroid.bean.AutoCompleteEntry;
import com.zhoupeng.stockandroid.bean.FavoriteItem;
import com.zhoupeng.stockandroid.bean.Latest;
import com.zhoupeng.stockandroid.bean.Stockhold;
import com.zhoupeng.stockandroid.data.AutoCompleteService;
import com.zhoupeng.stockandroid.data.FavoriteService;
import com.zhoupeng.stockandroid.data.QueryService;
import com.zhoupeng.stockandroid.data.TransactionService;
import com.zhoupeng.stockandroid.data.UpdateService;
import com.zhoupeng.stockandroid.utils.FavoriteItemMoveCallback;
import com.zhoupeng.stockandroid.utils.FavoriteRecyclerViewAdapter;
import com.zhoupeng.stockandroid.utils.PortfolioItemMoveCallback;
import com.zhoupeng.stockandroid.utils.PortfolioRecyclerViewAdapter;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements Observer {

    public static final String EXTRA_MESSAGE = "com.zhoupeng.stockandroid.MESSAGE";

    SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private ArrayList<AutoCompleteEntry> autoCompleteEntries;
    private ArrayAdapter<AutoCompleteEntry> autoCompleteAdapter;
    private SearchView.SearchAutoComplete searchAutoComplete;

    private SearchView searchView;
    private ScrollView contentView;
    private Toolbar toolbar;
    private ProgressBar progressbar;

    private TextView netWorthTextView;
    private TextView cashBalanceTextView;

    private RecyclerView portfolioRecyclerView;
    private RecyclerView favoritesRecyclerView;

    private PortfolioRecyclerViewAdapter portfolioRecyclerViewAdapter;
    private FavoriteRecyclerViewAdapter favoriteRecyclerViewAdapter;

    private List<Stockhold> stockholdList;
    private List<FavoriteItem> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        stockholdList = new ArrayList<>();
        favoriteList = new ArrayList<>();

        this.toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        this.progressbar = (ProgressBar) findViewById(R.id.my_progressbar);
        this.contentView = (ScrollView) findViewById(R.id.contentView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
        TextView mainDate = (TextView) findViewById(R.id.mainDate);
        mainDate.setText(dateFormat.format(new Date(System.currentTimeMillis())));

        this.netWorthTextView = (TextView) findViewById(R.id.mainNetWorth);
        this.cashBalanceTextView = (TextView) findViewById(R.id.mainCashBalance);

        this.requestQueue = Volley.newRequestQueue(this);
        FavoriteService.getInstance().addObserver(this);
        TransactionService.getInstance().addObserver(this);
        AutoCompleteService.getInstance().addObserver(this);
        UpdateService.getInstance().addObserver(this);

        setSupportActionBar(this.toolbar);
        contentView.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);

        // auto update
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::queryUpdateData, 0, 15, TimeUnit.SECONDS);

        // recycler view
        this.portfolioRecyclerView = (RecyclerView) findViewById(R.id.portfolioRecyclerView);
        this.favoritesRecyclerView = (RecyclerView) findViewById(R.id.favoritesRecyclerView);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext());
        portfolioRecyclerView.setLayoutManager(layoutManager1);
        favoritesRecyclerView.setLayoutManager(layoutManager2);
        portfolioRecyclerView.addItemDecoration(new DividerItemDecoration(portfolioRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        favoritesRecyclerView.addItemDecoration(new DividerItemDecoration(favoritesRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        portfolioRecyclerView.setNestedScrollingEnabled(false);
        favoritesRecyclerView.setNestedScrollingEnabled(false);

        TextView finnhubLink = (TextView) findViewById(R.id.finnhubLink);
        finnhubLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.finnhub.io"));
                startActivity(intent);
            }
        });

        initPersistentData();
        initPortfolioData();
        initFavoriteData();
        updatePortfolioHead();
    }

    public void setVisibility() {
        contentView.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search...");

        this.searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);

        this.autoCompleteEntries = new ArrayList<>();
        this.autoCompleteAdapter = new ArrayAdapter<AutoCompleteEntry>(this, android.R.layout.simple_dropdown_item_1line, autoCompleteEntries);
        this.searchAutoComplete.setAdapter(autoCompleteAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, "you clicked " + query, Toast.LENGTH_LONG).show();
                startStockPage(query.trim().toUpperCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 2) {
                    QueryService.getInstance().queryAutoComplete(requestQueue, newText.trim().toUpperCase());
                }
                return false;
            }
        });

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AutoCompleteEntry entry = (AutoCompleteEntry)adapterView.getItemAtPosition(i);
                String queryString = entry.getSymbol();
                startStockPage(queryString);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof AutoCompleteService) {
            //System.out.println("Observable class: " + observable.getClass().getName());
            //System.out.println(AutoCompleteService.getInstance().getAutoCompleteData().toString());
            autoCompleteEntries = AutoCompleteService.getInstance().getAutoCompleteData();
            autoCompleteAdapter.clear();
            autoCompleteAdapter.addAll(autoCompleteEntries);
            autoCompleteAdapter.notifyDataSetChanged();
            searchAutoComplete.showDropDown();
        } else if (observable instanceof FavoriteService) {
            changeFavoriteData();
        } else if (observable instanceof TransactionService) {
            changePortfolioData();
        } else if (observable instanceof UpdateService) {
            updatePortfolioData();
        }
    }

    public void startStockPage(String symbol) {
        Intent intent = new Intent(this, StockActivity.class);
        intent.putExtra(EXTRA_MESSAGE, symbol);
        startActivity(intent);
    }

    public static void queryAutoUpdate() {

    }

    public void initPortfolioData() {
        Map<String, Stockhold> stockholdMap = TransactionService.getInstance().getStockholds();
        for (Map.Entry<String, Stockhold> entry : stockholdMap.entrySet()) {
            if (entry.getValue().getQuantity() > 0) {
                stockholdList.add(entry.getValue());
            }
        }

        this.portfolioRecyclerViewAdapter = new PortfolioRecyclerViewAdapter(MainActivity.this,
                stockholdList);

        ItemTouchHelper.Callback portfolioTouchCallback = new PortfolioItemMoveCallback(this.portfolioRecyclerViewAdapter);
        ItemTouchHelper portfolioTouchHelper = new ItemTouchHelper(portfolioTouchCallback);
        portfolioTouchHelper.attachToRecyclerView(this.portfolioRecyclerView);

        this.portfolioRecyclerView.setAdapter(portfolioRecyclerViewAdapter);
    }

    public void changePortfolioData() {
        Map<String, Stockhold> stockholdMap = TransactionService.getInstance().getStockholds();
        Stockhold recent = TransactionService.getInstance().getRecentAddedStockhold();
        if (recent != null) {
            this.stockholdList.add(recent);
        }
        Iterator<Stockhold> it = this.stockholdList.iterator();
        while (it.hasNext()) {
            Stockhold item = it.next();
            if (!stockholdMap.containsKey(item.getTicker())) {
                it.remove();
            }
        }
        this.portfolioRecyclerViewAdapter.notifyDataSetChanged();
        updatePortfolioHead();
        updatePersistentData();
    }

    public void initFavoriteData() {
        Map<String, FavoriteItem> favoriteItemMap = FavoriteService.getInstance().getFavoriteItems();
        for (Map.Entry<String, FavoriteItem> entry : favoriteItemMap.entrySet()) {
            this.favoriteList.add(entry.getValue());
        }
        this.favoriteRecyclerViewAdapter = new FavoriteRecyclerViewAdapter(MainActivity.this,
                this.favoriteList);

        ItemTouchHelper.Callback favoriteItemTouchCallback = new FavoriteItemMoveCallback(this, favoriteRecyclerViewAdapter);
        ItemTouchHelper favoriteTouchHelper = new ItemTouchHelper(favoriteItemTouchCallback);
        favoriteTouchHelper.attachToRecyclerView(this.favoritesRecyclerView);

        this.favoritesRecyclerView.setAdapter(favoriteRecyclerViewAdapter);
    }

    public void changeFavoriteData() {
        Map<String, FavoriteItem> favoriteItemMap = FavoriteService.getInstance().getFavoriteItems();
        FavoriteItem recent = FavoriteService.getInstance().getRecentAddedFavorite();
        if (recent != null) {
            this.favoriteList.add(recent);
        }
        Iterator<FavoriteItem> it = this.favoriteList.iterator();
        while (it.hasNext()) {
            FavoriteItem item = it.next();
            if (!favoriteItemMap.containsKey(item.getTicker())) {
                it.remove();
            }
        }
        this.favoriteRecyclerViewAdapter.notifyDataSetChanged();
        updatePersistentData();
    }

    public void queryUpdateData() {
        Map<String, Stockhold> stockholdMap = TransactionService.getInstance().getStockholds();
        for (Map.Entry<String, Stockhold> stockholdEntry : stockholdMap.entrySet()) {
            QueryService.getInstance().queryLatestUpdate(this.requestQueue, stockholdEntry.getKey());
        }
        Map<String, FavoriteItem> favoriteItemMap = FavoriteService.getInstance().getFavoriteItems();
        for (Map.Entry<String, FavoriteItem> favoriteItemEntry : favoriteItemMap.entrySet()) {
            QueryService.getInstance().queryLatestUpdate(this.requestQueue, favoriteItemEntry.getKey());
        }
    }

    public void updatePortfolioData() {
        Map<String, Latest> latestMap = UpdateService.getInstance().getLatestMap();
        for (int i = 0; i < this.stockholdList.size(); i++) {
            String ticker = stockholdList.get(i).getTicker();
            if (latestMap.containsKey(ticker)) {
                if (latestMap.get(ticker).getC() != null) {
                    stockholdList.get(i).setPrice(latestMap.get(ticker).getC());
                }
                this.portfolioRecyclerViewAdapter.notifyItemChanged(i);
            }
        }
        for (int i = 0; i < this.favoriteList.size(); i++) {
            String ticker = favoriteList.get(i).getTicker();
            if (latestMap.containsKey(ticker)) {
                FavoriteItem item = favoriteList.get(i);
                item.setDelta(latestMap.get(ticker).getD());
                item.setPrice(latestMap.get(ticker).getC());
                item.setDp(latestMap.get(ticker).getDp());
                this.favoriteRecyclerViewAdapter.notifyItemChanged(i);
            }
        }
        updatePortfolioHead();
        updatePersistentData();
        setVisibility();
    }

    public void updatePortfolioHead() {
        this.cashBalanceTextView.setText("$" + String.format("%.2f", TransactionService.getInstance().getBalance()));
        this.netWorthTextView.setText("$" + String.format("%.2f", TransactionService.getInstance().getNetWorth()));
    }

    public void initPersistentData() {
        double balance = sharedPreferences.getFloat("balance4", 25000);
        TransactionService.getInstance().setBalance(balance);

        String transactionJSON = this.sharedPreferences.getString("transaction4", "{}");
        String favoriteJSON = this.sharedPreferences.getString("favorite4", "{}");
        Map<String, Object> transactionParsed = JSON.parseObject(transactionJSON);
        Map<String, Object> favoriteParsed = JSON.parseObject(favoriteJSON);

        if (transactionParsed.size() == 0 && favoriteParsed.size() == 0) {
            setVisibility();
        }

        for (Map.Entry<String, Object> entry : transactionParsed.entrySet()) {
            Stockhold stockhold = JSON.parseObject(entry.getValue().toString(), Stockhold.class);
            TransactionService.getInstance().addEntry(entry.getKey(), stockhold);
        }

        for (Map.Entry<String, Object> entry : favoriteParsed.entrySet()) {
            FavoriteItem item = JSON.parseObject(entry.getValue().toString(), FavoriteItem.class);
            FavoriteService.getInstance().addEntry(entry.getKey(), item);
        }
        queryUpdateData();
    }

    public void updatePersistentData() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putFloat("balance4", Float.parseFloat(TransactionService.getInstance().getBalance().toString()));

        Gson gson = new Gson();
        String transactionJSON = gson.toJson(TransactionService.getInstance().getStockholds());
        String favoritesJSON = gson.toJson(FavoriteService.getInstance().getFavoriteItems());
        editor.putString("transaction4", transactionJSON);
        editor.putString("favorite4", favoritesJSON);

        editor.apply();
    }
}