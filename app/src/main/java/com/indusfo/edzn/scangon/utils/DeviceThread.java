package com.indusfo.edzn.scangon.utils;

import java.util.HashMap;
import java.util.List;

public class DeviceThread {

    // 如果保存数据有多个,则使用Map集合
    private static ThreadLocal<HashMap<String, Boolean>> deviceThread = new ThreadLocal<HashMap<String, Boolean>>();

    public static void set(HashMap<String, Boolean> deviceIdList) {

        deviceThread.set(deviceIdList);
    }

    public static HashMap<String, Boolean> get() {

        return deviceThread.get();
    }

    // 线程销毁方法
    public static void remove() {

        deviceThread.remove();
    }
}
