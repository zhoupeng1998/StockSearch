package com.zhoupeng.stockandroid.data;

import com.alibaba.fastjson.JSON;
import com.zhoupeng.stockandroid.bean.Latest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class UpdateService extends Observable {

    private Map<String, Latest> latestMap;

    private static class UpdateServiceSingletonInstance {
        private static final UpdateService instance = new UpdateService();
    }

    private UpdateService() {
        latestMap = new HashMap<>();
    }

    public static UpdateService getInstance() {
        return UpdateServiceSingletonInstance.instance;
    }

    public Map<String, Latest> getLatestMap() {
        return latestMap;
    }

    public synchronized void setLatestData(String symbol, String data) {
        latestMap.put(symbol, JSON.parseObject(data, Latest.class));
        System.out.println(symbol + " updated");
        setChanged();
        notifyObservers();
    }
}
