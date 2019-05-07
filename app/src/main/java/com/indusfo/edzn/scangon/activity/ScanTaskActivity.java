package com.indusfo.edzn.scangon.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.indusfo.edzn.scangon.R;
import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.bean.Scanning;
import com.indusfo.edzn.scangon.bean.Task;
import com.indusfo.edzn.scangon.cons.IdiyMessage;
import com.indusfo.edzn.scangon.cons.NetworkConst;
import com.indusfo.edzn.scangon.controller.QueryController;
import com.indusfo.edzn.scangon.controller.ScanTaskController;
import com.indusfo.edzn.scangon.controller.UnfinishedTaskController;
import com.indusfo.edzn.scangon.utils.ActivityUtils;
import com.indusfo.edzn.scangon.utils.TopBar;
import com.symbol.scanning.BarcodeManager;
import com.symbol.scanning.ScannerInfo;
import com.symbol.scanning.Scanner;
import com.symbol.scanning.Scanner.DataListener;
import com.symbol.scanning.ScannerException;
import com.symbol.scanning.ScanDataCollection;
import com.symbol.scanning.ScanDataCollection.ScanData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ScanTaskActivity extends BaseActivity {

    private ImageView arrowsMaterialsCode;
    private ImageView arrowsEquipmentCode;
    private ImageView arrowsVer;
//    private ImageView qrCodeMaterials;
//    private ImageView qrCodeMaterialsSet;
    private TextView materialsCode;
    private TextView equipmentCode;
    private TextView verName;
    private TextView taskNumber;
//    private Button creatTask;
    private TableLayout tableLayout;
    private TextView scannResult;
    private EditText taskNumber2;
    private ScrollView scrollView;
    private TextView sumUsedUnused;
    private Button updateTask;
    private Drawable btShape3;
    private TopBar topBar;

    private ScanTaskController taskController;
    private UnfinishedTaskController unfinishedTaskController;

    // 标记，只有在新建任务单时，才能掉用二维码导入
    boolean newTaskFlag = true;
    // 标记，只有任务单号为空的时候才允许修改
    boolean updateTaskNumber = true;
    // 物料id
    private String materialsId = "";
    // 设备id
    private String deviceId = "";
    // 版本id
    private String verId = "";
    // 任务单id
    private String taskId = "";

    ///////////////////////////////////// SCAN ////////////////////////////////////////////////
    private String TAG = "EDSF_SCAN";
    private BarcodeManager mBarcodeManager = new BarcodeManager();
    private ScannerInfo mInfo =
            new ScannerInfo("se4710_cam_builtin", "DECODER_2D");
    private Scanner mScanner = mBarcodeManager.getDevice(mInfo);
//    private Scanner mScanner =
//            mBarcodeManager.getDevice(BarcodeManager.DeviceIdentifier.INTERNAL_CAMERA1);
    private List<ScannerInfo> scanInfoList = mBarcodeManager.getSupportedDevicesInfo();

//    private Scanner mScanner = new Scanner();
    private DataListener mDataListener;
    private boolean canDecode = true;
    ///////////////////////////////////// SCAN ////////////////////////////////////////////////

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case IdiyMessage.CREATE_TASK_RESULT:
                handleCreateTaskResult(msg);
                break;
            case IdiyMessage.QUERY_UNFINISHED_TASK_SINGLE_RESULT:
                handleQueryTaskResult(msg);
                break;
 /*           case IdiyMessage.CAPTURE_MATERIALS_RESULT:
                handleCheckMaterialsCode(msg);
                break;
            case IdiyMessage.CAPTURE_MATERIALS_SET_RESULT:
                handleCheckMaterialsCode(msg);
                break;*/
            case IdiyMessage.QR_CODE_RESULT:
                handleCheckQrCode(msg);
                break;
            case IdiyMessage.QUERY_SCANNING_RESULT:
                handleQueryScannings(msg);
                break;
            case IdiyMessage.DATA_LISTENER:
                setDecodeListener();
                break;
            case IdiyMessage.DATA_LISTENER_REMOVE:
                mScanner.removeDataListener(mDataListener);
                break;
            case IdiyMessage.UPDATE_TASK_RESULT:
                handleUpdateTaskResult(msg);
                break;
            default:
                break;
        }
    }

    private void handleUpdateTaskResult(Message msg) {

        RResult rResult = (RResult) msg.obj;
        if (null == rResult || null == rResult.getStatus()) {
            tip("网络连接错误");
            return;
        }
        if ("200".equals(rResult.getStatus())) {
            tip("任务单关联成功");
            updateTaskNumber = false;
            updateTask.setBackgroundDrawable(btShape3);
            taskNumber2.setEnabled(false);
            taskNumber2.setFocusable(false);
            taskNumber2.setKeyListener(null);
        }
        if (!rResult.isOk()) {
            ActivityUtils.showDialog(this, "任务单关联错误", rResult.getMsg());
        }
    }

    // 查询扫码单下的扫码记录
    private void handleQueryScannings(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult || null == rResult.getStatus()) {
            tip("网络连接错误");
            return;
        }
        try {
            if (!"200".equals(rResult.getStatus())) {
                tip(rResult.getMsg());
            }

            Scanning scanning = null;
            List<Scanning> listScannings = JSON.parseArray(rResult.getData(), Scanning.class);
            for (Iterator<Scanning> iterator = listScannings.iterator();iterator.hasNext();) {
                scanning = iterator.next();
                // 生成表格
                createRow(scanning);
            }

            // 显示以扫描和为扫描的数目
            Integer seatSum = scanning.getSeatSum();
            Integer seatUsed = scanning.getSeatUsed();
            Integer seatUnused = scanning.getSeatUnused();
            String text = "当前物料" + seatSum +"个，已经扫描" + seatUsed + "还有" + seatUnused + "个待扫描";
            sumUsedUnused.setText(text);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private void handleCheckQrCode(Message msg) {
        Scanning scanning = (Scanning) msg.obj;
        if (null == scanning || null == scanning.getStatus()) {
            tip("网络连接错误");
            return;
        }
        try {
            if (!"200".equals(scanning.getStatus())) {
                // 弹框
                ActivityUtils.showDialog(this, "扫码错误", scanning.getErro());
                scannResult.setText(scanning.getQr());
                scannResult.setTextColor(Color.RED);
//                tip(scanning.getErro());
                return;
            }

            // 标记，用来判断物料编码和料位编码是否同时不为空
//            boolean flag = false;
//            if (scanning.getVcMaterialsCode()!=null && scanning.getVcSeatCode() != null) {
//                flag = true;
//            }
            // 根据ifRow来判断是否生成行，还是覆盖
            if (scanning.getIfRow()== 1) {
                // 生成表格
                createRow(scanning);
            }
            if(scanning.getIfRow() == 0) {
                // 覆盖数据
                coverRow(scanning);
            }
            if (scanning.getlValid() != 1) {
                // 弹框
                ActivityUtils.showDialog(this, "扫码错误", scanning.getErro());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


   /* private void handleCheckMaterialsSetCode(Message msg) {

        Scanning scanning = (Scanning) msg.obj;

        if (scanning.getErro().isEmpty()) {
            tip(scanning.getErro());
            return;
        }
    }

    private void handleCheckMaterialsCode(Message msg) {

        Scanning scanning = (Scanning) msg.obj;

        try {
            if (!"200".equals(scanning.getStatus())) {
                tip(scanning.getErro());
                return;
            }

            // 标记，用来判断物料编码和料位编码是否同时不为空
            boolean flag = false;
            if (scanning.getVcMaterialsCode()!=null && scanning.getVcSeatCode() != null) {
                flag = true;
            }
            // 根据ifRow来判断是否生成行，还是覆盖
            if (!flag && scanning.getIfRow()!=0) {
                // 生成表格
                createRow(scanning);
            } else if(scanning.getIfRow() != 1) {
                // 覆盖数据
                coverRow(scanning);
            } else if (flag) {
                coverRow(scanning);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
*/


    private void handleQueryTaskResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult || null == rResult.getStatus()) {
            tip("网络连接错误");
            return;
        }

        // 状态为200
        try {
            if ("200".equals(rResult.getStatus())) {
                List<Task> taskList = JSON.parseArray(rResult.getData(), Task.class);
                Task unfinishedTask = taskList.get(0);
                materialsCode.setText(unfinishedTask.getVcMaterialsCode());
                if (unfinishedTask.getlMaterialsId() != null) {
                    materialsId = unfinishedTask.getlMaterialsId();
                }
                equipmentCode.setText(unfinishedTask.getVcDeviceCode());
                if (unfinishedTask.getlDeviceId() != null) {
                    deviceId = unfinishedTask.getlDeviceId();
                }
                verName.setText(unfinishedTask.getVcMaterialsVerName());
                if (unfinishedTask.getlMaterialsVerId() != null) {
                    verId = unfinishedTask.getlMaterialsVerId();
                }
                taskNumber.setText(unfinishedTask.getVcScanNumber());
                taskId = unfinishedTask.getlTaskId();

                // 回显关联任务单号
                if (!unfinishedTask.getVcTaskNumber().isEmpty()) {
                    taskNumber2.setText(unfinishedTask.getVcTaskNumber());
                    // 不允许修改关联任务单号
                    updateTaskNumber = false;
                    updateTask.setBackgroundDrawable(btShape3);
                    taskNumber2.setEnabled(false);
                    taskNumber2.setFocusable(false);
                    taskNumber2.setKeyListener(null);
                }

                // 如果已经存在任务单号，则右边按钮不可见
                if (!taskNumber.getText().toString().isEmpty()) {
                    topBar.setRightButtonVisibility(false);
                }

            }
        } catch (NullPointerException e) {
            Log.w("空指针异常", "服务器端返回的数据存在null值");
        }

        if (!rResult.isOk() || rResult.getData().isEmpty()) {
            tip(rResult.getMsg());
        }
    }

    private void handleCreateTaskResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult || null == rResult.getStatus()) {
            tip("网络连接错误");
            return;
        }
        // 状态为200
        if ("200".equals(rResult.getStatus())) {
            Task task = JSON.parseObject(rResult.getData(), Task.class);
            taskNumber.setText(task.getVcScanNumber());
            taskId = task.getlTaskId();
//            tip("新建扫码单成功");
            ActivityUtils.showDialog(this, "创建扫码单", "创建扫码单成功");
//            creatTask.setBackgroundDrawable(btShape3);

            // 拉取物料料位表
            taskController.sendAsynMessage(IdiyMessage.QUERY_SCANNING, taskId);
        }

        if (!rResult.isOk() || rResult.getData().isEmpty()) {
//            tip(rResult.getMsg());
            ActivityUtils.showDialog(this, "新建扫码单失败！", rResult.getMsg());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); // 注意顺序
        setContentView(R.layout.activity_scan_task);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.title);

