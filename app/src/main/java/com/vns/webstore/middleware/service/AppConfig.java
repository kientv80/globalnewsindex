package com.vns.webstore.middleware.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vns.webstore.middleware.network.HttpClientHelper;
import com.vns.webstore.middleware.network.HttpRequestListener;
import com.vns.webstore.middleware.storage.LocalStorageHelper;

/**
 * Created by LAP10572-local on 9/22/2016.
 */
public class AppConfig implements HttpRequestListener{
    public void loadAppConfig(){
        String conf = LocalStorageHelper.getFromFile("config");
        String version = "";
        if(conf != null && !conf.isEmpty()){
            try{
                JsonElement jsonConf = new JsonParser().parse(conf);
                JsonObject jsonConfObj = jsonConf.getAsJsonObject();
                version = jsonConfObj.get("version").getAsString();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        HttpClientHelper.executeHttpGetRequest("http://360hay.com/mobile/config?version=" + version,this);
    }
    public static String getConfig(String key){
        String conf = LocalStorageHelper.getFromFile("config");
        if(conf != null && !conf.isEmpty()) {
            JsonElement jsonConf = new JsonParser().parse(conf);
            JsonObject jsonConfObj = jsonConf.getAsJsonObject();
            String cates = jsonConfObj.get("categories").getAsString();
            return cates;
        }
        return null;
    }

    @Override
    public void onRecievedData(Object data) {
        if(data != null) {
            try {
                JsonElement jsonConf = new JsonParser().parse(data.toString());
                JsonObject jsonConfObj = jsonConf.getAsJsonObject();
                String errorCode = jsonConfObj.get("errorCode").getAsString();
                if("0".equals(errorCode))
                    LocalStorageHelper.saveToFile("config", data.toString());
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
    }
}
