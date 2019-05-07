package com.indusfo.edzn.scangon.controller;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.bean.Scanning;
import com.indusfo.edzn.scangon.cons.AppParams;
import com.indusfo.edzn.scangon.cons.IdiyMessage;
import com.indusfo.edzn.scangon.cons.NetworkConst;
import com.indusfo.edzn.scangon.db.ScanningDb;
import com.indusfo.edzn.scangon.utils.CookieUtil;
import com.indusfo.edzn.scangon.utils.NetworkUtil;
import java.io.File;
import java.util.HashMap;

public class ScanTaskController extends BaseController {

    ScanningDb scanningDb;
    private File cookieFile = new File(AppParams.COOKIE_FILE_DIR, AppParams.COOKIE_FILE_NAME);
    private String cookie = CookieUtil.getCookie(cookieFile);

    public ScanTaskController(Context c) {
        super(c);
        scanningDb = new ScanningDb(c);
    }

    @Override
    protected void handleMessage(int action, Object... values) {
        RResult rResult;
        Scanning scanning;
        switch (action) {
            case IdiyMessage.CREATE_TASK:
                String materialsId = (String)values[0];
                String deviceId = (String)values[1];
                String verId = (String)values[2];
                String lDataState = (String) values[3];
                String taskNumber2 = (String) values[4];
                // 查询物料编码
                rResult = createTaskPost(NetworkConst.CREATE_TASK_URL, materialsId, deviceId, verId, lDataState, taskNumber2);
                mListener.onModeChange(IdiyMessage.CREATE_TASK_RESULT, rResult);
                break;
/*            case IdiyMessage.CAPTURE_MATERIALS:
                // 数据发往后端进行校验
                rResult = checkMaterialsCode(NetworkConst.CHECK_MATERIALS_CODE_URL, (String)values[0], (String)values[1]);
                scanning = saveScanning(rResult, IdiyMessage.CAPTURE_MATERIALS_RESULT);
                mListener.onModeChange(IdiyMessage.CAPTURE_MATERIALS_RESULT, scanning);
                break;
            case IdiyMessage.CAPTURE_MATERIALS_SET:
                rResult = checkMaterialsSetCode(NetworkConst.CHECK_MATERIALS_CODE_URL, (String)values[0], (String)values[1]);
                scanning = saveScanning(rResult, IdiyMessage.CAPTURE_MATERIALS_SET_RESULT);
                mListener.onModeChange(IdiyMessage.CAPTURE_MATERIALS_SET_RESULT, scanning);
                break;*/
            case IdiyMessage.QR_CODE:
                rResult = checkQrCode(NetworkConst.CHECK_MATERIALS_CODE_URL, (String)values[0], (String)values[1]);
                scanning = saveScanning(rResult);
                mListener.onModeChange(IdiyMessage.QR_CODE_RESULT, scanning);
                break;
            case IdiyMessage.QUERY_SCANNING:
                rResult = queryScanningPost(NetworkConst.QUERY_SCANNING_URL, (String)values[0]);
                // 将查询到的最后一条数据保存到数据库中
                saveScanningSingle(rResult);
                mListener.onModeChange(IdiyMessage.QUERY_SCANNING_RESULT, rResult);
                break;
            case IdiyMessage.UPDATE_TASK:
                rResult = updateScanTask(NetworkConst.UPDATE_TASK_URL, (String) values[0], (String) values[1]);
                mListener.onModeChange(IdiyMessage.UPDATE_TASK_RESULT, rResult);
                break;
            default:
                break;
        }
    }

