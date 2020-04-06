package com.kc.neteasy.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

public class NetWorkAvailable {

    private static NetWorkAvailable netWorkAvailable;
    private ConnectivityManager connectivityManager;

    private Context context;
    public NetWorkAvailable(Context context){
        this.context = context;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static synchronized NetWorkAvailable getInstance(Context context){
        if (netWorkAvailable == null){
            netWorkAvailable = new NetWorkAvailable(context);
        }
        return netWorkAvailable;
    }

    public boolean wifiConnected(){
        boolean isWifiConn = false;
        for (Network network : connectivityManager.getAllNetworks()){
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
        }
        return isWifiConn;
    }

    public boolean moblieConnected(){
        boolean isMobileConn = false;
        for (Network network : connectivityManager.getAllNetworks()){
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
            System.out.println("netWork.type:"+networkInfo.getType());
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        return isMobileConn;
    }

}
