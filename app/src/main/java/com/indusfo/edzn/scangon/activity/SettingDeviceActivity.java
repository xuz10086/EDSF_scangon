package com.indusfo.edzn.scangon.activity;

import android.os.Message;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.indusfo.edzn.scangon.R;
import com.indusfo.edzn.scangon.adpater.SettingDeviceAdapter;
import com.indusfo.edzn.scangon.bean.Device;
import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.cons.IdiyMessage;
import com.indusfo.edzn.scangon.controller.SettingDeviceController;
import com.indusfo.edzn.scangon.utils.ActivityUtils;
import com.indusfo.edzn.scangon.utils.DeviceThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SettingDeviceActivity extends BaseActivity {

    private ListView settingDeviceList;
    private Button settingDeviceButton;
    private HashMap<String, Boolean> isChechedMap = new HashMap<String, Boolean>();

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case IdiyMessage.QUERY_EQUIPMENT_RESULT:
                handlerQueryEquipmentResult(msg);
                break;
            case IdiyMessage.SAVE_DEVICE_TODB_RESULT:
                handlerSaveDeviceToDb(msg);
                break;
            case IdiyMessage.GET_DEVICE_FROM_DB_RESULT:
                handlerGetDeviceFromDb(msg);
                break;
            default:
                break;
        }

    }

    private void handlerGetDeviceFromDb(Message msg) {

        List<Device> dList = (List<Device>) msg.obj;
        List<String> lDeviceIdList = new ArrayList<String>();
        for (Iterator iterator = dList.iterator(); iterator.hasNext();) {
            Device device = (Device) iterator.next();
            if ("true".equals(device.getIfChecked())) {
                String lDeviceId = device.getlDeviceId();
                isChechedMap.put(device.getlDeviceId(), true);
                lDeviceIdList.add(lDeviceId);
            } else {
                isChechedMap.put(device.getlDeviceId(), false);
            }
        }

        DeviceThread.set(isChechedMap);

        // 发送网络请求，查询设备信息
//        mController.sendAsynMessage(IdiyMessage.QUERY_EQUIPMENT);

        // 回显选中的状态
        List<String> checked = new ArrayList<String>();
        for (Map.Entry<String, Boolean> entry : isChechedMap.entrySet()) {
            if (entry.getValue()) {
                checked.add(entry.getKey());
            }
        }
        for (int j=0; j<checked.size(); j++) {
            String lDeviceId = checked.get(j);
            for (int i = 0; i < settingDeviceList.getChildCount(); i++) {
                CheckedTextView checkedTextView = (CheckedTextView) settingDeviceList.getChildAt(i);
                String str = checkedTextView.getHint().toString();

                if (lDeviceId.equals(str)) {

                    checkedTextView.setChecked(true);
//                    checkedTextView.setChecked(false);
                }

            }
        }

    }

    private void handlerSaveDeviceToDb(Message msg) {
        boolean flag = (boolean) msg.obj;
        if (!flag) {
            ActivityUtils.showDialog(this, "设备设置失败", "请重新设置");
        }
        // 如果没有问题，跳转到登录页面
        ActivityUtils.start(this, LoginActivity.class, true);
    }

    private void handlerQueryEquipmentResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult || null == rResult.getStatus()) {
            tip("网络连接错误");
            return;
        }
        if ("200".equals(rResult.getStatus())) {
            List<Device> deviceList = JSON.parseArray(rResult.getData(), Device.class);

            SettingDeviceAdapter settingDeviceAdapter =
                    new SettingDeviceAdapter(this, R.layout.setting_device_item, deviceList);
            settingDeviceList.setAdapter(settingDeviceAdapter);
            settingDeviceAdapter.notifyDataSetChanged();

            // 查询设备表，给列表设置选中状态
            mController.sendAsynMessage(IdiyMessage.GET_DEVICE_FROM_DB);
        }


        if (!rResult.isOk()) {
            tip(rResult.getMsg());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device);
        initController();
        initUI();
    }

    private void initController() {
        mController = new SettingDeviceController(this);
        mController.setIModeChangeListener(this);
    }

    private void initUI() {
        settingDeviceList = findViewById(R.id.setting_device_list);
        // 设置多选
        settingDeviceList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        settingDeviceButton = findViewById(R.id.setting_device_button);

        // 查询以选择的设备id
//        mController.sendAsynMessage(IdiyMessage.GET_DEVICE_FROM_DB);

        // 发送网络请求，查询设备信息
        mController.sendAsynMessage(IdiyMessage.QUERY_EQUIPMENT);

        settingDeviceList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int arg) {

                List<String> checked = new ArrayList<String>();
                for (Map.Entry<String, Boolean> entry : DeviceThread.get().entrySet()) {
                    if (entry.getValue()) {
                        checked.add(entry.getKey());
                    }
                }

                // 回显选中的状态
                for (int j=0; j<checked.size(); j++) {
                    String lDeviceId = checked.get(j);
                    for (int i=0; i<settingDeviceList.getChildCount(); i++) {
                        CheckedTextView checkedTextView2 = (CheckedTextView) settingDeviceList.getChildAt(i);
                        String str = checkedTextView2.getHint().toString();
                        if (lDeviceId.equals(str)) {
                            checkedTextView2.setChecked(true);
//                            isChechedMap.put(lDeviceId, true);
                        } else {
//                            isChechedMap.put(lDeviceId, false);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });

        settingDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Device> deviceList = new ArrayList<Device>();

                for (Map.Entry<String, Boolean> entry : isChechedMap.entrySet()) {
                    String key = entry.getKey();
                    boolean value = entry.getValue();
                    if (value) {
                        Device device = new Device();
                        device.setlDeviceId(entry.getKey());
                        device.setIfChecked("true");

                        deviceList.add(device);
                    }
                }

                // 交给其他线程进行保存
                mController.sendAsynMessage(IdiyMessage.SAVE_DEVICE_TODB, deviceList);
            }
        });

    }

    // 按返回键跳到登录页
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            ActivityUtils.start(SettingDeviceActivity.this, LoginActivity.class, true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
