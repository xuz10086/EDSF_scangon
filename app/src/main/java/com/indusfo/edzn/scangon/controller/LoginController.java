package com.indusfo.edzn.scangon.controller;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.bean.User;
import com.indusfo.edzn.scangon.cons.AppParams;
import com.indusfo.edzn.scangon.cons.IdiyMessage;
import com.indusfo.edzn.scangon.cons.NetworkConst;
import com.indusfo.edzn.scangon.db.UserDb;
import com.indusfo.edzn.scangon.utils.AESUtils;
import com.indusfo.edzn.scangon.utils.CookieUtil;
import com.indusfo.edzn.scangon.utils.NetworkUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;


public class LoginController extends BaseController{

    private UserDb userDb;
    private File cookieFile = new File(AppParams.COOKIE_FILE_DIR, AppParams.COOKIE_FILE_NAME);
    private String cookie = CookieUtil.getCookie(cookieFile);

    public LoginController(Context c) {
        super(c);
        userDb = new UserDb(mContext);
    }

    @Override
    protected void handleMessage(int action, Object... values) {

        switch (action) {
            case IdiyMessage.LOGIN_ACTION:
                // 登录请求
                RResult rResult = doLogin(NetworkConst.LOGIN_URL,(String) values[0], (String) values[1]);
                mListener.onModeChange(IdiyMessage.LOGIN_ACTION_RESULT, rResult);
                break;
            case IdiyMessage.SAVE_USER_TODB:
                boolean saveUserToDb = saveUserToDb((String)values[0], (String)values[1], (Integer) values[2], (String)values[3]);
                mListener.onModeChange(IdiyMessage.SAVE_USER_TODB_RESULT, saveUserToDb);
                break;
            case IdiyMessage.GET_USER_FROM_DB:
                User user = queryUser();
                mListener.onModeChange(IdiyMessage.GET_USER_FROM_DB_RESULT, user);
                break;
            case IdiyMessage.SAVE_COOKIE:
                saveCookie((String)values[0]);
                break;
            default:
                break;
        }
    }

    /**
     * 查询数据库用户信息
     *
     * @author xuz
     * @date 2019/1/11 12:50 PM
     * @param []
     * @return com.indusfo.edzn.scangon.bean.User
     */
    private User queryUser() {
        User user = userDb.queryUser();
        if (user != null) {
            try {
                user.setUsername(AESUtils.decrypt(user.getUsername()));
                user.setPassword(AESUtils.decrypt(user.getPassword()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return user;
        }
        return null;
    }

    /**
     * 保存用户数据到用户表
     *
     * @author xuz
     * @date 2019/1/11 12:33 PM
     * @param [username, password, ifsave, userId]
     * @return boolean
     */
    private boolean saveUserToDb(String username, String password, Integer ifsave, String userId) {
        // 清空用户表数据
        userDb.clearUser();
        // 可逆性加密
        try {
            username = AESUtils.encrypt(username);
            password = AESUtils.encrypt(password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userDb.saveUser(username, password, ifsave, userId);
    }

    /**
     * 发送POST请求，登录
     * 获取服务器的登录响应信息，并且得到SESSIONID
     *
     * @author xuz
     * @date 2019/1/4 9:57 AM
     * @param [loginUrl, username, password]
     * @return com.indusfo.edzn.scangon.bean.RResult
     */
    private RResult doLogin(String loginUrl, String username, String password) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("vcUserCode", username);
        params.put("vcUserPwd", password);
        List<String> list = NetworkUtil.getCookieAndData(loginUrl, params);

        String json = null;
        String cookie = null;
        try {
            json = list.get(0);
            cookie = list.get(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (json.isEmpty()) {
            return null;
        }
        RResult rResult = JSON.parseObject(json, RResult.class);
        rResult.setCookie(cookie);
        return rResult;
    }

    /**
     * 保存cookie到本地缓存
     *
     * @author xuz
     * @date 2019/1/11 4:14 PM
     * @param [value, value1]
     * @return void
     */
    private void saveCookie(String cookie) {

//        File cookieFile = new File(fileDir, AppParams.COOKIE_FILE_NAME);
//        CookieUtil.saveCookie(cookie, cookieFile);
        CookieUtil.saveCookie(cookie, cookieFile);
    }

}
