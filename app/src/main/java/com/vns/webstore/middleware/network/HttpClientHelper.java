package com.vns.webstore.middleware.network;

import android.util.Pair;

import java.util.List;

/**
 * Created by LAP10572-local on 7/2/2016.
 */
public class HttpClientHelper {
    public static void executeHttpGetRequest(String url, HttpRequestListener callbackListener){
        new HttpGetRequest().execute(url,callbackListener);
    }
    public static void executeHttpPostRequest(String url, HttpRequestListener callbackListener, List<Pair<String,String>> params){
        new HttpPostRequest().execute(url,callbackListener, params);
    }
}
