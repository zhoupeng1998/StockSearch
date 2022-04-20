package com.zhoupeng.stockandroid.jsinterface;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.zhoupeng.stockandroid.MainActivity;
import com.zhoupeng.stockandroid.StockActivity;
import com.zhoupeng.stockandroid.bean.Latest;
import com.zhoupeng.stockandroid.bean.Profile;

import java.util.List;

public class InfoInterface {

    Context context;
    String ticker;

    String openPrice;
    String highPrice;
    String lowPrice;
    String prevClosePrice;

    String ipo;
    String industry;
    String webPage;
    String peers;

    public InfoInterface(Context context, String ticker, Profile profile, Latest latest,
                         List<String> peersList) {
        this.context = context;
        this.ticker = ticker;

        this.openPrice = String.format("%.2f", latest.getO());
        this.highPrice = String.format("%.2f", latest.getH());
        this.lowPrice = String.format("%.2f", latest.getL());
        this.prevClosePrice = String.format("%.2f", latest.getPc());

        this.ipo = profile.getIpo();
        this.industry = profile.getIndustry();
        this.webPage = profile.getWeburl();
        this.peers = JSON.toJSONString(peersList);
    }

    @JavascriptInterface
    public void gotoWebPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.webPage));
        context.startActivity(intent);
    }

    @JavascriptInterface
    public void gotoPeersActivity(String ticker) {
        Intent intent = new Intent(context, StockActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, ticker);
        context.startActivity(intent);
    }

    @JavascriptInterface
    public String getTicker() {
        return ticker;
    }

    @JavascriptInterface
    public String getOpenPrice() {
        return openPrice;
    }

    @JavascriptInterface
    public String getHighPrice() {
        return highPrice;
    }

    @JavascriptInterface
    public String getLowPrice() {
        return lowPrice;
    }

    @JavascriptInterface
    public String getPrevClosePrice() {
        return prevClosePrice;
    }

    @JavascriptInterface
    public String getIpo() {
        return ipo;
    }

    @JavascriptInterface
    public String getIndustry() {
        return industry;
    }

    @JavascriptInterface
    public String getWebPage() {
        return webPage;
    }

    @JavascriptInterface
    public String getPeers() {
        return peers;
    }
}
