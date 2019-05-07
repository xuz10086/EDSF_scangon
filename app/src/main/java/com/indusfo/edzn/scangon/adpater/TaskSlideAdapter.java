package com.indusfo.edzn.scangon.adpater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.indusfo.edzn.scangon.R;
import com.indusfo.edzn.scangon.activity.ScanTaskActivity;
import com.indusfo.edzn.scangon.activity.UnfinishedTaskActivity;
import com.indusfo.edzn.scangon.bean.Task;
import com.indusfo.edzn.scangon.cons.IdiyMessage;
import com.indusfo.edzn.scangon.controller.UnfinishedTaskController;
import com.roamer.slidelistview.SlideBaseAdapter;

import java.util.List;

public class TaskSlideAdapter extends SlideBaseAdapter {

    private Context context;
    private List<Task> taskList;
    private int resourceId;
    private UnfinishedTaskController unfinishedTaskController;
    private UnfinishedTaskActivity unfinishedTaskActivity;

    public TaskSlideAdapter(UnfinishedTaskActivity activity, Context context, int resource, List<Task> objects) {
        super(context);
        this.context = context;
        taskList = objects;
        resourceId = resource;
        this.unfinishedTaskActivity = activity;

        unfinishedTaskController = new UnfinishedTaskController(context);
        unfinishedTaskController.setIModeChangeListener(activity);
    }

    @Override
    public int getCount() {
        return taskList!=null?taskList.size():0;
    }

    @Override
    public Task getItem(int position) {
        return taskList!=null?taskList.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Task task = getItem(position);
        // 内部类
        TaskSlideAdapter.ViewHolder viewHolder;

        /* getView()方法中的converView参数表示之前加载的布局 */
        if (convertView == null) {
            /* 如果converView参数值为null，则使用LayoutInflater去加载布局 */
            convertView = createConvertView(position);
            viewHolder = new ViewHolder();

            viewHolder.theFirst = (TextView) convertView.findViewById(R.id.the_first);
            viewHolder.theSecond = (TextView) convertView.findViewById(R.id.the_second);
            viewHolder.theThrid = (TextView) convertView.findViewById(R.id.the_thrid);
            viewHolder.delete = convertView.findViewById(R.id.delete_task);
            convertView.setTag(viewHolder);
        } else {
            /* 否则，直接对converView进行重用 */
            viewHolder = (TaskSlideAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.theFirst.setText("设备号:" + task.getVcDeviceCode() + "  创建时间:" + task.getdCreateTime());
        viewHolder.theSecond.setText("创建人:" + task.getVcUserName() + "  版本号:" + task.getVcMaterialsVerName());
        viewHolder.theThrid.setText("扫码单:" + task.getVcScanNumber() + "  物料:" + task.getVcMaterialsCode());

        // 事件监听
        final LinearLayout listViewItem = convertView.findViewById(R.id.task_linear);
        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ScanTaskActivity.class);
                intent.putExtra("lTaskId", task.getlTaskId()+"");
                context.startActivity(intent);
            }
        });
       /* viewHolder.theFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ScanTaskActivity.class);
                intent.putExtra("lTaskId", task.getlTaskId()+"");
                context.startActivity(intent);
            }
        });
        viewHolder.theSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ScanTaskActivity.class);
                intent.putExtra("lTaskId", task.getlTaskId()+"");
                context.startActivity(intent);
            }
        });
        viewHolder.theThrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ScanTaskActivity.class);
                intent.putExtra("lTaskId", task.getlTaskId()+"");
                context.startActivity(intent);
            }
        });*/

        viewHolder.delete = convertView.findViewById(R.id.delete_task);
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 弹框
                AlertDialog alertDialog = new AlertDialog.Builder(unfinishedTaskActivity)
                        .setTitle("关闭扫码单").setMessage("是否关闭扫码单").setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                unfinishedTaskController.sendAsynMessage(IdiyMessage.CLOSE_TASK, task.getlTaskId(), "false");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create();
                alertDialog.show();
//                unfinishedTaskController.sendAsynMessage(IdiyMessage.CLOSE_TASK, task.getlTaskId(), "false");
            }
        });
        return convertView;
    }

    @Override
    public int getFrontViewId(int position) {
        return R.layout.item_list_view_task;
    }

    @Override
    public int getLeftBackViewId(int position) {
        /*if (position % 2 == 0) {
            return R.layout.activity_main_list_view_item_right;
        }*/
        return R.layout.item_list_view_task_left;
    }

    @Override
    public int getRightBackViewId(int position) {
        return R.layout.item_list_view_task_right;
    }

    /**
     * 清空数据
     *
     * @author xuz
     * @date 2019/3/22 10:01 AM
     * @param []
     * @return void
     */
    @Override
    public void clear() {
        taskList.clear();
//        notifyDataSetChanged();
    }

    class ViewHolder {
        LinearLayout taskLinear;
        TextView theFirst;
        TextView theSecond;
        TextView theThrid;
        View delete;
    }
}
