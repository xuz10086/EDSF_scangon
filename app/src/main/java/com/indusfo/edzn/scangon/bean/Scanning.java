package com.indusfo.edzn.scangon.bean;

import android.content.Intent;

import java.io.Serializable;

public class Scanning implements Serializable {

    private static final long serialVersionUID = 2466283820960216534L;
    // 扫描记录ID
    private Integer lScanningId;
    private String lUserId;
    // 扫描时间
    private String dScanningTime;
    private String qr;
    // 物料批号
    private String vcMaterialsBatch;
    // 物料id
    private String lMaterialsId;
    // 物料编码
    private String vcMaterialsCode;
    // 物料描述
    private String vcMaterialsModel;
    // 料位编码
    private String vcSeatCode;
    // 任务单ID
    private String lTaskId;
    // 有效情况
    private Integer lValid;
    // 是否换行
    private Integer ifRow;
    // 状态
    private String status;
    // 错误消息
    private String erro;
    // 第几次
    private Integer lN;
    // 关联任务单号
    private String vcTaskNumber;

    // 需要扫描的总料位
    private Integer seatSum;
    // 已经扫描的料位数
    private Integer seatUsed;
    // 未扫描的料位数
    private Integer seatUnused;

    /*--------------------------------------------
    |  A C C E S S O R S / M O D I F I E R S    |
    ============================================*/

    public Integer getlScanningId() {
        return lScanningId;
    }

    public void setlScanningId(Integer lScanningId) {
        this.lScanningId = lScanningId;
    }

    public String getlUserId() {
        return lUserId;
    }

    public void setlUserId(String lUserId) {
        this.lUserId = lUserId;
    }

    public String getdScanningTime() {
        return dScanningTime;
    }

    public void setdScanningTime(String dScanningTime) {
        this.dScanningTime = dScanningTime;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public String getVcMaterialsBatch() {
        return vcMaterialsBatch;
    }

    public void setVcMaterialsBatch(String vcMaterialsBatch) {
        this.vcMaterialsBatch = vcMaterialsBatch;
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

    public String getVcMaterialsModel() {
        return vcMaterialsModel;
    }

    public void setVcMaterialsModel(String vcMaterialsModel) {
        this.vcMaterialsModel = vcMaterialsModel;
    }

    public String getVcSeatCode() {
        return vcSeatCode;
    }

    public void setVcSeatCode(String vcSeatCode) {
        this.vcSeatCode = vcSeatCode;
    }

    public String getlTaskId() {
        return lTaskId;
    }

    public void setlTaskId(String lTaskId) {
        this.lTaskId = lTaskId;
    }

    public Integer getlValid() {
        return lValid;
    }

    public void setlValid(Integer lValid) {
        this.lValid = lValid;
    }

    public Integer getIfRow() {
        return ifRow;
    }

    public void setIfRow(Integer ifRow) {
        this.ifRow = ifRow;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public Integer getlN() {
        return lN;
    }

    public void setlN(Integer lN) {
        this.lN = lN;
    }

    public String getVcTaskNumber() {
        return vcTaskNumber;
    }

    public void setVcTaskNumber(String vcTaskNumber) {
        this.vcTaskNumber = vcTaskNumber;
    }

    public Integer getSeatSum() {
        return seatSum;
    }

    public void setSeatSum(Integer seatSum) {
        this.seatSum = seatSum;
    }

    public Integer getSeatUsed() {
        return seatUsed;
    }

    public void setSeatUsed(Integer seatUsed) {
        this.seatUsed = seatUsed;
    }

    public Integer getSeatUnused() {
        return seatUnused;
    }

    public void setSeatUnused(Integer seatUnused) {
        this.seatUnused = seatUnused;
    }

    @Override
    public String toString() {
        return "Scanning{" +
                "lScanningId=" + lScanningId +
                ", lUserId='" + lUserId + '\'' +
                ", dScanningTime='" + dScanningTime + '\'' +
                ", qr='" + qr + '\'' +
                ", vcMaterialsBatch='" + vcMaterialsBatch + '\'' +
                ", lMaterialsId='" + lMaterialsId + '\'' +
                ", vcMaterialsCode='" + vcMaterialsCode + '\'' +
                ", vcMaterialsModel='" + vcMaterialsModel + '\'' +
                ", vcSeatCode='" + vcSeatCode + '\'' +
                ", lTaskId='" + lTaskId + '\'' +
                ", lValid=" + lValid +
                ", ifRow=" + ifRow +
                ", status='" + status + '\'' +
                ", erro='" + erro + '\'' +
                ", lN=" + lN +
                ", vcTaskNumber='" + vcTaskNumber + '\'' +
                ", seatSum=" + seatSum +
                ", seatUsed=" + seatUsed +
                ", seatUnused=" + seatUnused +
                '}';
    }
}
