package com.indusfo.edzn.scangon.cons;

/**
 * URL常量类
 *  
 * @author xuz
 * @date 2019/1/11 9:21 AM
 */
public class NetworkConst {

    // 数据库存储数据状态，正常
    public static final String L_DATA_STATE = "1";

//    public static final String BASE_URL = "http://192.168.0.179:8180";
    public static final String BASE_URL = "http://10.10.1.5:9090";

    // 登陆
    public static final String LOGIN_URL = BASE_URL+"/service/doLogin";

    // 查询未完成的任务
    public static final String QUERY_UNFINISHED_TASK_URL = BASE_URL+"/service/pda/pdatask/queryPdaTask";

    // 查询物料编码
    public static final String QUERY_MATERIALS_URL = BASE_URL + "/service/pda/materials/queryMaterial";

    // 查询设备编码（模糊）
    public static final String QUERY_EQUIPMENT_URL = BASE_URL + "/service/pda/device/queryDeviceFuzzily";
    // 查询设备编码 （后台放行）
    public static final String QUERY_EQUIPMENT_URL2 = BASE_URL + "/service/pda/device/queryDevice";

    // 查询版本
    public static final String QUERY_VER_URL = BASE_URL + "/service/pda/materialsVer/queryMaterialsVer";

    // 新增任务单
    public static final String CREATE_TASK_URL = BASE_URL + "/service/pda/pdatask/insertPdaTask";

    // 二维码防错
    public static final String CHECK_MATERIALS_CODE_URL = BASE_URL + "/service/pda/scanning/yoke";
    // 查询扫码单的扫码记录
    public static final String QUERY_SCANNING_URL = BASE_URL + "/service/pda/scanning/queryPdaScanningByLTaskId";

    // 关闭任务
    public static final String CLOSE_TASK_URL = BASE_URL + "/service/pda/pdatask/closePdaTask";
    // 修改任务单
    public static final String UPDATE_TASK_URL = BASE_URL + "/service/pda/pdatask/updatePdaTask";

    // 更新App
    public static final String UPDATA_VERSION_REQ = BASE_URL + "/service/pda/scanApp/queryScanApp";
}
