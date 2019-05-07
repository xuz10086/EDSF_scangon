package com.indusfo.edzn.scangon.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.indusfo.edzn.scangon.R;
import com.indusfo.edzn.scangon.bean.Device;
import com.indusfo.edzn.scangon.utils.DeviceThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SettingDeviceAdapter extends ArrayAdapter<Device> {

    private Context context;
    private List<Device> deviceList;
    private int resourceId;
    private HashMap<String, Boolean> deviceMap;

    public SettingDeviceAdapter(Context context, int resource, List<Device> objects) {
        super(context, resource, objects);
        this.context = context;
        deviceList = objects;
        resourceId = resource;
    }

    @Override
    public int getCount() {
        return deviceList!=null?deviceList.size():0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Device device = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.deviceText = convertView.findViewById(R.id.device_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.deviceText.setText("设备编码:" + device.getVcDeviceCode());
        viewHolder.deviceText.setHint(device.getlDeviceId());

        viewHolder.deviceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                // 获取线程中已存在的设备集合
                deviceMap = DeviceThread.get();
                if (checkedTextView.isChecked()) {
                    checkedTextView.setChecked(false);
                    deviceMap.put((String) checkedTextView.getHint(), false);
                } else {
                    checkedTextView.setChecked(true);
                    deviceMap.put((String) checkedTextView.getHint(), true);
                }
                DeviceThread.set(deviceMap);
            }
        });

        return convertView;
    }

    @Override
    public Device getItem(int position) {
        return deviceList!=null?deviceList.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    class ViewHolder {
        CheckedTextView deviceText;
    }
}
