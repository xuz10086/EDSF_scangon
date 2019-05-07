package com.indusfo.edzn.scangon.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.indusfo.edzn.scangon.R;


import java.util.List;

public abstract class MyAdapter<T> extends BaseAdapter {

    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected Context context;

    public MyAdapter(Context c) {
        context = c;
        mInflater = LayoutInflater.from(c);
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas!=null?mDatas.size():0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        TextView tv=null;
        if (convertView==null) {
            convertView = mInflater.inflate(R.layout.my_list_item_textview, null);
            tv=(TextView) convertView.findViewById(R.id.textview1);
            convertView.setTag(tv);
        }else {
            tv=(TextView) convertView.getTag();
        }
        final String text = setText(position, tv);
        /*// 选中后将数据传递给ScanTaskActivity
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ScanTaskActivity.class);
                intent.putExtra("text", text);
                *//*mListener.getText(text);
                ActivityUtils.start( context, ScanTaskActivity.class , true);*//*
            }
        });*/
        return convertView;
    }

    public String setText(int position, TextView tv){
        // 由子类实现
        return "";
    }

    @Override
    public Object getItem(int position) {
        return mDatas!=null?mDatas.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
