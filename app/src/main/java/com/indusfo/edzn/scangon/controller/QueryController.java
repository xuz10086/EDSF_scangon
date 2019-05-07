package com.indusfo.edzn.scangon.controller;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.cons.AppParams;
import com.indusfo.edzn.scangon.cons.IdiyMessage;
import com.indusfo.edzn.scangon.cons.NetworkConst;
import com.indusfo.edzn.scangon.utils.CookieUtil;
import com.indusfo.edzn.scangon.utils.NetworkUtil;

import java.io.File;
import java.util.HashMap;

public class QueryController extends BaseController {

    // 获取Cookie在本地的缓存位置
    private File cookieFile = new File(AppParams.COOKIE_FILE_DIR, AppParams.COOKIE_FILE_NAME);
    private String cookie = CookieUtil.getCookie(cookieFile);

    public QueryController(Context c) {
        super(c);
    }

    @Override
    protected void handleMessage(int action, Object... values) {

        RResult rResult;
        String pageindex = (String)values[0];
        String pagesize = (String)values[1];
        String condition = (String)values[2];

        switch (action) {
            case IdiyMessage.QUERY_MATERIALS:
                // 查询物料编码
                rResult = queryMaterialsPost(NetworkConst.QUERY_MATERIALS_URL, pageindex, pagesize, condition);
                mListener.onModeChange(IdiyMessage.QUERY_MATERIALS_RESULT, rResult);
                break;
            case IdiyMessage.QUERY_EQUIPMENT:
                // 查询设备编码
                rResult = queryEquipmentPost(NetworkConst.QUERY_EQUIPMENT_URL, pageindex, pagesize, condition);
                mListener.onModeChange(IdiyMessage.QUERY_EQUIPMENT_RESULT, rResult);
                break;
            case IdiyMessage.QUERY_VER:
                // 查询版本号
                rResult = queryVerPost(NetworkConst.QUERY_VER_URL, pageindex, pagesize, condition, (String)values[3], (String)values[4]);
                mListener.onModeChange(IdiyMessage.QUERY_VER_RESULT, rResult);
                break;
//            case IdiyMessage.CAPTURE_MATERIALS:
//                // 数据发往后端进行校验
//                checkMaterialsCode(NetworkConst.CHECK_MATERIALS_CODE, (String)values[0], (String)values[1]);

//                rResult = checkMaterialsCode(NetworkConst.QUERY_EQUIPMENT_URL, pageindex, pagesize, condition);
//                mListener.onModeChange(IdiyMessage.QUERY_EQUIPMENT_RESULT, rResult);
//                break;
            default:
                break;
        }
    }

    /**
     * POST请求，查询版本
     *
     * @author xuz
     * @date 2019/1/9 1:20 PM
     * @param [queryEquipmentUrl, pageindex, pagesize, condition, value, value1]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
    private RResult queryVerPost(String url, String pageindex, String pagesize, String vcMaterialsVerName, String materialsId, String deviceId) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("pageindex", pageindex);
        params.put("pagesize", pagesize);
        params.put("materialsId", materialsId);
        params.put("deviceId", deviceId);
        if (!vcMaterialsVerName.isEmpty()) {
            params.put("vcMaterialsVerName", vcMaterialsVerName);
        }
//        String json = NetworkUtil.doPost(url, params);
        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }

    /**
     * 发送POST请求，模糊查询物料编码
     *
     * @author xuz
     * @date 2019/1/7 8:55 AM
     * @param [queryMaterialsCode]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
    private RResult queryMaterialsPost(String url, String pageindex, String pagesize, String vcMaterialsCode) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("pageindex", pageindex);
        params.put("pagesize", pagesize);
        if (!vcMaterialsCode.isEmpty()) {
            params.put("vcMaterialsCode", vcMaterialsCode);
        }
//        String json = NetworkUtil.doPost(url, params);
        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }

    /**
     * 发送POST请求，模糊查询设备
     *
     * @author xuz
     * @date 2019/1/7 2:11 PM
     * @param [url]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
    private RResult queryEquipmentPost(String url, String pageindex, String pagesize, String vcDeviceCode) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("pageindex", pageindex);
        params.put("pagesize", pagesize);
        if (!vcDeviceCode.isEmpty()) {
            params.put("vcDeviceCode", vcDeviceCode);
        }
//        String json = NetworkUtil.doPost(url, params);
        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }

    /**
     * 发送POST请求，校验物料编码
     *  
     * @author xuz
     * @date 2019/1/8 11:09 AM
     * @param [queryEquipmentUrl, materialCode]
     * @return com.indusfo.edzn.scangon.bean.RResult
     *//*
    private RResult checkMaterialsCode(String url, String codeResult, String taskId) {

        HashMap<String, String> params = new HashMap<String, String>();
        // 扫描结果进行分割
        String[] codeResultList = codeResult.split(";");

        // 请求参数封装
        params.put("vcMaterialsCode", codeResultList[0]);
        params.put("vcSeatCode", codeResultList[1]);
        params.put("vcMaterialsBatch", codeResultList[2]);
        params.put("lTaskId", taskId);

        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }*/
}
