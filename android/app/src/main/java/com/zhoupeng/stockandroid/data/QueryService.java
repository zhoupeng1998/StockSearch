package com.zhoupeng.stockandroid.data;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zhoupeng.stockandroid.bean.Latest;
import com.zhoupeng.stockandroid.bean.Profile;

public class QueryService {

    private static class QueryServiceSingletonInstance {
        private static final QueryService instance = new QueryService();
    }

    private QueryService() {}

    public static QueryService getInstance() {
        return QueryServiceSingletonInstance.instance;
    }

    public void queryAll(RequestQueue requestQueue, String symbol) {
        DataService.getInstance().resetDataService();
        queryProfile(requestQueue, symbol);
        queryLatest(requestQueue, symbol);
        queryNews(requestQueue, symbol);
        queryRecommendation(requestQueue, symbol);
        querySocial(requestQueue, symbol);
        queryPeers(requestQueue, symbol);
        queryEarnings(requestQueue, symbol);
        queryHistoryCandle(requestQueue, symbol);
    }

    public void queryProfile(RequestQueue requestQueue, String symbol) {
        symbol = symbol.trim().toUpperCase();
        String url = "http://node571-env.eba-srjtpbg8.us-west-2.elasticbeanstalk.com/api/profile/" + symbol;
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DataService.getInstance().setProfileData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DataService.getInstance().setProfileData("{}");
                    }
                });
        requestQueue.add(request);
    }

    public void queryLatest(RequestQueue requestQueue, String symbol) {
        symbol = symbol.trim().toUpperCase();
        String url = "http://node571-env.eba-srjtpbg8.us-west-2.elasticbeanstalk.com/api/latest/" + symbol;
        String finalSymbol = symbol;
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DataService.getInstance().setLatestData(response);
                        Latest latest = DataService.getInstance().getLatestData();
                        if (latest != null && latest.getT() != null) {
                            Long timestamp = latest.getT();
                            Long current = System.currentTimeMillis();
                            if (current - timestamp < 300000) {
                                DataService.getInstance().setMarketOpenFlag(true);
                                querySummaryCandle(requestQueue, finalSymbol, String.valueOf(current / 1000));
                            } else {
                                DataService.getInstance().setMarketOpenFlag(false);
                                querySummaryCandle(requestQueue, finalSymbol, String.valueOf(timestamp / 1000));
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DataService.getInstance().setLatestData("{}");
                    }
                });
        requestQueue.add(request);
    }

    public void queryNews(RequestQueue requestQueue, String symbol) {
        symbol = symbol.trim().toUpperCase();
        String url = "http://node571-env.eba-srjtpbg8.us-west-2.elasticbeanstalk.com/api/news/" + symbol;
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DataService.getInstance().setNewsListData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DataService.getInstance().setNewsListData("[]");
                    }
                });
        requestQueue.add(request);
    }

    public void queryRecommendation(RequestQueue requestQueue, String symbol) {
        symbol = symbol.trim().toUpperCase();
        String url = "http://node571-env.eba-srjtpbg8.us-west-2.elasticbeanstalk.com/api/recommendation/" + symbol;
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DataService.getInstance().setRecommendationListData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DataService.getInstance().setRecommendationListData("[]");
                    }
                });
        requestQueue.add(request);
    }

    public void querySocial(RequestQueue requestQueue, String symbol) {
        symbol = symbol.trim().toUpperCase();
        String url = "http://node571-env.eba-srjtpbg8.us-west-2.elasticbeanstalk.com/api/social/" + symbol;
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DataService.getInstance().setSocialData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DataService.getInstance().setSocialData("{}");
                    }
                });
        requestQueue.add(request);
    }

    public void queryPeers(RequestQueue requestQueue, String symbol) {
        symbol = symbol.trim().toUpperCase();
        String url = "http://node571-env.eba-srjtpbg8.us-west-2.elasticbeanstalk.com/api/peers/" + symbol;
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DataService.getInstance().setPeersListData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DataService.getInstance().setPeersListData("[]");
                    }
                });
        requestQueue.add(request);
    }

    public void queryEarnings(RequestQueue requestQueue, String symbol) {
        symbol = symbol.trim().toUpperCase();
        String url = "http://node571-env.eba-srjtpbg8.us-west-2.elasticbeanstalk.com/api/earnings/" + symbol;
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DataService.getInstance().setEarningsListData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DataService.getInstance().setEarningsListData("[]");
                    }
                });
        requestQueue.add(request);
    }

    public void queryHistoryCandle(RequestQueue requestQueue, String symbol) {
        symbol = symbol.trim().toUpperCase();
        String url = "http://node571-env.eba-srjtpbg8.us-west-2.elasticbeanstalk.com/api/candle/year/" + symbol;
        System.out.println(url);
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DataService.getInstance().setHistoryCandleData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DataService.getInstance().setHistoryCandleData("{}");
                    }
                });
        requestQueue.add(request);
    }

    public void querySummaryCandle(RequestQueue requestQueue, String symbol, String timestamp) {
        symbol = symbol.trim().toUpperCase();
        String url = "http://node571-env.eba-srjtpbg8.us-west-2.elasticbeanstalk.com/api/candle/hour/" + symbol + '/' + timestamp;
        System.out.println(url);
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DataService.getInstance().setSummaryCandleData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DataService.getInstance().setSummaryCandleData("{}");
                    }
                });
        requestQueue.add(request);
    }

    /**
     * Served in AutoCompleteService, apart from DataService
     * */
    public void queryAutoComplete(RequestQueue requestQueue, String symbol) {
        symbol = symbol.trim().toUpperCase();
        String url = "http://node571-env.eba-srjtpbg8.us-west-2.elasticbeanstalk.com/api/autocomplete/" + symbol;
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        AutoCompleteService.getInstance().setAutoCompleteData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                        AutoCompleteService.getInstance().setAutoCompleteData("[]");
                    }
                });
        requestQueue.add(request);
    }

    /**
     * For Auto update in main page only!
     * */
    public void queryLatestUpdate(RequestQueue requestQueue, String symbol) {
        symbol = symbol.trim().toUpperCase();
        String url = "http://node571-env.eba-srjtpbg8.us-west-2.elasticbeanstalk.com/api/latest/" + symbol;
        String finalSymbol = symbol;
        String finalSymbol1 = symbol;
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(finalSymbol1 + " " + response);
                        UpdateService.getInstance().setLatestData(finalSymbol1, response);
                        DataService.getInstance().setLatestData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        DataService.getInstance().setLatestData("{}");
                    }
                });
        requestQueue.add(request);
    }
}
