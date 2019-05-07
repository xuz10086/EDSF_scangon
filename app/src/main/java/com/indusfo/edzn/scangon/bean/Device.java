package com.indusfo.edzn.scangon.bean;

import java.io.Serializable;

/**
 * 设备
 *
 * @author xuz
 * @date 2019/1/11 2:15 PM
 */
public class Device implements Serializable {

    private static final long serialVersionUID = -298037225629336296L;

    // 设备id
    private String lDeviceId;
    // 设备编码
    private String vcDeviceCode;
    // 设备名称
    private String vcDeviceName;
    // 设备型号
    private String vcDeviceModel;
    // 是否被选中
    private String ifChecked;
    /*--------------------------------------------
    |  A C C E S S O R S / M O D I F I E R S    |
    ============================================*/

    public String getlDeviceId() {
        return lDeviceId;
    }

    public void setlDeviceId(String lDeviceId) {
        this.lDeviceId = lDeviceId;
    }

    public String getVcDeviceCode() {
        return vcDeviceCode;
    }

    public void setVcDeviceCode(String vcDeviceCode) {
        this.vcDeviceCode = vcDeviceCode;
    }

    public String getVcDeviceName() {
        return vcDeviceName;
    }

    public void setVcDeviceName(String vcDeviceName) {
        this.vcDeviceName = vcDeviceName;
    }

    public String getVcDeviceModel() {
        return vcDeviceModel;
    }

    public void setVcDeviceModel(String vcDeviceModel) {
        this.vcDeviceModel = vcDeviceModel;
    }

    public String getIfChecked() {
        return ifChecked;
    }

    public void setIfChecked(String ifChecked) {
        this.ifChecked = ifChecked;
    }

    @Override
    public String toString() {
        return "Device{" +
                "lDeviceId='" + lDeviceId + '\'' +
                ", vcDeviceCode='" + vcDeviceCode + '\'' +
                ", vcDeviceName='" + vcDeviceName + '\'' +
                ", vcDeviceModel='" + vcDeviceModel + '\'' +
                ", ifChecked='" + ifChecked + '\'' +
                '}';
    }
}
