package com.indusfo.edzn.scangon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.indusfo.edzn.scangon.bean.Device;
import com.indusfo.edzn.scangon.cons.AppParams;

import java.util.ArrayList;
import java.util.List;

public class DeviceDb {

    private DbOpenHelper deviceHelper;

    public DeviceDb(Context c) {
        deviceHelper = new DbOpenHelper(c);
    }

    // 保存操作
    public boolean saveDevice(Device device) {

        SQLiteDatabase db = deviceHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppParams.L_DEVICE_ID, device.getlDeviceId());
        values.put(AppParams.VC_DEVICE_CODE, device.getVcDeviceCode());
        values.put(AppParams.VC_DEVICE_NAME, device.getVcDeviceName());
        values.put(AppParams.VC_DEVICE_MODE, device.getVcDeviceModel());
        values.put(AppParams.IF_CHECKED, device.getIfChecked());
        long insertId = db.insert(AppParams.DEVICE_TABLE_NAME, null, values);
        return insertId != -1;

    }

    // 清空数据库表
    public void clearDevice() {
        SQLiteDatabase db = deviceHelper.getWritableDatabase();
        db.delete(AppParams.DEVICE_TABLE_NAME, null, null);
    }

    // 查询操作
    public List<Device> queryDevice() {
        SQLiteDatabase db = deviceHelper.getWritableDatabase();
        List<Device> deviceList = new ArrayList<Device>();
        Cursor cursor = db.query(AppParams.DEVICE_TABLE_NAME,
                new String[]{AppParams.L_DEVICE_ID, AppParams.VC_DEVICE_CODE,
                        AppParams.VC_DEVICE_NAME, AppParams.VC_DEVICE_MODE, AppParams.IF_CHECKED},
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Device device = new Device();
            device.setlDeviceId(cursor.getString(0));
            device.setVcDeviceCode(cursor.getString(1));
            device.setVcDeviceName(cursor.getString(2));
            device.setVcDeviceModel(cursor.getString(3));
            device.setIfChecked(cursor.getString(4));

            deviceList.add(device);
        }
        return deviceList;
    }
}
