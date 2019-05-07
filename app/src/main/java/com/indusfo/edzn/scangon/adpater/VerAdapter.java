package com.indusfo.edzn.scangon.adpater;

import android.content.Context;
import android.widget.TextView;

import com.indusfo.edzn.scangon.bean.Ver;

public class VerAdapter extends MyAdapter<Ver> {

    public VerAdapter(Context c) {
        super(c);
    }

    /**
     * 给TextView设置文本
     *
     * @author xuz
     * @date 2019/1/9 1:56 PM
     * @param [position, tv]
     * @return java.lang.String
     */
    @Override
    public String setText(int position, TextView tv) {
        String text = "版本号: " + mDatas.get(position).getVcMaterialsVerName();
        tv.setText(text);
        return text;
    }
}
