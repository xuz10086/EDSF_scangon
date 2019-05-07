package com.indusfo.edzn.scangon.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.indusfo.edzn.scangon.R;
import com.indusfo.edzn.scangon.adpater.TaskAdapter;
import com.indusfo.edzn.scangon.adpater.TaskSlideAdapter;
import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.bean.Task;
import com.indusfo.edzn.scangon.cons.IdiyMessage;
import com.indusfo.edzn.scangon.controller.SettingDeviceController;
import com.indusfo.edzn.scangon.controller.UnfinishedTaskController;
import com.indusfo.edzn.scangon.utils.ActivityUtils;
import com.indusfo.edzn.scangon.utils.TopBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class UnfinishedTaskActivity extends BaseActivity {

//    private TableLayout tableLayout;
//    private Button taskFront;
//    private Button taskNext;
//    private Button taskSkip;
//    private TextView taskPagecurrent;
//    private EditText taskPageText;
    private SettingDeviceController settingDeviceController;
//    private Button createTask;
    private TaskSlideAdapter taskAdapter;
    private ListView listView;
    private TextView taskDeviceText;
    private TopBar topBar;

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    // 用于计算文本框的输入间隔时间
    private long editTime = 0;
    String[] lDeviceIds;
    // 分页
//    Integer pageindex = 1;
//    Integer pagecurrent = 1;
//    Integer lCounts;

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case IdiyMessage.QUERY_UNFINISHED_TASK_RESULT:
                handleUnfinishedTaskResult(msg);
                break;
            case IdiyMessage.CLOSE_TASK_RESULT:
                handleCloseTaskResult(msg);
                break;
            default:
                break;
        }
    }

    private void handleCloseTaskResult(Message msg) {
        final RResult rResult = (RResult) msg.obj;
        if (null == rResult || null == rResult.getStatus()) {
            tip("网络连接错误");
            return;
        }
        // 状态为200，则为扫描完的状态，直接删除
        if ("200".equals(rResult.getStatus())) {
            tip(rResult.getMsg());

            // 刷新页面
            // 最后一条数据一直存在，清掉
            taskAdapter.clear();
            onResume();
        }

        if ("402".equals(rResult.getStatus())) {
            // 弹框
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("关闭扫码单").setMessage(rResult.getMsg()).setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mController.sendAsynMessage(IdiyMessage.CLOSE_TASK, rResult.getData(), "true");
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create();
            alertDialog.show();
        }

    }

    private void handleUnfinishedTaskResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult || null == rResult.getStatus()) {
            tip("网络连接错误");
            return;
        }
        // 状态为200，跳转到主页
        if ("200".equals(rResult.getStatus())) {
//            lCounts = rResult.getlCounts();
//            taskPagecurrent.setText(pagecurrent+"/"+(lCounts/AppParams.TASK_PAGESIZE+(lCounts%AppParams.TASK_PAGESIZE==0?0:1)));
            String data = rResult.getData();
            List<Task> taskList = JSON.parseArray(data, Task.class);
            taskAdapter = new TaskSlideAdapter(this,this, R.layout.item_list_view_task, taskList);
            listView.setAdapter(taskAdapter);
            taskAdapter.notifyDataSetChanged();
            // 拿到数据，生成表格
//            makeTable(rResult.getData());
        }

        if(!rResult.isOk() || rResult.getData().isEmpty()) {
            tip(rResult.getMsg());
            taskAdapter = new TaskSlideAdapter(this,this, R.layout.item_list_view_task, null);
            listView.setAdapter(taskAdapter);
            taskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfinished_task);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        initController();
        initUI();
        // 从服务器拿到未完成任务数据
        getUnfinishedTaskData();
    }

    // 自动刷新
    @Override
    protected void onResume() {
        super.onResume();
        initController();
        initUI();
        taskDeviceText.setText("");
        // 从服务器拿到未完成任务数据
        getUnfinishedTaskData();
    }

    private void initController() {
        mController = new UnfinishedTaskController(this);
        mController.setIModeChangeListener(this);

//        settingDeviceController = new SettingDeviceController(this);
//        settingDeviceController.setIModeChangeListener(this);
    }

    private void initUI() {
        //Toast.makeText(this, xdpi +"/"+ydpi, Toast.LENGTH_SHORT).show(); 分辨率
        //tableView = findViewById(R.id.unfinishedTable);
//        tableLayout = findViewById(R.id.unfinished_task);
//        taskFront = findViewById(R.id.task_front);
//        taskNext = findViewById(R.id.task_next);
//        taskSkip = findViewById(R.id.task_skip);
//        taskPagecurrent = findViewById(R.id.task_pagecurrent);
//        taskPageText = findViewById(R.id.task_page_text);
//        createTask = findViewById(R.id.create_task);
        listView = findViewById(R.id.list_view_task);
        taskDeviceText = findViewById(R.id.task_device_text);
        topBar = (TopBar) findViewById(R.id.unfinishedTask_topbar);

        /*taskDeviceText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mController.sendAsynMessage(IdiyMessage.QUERY_UNFINISHED_TASK, taskDeviceText.getText().toString());
            }
        });*/
        // 设置文本变化监听
        taskDeviceText.addTextChangedListener(textWatcher);

     /*   createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.start(UnfinishedTaskActivity.this, ScanTaskActivity.class, false);
            }
        });*/

        topBar.setOnLeftAndRightClickListener(new TopBar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    //弹出提示，可以有多种方式
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }

            @Override
            public void OnRightButtonClick() {
                ActivityUtils.start(UnfinishedTaskActivity.this, ScanTaskActivity.class, false);
            }
        });

