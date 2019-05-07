package com.indusfo.edzn.scangon.adpater;

import android.content.Context;
import android.widget.TextView;

import com.indusfo.edzn.scangon.bean.Materials;

import java.util.Iterator;

public class MaterialCodeAdapter extends MyAdapter<Materials> {

    public MaterialCodeAdapter(Context c) {
        super(c);
    }

    /**
     * 给TextView设置文本
     *  
     * @author xuz
     * @date 2019/1/7 11:49 AM
     * @param [position, tv]
     * @return void
     */
    @Override
    public String setText(int position, TextView tv) {
        String text = "编码: "+ mDatas.get(position).getVcMaterialsCode() + " ；\n物料描述: " + mDatas.get(position).getVcMaterialsModel();
        tv.setText(text);
        return text;
    }
}
