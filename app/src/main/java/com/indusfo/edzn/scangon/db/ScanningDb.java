package com.indusfo.edzn.scangon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.indusfo.edzn.scangon.bean.Scanning;
import com.indusfo.edzn.scangon.cons.AppParams;

public class ScanningDb {

    private DbOpenHelper scanningHelper;

    public ScanningDb(Context c) {
        scanningHelper = new DbOpenHelper(c);
    }

    // 保存操作
    public boolean saveScanning(Scanning scanning) {

        SQLiteDatabase db = scanningHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppParams.QR, scanning.getQr());
        values.put(AppParams.VC_MATERIALS_CODE, scanning.getVcMaterialsCode());
        values.put(AppParams.VC_SEAT_CODE, scanning.getVcSeatCode());
        values.put(AppParams.L_VALID, scanning.getlValid());
        values.put(AppParams.VC_MATERIALS_BATCH, scanning.getVcMaterialsBatch());
        values.put(AppParams.IF_ROW, scanning.getIfRow());
        long insertId = db.insert(AppParams.SCANNING_TABLE_NAME, null, values);
        return insertId != -1;

    }

    // 清空数据库表
    public void clearScanning() {
        SQLiteDatabase db = scanningHelper.getWritableDatabase();
        db.delete(AppParams.SCANNING_TABLE_NAME, null, null);
    }

    // 查询操作
    public Scanning queryScanning() {
        SQLiteDatabase db = scanningHelper.getWritableDatabase();
        Cursor cursor = db.query(AppParams.SCANNING_TABLE_NAME,
                new String[]{AppParams.QR,AppParams.VC_MATERIALS_CODE, AppParams.VC_SEAT_CODE, AppParams.VC_MATERIALS_BATCH,
                        AppParams.D_SCANNING_TIME, AppParams.L_VALID, AppParams.IF_ROW},
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            Scanning scanning = new Scanning();
            scanning.setQr(cursor.getString(0));
            scanning.setVcMaterialsCode(cursor.getString(1));
            scanning.setVcSeatCode(cursor.getString(2));
            scanning.setVcMaterialsBatch(cursor.getString(3));
            scanning.setdScanningTime(cursor.getString(4));
            scanning.setlValid(cursor.getInt(5));
            scanning.setIfRow(cursor.getInt(6));
            return scanning;
        }
        return null;
    }
}
