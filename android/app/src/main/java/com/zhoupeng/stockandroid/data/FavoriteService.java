package com.zhoupeng.stockandroid.data;

import com.zhoupeng.stockandroid.bean.FavoriteItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class FavoriteService extends Observable {

    private FavoriteItem recentAddedFavorite;
    private Map<String, FavoriteItem> favoriteItems;

    private static class FavoriteServiceSingletonInstance {
        private static final FavoriteService instance = new FavoriteService();
    }

    private FavoriteService() {
        favoriteItems = new HashMap<>();
        // TODO: load from local storage / database
    }

    public static FavoriteService getInstance() {
        return FavoriteServiceSingletonInstance.instance;
    }

    public Map<String, FavoriteItem> getFavoriteItems() {
        return favoriteItems;
    }

    public boolean isFavorite(String symbol) {
        return favoriteItems.containsKey(symbol);
    }

    public void addToFavorite(String symbol, String name, Double price, Double delta, Double dp) {
        FavoriteItem item = new FavoriteItem(symbol, name, price, delta, dp);
        favoriteItems.put(symbol, item);
        recentAddedFavorite = item;
        setChanged();
        notifyObservers();
    }

    public void removeFromFavorite(String symbol) {
        favoriteItems.remove(symbol);
        recentAddedFavorite = null;
        setChanged();
        notifyObservers();
    }

    public FavoriteItem getRecentAddedFavorite() {
        FavoriteItem item = recentAddedFavorite;
        recentAddedFavorite = null;
        return item;
    }

    public void addEntry(String key, FavoriteItem item) {
        this.favoriteItems.put(key, item);
    }
}
