package com.zhoupeng.stockandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.zhoupeng.stockandroid.data.DataService;
import com.zhoupeng.stockandroid.jsinterface.SummaryChartInterface;

import java.util.Observable;
import java.util.Observer;

public class HourChartFragment extends Fragment implements Observer {

    private boolean isDataLoaded;
    private String ticker;

    private WebView summaryChartView;

    public HourChartFragment() {
        // Required empty public constructor
        System.out.println("Fragment created!");
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.isDataLoaded = false;
        DataService.getInstance().addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hour_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.summaryChartView = (WebView) getView().findViewById(R.id.summaryChartView);
        this.summaryChartView.getSettings().setJavaScriptEnabled(true);
        this.summaryChartView.setWebChromeClient(new WebChromeClient());
        this.summaryChartView.addJavascriptInterface(new SummaryChartInterface(
                this.ticker,
                        DataService.getInstance().getSummaryCandleDataString(),
                        DataService.getInstance().getLatestData().getDp().toString()),
                "SummaryChartInterface");
        this.summaryChartView.loadUrl("file:///android_asset/summary_chart.html");
    }

    @Override
    public void update(Observable observable, Object o) {
        /*
        System.out.println("Updated!!");
        if (!isDataLoaded && !DataService.getInstance().isDataInvalid()) {
            Candle summaryCandle = DataService.getInstance().getHistoryCandleData();
            testTextView.setText(summaryCandle.toString());
        }
        */
    }
}