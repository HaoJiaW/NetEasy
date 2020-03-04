package com.kc.neteasy.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Handler;
import android.renderscript.ScriptIntrinsicYuvToRGB;

import com.kc.neteasy.ICallBack.INetWork;
import com.kc.neteasy.ICallBack.IResult;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;

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

    public Network getNetWork() {
        connectivityManager.requestNetwork(build, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                mobileNetWork = network;
            }
        });
        System.out.println("当前获取的移动网络NetWork是否为空：" + (mobileNetWork == null));
        return mobileNetWork;
    };

    private Handler handler = new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if (!requestSuccess){
                if (response!=null){
                    response.onNetworkBack(null);
                }
            }
        }
    };

    private boolean requestSuccess = false;
    private INetWork response = null;
    public void getNetWork(INetWork response) {
        requestSuccess = false;
        this.response= response;
        handler.postDelayed(runnable, 5000);
        connectivityManager.requestNetwork(build, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                requestSuccess = true;
                response.onNetworkBack(network);
                System.out.println("onAvailable");
            }
        });
    }

    ;
}
