package com.indusfo.edzn.scangon.bean;

import java.io.Serializable;

public class VersionInfo implements Serializable {

    private static final long serialVersionUID = 2720044883179131812L;

    private String id;
    private String appName; // App名
    private String versionName;//版本名
    private int versionCode;//版本号
    private String versionDesc;//版本描述信息内容
    private String downloadUrl;//新版本的下载路径
    private String versionSize;//版本大小

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersionSize() {
        return versionSize;
    }

    public void setVersionSize(String versionSize) {
        this.versionSize = versionSize;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "id='" + id + '\'' +
                ", appName='" + appName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", versionDesc='" + versionDesc + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", versionSize='" + versionSize + '\'' +
                '}';
    }
}
