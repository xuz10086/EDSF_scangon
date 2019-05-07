package com.indusfo.edzn.scangon.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.indusfo.edzn.scangon.R;
import com.indusfo.edzn.scangon.utils.ActivityUtils;
import com.indusfo.edzn.scangon.utils.NetworkUtil;

public class FunctionActivity extends BaseActivity {

    private ImageView newScanTask;
    private ImageView unfinishedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        initUI();
    }

    private void initUI() {
        newScanTask = (ImageView) findViewById(R.id.newScanTask);
        unfinishedTask = (ImageView) findViewById(R.id.unfinishedTask);
        newScanTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到扫码页面
                //ActivityUtils.start(FunctionActivity.this, CaptureActivity.class, false);
                // 跳转到新建任务
                ActivityUtils.start(FunctionActivity.this, ScanTaskActivity.class, false);
            }
        });
        unfinishedTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到未完成任务页面
                ActivityUtils.start(FunctionActivity.this, UnfinishedTaskActivity.class, false);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