    /**
     * 更新任务单
     *  
     * @author xuz
     * @date 2019/1/24 2:50 PM
     * @param [url, lTaskId, vcTaskNumber]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
    private RResult updateScanTask(String url, String lTaskId, String vcTaskNumber) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lTaskId", lTaskId);
        params.put("vcTaskNumber", vcTaskNumber);

        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }

    /**
     * 新建任务单
     *
     * @author xuz
     * @date 2019/1/11 2:19 PM
     * @param [url, materialsId, deviceId, verId]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
    private RResult createTaskPost(String url, String materialsId, String deviceId, String verId, String lDataState, String taskNumber2) {


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lMaterialsId", materialsId);
        params.put("lDeviceId", deviceId);
        params.put("lMaterialsVerId", verId);
        params.put("lDataState", lDataState);
        params.put("vcTaskNumber", taskNumber2);

        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }

    /**
     * POST请求，校验物料，料位编码
     *
     * @author xuz
     * @date 2019/1/18 1:43 PM
     * @param [url, codeResult, taskId]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
    private RResult checkQrCode(String url, String qrCode, String taskId) {

        HashMap<String, String> params = new HashMap<String, String>();
        // 请求参数封装
        params.put("qr", qrCode);
        params.put("lTaskId", taskId);

        // 查询数据表
        Scanning scanning = queryScanning();
        String vcMaterialsCode = null;
        String vcSeatCode = null;
        String vcMaterialsBatch = null;
        try {
            vcMaterialsBatch = scanning.getVcMaterialsBatch();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            vcMaterialsCode = scanning.getVcMaterialsCode();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            vcSeatCode = scanning.getVcSeatCode();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            // 如果上条数据是正确的，则补全所有的数据，发送给服务器
            if (scanning.getlValid() != null && scanning.getlValid() != 0) {
                if (vcMaterialsCode != null && vcSeatCode != null) {
                    // 如果数据库中所有信息是全的，则不补全
                } else {
                    if (vcSeatCode != null) {
                        params.put("vcSeatCode", scanning.getVcSeatCode());
                    }
                    if (vcMaterialsBatch != null && vcMaterialsCode != null) {
                        params.put("vcMaterialsBatch", scanning.getVcMaterialsBatch());
                        params.put("vcMaterialsCode", scanning.getVcMaterialsCode());
                    }
                }

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }

    /**
     * 查询扫描记录表
     *  
     * @author xuz
     * @date 2019/1/16 10:26 AM
     * @param []
     * @return com.indusfo.edzn.scangon.bean.Scanning
     */
    private Scanning queryScanning() {

        Scanning scanning = scanningDb.queryScanning();
        if (scanning != null) {
            return scanning;
        }
        return null;
    }

