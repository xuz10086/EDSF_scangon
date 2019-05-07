package com.indusfo.edzn.scangon.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.indusfo.edzn.scangon.bean.RResult;
import com.indusfo.edzn.scangon.bean.User;
import com.indusfo.edzn.scangon.bean.VersionInfo;
import com.indusfo.edzn.scangon.cons.IdiyMessage;
import com.indusfo.edzn.scangon.cons.UpdateStatus;
import com.indusfo.edzn.scangon.controller.LoginController;
import com.indusfo.edzn.scangon.R;
import com.indusfo.edzn.scangon.utils.ActivityUtils;
import com.indusfo.edzn.scangon.utils.AppUtils;
import com.indusfo.edzn.scangon.utils.DeviceThread;
import com.indusfo.edzn.scangon.utils.Md5Util;
import com.indusfo.edzn.scangon.utils.SDCardUtils;
import com.indusfo.edzn.scangon.utils.ToastUtils;
import com.indusfo.edzn.scangon.utils.UpdateVersionUtil;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * 登陆
 *
 * @author xuz
 * @date 2019/1/2 2:35 PM
 */
public class LoginActivity extends BaseActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /*
     * UI 界面
     */
    private EditText mUsernameView;
    private EditText mPasswordView;
    // 进度条
    private View mProgressView;
    // 登陆表单
    private View mLoginFormView;
    // 勾选框
    private CheckBox checkBox;
    // 设置设备号
    private ImageView settingDevice;
    // 版本号
    private TextView version;
    // 更新
    private TextView updateApp;

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case IdiyMessage.LOGIN_ACTION_RESULT:
                handleLoginResult(msg);
                break;
            case IdiyMessage.SAVE_USER_TODB_RESULT:
                handleSaveUserToDb((boolean) msg.obj);
                break;
            case IdiyMessage.GET_USER_FROM_DB_RESULT:
                handleGetUser(msg.obj);
                break;
            default:
                break;
        }
    }

    private void handleGetUser(Object o) {
        if (o != null) {
            User user = (User) o;
            // 判断ifsave是否为1:勾选了记住密码
            if (user.getIfsave() == 1) {
                checkBox.setChecked(true);
                mUsernameView.setText(user.getUsername());
                mPasswordView.setText(user.getPassword());
            }
        }
    }

    private void handleSaveUserToDb(boolean ifSuccess) {
        if (ifSuccess) {
            ActivityUtils.start(this, UnfinishedTaskActivity.class, true);
        } else {
            tip("登录异常");
        }
    }

    private void handleLoginResult(Message msg) {
        RResult rResult = (RResult) msg.obj;
        if (null == rResult || null == rResult.getStatus()) {
            tip("网络连接错误");
            return;
        }
        // 状态为200，跳转到主页
        if ("200".equals(rResult.getStatus())) {

//            ActivityUtils.start(this, FunctionActivity.class, true);
            // 保存用户信息
            String username = mUsernameView.getText().toString();
            String password = mPasswordView.getText().toString();
            String cookie = rResult.getCookie();
//            String fileDir = getCacheDir().getPath();
            int ifsave;
            if (checkBox.isChecked()) {
                ifsave = 1;
            } else {
                ifsave = 0;
            }
            String userId = "111";

            mController.sendAsynMessage(IdiyMessage.SAVE_USER_TODB, username, password, ifsave, userId);

//            mController.sendAsynMessage(IdiyMessage.SAVE_COOKIE, cookie, fileDir);
            mController.sendAsynMessage(IdiyMessage.SAVE_COOKIE, cookie);

        } else{
            mPasswordView.setError("用户名或密码错误！");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        initController();
        initUI();

        // 如果存在安装包，则删除
        String filePath = SDCardUtils.getRootDirectory()+"/updateVersion/app-release.apk";
        SDCardUtils.removeFile(filePath);
    }

    private void initController() {

        mController = new LoginController(this);
        mController.setIModeChangeListener(this);
    }

    private void initUI() {
        // 用户名文本输入框
        mUsernameView = (EditText) findViewById(R.id.email);
        // Password文本输入框
        mPasswordView = (EditText) findViewById(R.id.password);
        // 表单
        mLoginFormView = findViewById(R.id.login_form);
        // 进度条
        mProgressView = findViewById(R.id.login_progress);
        checkBox = findViewById(R.id.checkbox_pwd);
        settingDevice = findViewById(R.id.setting_device);
        // 版本号
        version = findViewById(R.id.version);
        // 更新
        updateApp = findViewById(R.id.update_app);

        // 获取版本号并设置
        String versionName = AppUtils.getVersionName(LoginActivity.this);
        version.setText(versionName);

//        populateAutoComplete();

        // 给Password文本框添加监听事件
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    //attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mController.sendAsynMessage(IdiyMessage.GET_USER_FROM_DB, 0);

        settingDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.start(LoginActivity.this, SettingDeviceActivity.class, true);
            }
        });

        // 更新App
        updateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //访问服务器 试检测是否有新版本发布
                UpdateVersionUtil.checkVersion(LoginActivity.this, new UpdateVersionUtil.UpdateListener() {

                    @Override
                    public void onUpdateReturned(int updateStatus, VersionInfo versionInfo) {
                        //判断回调过来的版本检测状态
                        switch (updateStatus) {
                            case UpdateStatus.YES:
                                //弹出更新提示
                                UpdateVersionUtil.showDialog(LoginActivity.this, versionInfo);
                                break;
                            case UpdateStatus.NO:
                                //没有新版本
                                ToastUtils.showToast(getApplicationContext(), "已经是最新版本了!");
                                break;
                            case UpdateStatus.NOWIFI:
                                //当前是非wifi网络
                                ToastUtils.showToast(getApplicationContext(), "只有在wifi下更新！");
//                                    DialogUtils.showDialog(LoginActivity.this, "温馨提示","当前非wifi网络,下载会消耗手机流量!", "确定", "取消",new DialogOnClickListenner() {
//                                        @Override
//                                        public void btnConfirmClick(Dialog dialog) {
//                                            dialog.dismiss();
//                                            //点击确定之后弹出更新对话框
//                                            UpdateVersionUtil.showDialog(SystemActivity.this,versionInfo);
//                                        }
//
//                                        @Override
//                                        public void btnCancelClick(Dialog dialog) {
//                                            dialog.dismiss();
//                                        }
//                                    });
                                break;
                            case UpdateStatus.ERROR:
                                //检测失败
                                ToastUtils.showToast(getApplicationContext(), "检测失败，请稍后重试！");
                                break;
                            case UpdateStatus.TIMEOUT:
                                //链接超时
                                ToastUtils.showToast(getApplicationContext(), "链接超时，请检查网络设置!");
                                break;
                        }
                    }

                });

            }
        });
    }

    /**
     * 点击登录按钮
     * 输入框值判断，发送网络请求来请求服务器
     *
     * @author xuz
     * @date 2019/1/4 9:20 AM
     * @param [view]
     * @return void
     */
    public void loginClick(View view) {

        // 在尝试登录时存储值。
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String md5pwd = Md5Util.md5(password);
        if (ifValueWasEmpty(username, password)) {
            tip("请输入账号密码");
        }


        // 关闭ThreadLocal线程，避免内存泄漏
        DeviceThread.remove();
        // 发送网络请求
        mController.sendAsynMessage(IdiyMessage.LOGIN_ACTION, username, md5pwd);
    }


    /*private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // 在尝试登录时存储值。
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        // 标记：
        boolean cancel = false;
        View focusView = null;

        // 密码校验
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // 用户名校验
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // 错误，登陆表单填写错误
            focusView.requestFocus();
        } else {
            // 显示一个进度转轮，并启动一个后台任务
            // 执行用户登录尝试。
            showProgress(true);
            // post请求，参数
            String postValue = null;
            try {
                // md5加密
                String md5pwd = Md5Util.md5(password);
                postValue = URLEncoder.encode("vcUserCode", "UTF-8") + "=" +
                        URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("vcUserPwd", "UTF-8") + "=" +
                        URLEncoder.encode(md5pwd, "UTF-8");
                mAuthTask = new UserLoginTask(this, postValue, URL);
                mAuthTask.execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }*/

    // ==================================================
  /*  private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }*/

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }*/

    /**
     * 展示进度条
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

   /* @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }
*/
   /* @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }*/

 /*   @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
*/
   /* private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsernameView.setAdapter(adapter);
    }*/

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
    // ====================================

