package com.nickteck.cus_prawnandcrab.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.nickteck.cus_prawnandcrab.additional_class.Constants;
import com.nickteck.cus_prawnandcrab.additional_class.FontsOverride;


/**
 * Created by admin on 3/7/2018.
 */




public class MyApplication extends Application {

    private static MyApplication mInstance;
    NetworkChangeReceiver networkChangeReceiver;
    String isConnection = null;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Lato_Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "Lato_Medium.ttf");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkChangeReceiver = new NetworkChangeReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent) {
                    super.onReceive(context, intent);

                    if (isConnection == null) {
                        Bundle b = intent.getExtras();
                        isConnection = b.getString(Constants.MESSAGE);
                    }
                }
            };

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            intentFilter.addAction(Constants.BROADCAST);
            this.registerReceiver(networkChangeReceiver,
                    intentFilter);
        }
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener listener) {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }


}
