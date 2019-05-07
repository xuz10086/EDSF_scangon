package com.indusfo.edzn.scangon.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Cookie工具类
 *  
 * @author xuz
 * @date 2019/1/11 2:18 PM
 */
public class CookieUtil {

    private static File dataFile = Environment.getDownloadCacheDirectory();
    private static String filePath = dataFile.getAbsolutePath();
    /**
     * 获取Cookie
     *
     * @author xuz
     * @date 2019/1/5 9:04 AM
     * @param [conn]
     * @return void
     */
    public static String getCookie(HttpURLConnection conn) {
        Map<String, List<String>> cookie_map = conn.getHeaderFields();
        List<String> cookies = cookie_map.get("Set-Cookie");
        if (null != cookies && 0 < cookies.size()) {
            String s = "";
            for (String cookie : cookies) {
                if (s.isEmpty()) {
                    s = cookie;
                } else {
                    s += ";" + cookie;
                }
            }
//            Log.w("ss", s);
            return s;
            // JSESSIONID=D4357788F5F2735440F0ACE291D532D2; Path=/; HttpOnly;rememberMe=deleteMe; Path=/; Max-Age=0; Expires=Fri, 11-Jan-2019 05:07:05 GMT
        }
        return "";
    }

    /**
     * 保存Cookie
     *
     * @author xuz
     * @date 2019/1/5 9:37 AM
     * @param [cookie]
     * @return void
     */
    public static void saveCookie(String cookie,File cookieFile) {
//        Log.e("dataFile", dataFile.getAbsolutePath());
//        File cookieFile = new File(filePath, "cookieFile.txt");
        try {
            cookieFile.createNewFile();
            BufferedWriter bufw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cookieFile)));
            bufw.write(cookie);
            bufw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Cookie
     *
     * @author xuz
     * @date 2019/1/5 9:40 AM
     * @param []
     * @return java.lang.String
     */
    public static String getCookie(File cookieFile) {
//        File cookieFile = new File(filePath, "cookieFile.txt");
        try {
            if (cookieFile.exists() && cookieFile.length() >0) {
                BufferedReader bufr = new BufferedReader(new InputStreamReader(new FileInputStream(cookieFile)));
                String line = bufr.readLine();
//                Log.e("cookie", line);
                bufr.close();
                String cookie = line.split(";")[0];
                // JSESSIONID=D4357788F5F2735440F0ACE291D532D2
                return cookie;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