    /**
     * 保存扫描记录表
     *  
     * @author xuz
     * @date 2019/1/16 10:26 AM
     * @param [rResult]
     * @return void
     */
    private Scanning saveScanning(RResult rResult) {

        // 获取上次表格中的扫描结果
        Integer ifRow = null;
        Scanning scanning;

        // 如果网络请求超时，这里为了防止NullPointException
        if (null == rResult) {
            return null;
        }

        if (rResult.getData() != null) {
            scanning = JSON.parseObject(rResult.getData(), Scanning.class);
        } else {
            scanning = new Scanning();
        }

        // 如果扫码信息有误，则不保存
        if (!"200".equals(rResult.getStatus())) {
            scanning.setErro(rResult.getMsg());
            scanning.setStatus(rResult.getStatus());
            return scanning;
        }

        Scanning sc = new Scanning();
        sc = scanningDb.queryScanning();

        Integer lCounts = null;
        String vcMaterialsCode = null;
        String vcSeatCode = null;
        String scVcMaterialsCode = null;
        String scVcSeatCode = null;
        Integer sclValid = null;

        try {
            vcSeatCode = scanning.getVcSeatCode();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            vcMaterialsCode = scanning.getVcMaterialsCode();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            lCounts = rResult.getlCounts();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            scVcMaterialsCode = sc.getVcMaterialsCode();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            scVcSeatCode = sc.getVcSeatCode();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            sclValid = sc.getlValid();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            // 状态码为200则封装数据，并保存
            if ("200".equals(rResult.getStatus())) {

                if (vcMaterialsCode != null && vcSeatCode != null) {
                    ifRow = 0;
                }
                if (vcMaterialsCode != null && vcSeatCode == null) {
                    ifRow = 1;
                    if (sclValid!=null && sclValid == 0) {
                        ifRow = 0;
                    }
                    if (scVcMaterialsCode != null && scVcSeatCode == null) {
                        ifRow = 0;
                    }

                }
                if (vcMaterialsCode == null && vcSeatCode != null) {
                    ifRow = 1;
                    if (sclValid!=null && sclValid == 0) {
                        ifRow = 0;
                    }
                    if (scVcSeatCode != null && scVcMaterialsCode == null) {
                        ifRow = 0;
                    }

                }

                scanning.setIfRow(ifRow);
                // 清空表格
                scanningDb.clearScanning();
                // 保存新的数据
                scanningDb.saveScanning(scanning);
            }

            if (rResult.getStatus() != null) {
                scanning.setStatus(rResult.getStatus());
            }
            if (rResult.getMsg() != null) {
                scanning.setErro(rResult.getMsg());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return scanning;

    }

    /**
     * 保存最后一条
     *
     * @author xuz
     * @date 2019/1/18 6:57 PM
     * @param [rResult]
     * @return void
     */
    private void saveScanningSingle(RResult rResult) {

        scanningDb.clearScanning();

        /*if ("200".equals(rResult.getStatus())) {
            List<Scanning> scanningList = JSON.parseArray(rResult.getData(), Scanning.class);
            Scanning scanning = scanningList.get(scanningList.size()-1);
            scanning.setIfRow(0);
            scanningDb.saveScanning(scanning);
        }*/
    }

    /**
     * POST请求，查询任务单下的扫描记录
     *
     * @author xuz
     * @date 2019/1/17 10:24 AM
     * @param [url, lTaskId]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
    private RResult queryScanningPost(String url, String lTaskId) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lTaskId", lTaskId);

        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }

    /**
     * 发送POST请求，校验物料编码
     *
     * @author xuz
     * @date 2019/1/8 11:09 AM
     * @param [url, codeResult, taskId]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
 /*   private RResult checkMaterialsCode(String url, String codeResult, String taskId) {

        HashMap<String, String> params = new HashMap<String, String>();
        // 扫描结果进行分割
        String vcMaterialsBatch = codeResult;
        String vcMaterialsCode = codeResult;

        // 请求参数封装
        params.put("vcMaterialsCode", vcMaterialsCode);
//        params.put("vcSeatCode", codeResultList[1]);
        params.put("vcMaterialsBatch", vcMaterialsBatch);
        params.put("lTaskId", taskId);
        // 查询数据表
        Scanning scanning = queryScanning();

        try {
            if (scanning.getVcMaterialsCode() == null && scanning.getlValid() != 0) {
                if (scanning.getVcSeatCode() != null) {
                    params.put("vcSeatCode", scanning.getVcSeatCode());
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }*/

    /**
     * 发送POST请求，校验料位编码
     *
     * @author xuz
     * @date 2019/1/15 3:14 PM
     * @param [url, codeResult, taskId]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
   /* private RResult checkMaterialsSetCode(String url, String codeResult, String taskId) {

        HashMap<String, String> params = new HashMap<String, String>();
        // 扫描结果进行分割
        String vcSeatCode = codeResult;
        String vcMaterialsCode = codeResult.substring(7);

        // 请求参数封装
//        params.put("vcMaterialsCode", vcMaterialsCode);
        params.put("vcSeatCode", codeResult);
//        params.put("vcMaterialsBatch", vcMaterialsBatch);
        params.put("lTaskId", taskId);
        // 查询数据表
        Scanning scanning = queryScanning();
        try {
            if (scanning.getVcSeatCode() == null && scanning.getlValid() != 0) {
                if (scanning.getVcMaterialsCode() != null) {
                    params.put("vcMaterialsCode", scanning.getVcMaterialsCode());
                }
                if (scanning.getVcMaterialsBatch() != null) {
                    params.put("vcMaterialsBatch", scanning.getVcMaterialsBatch());
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        String json = NetworkUtil.doPostSetCookie(url, params, cookie);
        return JSON.parseObject(json, RResult.class);
    }*/

    /**
     * 保存扫描记录表
     *
     * @author xuz
     * @date 2019/1/16 10:26 AM
     * @param [rResult]
     * @return void
     */
   /* private Scanning saveScanning(RResult rResult, Integer action) {

        // 获取上次表格中的扫描结果
        Integer ifRow = null;
        Scanning scanning = null;
        Scanning sc = null;
        try {
            sc = scanningDb.queryScanning();
            ifRow = sc.getlValid();
            // 如果重复扫描，则设置为不换行，覆盖
            switch (action) {
                case IdiyMessage.CAPTURE_MATERIALS_RESULT:
                    if (sc.getVcMaterialsCode() != null) {
                        ifRow = 0;
                    }
                    break;
                case IdiyMessage.CAPTURE_MATERIALS_SET_RESULT:
                    if (sc.getVcSeatCode() != null) {
                        ifRow = 0;
                    }
                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        // 清空表格
        scanningDb.clearScanning();

        if ("200".equals(rResult.getStatus())) {
            scanning = JSON.parseObject(rResult.getData(), Scanning.class);
            scanning.setIfRow(ifRow);
            scanning.setStatus(rResult.getStatus());

            scanningDb.saveScanning(scanning);
            return scanning;
        }
        // 如果响应不为200，封装错误消息
        scanning.setStatus(rResult.getStatus());
        scanning.setErro(rResult.getMsg());
        scanning.setNum(rResult.getlCounts());
        return scanning;
    }*/

}
