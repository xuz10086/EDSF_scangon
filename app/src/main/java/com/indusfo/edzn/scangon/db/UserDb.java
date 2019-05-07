package com.indusfo.edzn.scangon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.indusfo.edzn.scangon.bean.User;
import com.indusfo.edzn.scangon.cons.AppParams;

/**
 * 用户表操作类
 *
 * @author xuz
 * @date 2019/1/11 10:02 AM
 */
public class UserDb  {

    private DbOpenHelper userHelper;

    public UserDb(Context c) {
        userHelper = new DbOpenHelper(c);
    }

    // 保存操作
    public boolean saveUser(String username, String paasword, Integer ifsave, String userId) {

        SQLiteDatabase db = userHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppParams.USERNAME, username);
        values.put(AppParams.PASSWORD, paasword);
        values.put(AppParams.IFSAVE, ifsave);
        values.put(AppParams.USERID, userId);
        long insertId = db.insert(AppParams.USER_TABLE_NAME, null, values);
        return insertId != -1;

    }

    // 清空数据库表
    public void clearUser() {
        SQLiteDatabase db = userHelper.getWritableDatabase();
        db.delete(AppParams.USER_TABLE_NAME, null, null);
    }

    // 查询操作
    public User queryUser() {
        SQLiteDatabase db = userHelper.getWritableDatabase();
        Cursor cursor = db.query(AppParams.USER_TABLE_NAME, new String[]{AppParams.USERNAME, AppParams.PASSWORD, AppParams.IFSAVE, AppParams.USERID},
                null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            User user = new User();
            user.setUsername(cursor.getString(0));
            user.setPassword(cursor.getString(1));
            user.setIfsave(Integer.valueOf(cursor.getString(2)));
            user.setUserId(cursor.getString(3));
            return user;
        }
        return null;
    }
}
