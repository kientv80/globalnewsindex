package com.vns.webstore.middleware.worker;

import com.vns.webstore.middleware.entity.NotifyInfo;
import com.vns.webstore.middleware.entity.Profile;
import com.vns.webstore.middleware.network.HttpClientHelper;
import com.vns.webstore.middleware.network.HttpRequestListener;
import com.vns.webstore.middleware.service.ProfileService;
import com.vns.webstore.middleware.storage.LocalStorageHelper;
import com.vns.webstore.middleware.utils.JSONHelper;

import java.util.List;
import java.util.Timer;

/**
 * Created by LAP10572-local on 9/1/2016.
 */
public class NotifyWorker {
    public static final String UPDATE = "update";
    public static final String NOTIFYCOUNT = "notifycount";
    public static Timer t = new Timer();

    public static void getUpdate(final NotifyListener listener){
        Profile p = ProfileService.getProfile();
        String userId = "NA";
        if(p != null)
            userId = p.getId();
        String lastUpdateTime = LocalStorageHelper.getFromFile("lasttimeupdate");
        HttpClientHelper.executeHttpGetRequest("http://360hay.com/getupdate?uid=" + userId + "&lasttimeupdate=" + lastUpdateTime, new HttpRequestListener() {
            @Override
            public void onRecievedData(Object data) {
                if(data != null) {
                    //Save in file and it will be use at a suitable time
                    String newNotify = data.toString();
                    String oldNotify = LocalStorageHelper.getFromFile(UPDATE);
                    String unreadNotifyCount = LocalStorageHelper.getFromFile("notifycount");
                    List<NotifyInfo> oldNotifyInfoList = null;
                    if(oldNotify != null && !oldNotify.isEmpty()){
                        oldNotifyInfoList =  JSONHelper.toObjects(oldNotify, NotifyInfo.class);
                    }
                    List<NotifyInfo> newNotifyInfoList =  JSONHelper.toObjects(newNotify,NotifyInfo.class);
                    int newNotifyCount = newNotifyInfoList.size();
                    if(oldNotifyInfoList != null) {
                        newNotifyInfoList.removeAll(oldNotifyInfoList);
                        newNotifyCount = newNotifyInfoList.size();
                        newNotifyInfoList.addAll(oldNotifyInfoList);
                    }
                    if(newNotifyInfoList.size() > 20){
                        List<NotifyInfo> updateNotifyInfoList = newNotifyInfoList.subList(0,20);
                        LocalStorageHelper.saveToFile(UPDATE,JSONHelper.toJSON(updateNotifyInfoList));
                    }else if(newNotifyInfoList != null && newNotifyInfoList.size() > 0){
                        LocalStorageHelper.saveToFile(UPDATE,JSONHelper.toJSON(newNotifyInfoList));
                    }
                    if(newNotifyCount > 0){
                        if(unreadNotifyCount != null && !unreadNotifyCount.isEmpty()){
                            newNotifyCount = newNotifyCount + Integer.parseInt(unreadNotifyCount);
                            LocalStorageHelper.saveToFile(NOTIFYCOUNT, newNotifyCount+"");
                        }else{
                            LocalStorageHelper.saveToFile(NOTIFYCOUNT, newNotifyCount+"");
                        }
                        listener.haveNewUpdate(newNotifyCount);
                    }
                }
            }
        });
        lastUpdateTime = System.currentTimeMillis() + "";
        LocalStorageHelper.saveToFile("lasttimeupdate",lastUpdateTime);
    }
}
