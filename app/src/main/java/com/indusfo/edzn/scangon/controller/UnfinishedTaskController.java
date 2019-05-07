package com.indusfo.edzn.scangon.controller;
import com.alibaba.fastjson.JSON;
import com.indusfo.edzn.scangon.bean.Device;
import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.cons.AppParams;
import com.indusfo.edzn.scangon.cons.IdiyMessage;
import com.indusfo.edzn.scangon.cons.NetworkConst;
import com.indusfo.edzn.scangon.db.DeviceDb;
import com.indusfo.edzn.scangon.utils.CookieUtil;
import com.indusfo.edzn.scangon.utils.NetworkUtil;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UnfinishedTaskController extends BaseController {

    private DeviceDb deviceDb;
    private File cookieFile = new File(AppParams.COOKIE_FILE_DIR, AppParams.COOKIE_FILE_NAME);
    private String cookie = CookieUtil.getCookie(cookieFile);

    public UnfinishedTaskController(Context c) {
        super(c);
        deviceDb = new DeviceDb(mContext);
    }

    @Override
    protected void handleMessage(int action, Object... values) {
        RResult rResult;
        switch (action) {
            // 查询请求
            case IdiyMessage.QUERY_UNFINISHED_TASK_SINGLE:
                rResult = queryUnfinshedTaskSingle(
                        NetworkConst.QUERY_UNFINISHED_TASK_URL, (String) values[0]);
                mListener.onModeChange(IdiyMessage.QUERY_UNFINISHED_TASK_SINGLE_RESULT, rResult);
                break;
            case IdiyMessage.QUERY_UNFINISHED_TASK:
                rResult = queryUnfinshedTask(NetworkConst.QUERY_UNFINISHED_TASK_URL, (String) values[0]);
                mListener.onModeChange(IdiyMessage.QUERY_UNFINISHED_TASK_RESULT, rResult);
                break;
            case IdiyMessage.CLOSE_TASK:
                rResult = closeTask(NetworkConst.CLOSE_TASK_URL, (String) values[0], (String) values[1]);
                mListener.onModeChange(IdiyMessage.CLOSE_TASK_RESULT, rResult);
                break;
            default:
                break;
        }
    }

    private RResult closeTask(String url, String lTaskId, String force) {

        HashMap<String, String> params = new HashMap<String, String>();
        if (!lTaskId.isEmpty()) {
            params.put("lTaskId", lTaskId);
        }
        if (!force.isEmpty()) {
            params.put("force", force);
        }

        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }


    /**
     * 发送POST请求，查询任务单
     * 有lTaskId为单条查询
     *
     * @author xuz
     * @date 2019/1/10 3:35 PM
     * @param [url, lTaskId, pageindex, pagesize]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
    private RResult queryUnfinshedTaskSingle(String url, String lTaskId) {
        HashMap<String, String> params = new HashMap<String, String>();
        if (!lTaskId.isEmpty()) {
            params.put("lTaskId", lTaskId);
        }

//        String json = NetworkUtil.doPost(url, params);
        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }

    /**
     * 查询任务单
     *
     * @author xuz
     * @date 2019/1/11 2:19 PM
     * @param [url, pageindex, pagesize]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
    private RResult queryUnfinshedTask(String url, String vcDeviceCode) {

        // 查询选中的设备ID
        List<Device> deviceList = deviceDb.queryDevice();
        String lDeviceIds = "";
        HashMap<String, String> params = new HashMap<String, String>();

        if (deviceList.size() > 0) {
            for (int i=0; i<deviceList.size(); i++) {
                lDeviceIds += "," + deviceList.get(i).getlDeviceId();
            }
            lDeviceIds = lDeviceIds.substring(1);
            params.put("lDeviceIds", lDeviceIds);
        }
        if (!vcDeviceCode.isEmpty()) {
            params.put("vcDeviceCode", vcDeviceCode);
        }
//        String json = NetworkUtil.doPost(url, params);
        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }
}
