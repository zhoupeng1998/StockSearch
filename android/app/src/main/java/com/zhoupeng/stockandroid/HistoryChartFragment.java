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
import com.zhoupeng.stockandroid.jsinterface.HistoryChartInterface;

public class HistoryChartFragment extends Fragment {

    private String ticker;

    private WebView historyChartView;

    public HistoryChartFragment() {
        // Required empty public constructor
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.historyChartView = (WebView) getView().findViewById(R.id.historyChartView);
        this.historyChartView.getSettings().setJavaScriptEnabled(true);
        this.historyChartView.setWebChromeClient(new WebChromeClient());
        historyChartView.addJavascriptInterface(new HistoryChartInterface(
                this.ticker, DataService.getInstance().getHistoryCandleDataString()),
                "HistoryChartInterface");
        historyChartView.loadUrl("file:///android_asset/history_chart.html");
    }
}