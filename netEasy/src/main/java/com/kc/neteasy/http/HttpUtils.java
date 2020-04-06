package com.kc.neteasy.http;

import android.content.Context;
import android.net.Network;
import android.widget.Toast;

import com.kc.neteasy.ICallBack.INetWork;
import com.kc.neteasy.ICallBack.IResult;
import com.kc.neteasy.network.NetWorkAvailable;
import com.kc.neteasy.network.NetWorkUtils;
import com.kc.neteasy.view.ProgressUtils;
import com.maning.mndialoglibrary.config.MDialogConfig;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Handler;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HttpUtils {

    private static HttpUtils httpUtils;
    private static boolean showProgressOrToast;

    public static synchronized HttpUtils getInstance(boolean showProgress) {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        showProgressOrToast = showProgress;
        return httpUtils;
    }

    private Network network;
    private String result = "";

    public void getJsonStrFromUrl(Context context, String urlTxt, IResult response) {
        boolean mobileAva = NetWorkAvailable.getInstance(context).moblieConnected();
        boolean wifiAva = NetWorkAvailable.getInstance(context).wifiConnected();
        System.out.println("mobileAva:" + mobileAva + ",wifiAva:" + wifiAva);
        if (!wifiAva && !mobileAva) {
            ProgressUtils.getInstance(context).showMT("移动网络不可用！");
            return;
        }
        ProgressUtils.getInstance(context).showPD(showProgressOrToast,"查询数据中");
        NetWorkUtils.getInstance(context).getNetWork(new INetWork() {
            @Override
            public void onNetworkBack(Network network) {
                if (network == null) {
                    ProgressUtils.getInstance(context).showMT("移动网络不可用！");
                    ProgressUtils.getInstance(context).closePD();
                    return;
                }
                Observable.just(network)
                        .map(new Function<Network, String>() {
                            @Override
                            public String apply(Network network) throws Throwable {
                                return getJsonStrFromUrl(network, urlTxt);
                            }
                        }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String s) {
                                response.onResponse(s);
                                ProgressUtils.getInstance(context).closePD();
                            }

                            @Override
                            public void onError(Throwable e) {
                                ProgressUtils.getInstance(context).showMT("查询出错！");
                                ProgressUtils.getInstance(context).closePD();
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
            }
        });
    }

    public void wirteJsonStrToUrl(Context context, String urlTxt, String jsonStr, IResult response) {
        boolean mobileAva = NetWorkAvailable.getInstance(context).moblieConnected();
        boolean wifiAva = NetWorkAvailable.getInstance(context).wifiConnected();
        System.out.println("wifiAva:" + wifiAva);
        if (!mobileAva && !wifiAva) {
            ProgressUtils.getInstance(context).showMT("移动网络不可用！");
            return;
        }

        ProgressUtils.getInstance(context).showPD(showProgressOrToast,"上传数据中");
        NetWorkUtils.getInstance(context).getNetWork(new INetWork() {
            @Override
            public void onNetworkBack(Network network) {
                if (network == null) {
                    ProgressUtils.getInstance(context).showMT("移动网络不可用！");
                    ProgressUtils.getInstance(context).closePD();
                    return;
                }
                Observable.just(network)
                        .map(new Function<Network, String>() {
                            @Override
                            public String apply(Network network) throws Throwable {
                                return wirteJsonStr(network, jsonStr, urlTxt);
                            }
                        }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String s) {
                                response.onResponse(s);
                                ProgressUtils.getInstance(context).closePD();
                            }

                            @Override
                            public void onError(Throwable e) {
                                ProgressUtils.getInstance(context).showMT("上传出错！");
                                ProgressUtils.getInstance(context).closePD();
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
            }
        });
    }

    public String getJsonStrFromUrl(Network network, String urlTxt) {
        URL url = null;
        String jsonStr = "";
        InputStream is = null;
        try {
            url = new URL(urlTxt);
            HttpURLConnection ct = (HttpURLConnection) network.openConnection(url);
            ct.setConnectTimeout(4000);
            ct.setRequestMethod("POST");
            is = ct.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            jsonStr = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonStr;
    }


    public String wirteJsonStr(Network network, String jsonStr, String urlTxt) {
        URL url = null;
        OutputStream os = null;
        DataOutputStream dos = null;
        String response = "";
        try {
            url = new URL(urlTxt);
            HttpURLConnection ct = (HttpURLConnection) network.openConnection(url);
            ct.setConnectTimeout(4000);
            ct.setRequestMethod("POST");
            os = ct.getOutputStream();
            dos = new DataOutputStream(os);
            dos.write(jsonStr.getBytes());
            dos.flush();

            int resultCode = ct.getResponseCode();
            if (resultCode == HttpURLConnection.HTTP_OK) {
                //  reportNow=false;
                BufferedReader bf = new BufferedReader(new InputStreamReader(ct.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bf.readLine()) != null) {
                    sb.append(line);
                }
                response = sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }


    //Retrofit模板代码 暂时留着
//    public void test(){
//        Retrofit retrofit = new Retrofit.Builder().baseUrl("").addConverterFactory(GsonConverterFactory.create())
//                .build();
//        IResultResponse iResponse = retrofit.create(IResultResponse.class);
//        Call<Data> call = iResponse.getData("");
//        call.enqueue(new Callback<Data>() {
//            @Override
//            public void onResponse(Call<Data> call, Response<Data> response) {
//            }
//
//            @Override
//            public void onFailure(Call<Data> call, Throwable t) {
//            }
//        });
//    }

}
