package com.indusfo.edzn.scangon.bean;

import java.io.Serializable;

/**
 * 封装服务器端返回结果
 *
 * @author xuz
 * @date 2019/1/11 2:15 PM
 * @param
 * @return
 */
public class RResult implements Serializable {

    private static final long serialVersionUID = -5562984834736752966L;
    private String status;
    private String msg;
    private String data;
    private Integer lCounts;
    private boolean ok;
    private String cookie;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getlCounts() {
        return lCounts;
    }

    public void setlCounts(Integer lCounts) {
        this.lCounts = lCounts;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
