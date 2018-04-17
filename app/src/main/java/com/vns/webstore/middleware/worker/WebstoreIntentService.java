package com.vns.webstore.middleware.worker;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.vns.webstore.middleware.network.ConnectionManager;
import com.vns.webstore.ui.notification.Notification;
import com.webstore.webstore.R;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WebstoreIntentService extends IntentService implements NotifyListener {
    List<Integer> timeToShowNotification = new ArrayList<>();
    BroadcastReceiver networkStateReceiver = null;
    public WebstoreIntentService() {
        super("Webstore");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            networkStateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    System.out.println("Network Listener Network Type Changed");
                    ConnectionManager.onReceive(context,intent);
                }
            };
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(networkStateReceiver, filter);


            Thread t = new Thread();
            while(true){
                try {
                    t.sleep(10*60*1000);//check every one hour
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(getApplicationContext() != null && ConnectionManager.isNetworkAvailable()) {
                    NotifyWorker.getUpdate(this);
                }else{
                    System.out.print("Not application context or no connection" + getApplicationContext());
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(networkStateReceiver);
        super.onDestroy();
    }

    @Override
    public void haveNewUpdate(int newNotifyCount) {
        try {
            Notification.notifyNewUpate(this,"Tin Mới","Có " + newNotifyCount + " tin mới", R.drawable.ic_update_white_36dp, Notification.NOTIFY_ID_NEWS_UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
