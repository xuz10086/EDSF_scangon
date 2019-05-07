package com.indusfo.edzn.scangon.bean;

import java.io.Serializable;

/**
 * 用户
 *
 * @author xuz
 * @date 2019/1/11 2:16 PM
 */
public class User implements Serializable {

    private static final long serialVersionUID = -4724609622839827693L;

    private String username;
    private String password;
    private Integer ifsave;
    private String userId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIfsave() {
        return ifsave;
    }

    public void setIfsave(Integer ifsave) {
        this.ifsave = ifsave;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", ifsave=" + ifsave +
                ", userId='" + userId + '\'' +
                '}';
    }
}
