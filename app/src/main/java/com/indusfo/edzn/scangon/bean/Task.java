package com.indusfo.edzn.scangon.bean;

import java.io.Serializable;

/**
 * 任务单
 *
 * @author xuz
 * @date 2019/1/11 2:16 PM
 * @param
 * @return
 */
public class Task implements Serializable {

    private static final long serialVersionUID = 2735537504250499524L;
    // 任务单id
    private String lTaskId;
    // 任务单号
    private String vcScanNumber;
    // 物料id
    private String lMaterialsId;
    private String vcMaterialsCode;
    // 产品编码
    private String vcProductModel;
    // 版本id
    private String lMaterialsVerId;
    // 版本号
    private String vcMaterialsVerName;
    // 设备id
    private String lDeviceId;
    // 设备编码
    private String vcDeviceCode;
    // 创建人
    private String lUserId;
    private String vcUserName;
    private String dCreateTime;

    // 关联任务单号
    private String vcTaskNumber;

    /*--------------------------------------------
    |  A C C E S S O R S / M O D I F I E R S    |
    ============================================*/

    public String getlTaskId() {
        return lTaskId;
    }

    public void setlTaskId(String lTaskId) {
        this.lTaskId = lTaskId;
    }

    public String getVcScanNumber() {
        return vcScanNumber;
    }

    public void setVcScanNumber(String vcScanNumber) {
        this.vcScanNumber = vcScanNumber;
    }

    public String getlMaterialsId() {
        return lMaterialsId;
    }

    public void setlMaterialsId(String lMaterialsId) {
        this.lMaterialsId = lMaterialsId;
    }

    public String getVcMaterialsCode() {
        return vcMaterialsCode;
    }

    public void setVcMaterialsCode(String vcMaterialsCode) {
        this.vcMaterialsCode = vcMaterialsCode;
    }

    public String getVcProductModel() {
        return vcProductModel;
    }

    public void setVcProductModel(String vcProductModel) {
        this.vcProductModel = vcProductModel;
    }

    public String getlMaterialsVerId() {
        return lMaterialsVerId;
    }

    public void setlMaterialsVerId(String lMaterialsVerId) {
        this.lMaterialsVerId = lMaterialsVerId;
    }

    public String getVcMaterialsVerName() {
        return vcMaterialsVerName;
    }

    public void setVcMaterialsVerName(String vcMaterialsVerName) {
        this.vcMaterialsVerName = vcMaterialsVerName;
    }

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

    public String getlUserId() {
        return lUserId;
    }

    public void setlUserId(String lUserId) {
        this.lUserId = lUserId;
    }

    public String getVcUserName() {
        return vcUserName;
    }

    public void setVcUserName(String vcUserName) {
        this.vcUserName = vcUserName;
    }

    public String getdCreateTime() {
        return dCreateTime;
    }

    public void setdCreateTime(String dCreateTime) {
        this.dCreateTime = dCreateTime;
    }

    public String getVcTaskNumber() {
        return vcTaskNumber;
    }

    public void setVcTaskNumber(String vcTaskNumber) {
        this.vcTaskNumber = vcTaskNumber;
    }

    @Override
    public String toString() {
        return "Task{" +
                "lTaskId='" + lTaskId + '\'' +
                ", vcScanNumber='" + vcScanNumber + '\'' +
                ", lMaterialsId='" + lMaterialsId + '\'' +
                ", vcMaterialsCode='" + vcMaterialsCode + '\'' +
                ", vcProductModel='" + vcProductModel + '\'' +
                ", lMaterialsVerId='" + lMaterialsVerId + '\'' +
                ", vcMaterialsVerName='" + vcMaterialsVerName + '\'' +
                ", lDeviceId='" + lDeviceId + '\'' +
                ", vcDeviceCode='" + vcDeviceCode + '\'' +
                ", lUserId='" + lUserId + '\'' +
                ", vcUserName='" + vcUserName + '\'' +
                ", dCreateTime='" + dCreateTime + '\'' +
                ", vcTaskNumber='" + vcTaskNumber + '\'' +
                '}';
    }
}

