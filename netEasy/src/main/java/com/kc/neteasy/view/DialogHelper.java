package com.kc.neteasy.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.kc.neteasy.wifi.WifiUtils;

import androidx.appcompat.app.AlertDialog;

public class DialogHelper {

    private static DialogHelper instance;

    public static DialogHelper getInstance() {
        if (instance == null){
            instance = new DialogHelper();
        }
        return instance;
    }

    private AlertDialog alertDialog;

    public void showWifiEnableDialog(Context context){
        if (alertDialog == null){
            alertDialog = new MaterialAlertDialogBuilder(context)
                    .setTitle("网络检测异常")
                    .setMessage("检测到WIFI不可用，是否启动WIFI")
                    .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }else {
                                WifiUtils.getInstance(context).setWifiEnable(context,true);
                                ProgressUtils.getInstance(context).showMT("打开中,请稍后...");
                            }
                        }
                    }).setNegativeButton("不了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
        }
        alertDialog.show();
    }


}
