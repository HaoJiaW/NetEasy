package com.kc.neteasy.view;

import android.content.Context;

import com.maning.mndialoglibrary.MProgressDialog;
import com.maning.mndialoglibrary.MToast;
import com.maning.mndialoglibrary.config.MDialogConfig;
import com.maning.mndialoglibrary.config.MToastConfig;

public class ProgressUtils {
    //自定义背景
    private MDialogConfig mDialogConfig;
    private MToastConfig mToastConfig;
    private static ProgressUtils instance;
    private Context context;

    public ProgressUtils(Context context) {
        this.context = context;
        initView();
    }

    public synchronized static ProgressUtils getInstance(Context context) {
        if (instance == null) {
            instance = new ProgressUtils(context);
        }
        return instance;
    }

    private void initView() {
        mDialogConfig = new MDialogConfig.Builder()
                .isCanceledOnTouchOutside(false)
                .build();
        mToastConfig = new MToastConfig.Builder()
                .setGravity(MToastConfig.MToastGravity.CENTRE)
                .build();
    }

    public void showPD() {
        MProgressDialog.showProgress(context, "loading...", mDialogConfig);
    }

    public void showPD(String msg) {
        MProgressDialog.showProgress(context, msg, mDialogConfig);
    }

    public void showPD(boolean show, String msg) {
        MProgressDialog.showProgress(context, msg, mDialogConfig);
    }

    public void showMT(String msg) {
        MToast.makeTextShort(context, msg, mToastConfig);
    }


    public void closePD() {
        MProgressDialog.dismissProgress();
    }


}
