package com.mmt.record.mvp.model.mvp.ui.Receiver;
 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.mmt.record.mvp.model.mvp.util.NetworkType;
import com.mmt.record.mvp.model.mvp.util.NetworkUtil;

 
import java.util.ArrayList;
import java.util.List;
 
public class NetStateChangeReceiver extends BroadcastReceiver {
 
    private static class InstanceHolder {
        private static final NetStateChangeReceiver INSTANCE = new NetStateChangeReceiver();
    }
 
    private List<NetStateChangeObserver> mObservers = new ArrayList<>();
 
    @Override
    public void onReceive(Context context, Intent intent) {
 
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkType networkType = NetworkUtil.getNetworkType(context);
            Log.i("NetStateChangeReceiver", networkType.toString());
            notifyObservers(networkType);
        }
    }
 
    public static void registerReceiver(Context context) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(InstanceHolder.INSTANCE, intentFilter);
    }
 
    public static void unRegisterReceiver(Context context) {
        context.unregisterReceiver(InstanceHolder.INSTANCE);
    }
 
    public static void registerObserver(NetStateChangeObserver observer) {
        if (observer == null) {
            return;
        }
        if (!InstanceHolder.INSTANCE.mObservers.contains(observer)) {
            InstanceHolder.INSTANCE.mObservers.add(observer);
        }
    }
 
    public static void unRegisterObserver(NetStateChangeObserver observer) {
        if (observer == null) {
            return;
        }
        if (InstanceHolder.INSTANCE.mObservers == null) {
            return;
        }
        InstanceHolder.INSTANCE.mObservers.remove(observer);
    }
 
    private void notifyObservers(NetworkType networkType) {
        if (networkType == NetworkType.NETWORK_NO) {
            for (NetStateChangeObserver observer : mObservers) {
                observer.onNetDisconnected();
            }
        } else {
            for (NetStateChangeObserver observer : mObservers) {
                observer.onNetConnected(networkType);
            }
        }
    }



    //获取wifi信号强度
    private void getWifi() {
  /*      //x心跳包请求成功，获取真实网速，请求失败，网络错误
        WifiManager wifi_service = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifi_service.getConnectionInfo();

        int wStrength = Math.abs(wifiInfo.getRssi());
        if (wStrength < 50) {
            ivNet.setImageResource(R.drawable.net4);
        } else if (49 < wStrength && wStrength < 100) {
            ivNet.setImageResource(R.drawable.net3);
        } else if (100 < wStrength) {
            ivNet.setImageResource(R.drawable.net2);
        }*/

    }

}