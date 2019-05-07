package com.indusfo.edzn.scangon.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.controller.BaseController;
import com.indusfo.edzn.scangon.listener.IModeChangeListener;
import com.indusfo.edzn.scangon.utils.NetworkUtil;

public abstract class BaseActivity extends AppCompatActivity implements IModeChangeListener {

    protected BaseController mController;
    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            handlerMessage(msg);
        }
    };
    private static Toast toast;

    protected void handlerMessage(Message msg) {
        // default Empty implementn
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 判断文本框输入的值是否为空
     *
     * @author xuz
     * @date 2019/1/4 9:17 AM
     * @param [values]
     * @return boolean
     */
    protected boolean ifValueWasEmpty(String... values) {

        for (String value : values) {
            if (TextUtils.isEmpty(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 提示内容
     *
     * @author xuz
     * @date 2019/1/4 9:21 AM
     * @param [tipStr]
     * @return void
     */
    public void tip(String tipStr) {
//        Toast.makeText(this, tipStr, Toast.LENGTH_SHORT).show();
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), tipStr, Toast.LENGTH_SHORT);
        } else {
            toast.setText(tipStr);
        }
        toast.show();

    }

    @Override
    public void onModeChange(int action, Object... values) {
        mHandler.obtainMessage(action, values[0]).sendToTarget();
    }
}
