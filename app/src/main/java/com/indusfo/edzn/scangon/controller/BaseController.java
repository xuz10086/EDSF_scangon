package com.indusfo.edzn.scangon.controller;

import android.content.Context;

import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.listener.IModeChangeListener;

public abstract class BaseController {
    
    protected Context mContext;
    protected IModeChangeListener mListener;
    
    public BaseController(Context c) {
        mContext = c;
    }

    public void setIModeChangeListener(IModeChangeListener listener) {
        mListener = listener;
    }


    /**
     * 一个页面可能有多个请求
     *  
     * @author xuz
     * @date 2019/1/4 9:24 AM
     * @param [action, values] [用来区分请求，请求的数据]
     * @return void
     */
    public void sendAsynMessage(final int action, final Object... values) {
        new Thread() {
            public void run() {
                handleMessage(action, values);
            }
        }.start();
    }

    /**
     * 同步请求
     *
     * @author xuz
     * @date 2019/1/18 10:13 AM
     * @param [action, values]
     * @return void
     */
    public void sendUnAsynMessage(final int action, final Object... values) {
        handleMessage(action, values);
    }

    /**
     * 子类处理请求的业务代码
     *  
     * @author xuz
     * @date 2019/1/4 9:36 AM
     * @param [action, values]
     * @return void
     */
    protected abstract void handleMessage(int action, Object... values);
}
