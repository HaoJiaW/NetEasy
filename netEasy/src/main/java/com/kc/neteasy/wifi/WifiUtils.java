package com.kc.neteasy.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.kc.neteasy.bean.AppContants;
import com.kc.neteasy.bean.WifiBean;
import com.kc.neteasy.view.ProgressUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WifiUtils {

    private Context context;
    public WifiUtils(Context context){
        this.context = context;
    }


    private static WifiUtils instance;
    public static WifiUtils getInstance(Context context) {
        if (instance == null){
            instance = new WifiUtils(context);
        }
        return instance;
    }

    /**
     * 获取wifi列表然后将bean转成自己定义的WifiBean
     */
    public List<WifiBean> sortScaResult(){
        List<ScanResult> scanResults = noSameName(getWifiScanResult(context));
//        realWifiList.clear();
        List<WifiBean> realWifiList = new ArrayList<>();
        if(scanResults!=null && scanResults.size()>0){
            for(int i = 0;i < scanResults.size();i++){
                WifiBean wifiBean = new WifiBean();
                wifiBean.setWifiName(scanResults.get(i).SSID);
                wifiBean.setState(AppContants.WIFI_STATE_UNCONNECT);   //只要获取都假设设置成未连接，真正的状态都通过广播来确定
                wifiBean.setCapabilities(scanResults.get(i).capabilities);
                wifiBean.setLevel(getLevel(scanResults.get(i).level)+"");
                realWifiList.add(wifiBean);
//                realWifiList.add(wifiBean);
                //排序
                Collections.sort(realWifiList);
//                adapter.notifyDataSetChanged();
            }
        }
        return realWifiList;
    }

    public  List<ScanResult> getWifiScanResult(Context context) {
        boolean b = context == null;
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getScanResults();
    }

    public void connectAssignWifi(String wifiName){
        String currentWifiName = getWifiSSID(context);
        if (!currentWifiName.equals(wifiName)) {
            System.out.println("网络连接改变了，目前的wifi："+currentWifiName);
            WifiConfiguration tempConfig = isExsits(wifiName, context);
            if (tempConfig!=null){
                ProgressUtils.getInstance(context).showMT("网络连接改变了,尝试连接指定wifi");
                addNetWork(tempConfig, context);
            }else {
                ProgressUtils.getInstance(context).showMT("无法连接指定wifi");
            }
        }
    }

    //查看以前是否也配置过这个网络
    public static WifiConfiguration isExsits(String SSID, Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> existingConfigs = wifimanager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    public  String getWifiSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();

        //Android9.0获取不到wifi参数的解决方案
        int wifiId = wifiInfo.getNetworkId();
        List<WifiConfiguration> wifiConfigurationList = wifiManager.getConfiguredNetworks();
        if (wifiConfigurationList != null) {
            for (WifiConfiguration configuration : wifiConfigurationList) {
                if (configuration.networkId == wifiId) {
                    ssid = configuration.SSID;
                }
            }
        }

        if (ssid.startsWith("\"")) {
            ssid = ssid.substring(1);
        }
        if (ssid.endsWith("\"")) {
            ssid = ssid.substring(0, ssid.length() - 1);
        }

        return ssid;
    }

    /**
     * 接入某个wifi热点
     */
    public static boolean addNetWork(WifiConfiguration config, Context context) {

        WifiManager wifimanager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifiinfo = wifimanager.getConnectionInfo();

        if (null != wifiinfo) {
            wifimanager.disableNetwork(wifiinfo.getNetworkId());
        }

        boolean result = false;

        if (config.networkId > 0) {
            result = wifimanager.enableNetwork(config.networkId, true);
            wifimanager.updateNetwork(config);
        } else {

            int i = wifimanager.addNetwork(config);
            result = false;

            if (i > 0) {

                wifimanager.saveConfiguration();
                return wifimanager.enableNetwork(i, true);
            }
        }

        return result;

    }

    /**
     * 去除同名WIFI
     *
     * @param oldSr 需要去除同名的列表
     * @return 返回不包含同命的列表
     */
    public  List<ScanResult> noSameName(List<ScanResult> oldSr)
    {
        List<ScanResult> newSr = new ArrayList<ScanResult>();
        for (ScanResult result : oldSr)
        {
            if (!TextUtils.isEmpty(result.SSID) && !containName(newSr, result.SSID))
                newSr.add(result);
        }
        return newSr;
    }

    /**
     * 判断一个扫描结果中，是否包含了某个名称的WIFI
     * @param sr 扫描结果
     * @param name 要查询的名称
     * @return 返回true表示包含了该名称的WIFI，返回false表示不包含
     */
    public  boolean containName(List<ScanResult> sr, String name)
    {
        for (ScanResult result : sr)
        {
            if (!TextUtils.isEmpty(result.SSID) && result.SSID.equals(name))
                return true;
        }
        return false;
    }

    /**
     * 返回level 等级
     */
    public int getLevel(int level){
        if (Math.abs(level) < 50) {
            return 1;
        } else if (Math.abs(level) < 75) {
            return 2;
        } else if (Math.abs(level) < 90) {
            return 3;
        } else {
            return 4;
        }
    }



}