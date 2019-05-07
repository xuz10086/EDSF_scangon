package com.indusfo.edzn.scangon.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 网络请求
 *  
 * @author xuz
 * @date 2019/1/11 2:18 PM
 */
public class NetworkUtil {

    private static String cookie;
    private static int timeOut = 5000;

    /**
     * 用于网络检测方面
     */
    // 没有网络
    public static final int NONETWORK = 0;
    // 当前是wifi连接
    public static final int WIFI = 1;
    // 不是wifi连接
    public static final int NOWIFI = 2;

    // -------------------------------- 网络请求 ----------------------------
    /**
     * 发送GET请求
     * 不完善，以后要用再加
     *  
     * @author xuz
     * @date 2019/1/12 2:54 PM
     * @param [urlPath]
     * @return java.lang.String
     */
    public static String doGet(String urlPath){
        HttpURLConnection conn = null;
        try {
            URL url=new URL(urlPath);
            conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode()==200) {
                InputStream is = conn.getInputStream();
                BufferedReader buf=new BufferedReader(new InputStreamReader(is));
                return buf.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 关闭此次的网络请求
            conn.disconnect();
            // 通过接口对象调用抽象方法的fresh()方法
//            refresh.fresh();
        }
        return "";
    }
    
    /**
     * 发送POST请求
     *  
     * @author xuz
     * @date 2019/1/12 2:54 PM
     * @param [urlPath, params]
     * @return java.lang.String
     */
    public static String doPost(String urlPath,HashMap<String, String> params){

        HttpURLConnection conn = null;
        try {
            URL url=new URL(urlPath);
            conn=(HttpURLConnection) url.openConnection();
            //conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut);
            // 获取本地缓存中的cookie
//			--------------------------------------------
            // 设置URL的params参数
            String paramStr="";
            Set<HashMap.Entry<String, String>> entrySet = params.entrySet();
            for (HashMap.Entry<String, String> entry : entrySet) {
                paramStr+=("&"+entry.getKey()+"="+entry.getValue());
            }

            if (!params.isEmpty()) {
                paramStr = paramStr.substring(1);
            }
//			--------------------------------------------
            conn.setDoOutput(true);
            conn.getOutputStream().write(paramStr.getBytes());

            if (conn.getResponseCode()==200) {
                InputStream is = conn.getInputStream();
                BufferedReader buf=new BufferedReader(new InputStreamReader(is));
                String data = buf.readLine();
                buf.close();
                conn.disconnect();
                return data;
            }
            // 关闭连接
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            // 关闭此次的网络请求
            conn.disconnect();
            // 通过接口对象调用抽象方法的fresh()方法
//            refresh.fresh();
        }
        return "";
    }

    /**
     * 这个方法用来得到登录后服务器传来的sessionId
     *
     * @author xuz
     * @date 2019/1/11 3:12 PM
     * @param [urlPath, params]
     * @return java.lang.String
     */
    public static List<String> getCookieAndData(String urlPath, HashMap<String, String> params){
        ArrayList<String> list = new ArrayList<String>();
        HttpURLConnection conn = null;
        try {
            URL url=new URL(urlPath);
            conn=(HttpURLConnection) url.openConnection();
            //conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut);

//			--------------------------------------------
            // 设置URL的params参数
            String paramStr="";
            Set<HashMap.Entry<String, String>> entrySet = params.entrySet();
            for (HashMap.Entry<String, String> entry : entrySet) {
                paramStr+=("&"+entry.getKey()+"="+entry.getValue());
            }

            if (!params.isEmpty()) {
                paramStr = paramStr.substring(1);
            }
//			--------------------------------------------
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("Content-Length", paramStr.length() + "");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.getOutputStream().write(paramStr.getBytes());

            if (conn.getResponseCode()==200) {
                InputStream is = conn.getInputStream();
                BufferedReader buf=new BufferedReader(new InputStreamReader(is));
                String data = buf.readLine();
                buf.close();
                cookie = CookieUtil.getCookie(conn);
                list.add(data);
                list.add(cookie);
                conn.disconnect();
                return list;
            }
            // 关闭连接
            conn.disconnect();
        } catch (Exception e3) {
            e3.printStackTrace();
            // 关闭此次的网络请求
            conn.disconnect();
            // 通过接口对象调用抽象方法的fresh()方法
//            refresh.fresh();
        }
        return list;
    }

    /**
     * 发送带有cookie信息的请求
     *
     * @author xuz
     * @date 2019/1/12 8:46 AM
     * @param [urlPath, params, cookie]
     * @return java.lang.String
     */
    public static String doPostSetCookie(String urlPath,HashMap<String, String> params, String cookie){

        HttpURLConnection conn = null;
        try {
            URL url=new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            //conn.setInstanceFollowRedirects(false);
            // 设置cookie信息
            if (!cookie.isEmpty()) {
                conn.setRequestProperty("Cookie", cookie);
            }
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut);
//			--------------------------------------------
            // 设置URL的params参数
            String paramStr="";
            Set<HashMap.Entry<String, String>> entrySet = params.entrySet();
            for (HashMap.Entry<String, String> entry : entrySet) {
                paramStr+=("&"+entry.getKey()+"="+entry.getValue());
            }

            if (!params.isEmpty()) {
                paramStr = paramStr.substring(1);
            }
//			--------------------------------------------
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("Content-Length", paramStr.length() + "");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.getOutputStream().write(paramStr.getBytes()); 
            
            if (conn.getResponseCode()==200) {
                InputStream is = conn.getInputStream();
                BufferedReader buf=new BufferedReader(new InputStreamReader(is));
                String data = buf.readLine();
                buf.close();
                conn.disconnect();
                return data;
            }
            // 关闭连接
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            // 关闭此次的网络请求
            conn.disconnect();
            // 通过接口对象调用抽象方法的fresh()方法
//            refresh.fresh();
        }
        return "";
    }

    // -------------------------------- 网络检测 -----------------------------------

    /**
     * 检测当前网络的类型 是否是wifi
     * @param context
     * @return
     */
    public static int checkedNetWorkType(Context context){
        if(!checkedNetWork(context)){
            return NONETWORK;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting() ){
            return WIFI;
        }else{
            return NOWIFI;
        }
    }


    /**
     * 检查是否连接网络
     * @param context
     * @return
     */
    public static boolean  checkedNetWork(Context context){
        // 1.获得连接设备管理器
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm == null) return false;
        /**
         * 获取网络连接对象
         */
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo == null || !networkInfo.isAvailable()){
            return false;
        }
        return true;
    }

}