/*    public class UserLoginTask extends AsyncTask<Void, Integer, String> {

        private Context context;
        // 参数
        private String postValue;
        private String url;

        public UserLoginTask(Context context, String postValue, String url) {
            this.context = context;
            this.postValue = postValue;
            this.url = url;
        }

        *//**
         * 登陆业务
         * 访问服务器，如果请求登陆成功，则进入App操作界面
         * 否则
         *
         * @author xuz
         * @date 2019/1/2 5:04 PM
         * @param [voids]
         * @return java.lang.Boolean
         *//*
        @Override
        protected String doInBackground(Void... voids) {
            //在此方法执行耗时操作,耗时操作中发布进度，更新进度条
            //String result = download();
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                    //进度条每次更新10%,执行中创建新线程处理onProgressUpdate()
                    publishProgress(i);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            String result = LoginService.postGetJson(url,postValue);
            //第三个参数为String 所以此处return一个String类型的数据

            return result;
        }

        @Override
        protected void onPostExecute(final String success) {
            String status = null;
            String msg;
            mAuthTask = null;
            showProgress(false);
            String[] datas = success.split(",");
            
            for (String str : datas) {
                // 获取status
                if (str.contains("status")) {
                    status = str.split(":")[1];
                }
                // 获取msg
                if (str.contains("msg")) {
                    msg = str.split(":")[1];
                }
            }

            if ("200".equals(status)) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }*/

}

