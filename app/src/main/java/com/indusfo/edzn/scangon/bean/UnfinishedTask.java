package com.indusfo.edzn.scangon.bean;

import android.content.Intent;

import java.io.Serializable;

public class UnfinishedTask implements Serializable {

    private static final long serialVersionUID = -5140988048285392888L;
    // 任务单id
    private Integer lTaskId;
    // 任务单号
    private String vcScanNumber;
    // 物料料位版本id
    private Integer lMaterialsVerId;
    // 物料料位版本id(翻译)
    private String vcMaterialsVerName;
    // 物料资料id
    private Integer lMaterialsId;
    private String vcProductModel;
    private String vcMaterialsCode;
    private String dCreateTime;
    // 设备id
    private Integer lDeviceId;
    private String vcDeviceCode;
    // 创建人
    private Integer lUserId;
    private String vcUserName;
    private Integer lDataState;
    private String lDataStateName;
    // 关联任务单号
    private String vcTaskNumber;

    /*--------------------------------------------
    |  A C C E S S O R S / M O D I F I E R S    |
    ============================================*/

    public Integer getlTaskId() {
        return lTaskId;
    }

    public void setlTaskId(Integer lTaskId) {
        this.lTaskId = lTaskId;
    }

    public String getVcScanNumber() {
        return vcScanNumber;
    }

    public void setVcScanNumber(String vcScanNumber) {
        this.vcScanNumber = vcScanNumber;
    }

    public Integer getlMaterialsVerId() {
        return lMaterialsVerId;
    }

    public void setlMaterialsVerId(Integer lMaterialsVerId) {
        this.lMaterialsVerId = lMaterialsVerId;
    }

    public String getVcMaterialsVerName() {
        return vcMaterialsVerName;
    }

    public void setVcMaterialsVerName(String vcMaterialsVerName) {
        this.vcMaterialsVerName = vcMaterialsVerName;
    }

    public Integer getlMaterialsId() {
        return lMaterialsId;
    }

    public void setlMaterialsId(Integer lMaterialsId) {
        this.lMaterialsId = lMaterialsId;
    }

    public String getVcProductModel() {
        return vcProductModel;
    }

    public void setVcProductModel(String vcProductModel) {
        this.vcProductModel = vcProductModel;
    }

    public String getVcMaterialsCode() {
        return vcMaterialsCode;
    }

    public void setVcMaterialsCode(String vcMaterialsCode) {
        this.vcMaterialsCode = vcMaterialsCode;
    }

    public String getdCreateTime() {
        return dCreateTime;
    }

    public void setdCreateTime(String dCreateTime) {
        this.dCreateTime = dCreateTime;
    }

    public Integer getlDeviceId() {
        return lDeviceId;
    }

    public void setlDeviceId(Integer lDeviceId) {
        this.lDeviceId = lDeviceId;
    }

    public String getVcDeviceCode() {
        return vcDeviceCode;
    }

    public void setVcDeviceCode(String vcDeviceCode) {
        this.vcDeviceCode = vcDeviceCode;
    }

    public Integer getlUserId() {
        return lUserId;
    }

    public void setlUserId(Integer lUserId) {
        this.lUserId = lUserId;
    }

    public String getVcUserName() {
        return vcUserName;
    }

    public void setVcUserName(String vcUserName) {
        this.vcUserName = vcUserName;
    }

    public Integer getlDataState() {
        return lDataState;
    }

    public void setlDataState(Integer lDataState) {
        this.lDataState = lDataState;
    }

    public String getlDataStateName() {
        return lDataStateName;
    }

    public void setlDataStateName(String lDataStateName) {
        this.lDataStateName = lDataStateName;
    }

    public String getVcTaskNumber() {
        return vcTaskNumber;
    }

    public void setVcTaskNumber(String vcTaskNumber) {
        this.vcTaskNumber = vcTaskNumber;
    }

    @Override
    public String toString() {
        return "UnfinishedTask{" +
                "lTaskId=" + lTaskId +
                ", vcScanNumber='" + vcScanNumber + '\'' +
                ", lMaterialsVerId=" + lMaterialsVerId +
                ", vcMaterialsVerName='" + vcMaterialsVerName + '\'' +
                ", lMaterialsId=" + lMaterialsId +
                ", vcProductModel='" + vcProductModel + '\'' +
                ", vcMaterialsCode='" + vcMaterialsCode + '\'' +
                ", dCreateTime='" + dCreateTime + '\'' +
                ", lDeviceId=" + lDeviceId +
                ", vcDeviceCode='" + vcDeviceCode + '\'' +
                ", lUserId=" + lUserId +
                ", vcUserName='" + vcUserName + '\'' +
                ", lDataState=" + lDataState +
                ", lDataStateName='" + lDataStateName + '\'' +
                ", vcTaskNumber='" + vcTaskNumber + '\'' +
                '}';
    }
}
