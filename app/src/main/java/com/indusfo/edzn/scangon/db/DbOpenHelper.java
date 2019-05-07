package com.indusfo.edzn.scangon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.indusfo.edzn.scangon.cons.AppParams;

/**
 * 数据库帮助类
 *  
 * @author xuz
 * @date 2019/1/11 9:21 AM
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    
    public DbOpenHelper(@Nullable Context context) {
        super(context, AppParams.DB_NAME, null, AppParams.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建用户表
        db.execSQL("CREATE TABLE "+AppParams.USER_TABLE_NAME+"("+AppParams.USER_TABLE_NAME+" integer PRIMARY KEY AUTOINCREMENT, "+
                AppParams.USERNAME+" text, "+AppParams.PASSWORD+" text, "+AppParams.IFSAVE+" integer, "+AppParams.USERID+" text)");

        // 创建扫描结果表
        db.execSQL("CREATE TABLE "+AppParams.SCANNING_TABLE_NAME+"("+AppParams.SCANNING_ID+" integer PRIMARY KEY AUTOINCREMENT, "+
                AppParams.QR+" text, "+AppParams.VC_MATERIALS_CODE+" text, "+AppParams.VC_SEAT_CODE+" text, "+AppParams.VC_MATERIALS_BATCH+" text, "+
                AppParams.D_SCANNING_TIME+" text, "+AppParams.L_VALID+" integer, "+AppParams.IF_ROW+" Integer)");

        // 创建设备设置表
        db.execSQL("CREATE TABLE "+AppParams.DEVICE_TABLE_NAME+"("+AppParams._ID+" integer PRIMARY KEY AUTOINCREMENT, "+
                AppParams.L_DEVICE_ID+" text, "+AppParams.VC_DEVICE_CODE+" text, "+AppParams.VC_DEVICE_NAME+" text, "+AppParams.VC_DEVICE_MODE+" text, "+
                AppParams.IF_CHECKED+" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