//        Log.e("onCreate", ".....................................");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    mScanner.enable();
//                } catch (ScannerException e) {
//                    e.printStackTrace();
//                }
//                Message message = new Message();
//                message.what = IdiyMessage.DATA_LISTENER;
//                mHandler.sendMessage(message);
//            }
//        }).start();

        initController();
        initUI();
    }

    private void initController() {
        mController = new QueryController(this);
        mController.setIModeChangeListener(this);

        taskController = new ScanTaskController(this);
        taskController.setIModeChangeListener(this);

        unfinishedTaskController = new UnfinishedTaskController(this);
        unfinishedTaskController.setIModeChangeListener(this);
    }

    private void initUI() {
        arrowsMaterialsCode = (ImageView) findViewById(R.id.right_arrows1);
        arrowsEquipmentCode = (ImageView) findViewById(R.id.right_arrows2);
        arrowsVer = (ImageView) findViewById(R.id.right_arrows3);
//        qrCodeMaterials = (ImageView) findViewById(R.id.qr_code_materials);
//        qrCodeMaterialsSet = (ImageView) findViewById(R.id.qr_code_materials_set);
        materialsCode = (TextView) findViewById(R.id.materials_code);
        equipmentCode = (TextView) findViewById(R.id.equipment_code);
        verName = (TextView) findViewById(R.id.ver_code);
//        creatTask = (Button) findViewById(R.id.creat_task);
        taskNumber = (TextView) findViewById(R.id.task_number);
        tableLayout = (TableLayout) findViewById(R.id.scanningTable);
        scannResult = (TextView) findViewById(R.id.scann_result);
        taskNumber2 = (EditText) findViewById(R.id.task_number_2);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        sumUsedUnused = (TextView) findViewById(R.id.sum_used_unUsed);
        updateTask = (Button) findViewById(R.id.update_task);
        btShape3 = getResources().getDrawable(R.drawable.bt_shape3);
        topBar = (TopBar) findViewById(R.id.scanTask_topbar);

        try {
            Intent intent = getIntent();
            taskId = intent.getStringExtra("lTaskId");
            if (taskId != null) {
                // 打开已有的任务单，不能使用二维码导入功能
                newTaskFlag = false;
                // 发送网络请求，查询表头信息
                unfinishedTaskController.sendAsynMessage(IdiyMessage.QUERY_UNFINISHED_TASK_SINGLE, taskId);
//                creatTask.setBackgroundDrawable(btShape3);

                // 发送网络请求，查询扫码记录
                taskController.sendAsynMessage(IdiyMessage.QUERY_SCANNING, taskId);
            }
        } catch (Exception e) {
            Log.w("空指针", "没影响");
        }

        // 扫描物料
      /*  qrCodeMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//* 相机：将adpter发往查询页面
                Intent intent = new Intent(ScanTaskActivity.this, CaptureActivity.class);
                startActivityForResult(intent, IdiyMessage.CAPTURE_MATERIALS);*//*
                whichCheck = IdiyMessage.CAPTURE_MATERIALS;
                scanningMaterialsOrSeat();
            }
        });*/

        // 扫描料位
       /* qrCodeMaterialsSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//* 相机：将adpter发往查询页面
                Intent intent = new Intent(ScanTaskActivity.this, CaptureActivity.class);
                startActivityForResult(intent, IdiyMessage.CAPTURE_MATERIALS_SET);*//*
                whichCheck = IdiyMessage.CAPTURE_MATERIALS_SET;
                scanningMaterialsOrSeat();
            }

        });*/


        topBar.setOnLeftAndRightClickListener(new TopBar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                finish();
            }

            @Override
            public void OnRightButtonClick() {
                if (!taskNumber.getText().toString().isEmpty()) {
//                    tip("请先处理当前扫码单");
//                    ActivityUtils.showDialog(ScanTaskActivity.this, "新建扫码单错误", "请先处理当前扫码单");
//                    // 当任务单存在的时候，修改关联任务单号
//                    taskController.sendAsynMessage(IdiyMessage.UPDATE_TASK, taskId, taskNumber2.getText().toString());
                    return;
                }
                if (materialsId.isEmpty() || deviceId.isEmpty() || verId.isEmpty()) {
                    tip("请先选择物料编码，设备编码，版本号");
                    return;
                }
                String taskStr = taskNumber2.getText().toString();
                // 发送物料编码，设备编码，版本号，生成任务单
                taskController.sendAsynMessage(IdiyMessage.CREATE_TASK,
                        materialsId, deviceId, verId, NetworkConst.L_DATA_STATE+"", taskStr);
            }
        });

        /*creatTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!taskNumber.getText().toString().isEmpty()) {
//                    tip("请先处理当前扫码单");
//                    ActivityUtils.showDialog(ScanTaskActivity.this, "新建扫码单错误", "请先处理当前扫码单");
//                    // 当任务单存在的时候，修改关联任务单号
//                    taskController.sendAsynMessage(IdiyMessage.UPDATE_TASK, taskId, taskNumber2.getText().toString());
                    return;
                }
                if (materialsId.isEmpty() || deviceId.isEmpty() || verId.isEmpty()) {
                    tip("请先选择物料编码，设备编码，版本号");
                    return;
                }
                String taskStr = taskNumber2.getText().toString();
                // 发送物料编码，设备编码，版本号，生成任务单
                taskController.sendAsynMessage(IdiyMessage.CREATE_TASK,
                        materialsId, deviceId, verId, NetworkConst.L_DATA_STATE+"", taskStr);
            }
        });*/

        updateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (taskNumber.getText().toString().isEmpty()) {
                    ActivityUtils.showDialog(ScanTaskActivity.this, "关联任务单号错误", "请先填写任务单号");
                    return;
                }
                if (updateTaskNumber && !taskNumber.getText().toString().isEmpty()) {
                    // 修改关联任务单号
                    taskController.sendAsynMessage(IdiyMessage.UPDATE_TASK, taskId, taskNumber2.getText().toString());
                }
            }
        });

    }


    ///////////////////////////////////// SCAN ////////////////////////////////////////////////
    private void scanningMaterialsOrSeat() {
        if(canDecode)
        {
            canDecode = false;
            try
            {
                mScanner.read();
                //test ScannerConfig.isParamSupported(String)
                boolean upcaEnabled = mScanner.getConfig().isParamSupported(
                        "mScannerConfig.decoderParams.upca.enabled");
                Log.d(TAG, "upcaEnabled isParamSupported - "+upcaEnabled);
                Log.d(TAG, "upcaEnabled = "+
                        mScanner.getConfig().decoderParams.upca.enabled);
                //test BarcodeManager.getSupportedDevicesInfo()
                if(!scanInfoList.isEmpty())
                {
                    Log.d(TAG, "scanInfoList is not empty");
                    for(ScannerInfo info : scanInfoList)
                    {
                        Log.d(TAG, "scanning supprot "+info.getDeviceType());
                    }
                }


            }
            catch (ScannerException se)
            {
                se.printStackTrace();
            }
        }
        else
        {
            canDecode = true;

            try
            {
                mScanner.cancelRead();
            }
            catch (ScannerException se)
            {
                se.printStackTrace();
            }
        }
    }


    public void setDecodeListener()
    {
        mDataListener =  new DataListener()
        {
            public void onData(ScanDataCollection scanDataCollection)
            {
                String data = "";
                ArrayList<ScanData> scanDataList = scanDataCollection.getScanData();

                for(ScanData scanData :scanDataList)
                {
                    data = scanData.getData();
                }

                if (!data.isEmpty()) {
                    // 新建任务单时才会调用
                    String s = materialsCode.getText().toString();
                    if (newTaskFlag) {
                        if (materialsCode.getText().toString().isEmpty()
                                || equipmentCode.getText().toString().isEmpty()
                                || verName.getText().toString().isEmpty()) {
                            // 这个时候扫的二维码必定为导入
                            String[] strs = data.split("/");
                            if (strs.length < 3) {
//                                tip("二维码错误，无法导出数据");
                                ActivityUtils.showDialog(ScanTaskActivity.this, "二维码错误", "二维码错误，无法导出数据");
                            }
                            // 设置产品编码和id
                            String[] s1 = strs[0].split(":");
                            materialsId = s1[0];
                            materialsCode.setText(s1[1]);

                            // 设置设备编码和id
                            String[] s2 = strs[1].split(":");
                            deviceId = s2[0];
                            equipmentCode.setText(s2[1]);

                            // 设置版本和id
                            String[] s3 = strs[2].split(":");
                            verId = s3[0];
                            verName.setText(s3[1]);
                            return;
                        }
                    }

//                    tip(data);
                    // 发送网络请求
                    taskController.sendAsynMessage(IdiyMessage.QR_CODE, data, taskId);
                }

                canDecode = true;
            }
        };

        mScanner.addDataListener(mDataListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if((keyCode == KeyEvent.KEYCODE_BUTTON_L1)
                || (keyCode == KeyEvent.KEYCODE_BUTTON_R1)
                || (keyCode == KeyEvent.KEYCODE_BUTTON_L2))
        {
            Log.i("ScanApp", "onKeyDown");
            //if(canDecode && (event.getRepeatCount() == 0))
            if (event.getRepeatCount() == 0)
            {
                canDecode = false;
                try
                {
                    mScanner.read();
                }
                catch (ScannerException se)
                {
                    se.printStackTrace();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if((keyCode == KeyEvent.KEYCODE_BUTTON_L1)
                || (keyCode == KeyEvent.KEYCODE_BUTTON_R1)
                || (keyCode == KeyEvent.KEYCODE_BUTTON_L2))
        {
            Log.i("ScanApp", "onKeyUp");
//            if (true)
            if(!canDecode)
            {
                try
                {
                    mScanner.cancelRead();
                }
                catch (ScannerException se)
                {
                    se.printStackTrace();
                }
                canDecode = true;
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    protected void onStart() {
        Log.e("onStart", ".....................................");
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mScanner.enable();
                } catch (ScannerException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = IdiyMessage.DATA_LISTENER;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    protected void onStop() {
        try
        {
            if(!canDecode)
                mScanner.cancelRead();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    /*try {
                        mScanner.disable();
                    } catch (ScannerException e) {
                        e.printStackTrace();
                    }*/
                    Message message = new Message();
                    message.what = IdiyMessage.DATA_LISTENER_REMOVE;
                    mHandler.sendMessage(message);
                }
            }).start();
        }
        catch(ScannerException se)
        {
            se.printStackTrace();
        }
        finally
        {
            canDecode = true;
        }
        super.onStop();

        Log.e("onStop", ".........................................");
    }

    ///////////////////////////////////// SCAN ////////////////////////////////////////////////

    /**
     * 点击查询物料
     *
     * @author xuz
     * @date 2019/1/10 10:49 AM
     * @param []
     * @return void
     */
    public void queryMaterialsClick(View view) {

        // 如果存在扫码单号，则不能查询
        if (!taskNumber.getText().toString().isEmpty()) {
            return;
        }

        Intent intent = new Intent(ScanTaskActivity.this, QueryActivity.class);
        intent.putExtra("rightArrows", IdiyMessage.QUERY_MATERIALS);
        startActivityForResult(intent, IdiyMessage.QUERY_MATERIALS);
    }

    /**
     * 点击查询设备
     *
     * @author xuz
     * @date 2019/1/10 10:49 AM
     * @param []
     * @return void
     */
    public void queryDeviceClick(View view) {

        // 如果存在扫码单号，则不能查询
        if (!taskNumber.getText().toString().isEmpty()) {
            return;
        }

        // 将adpter发往查询页面
        Intent intent = new Intent(ScanTaskActivity.this, QueryActivity.class);
        intent.putExtra("rightArrows", IdiyMessage.QUERY_EQUIPMENT);
        //startActivity(intent);
        startActivityForResult(intent, IdiyMessage.QUERY_EQUIPMENT);
    }

    /**
     * 点击查询版本
     *
     * @author xuz
     * @date 2019/1/10 10:50 AM
     * @param []
     * @return void
     */
    public void queryVerClick(View view) {

        // 如果存在扫码单号，则不能查询
        if (!taskNumber.getText().toString().isEmpty()) {
            return;
        }

        // 将adpter发往查询页面
        Intent intent = new Intent(ScanTaskActivity.this, QueryActivity.class);
        intent.putExtra("rightArrows", IdiyMessage.QUERY_VER);
        if (materialsId.isEmpty() || deviceId.isEmpty()) {
            tip("请先选择物料编码和设备编码");
            return;
        }
        intent.putExtra("materialsId", materialsId);
        intent.putExtra("deviceId", deviceId);
        startActivityForResult(intent, IdiyMessage.QUERY_VER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IdiyMessage.QUERY_MATERIALS:
                echo(data);
                break;
            case IdiyMessage.QUERY_EQUIPMENT:
                echo(data);
                break;
            case IdiyMessage.QUERY_VER:
                echo(data);
                break;
/*            // 相机
            case IdiyMessage.CAPTURE_MATERIALS:
                checkMaterialsCode(data);
                break;
            case IdiyMessage.CAPTURE_MATERIALS_SET:
                checkMaterialsSetCode(data);
                break;*/
            default:
                break;
        }
    }

/*    // 相机
    *//**
     * 校验二维码的料位编码
     *  
     * @author xuz
     * @date 2019/1/15 3:08 PM
     * @param [data]
     * @return void
     *//*
    private void checkMaterialsSetCode(Intent data) {

        Bundle bundle = data.getExtras();
        String codeResult = bundle.getString("codeResult");
        // 发送网络请求
        taskController.sendAsynMessage(IdiyMessage.CAPTURE_MATERIALS_SET, codeResult, taskId);
    }

    *//**
     * 校验二维码的物料编码
     *
     * @author xuz
     * @date 2019/1/15 2:38 PM
     * @param [data]
     * @return void
     *//*
    private void checkMaterialsCode(Intent data) {

        Bundle bundle = data.getExtras();
        String codeResult = bundle.getString("codeResult");
        // 发送网络请求
        taskController.sendAsynMessage(IdiyMessage.CAPTURE_MATERIALS, codeResult, taskId);
    }*/

    /**
     * 将数据回显到TextView
     *  
     * @author xuz
     * @date 2019/1/9 12:46 PM
     * @param [data]
     * @return void
     */
    private void echo(Intent data) {
        try {
            // 获取查询页面选中的子项数据
            //Bundle bundle = getIntent().getExtras();
            Bundle bundle = data.getExtras();
            int which = bundle.getInt("which");
            String id = bundle.getString("id");
            String code = bundle.getString("code");

            switch (which) {
                case IdiyMessage.QUERY_MATERIALS_RESULT:
                    materialsCode.setText(code);
                    materialsId = id;
                    // 初始化版本号
                    verName.setText("");
                    verId = "";
                    break;
                case IdiyMessage.QUERY_EQUIPMENT_RESULT:
                    equipmentCode.setText(code);
                    deviceId = id;
                    // 初始化版本号
                    verName.setText("");
                    verId = "";
                    break;
                case IdiyMessage.QUERY_VER_RESULT:
                    verName.setText(code);
                    verId = id;
                    break;
                default:
                    break;
            }
        } catch (NullPointerException e) {
            Log.w("空指针：", "当前页的编码均为空");
        }
    }

    /**
     * 创建行
     *
     * @author xuz
     * @date 2019/1/19 10:19 AM
     * @param [scanning]
     * @return void
     */
    private void createRow(Scanning scanning) {

        // 移除重复的批号
//        removeChildRow(scanning);

        try {
            Integer lScanningId = scanning.getlScanningId();

/*        boolean flag = false;
        // 强转为ViewGroup，然后遍历
        ViewGroup viewGroup = (ViewGroup) tableLayout;
        int count = viewGroup.getChildCount();
        for (int i=0; i<count; i++) {
            View view = viewGroup.getChildAt(i);
            // 如果已经存在该扫描记录id，则不创建行，只做数据插入
            if (lScanningId == view.getId()) {
                flag = true;
            }
        }*/

            // 创建行
            LayoutInflater inflater = LayoutInflater.from(ScanTaskActivity.this);
            TableRow row = new TableRow(ScanTaskActivity.this);
            // 给行设置id
//            row.setId(lScanningId);
            row.setGravity(Gravity.CENTER);
            row.setBackgroundColor(Color.WHITE);
            row.setPadding( 1, 1, 1, 1);

            TextView tv1 = inflater.inflate(R.layout.scanning_textview, null).findViewById(R.id.textview3);
            TextView tv2 = inflater.inflate(R.layout.scanning_textview, null).findViewById(R.id.textview3);
            TextView tv3 = inflater.inflate(R.layout.scanning_textview, null).findViewById(R.id.textview3);
            TextView tv4 = inflater.inflate(R.layout.scanning_textview, null).findViewById(R.id.textview3);
            TextView tv5 = inflater.inflate(R.layout.scanning_textview, null).findViewById(R.id.textview3);
            TextView tv6 = inflater.inflate(R.layout.scanning_textview, null).findViewById(R.id.textview3);

            String vcMaterialsCode = scanning.getVcMaterialsCode();
            String vcSeatCode = scanning.getVcSeatCode();
            String vcMaterialsBatch = scanning.getVcMaterialsBatch();
            String dScanningTime = scanning.getdScanningTime();
            Integer lN = scanning.getlN();

            if (vcMaterialsCode != null) {
                tv1.setText(scanning.getVcMaterialsCode());
                /*if (scanning.getlValid() == 0) {
                    tv1.setTextColor(Color.RED);
                } else {
                    tv1.setTextColor(Color.GREEN);
                }*/
                if (scanning.getlValid()!=null && scanning.getlValid() == 1) {
                    tv1.setTextColor(Color.GREEN);
                }
            }
            row.addView(tv1);

            if (vcSeatCode != null) {
                tv2.setText(scanning.getVcSeatCode());
                /*if (scanning.getlValid() == 0) {
                    tv2.setTextColor(Color.RED);
                } else {
                    tv2.setTextColor(Color.GREEN);
                }*/
                if (scanning.getlValid()!=null && scanning.getlValid() == 1) {
                    tv2.setTextColor(Color.GREEN);
                }
            }
            row.addView(tv2);

            if (dScanningTime != null) {
                tv3.setText(scanning.getdScanningTime());
            }
            tv3.setTextSize(12);
            row.addView(tv3);

            // 第几次
            if (lN != null && lN != 0) {
                tv4.setText(scanning.getlN().toString());
            }
//            tv4.setTextSize(12);
            row.addView(tv4);

            if (scanning.getVcMaterialsModel() != null
                    && !scanning.getVcMaterialsModel().isEmpty()) {
                tv5.setText(scanning.getVcMaterialsModel());
            }
            row.addView(tv5);

            if (vcMaterialsBatch != null) {
                tv6.setText(scanning.getVcMaterialsBatch());
            }
//            tv4.setSingleLine(false);
//            //tv4.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
//            tv4.setMaxLines(3);
//            tv4.setWidth(300);
//            tv4.setTextSize(12);
            row.addView(tv6);



            tableLayout.addView(row, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

            // 显示扫码结果
            scannResult.setText(scanning.getQr());
            if (scanning.getlValid()!=null && scanning.getlValid() == 0) {
                scannResult.setTextColor(Color.RED);
            }
            if (scanning.getlValid()!=null && scanning.getlValid() == 1) {
                scannResult.setTextColor(Color.GREEN);
            }

            // 始终在最底部
            scrollBottom(scrollView, tableLayout);
        } catch (NullPointerException e) {
            Log.w("NullPointException:", "..");
        }
    }

    /**
     * 覆盖行
     *
     * @author xuz
     * @date 2019/1/16 1:28 PM
     * @param [scanning]
     * @return void
     */
    private void coverRow(Scanning scanning) {

        // 如果没有第一行，则新建一行
        if (tableLayout.getChildCount() <= 1) {
            createRow(scanning);
            return;
        }
        // 只有在有效数据的情况下，才去清除重复的行
        if (scanning.getVcMaterialsCode() != null && scanning.getVcSeatCode() != null
                && scanning.getlValid() == 1) {
            // 移除重复的批号
            removeChildRow(scanning);
        }


        try {
            // 获取最后一行
            int last = tableLayout.getChildCount() - 1;
            TableRow row = (TableRow) tableLayout.getChildAt(last);

            TextView tv1 = (TextView) row.getChildAt(0);
            TextView tv2 = (TextView) row.getChildAt(1);
            TextView tv3 = (TextView) row.getChildAt(2);
            TextView tv4 = (TextView) row.getChildAt(3);
            TextView tv5 = (TextView) row.getChildAt(4);
            TextView tv6 = (TextView) row.getChildAt(5);

            String vcMaterialsCode = tv1.getText().toString();
            String vcSeatCode = tv2.getText().toString();
            String vcMaterialsBatch = tv6.getText().toString();
            String dScanningTime = tv3.getText().toString();
            Integer lN = scanning.getlN();

            /*if (tv1.getText() != null) {
                vcMaterialsCode = tv1.getText().toString();
            }
            if (tv2.getText() != null) {
                vcSeatCode = tv2.getText().toString();
            }
            if (tv3.getText() != null) {
                vcMaterialsBatch = tv3.getText().toString();
            }
            if (tv4.getText() != null) {
                dScanningTime = tv4.getText().toString();
            }*/

/*            if (vcMaterialsCode.isEmpty()) {
                tv1.setText(scanning.getVcMaterialsCode());
                if (scanning.getlValid() == 0) {
                    tv1.setTextColor(Color.RED);
                } else {
                    tv1.setTextColor(Color.GREEN);
                }
            } else {
                tv1.setText(scanning.getVcMaterialsCode());
                tv1.setTextColor(Color.GREEN);
            }

            if (vcSeatCode.isEmpty()) {
                tv2.setText(scanning.getVcSeatCode());
                if (scanning.getlValid() == 0) {
                    tv2.setTextColor(Color.RED);
                } else {
                    tv2.setTextColor(Color.GREEN);
                }
            } else {
                tv2.setText(scanning.getVcSeatCode());
                tv2.setTextColor(Color.GREEN);
            }*/

            if (scanning.getlValid() == 0) {
                if (vcMaterialsCode.isEmpty()) {
                    tv1.setText(scanning.getVcMaterialsCode());
                    tv1.setTextColor(Color.RED);
                } else if (!vcMaterialsCode.isEmpty() && scanning.getVcSeatCode().isEmpty()) {
                    tv1.setText(scanning.getVcMaterialsCode());
                    tv1.setTextColor(Color.RED);
                } else if (!vcMaterialsCode.isEmpty() && !scanning.getVcSeatCode().isEmpty()) {
                    tv1.setText(scanning.getVcMaterialsCode());
                    tv1.setTextColor(Color.GREEN);
                }

                if (vcSeatCode.isEmpty()) {
                    tv2.setText(scanning.getVcSeatCode());
                    tv2.setTextColor(Color.RED);
                } else if (!vcSeatCode.isEmpty() && scanning.getVcMaterialsCode().isEmpty()) {
                    tv2.setText(scanning.getVcSeatCode());
                    tv2.setTextColor(Color.RED);
                } else if (!vcSeatCode.isEmpty() && !scanning.getVcMaterialsCode().isEmpty()) {
                    tv2.setText(scanning.getVcSeatCode());
                    tv2.setTextColor(Color.GREEN);
                }
            } else {
                tv1.setText(scanning.getVcMaterialsCode());
                tv1.setTextColor(Color.GREEN);
                tv2.setText(scanning.getVcSeatCode());
                tv2.setTextColor(Color.GREEN);
            }

            tv3.setText(scanning.getdScanningTime());
            tv3.setTextSize(12);

            if (lN != null && lN != 0) {
                tv4.setText(scanning.getlN().toString());
            } else {
                tv4.setText("");
            }
//            tv5.setTextSize(12);

            if (scanning.getVcMaterialsModel() != null
                    && !scanning.getVcMaterialsModel().isEmpty()) {
                tv5.setText(scanning.getVcMaterialsModel());
            } else {
                tv5.setText("");
            }

//            tv4.setTextSize(12);
            tv6.setText(scanning.getVcMaterialsBatch());

            // 显示扫码结果
            scannResult.setText(scanning.getQr());
            if (scanning.getlValid() == 0) {
                scannResult.setTextColor(Color.RED);
            }
            if (scanning.getlValid() == 1) {
                scannResult.setTextColor(Color.GREEN);
            }

            // 始终在最底部
            scrollBottom(scrollView, tableLayout);

            // 显示以扫描和为扫描的数目
            Integer seatSum = scanning.getSeatSum();
            Integer seatUsed = scanning.getSeatUsed();
            Integer seatUnused = scanning.getSeatUnused();
            String text = "当前物料" + seatSum +"个，已经扫描" + seatUsed + "个，还有" + seatUnused + "个待扫描";
            sumUsedUnused.setText(text);

            // 如果待扫描为0
            if (seatUnused == 0 && scanning.getlValid() == 1
                    && !scanning.getVcSeatCode().isEmpty() && !scanning.getVcMaterialsCode().isEmpty()) {
                ActivityUtils.showDialog(this, "扫描结果", "物料已经全部扫描完");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    /**
     * 移除重复的批号行
     *
     * @author xuz
     * @date 2019/1/19 10:20 AM
     * @param [scanning]
     * @return void
     */
    private void removeChildRow(Scanning scanning) {

        try {
            Integer ifrow = scanning.getIfRow();
            boolean flag = true;
            String vcMaterialsBatch = scanning.getVcMaterialsBatch();
            String vcMaterialsCode = scanning.getVcMaterialsCode();
            String vcSeatCode = scanning.getVcSeatCode();
            int childCounts = tableLayout.getChildCount();

            if (ifrow == 1) {
                for (int i=0; i<childCounts; i++) {
                    TableRow row = (TableRow) tableLayout.getChildAt(i);
                    TextView textView = (TextView) row.getChildAt(2);
                    String str = textView.getText().toString();
                    // 如果tablelayout中有重复的批号，先删除那一行
                    if (vcMaterialsBatch.equals(str)) {
                        tableLayout.removeViewAt(i);
                        return;
                    }
                }
            }

            if (ifrow != 1) {
                for (int i=0; i<childCounts; i++) {
                    TableRow row = (TableRow) tableLayout.getChildAt(i);
                    TextView textView = (TextView) row.getChildAt(0);
                    TextView textView2 = (TextView) row.getChildAt(1);
                    TextView textView3 = (TextView) row.getChildAt(3);
                    String seat = null;
                    String lN = null;
                    try {
                        seat = textView2.getText().toString();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    try {
                        lN = textView3.getText().toString();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    String str = textView.getText().toString();
                    // 如果tablelayout中有重复的批号且料位编码重复，是为扫描的数据，删除那一行
                    if (vcMaterialsCode.equals(str) && !seat.isEmpty()
                            && vcSeatCode.equals(seat) && lN.isEmpty()) {
                        tableLayout.removeViewAt(i);
                        return;
                    }

                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定位到最底部
     *
     * @author xuz
     * @date 2019/1/21 3:48 PM
     * @param [scrollView, inner]
     * @return void
     */
    private void scrollBottom(final ScrollView scrollView, final View inner) {
        Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (scrollView == null || inner == null) {
                    return;
                }
                // 内层高度超过外层
                int offset = inner.getMeasuredHeight()
                        - scrollView.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scrollView.scrollTo(0, offset);
            }
        });
    }

}
