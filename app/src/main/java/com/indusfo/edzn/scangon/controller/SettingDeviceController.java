package com.indusfo.edzn.scangon.controller;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.indusfo.edzn.scangon.bean.Device;
import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.cons.AppParams;
import com.indusfo.edzn.scangon.cons.IdiyMessage;
import com.indusfo.edzn.scangon.cons.NetworkConst;
import com.indusfo.edzn.scangon.db.DeviceDb;
import com.indusfo.edzn.scangon.utils.CookieUtil;
import com.indusfo.edzn.scangon.utils.NetworkUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SettingDeviceController extends BaseController {

    // 获取Cookie在本地的缓存位置
    private File cookieFile = new File(AppParams.COOKIE_FILE_DIR, AppParams.COOKIE_FILE_NAME);
    private String cookie = CookieUtil.getCookie(cookieFile);
    private DeviceDb deviceDb;

    public SettingDeviceController(Context c) {
        super(c);
        deviceDb = new DeviceDb(mContext);
    }

    @Override
    protected void handleMessage(int action, Object... values) {
        RResult rResult;
        switch (action) {
            case IdiyMessage.QUERY_EQUIPMENT:
                // 查询设备编码
                rResult = queryEquipmentPost(NetworkConst.QUERY_EQUIPMENT_URL2);
                mListener.onModeChange(IdiyMessage.QUERY_EQUIPMENT_RESULT, rResult);
                break;
            case IdiyMessage.SAVE_DEVICE_TODB:
                boolean saveDeviceToDb = saveDeviceToDb((List) values[0]);
                mListener.onModeChange(IdiyMessage.SAVE_DEVICE_TODB_RESULT, saveDeviceToDb);
                break;
            case IdiyMessage.GET_DEVICE_FROM_DB:
                List<Device> deviceList = getDeviceFromDb();
                mListener.onModeChange(IdiyMessage.GET_DEVICE_FROM_DB_RESULT, deviceList);
                break;
            default:
                break;
        }
    }

    private List<Device> getDeviceFromDb() {

        return deviceDb.queryDevice();

    }

    /**
     * 保存设备信息
     *
     * @author xuz
     * @date 2019/1/23 3:57 PM
     * @param [deviceList]
     * @return boolean
     */
    private boolean saveDeviceToDb(List deviceList) {
        // 清空设备表数据
        deviceDb.clearDevice();
        for (Iterator iterator = deviceList.iterator(); iterator.hasNext();) {
            Device device = (Device) iterator.next();
            boolean flag = deviceDb.saveDevice(device);
            if (!flag) {
                return false;
            }
        }
        return true;
    }

    /**
     * 发送POST请求，查询设备
     *
     * @author xuz
     * @date 2019/1/7 2:11 PM
     * @param [url]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
    private RResult queryEquipmentPost(String url) {
        HashMap<String, String> params = new HashMap<String, String>();

        String json = NetworkUtil.doPost(url, params);
        return JSON.parseObject(json, RResult.class);
    }
}
