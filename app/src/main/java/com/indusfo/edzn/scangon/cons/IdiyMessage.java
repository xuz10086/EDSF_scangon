package com.indusfo.edzn.scangon.cons;

/**
 * action标识类
 *  
 * @author xuz
 * @date 2019/1/11 9:21 AM
 * @param 
 * @return 
 */
public class IdiyMessage {

    // 登录action
    public static final int LOGIN_ACTION = 1;
    public static final int LOGIN_ACTION_RESULT = 2;

    // 未完成任务action
    public static final int QUERY_UNFINISHED_TASK = 3;
    public static final int QUERY_UNFINISHED_TASK_RESULT = 4;
    public static final int QUERY_UNFINISHED_TASK_SINGLE = 5;
    public static final int QUERY_UNFINISHED_TASK_SINGLE_RESULT = 6;

    // 物料编码action
    public static final int QUERY_MATERIALS = 7;
    public static final int QUERY_MATERIALS_RESULT = 8;

    // 设备编码action
    public static final int QUERY_EQUIPMENT = 9;
    public static final int QUERY_EQUIPMENT_RESULT = 10;

    // 版本action
    public static final int QUERY_VER = 11;
    public static final int QUERY_VER_RESULT = 12;

    // 二维码处理action
    public static final int CAPTURE_MATERIALS = 13;
    public static final int CAPTURE_MATERIALS_RESULT = 14;
    public static final int CAPTURE_MATERIALS_SET = 15;
    public static final int CAPTURE_MATERIALS_SET_RESULT = 16;

    // 新增任务单action
    public static final int CREATE_TASK = 17;
    public static final int CREATE_TASK_RESULT = 18;

    // 保存用户到数据库
    public static final int SAVE_USER_TODB = 19;
    public static final int SAVE_USER_TODB_RESULT = 20;
    public static final int GET_USER_FROM_DB = 21;
    public static final int GET_USER_FROM_DB_RESULT = 22;

    // 保存cookie到本地
    public static final int SAVE_COOKIE = 23;

    // 查询扫描记录action
    public static final int QUERY_SCANNING = 24;
    public static final int QUERY_SCANNING_RESULT = 25;

    // 二维码校验action
    public static final int QR_CODE = 26;
    public static final int QR_CODE_RESULT = 27;

    public static final int DATA_LISTENER = 30;
    public static final int DATA_LISTENER_REMOVE = 31;

    // 保存设备action
    public static final int SAVE_DEVICE_TODB = 32;
    public static final int SAVE_DEVICE_TODB_RESULT = 33;
    public static final int GET_DEVICE_FROM_DB = 34;
    public static final int GET_DEVICE_FROM_DB_RESULT = 35;

    // 关闭任务
    public static final int CLOSE_TASK = 36;
    public static final int CLOSE_TASK_RESULT = 37;

    // 修改扫码单
    public static final int UPDATE_TASK = 38;
    public static final int UPDATE_TASK_RESULT = 39;

    public static final int ERRO_CONNECT = 40;
}
