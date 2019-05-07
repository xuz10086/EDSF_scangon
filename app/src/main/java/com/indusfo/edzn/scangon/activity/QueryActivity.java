package com.indusfo.edzn.scangon.activity;

import android.content.Intent;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.indusfo.edzn.scangon.R;
import com.indusfo.edzn.scangon.adpater.DeviceCodeAdapter;
import com.indusfo.edzn.scangon.adpater.MaterialCodeAdapter;
import com.indusfo.edzn.scangon.adpater.VerAdapter;
import com.indusfo.edzn.scangon.bean.Device;
import com.indusfo.edzn.scangon.bean.Materials;
import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.bean.Ver;
import com.indusfo.edzn.scangon.cons.IdiyMessage;
import com.indusfo.edzn.scangon.cons.AppParams;
import com.indusfo.edzn.scangon.cons.NetworkConst;
import com.indusfo.edzn.scangon.controller.QueryController;
import com.indusfo.edzn.scangon.utils.TopBar;

import java.util.List;

public class QueryActivity extends BaseActivity {

    private ImageView search;
    private ListView lv;
    private MaterialCodeAdapter materialCodeAdapter;
    private DeviceCodeAdapter deviceCodeAdapter;
    private VerAdapter verAdapter;
    private EditText coditionText;
    private Button front;
    private Button next;
    private EditText pageText;
    private Button skip;
    private TextView pageCurrent;
    private TopBar topBar;

    private String coditionStr = "";
    private String pageStr = "";
    private int whichQuery;

