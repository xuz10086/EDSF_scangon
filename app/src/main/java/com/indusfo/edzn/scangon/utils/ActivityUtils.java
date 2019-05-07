package com.indusfo.edzn.scangon.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.indusfo.edzn.scangon.R;
import com.indusfo.edzn.scangon.activity.BaseActivity;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * 用于跳转页面
 *  
 * @author xuz
 * @date 2019/1/4 10:59 AM
 */
public class ActivityUtils {

    /**
     * 跳转
     *
     * @author xuz
     * @date 2019/1/4 10:59 AM
     * @param [c, clazz, ifFinishSelf]
     * @return void
     */
    public static void start(Context c, Class<? extends BaseActivity> clazz, boolean ifFinishSelf) {
        Intent intent = new Intent(c, clazz);
        c.startActivity(intent);
        if (ifFinishSelf) {
            ((Activity) c).finish();
        }
    }

    public static void showDialog(Context c, String title, String message) {
        // 弹框
        AlertDialog alertDialog = new AlertDialog.Builder(c)
                .setTitle(title).setMessage(message).create();
        alertDialog.show();

    }

}
