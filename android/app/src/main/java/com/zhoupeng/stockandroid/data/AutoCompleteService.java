package com.zhoupeng.stockandroid.data;

import com.alibaba.fastjson.JSON;
import com.zhoupeng.stockandroid.bean.AutoCompleteEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class AutoCompleteService extends Observable {
    private ArrayList<AutoCompleteEntry> autoCompleteEntries;

    private static class AutoCompleteServiceSingletonInstance {
        private static final AutoCompleteService instance = new AutoCompleteService();
    }

    private AutoCompleteService() {

    }

    public static AutoCompleteService getInstance() {
        return AutoCompleteServiceSingletonInstance.instance;
    }

    public void setAutoCompleteData(String data) {
        autoCompleteEntries = (ArrayList<AutoCompleteEntry>)JSON.parseArray(data, AutoCompleteEntry.class);
        setChanged();
        notifyObservers();
    }

    public ArrayList<AutoCompleteEntry> getAutoCompleteData() {
        return this.autoCompleteEntries;
    }
}
