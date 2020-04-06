package com.kc.neteasy.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;

import com.kc.neteasy.ICallBack.INetWork;

public class NetWorkUtils {
    private ConnectivityManager connectivityManager;
    private NetworkRequest.Builder builder;
    private NetworkRequest build;

    public NetWorkUtils(Context context) {
        initConfig(context);
    }

    private void initConfig(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        builder = new NetworkRequest.Builder();
        builder.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        builder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
        build = builder.build();
    }

    private static NetWorkUtils instance;

    public static synchronized NetWorkUtils getInstance(Context context) {
        if (instance == null) {
            instance = new NetWorkUtils(context);
        }
        return instance;
    }
    private Network mobileNetWork = null;

    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!requestSuccess) {
                if (response != null) {
                    response.onNetworkBack(null);
                }
            }
        }
    };

    private boolean requestSuccess = false;
    private INetWork response = null;
    private Long lastRequestTime = 0L;

    public void getNetWork(INetWork response) {
        requestSuccess = false;
        this.response = response;
        handler.postDelayed(runnable, 5000);
        lastRequestTime = System.currentTimeMillis();
        connectivityManager.requestNetwork(build, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                if (System.currentTimeMillis() - lastRequestTime <= 5000) {
                    requestSuccess = true;
                    response.onNetworkBack(network);
                    System.out.println("onAvailable");
                }
            }
        });
    };
}
