package com.vns.webstore.middleware.worker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.vns.webstore.middleware.network.ConnectionManager;
import com.vns.webstore.ui.notification.Notification;
import com.webstore.webstore.R;

/**
 * Created by LAP10572-local on 11/6/2016.
 */
public class WebstoreService extends Service{
    public static Boolean isRunning = false;
    private Thread runningThread = null;
    Intent intent;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        return START_STICKY;
    }
    /*
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Intent restartService = new Intent(getApplicationContext(),this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(getApplicationContext(), 1, restartService,PendingIntent.FLAG_ONE_SHOT);
        //Restart the service once it has been killed android
        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);

    }
    */
    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        runningThread =  new Thread(){
            @Override
            public void run() {
                while(isRunning){
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> webstore service");
                    if(getApplicationContext() != null && ConnectionManager.isNetworkAvailable()) {
                        NotifyWorker.getUpdate(new NotifyListener(){
                            @Override
                            public void haveNewUpdate(int newNotifyCount) {
                                try {
                                    Notification.notifyNewUpate(getApplicationContext(),"Tin Mới","Có " + newNotifyCount + " tin mới", R.drawable.ic_update_white_36dp, Notification.NOTIFY_ID_NEWS_UPDATE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else{
                        System.out.print("No application context or no connection" + getApplicationContext());
                    }
                    try {
                        Thread.sleep(5*60*1000);//30*60*1000
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>stop webstore service");
            }
        };
        runningThread.start();
    }

    @Override
    public void onDestroy() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>WebstoreService is onDestroy");
        super.onDestroy();
        isRunning = false;
    }


}