    // 分页
    private Integer pageindex = 1;
    private Integer pagecurrent = 1;
    private Integer lCounts;

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case IdiyMessage.QUERY_MATERIALS_RESULT:
                handleQueryMaterialsCodeResult(msg);
                break;
            case IdiyMessage.QUERY_EQUIPMENT_RESULT:
                handleQueryEquipmentCodeResult(msg);
                break;
            case IdiyMessage.QUERY_VER_RESULT:
                handleQueryVerResult(msg);
                break;
            default:
                break;
        }
    }

    private void handleQueryVerResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult || null == rResult.getStatus()) {
            tip("网络连接错误");
            return;
        }
        // 状态为200
        if ("200".equals(rResult.getStatus())) {
            lCounts = rResult.getlCounts();
            lv.setAdapter(verAdapter);
            // 拿到数据，并将数据放到adapter里
            final List<Ver> verList = JSON.parseArray(rResult.getData(), Ver.class);
            verAdapter.setDatas(verList);
            verAdapter.notifyDataSetChanged();
            pageCurrent.setText(pagecurrent+"/"+(lCounts/AppParams.QUERY_PAGESIZE+1));

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("which", IdiyMessage.QUERY_VER_RESULT);
                    bundle.putString("id", verList.get(position).getlMaterialsVerId());
                    bundle.putString("code", verList.get(position).getVcMaterialsVerName());
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        if (!rResult.isOk() || rResult.getData().isEmpty()) {
            lv.setAdapter(null);
            tip(rResult.getMsg());
        }
    }

    private void handleQueryEquipmentCodeResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult || null == rResult.getStatus()) {
            tip("网络连接错误");
            return;
        }
        // 状态为200
        if ("200".equals(rResult.getStatus())) {
            lCounts = rResult.getlCounts();
            lv.setAdapter(deviceCodeAdapter);
            // 拿到数据，并将数据放到adapter里
            final List<Device> deviceList = JSON.parseArray(rResult.getData(), Device.class);
            deviceCodeAdapter.setDatas(deviceList);
            deviceCodeAdapter.notifyDataSetChanged();
            pageCurrent.setText(pagecurrent+"/"+(lCounts/AppParams.QUERY_PAGESIZE+1));

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("which", IdiyMessage.QUERY_EQUIPMENT_RESULT);
                    bundle.putString("id", deviceList.get(position).getlDeviceId());
                    bundle.putString("code", deviceList.get(position).getVcDeviceCode());
//                    Intent intent = new Intent(QueryActivity.this, ScanTaskActivity.class);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
//                    startActivity(intent);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        if (!rResult.isOk() || rResult.getData().isEmpty()) {
            lv.setAdapter(null);
            tip(rResult.getMsg());
        }
    }

    private void handleQueryMaterialsCodeResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult || null == rResult.getStatus()) {
            tip("网络连接错误");
            return;
        }
        // 状态为200
        if ("200".equals(rResult.getStatus())) {
            lCounts = rResult.getlCounts();
            lv.setAdapter(materialCodeAdapter);
            // 拿到数据，并将数据放到adapter里
            final List<Materials> materialsList = JSON.parseArray(rResult.getData(), Materials.class);
            materialCodeAdapter.setDatas(materialsList);
            materialCodeAdapter.notifyDataSetChanged();
            pageCurrent.setText(pagecurrent+"/"+(lCounts/AppParams.QUERY_PAGESIZE+(lCounts%AppParams.QUERY_PAGESIZE==0?0:1)));

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("which", IdiyMessage.QUERY_MATERIALS_RESULT);
                    bundle.putString("id", materialsList.get(position).getlMaterialsId());
                    bundle.putString("code", materialsList.get(position).getVcMaterialsCode());
//                    Intent intent = new Intent(QueryActivity.this, ScanTaskActivity.class);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
//                    startActivity(intent);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        if (!rResult.isOk() || rResult.getData().isEmpty()) {
            lv.setAdapter(null);
            tip(rResult.getMsg());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        initController();
        initUI();
    }

    private void initController() {
        mController = new QueryController(this);
        materialCodeAdapter = new MaterialCodeAdapter(this);
        deviceCodeAdapter = new DeviceCodeAdapter(this);
        verAdapter = new VerAdapter(this);
        mController.setIModeChangeListener(this);
    }

    private void initUI() {
        search = (ImageView) findViewById(R.id.search);
        coditionText = (EditText) findViewById(R.id.coditionText);
        lv = (ListView) findViewById(R.id.lv);
        front = (Button) findViewById(R.id.front);
        next = (Button) findViewById(R.id.next);
        pageText = (EditText) findViewById(R.id.pageText);
        skip = (Button) findViewById(R.id.skip);
        pageCurrent = findViewById(R.id.page_current);
        topBar = findViewById(R.id.query_topbar);

        // 接受上一个活动传递的数据
        Intent intent = getIntent();
//        String str = intent.getStringExtra("rightArrows");
//        int i = Integer.valueOf(str);
        whichQuery = intent.getIntExtra("rightArrows", 0);
        // 根据whichQuery，发送不同的请求，版本查询需要额外的参数
        if (whichQuery == IdiyMessage.QUERY_VER) {
            String materialsId = intent.getStringExtra("materialsId");
            String deviceId = intent.getStringExtra("deviceId");
            mController.sendAsynMessage(whichQuery, pageindex+"", AppParams.QUERY_PAGESIZE+"", coditionStr, materialsId, deviceId);
        } else {
            mController.sendAsynMessage(whichQuery, pageindex+"", AppParams.QUERY_PAGESIZE+"", coditionStr);
        }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取搜索框中的模糊查询条件
                coditionStr = coditionText.getText().toString();
                pageindex = 1;
                pagecurrent = 1;

                mController.sendAsynMessage(whichQuery, pageindex+"", AppParams.QUERY_PAGESIZE+"", coditionStr);
            }
        });

        // 设置文本框修改监听
        coditionText.addTextChangedListener(textWatcher);

        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取搜索框中的模糊查询条件
                coditionStr = coditionText.getText().toString();
               // 查询前一百条数据
                if (pagecurrent <= 1) {
                    tip("已经是第一页了");
                    return;
                }
                pagecurrent -= 1;
                pageindex -= 1;

                mController.sendAsynMessage(whichQuery, pageindex+"", AppParams.QUERY_PAGESIZE+"", coditionStr);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取搜索框中的模糊查询条件
                coditionStr = coditionText.getText().toString();
               // 查询后一百条数据
                pagecurrent += 1;
                pageindex += 1;

                mController.sendAsynMessage(whichQuery, pageindex+"", AppParams.QUERY_PAGESIZE+"", coditionStr);
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取搜索框中的模糊查询条件
                coditionStr = coditionText.getText().toString();

                // 获取跳转页码
                if (pageText.getText().toString().isEmpty()) { // 这里需要验证输入的参数
                    tip("请填写跳转页码");
                    return;
                }
                pagecurrent = Integer.valueOf(pageText.getText().toString());
                pageindex = pagecurrent;

                mController.sendAsynMessage(whichQuery, pageindex+"", AppParams.QUERY_PAGESIZE+"", coditionStr);
            }
        });

        // 使右边的按钮不可见
        topBar.setRightButtonVisibility(false);
        topBar.setOnLeftAndRightClickListener(new TopBar.OnLeftAndRightClickListener() {
            @Override
            public void OnLeftButtonClick() {
                finish();
            }

            @Override
            public void OnRightButtonClick() {
                // 获取搜索框中的模糊查询条件
                coditionStr = coditionText.getText().toString();
                pageindex = 1;
                pagecurrent = 1;

                mController.sendAsynMessage(whichQuery, pageindex+"", AppParams.QUERY_PAGESIZE+"", coditionStr);
            }
        });
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
            // 获取搜索框中的模糊查询条件
            coditionStr = coditionText.getText().toString();
            pageindex = 1;
            pagecurrent = 1;

            mController.sendAsynMessage(whichQuery, pageindex+"", AppParams.QUERY_PAGESIZE+"", coditionStr);
        }
    };
}