/*        taskFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 查询前一百条数据
                if (pagecurrent <= 1) {
                    tip("已经是第一页了");
                    return;
                }
                pagecurrent -= 1;
                pageindex -= 1;

                mController.sendAsynMessage(IdiyMessage.QUERY_UNFINISHED_TASK, pageindex+"", AppParams.TASK_PAGESIZE+"");
            }
        });*/

  /*      taskNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 查询后一百条数据
                pagecurrent += 1;
                pageindex += 1;

                mController.sendAsynMessage(IdiyMessage.QUERY_UNFINISHED_TASK, pageindex+"", AppParams.TASK_PAGESIZE+"");
            }
        });*/

    /*    taskSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取跳转页码
                if (taskPageText.getText().toString().isEmpty()) { // 这里需要验证输入的参数
                    tip("请填写跳转页码");
                    return;
                }
                pagecurrent = Integer.valueOf(taskPageText.getText().toString());
                pageindex = pagecurrent;

                mController.sendAsynMessage(IdiyMessage.QUERY_UNFINISHED_TASK, pageindex+"", AppParams.TASK_PAGESIZE+"");
            }
        });*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

        }

        // 编辑后发送网络请求，模糊查询扫码单
        @Override
        public void afterTextChanged(Editable editable) {

            if ((System.currentTimeMillis() - editTime) > 300 || taskDeviceText.getText().toString().equals("")) {
                mController.sendAsynMessage(IdiyMessage.QUERY_UNFINISHED_TASK, taskDeviceText.getText().toString());
                editTime = System.currentTimeMillis();
            } else {
                return;
            }
//            mController.sendAsynMessage(IdiyMessage.QUERY_UNFINISHED_TASK, taskDeviceText.getText().toString());
        }
    };

    /**
     * 发送网络请求，获取未完成任务的数据
     *  
     * @author xuz
     * @date 2019/1/4 3:28 PM
     * @param []
     * @return void
     */
    private void getUnfinishedTaskData() {
//        // 查询设备表中的选中设备id,然后查询扫码单
//        settingDeviceController.sendAsynMessage(IdiyMessage.GET_DEVICE_FROM_DB);

        // 发送网络请求，根据设备id查询扫码单
        mController.sendAsynMessage(IdiyMessage.QUERY_UNFINISHED_TASK, "");

    }

   /* private void makeTable(String data) {
        List<UnfinishedTask> unfinishedTaskList = JSON.parseArray(data, UnfinishedTask.class);
        unfinishedTaskList.get(0);
        tableView.clearTableContents()
                .setHeader("任务单号","物料编码","设备号","版本号","创建时间","创建人","操作");
        for (UnfinishedTask uTask: unfinishedTaskList) {
            tableView.addContent(uTask.getVcTaskNumber(), uTask.getVcMaterialsVerName(),
                    uTask.getVcDeviceCode(), uTask.getlMaterialsVerId()+"",uTask.getdCreateTime(),uTask.getVcUserName(),"");
        }
        tableView.refreshTable();
    }*/

   /**
    * 生成表格
    *  
    * @author xuz
    * @date 2019/1/9 8:36 PM
    * @param [data]
    * @return void
    */
  /* private void makeTable(String data) {

       // 初始化表格
       try {
           int count = tableLayout.getChildCount() - 1;
           tableLayout.removeViews(1, count);
       } catch (Exception e) {
           Log.w("此行不存在", "删除失败");
       }

       // 生成行
       List<UnfinishedTask> unfinishedTaskList = JSON.parseArray(data, UnfinishedTask.class);
       for (Iterator iterators = unfinishedTaskList.iterator(); iterators.hasNext();) {

           LayoutInflater inflater = LayoutInflater.from(UnfinishedTaskActivity.this);
           final UnfinishedTask unfinishedTask = (UnfinishedTask) iterators.next();
           TableRow row = new TableRow(UnfinishedTaskActivity.this);
           row.setBackgroundColor(Color.rgb(222, 220, 210));
           row.setPadding( 1, 1, 1, 1);

           TextView tv1 = inflater.inflate(R.layout.my_tablelayout_textview, null).findViewById(R.id.textview2);
           TextView tv2 = inflater.inflate(R.layout.my_tablelayout_textview, null).findViewById(R.id.textview2);
           TextView tv3 = inflater.inflate(R.layout.my_tablelayout_textview, null).findViewById(R.id.textview2);
           TextView tv4 = inflater.inflate(R.layout.my_tablelayout_textview, null).findViewById(R.id.textview2);
           TextView tv5 = inflater.inflate(R.layout.my_tablelayout_textview, null).findViewById(R.id.textview2);
           TextView tv6 = inflater.inflate(R.layout.my_tablelayout_textview, null).findViewById(R.id.textview2);

           tv1.setText(unfinishedTask.getVcScanNumber());
           row.addView(tv1);
           tv2.setText(unfinishedTask.getVcProductModel());
           row.addView(tv2);
           tv3.setText(unfinishedTask.getVcDeviceCode());
           row.addView(tv3);
           tv4.setText(unfinishedTask.getVcMaterialsVerName());
           row.addView(tv4);
           tv5.setText(unfinishedTask.getdCreateTime());
           row.addView(tv5);
           tv6.setText(unfinishedTask.getVcUserName());
           row.addView(tv6);

           tableLayout.addView(row, new TableLayout.LayoutParams(
                   TableRow.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

           // 给每一行设置一个点击监听，跳转到ScanTaskActivity界面
           row.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent = new Intent(UnfinishedTaskActivity.this, ScanTaskActivity.class);
                   intent.putExtra("lTaskId", unfinishedTask.getlTaskId()+"");
                   startActivity(intent);
               }
           });
       }

   }*/
}
