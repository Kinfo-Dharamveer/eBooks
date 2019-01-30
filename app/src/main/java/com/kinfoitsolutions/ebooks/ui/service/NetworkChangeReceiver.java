package com.kinfoitsolutions.ebooks.ui.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kinfoitsolutions.ebooks.ui.data.Constants;
import com.kinfoitsolutions.ebooks.ui.data.MessageEvent;
import com.kinfoitsolutions.ebooks.ui.utils.NetworkUtil;
import org.greenrobot.eventbus.EventBus;


public class NetworkChangeReceiver extends BroadcastReceiver {
    Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String status = NetworkUtil.getConnectivityStatusString(context);

        Log.e("Receiver ", "" + status);

        if (status.equals(Constants.NOT_CONNECT)) {
            Log.e("Receiver ", "not connection");// your code when internet lost
            EventBus.getDefault().post(new MessageEvent(status));



        } else {
           // checkNetStatus(false);
            EventBus.getDefault().post(new MessageEvent(status));

            Log.e("Receiver ", "connected to internet");//your code when internet connection come back
        }
       // DashboardActivity.addLogText(status);

    }




}
