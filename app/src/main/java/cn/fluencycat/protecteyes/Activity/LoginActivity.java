package cn.fluencycat.protecteyes.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import cn.fluencycat.protecteyes.Bean.Tools;
import cn.fluencycat.protecteyes.Const.CONTEXT;
import cn.fluencycat.protecteyes.Const.ReturnCode;
import cn.fluencycat.protecteyes.Const.Url;
import cn.fluencycat.protecteyes.Const.UserInfo;
import cn.fluencycat.protecteyes.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {
    private Intent intent = new Intent();//Activity之间的跳转intent
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private EditText edit_phone;
    private EditText edit_password;

    private String phone = "";
    private String password = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        CONTEXT.setContext(getApplicationContext());
        sp = this.getSharedPreferences("INFO", MODE_PRIVATE);
        editor = sp.edit();
        boolean isLogin = sp.getBoolean("ISLOGIN", false);
        String phone_num = sp.getString("PHONE", "");
        String user_password = sp.getString("PASSWORD", "");
        initData();//初始化
        if (isLogin) {
            edit_phone.setText(phone_num);
            edit_password.setText(user_password);
            phone = phone_num;
            password = user_password;
            Login_operation();
        } else if (!phone_num.equals(""))
            edit_phone.setText(phone_num);
        else
            ;
    }

    private void initData() {
        edit_phone = findViewById(R.id.edit_login_phone);
        edit_password = findViewById(R.id.edit_login_pwd);
    }

    /**
     * 登陆，按钮点击事件
     *
     * @param v
     */
    public void Login(View v) {
        phone = edit_phone.getText().toString().trim();
        password = edit_password.getText().toString().trim();
        Login_operation();

    }//点击登陆按钮

    /**
     * 登陆操作网络通信
     */
    private void Login_operation() {
        showProgressDialog(this, "登陆...");
        OkHttpClient okHttpClient = new OkHttpClient();//创建一个okhttpclient对象
        FormBody formBody = new FormBody.Builder()
                .add("phone", phone).add("password", Tools.MD5(password))
                .build();//申明表单对象，添加键值对,对密码进行MD5加密
        Request request = new Request.Builder()
                .url(Url.Login)
                .post(formBody)
                .build();//创建request请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();//通信完成，关闭加载框
                        Tools.showShortToast(getApplicationContext(), "连接服务器超时");
                    }
                });//请求失败，在主线程中提示结果
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string().trim();
                if (result.equals(ReturnCode.LOGIN_SUCCESSFUL + "")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getUserInfo();
                        }
                    });//账户检测成功，获取用户信息
                } else if (result.equals(ReturnCode.LOGIN_FAULT + "")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();//通信完成，关闭加载框
                            Tools.showShortToast(getApplicationContext(), "账号或密码错误，请重试");
                        }
                    });//登陆失败，在主线程中提示结果
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();//通信完成，关闭加载框
                            Tools.showShortToast(getApplicationContext(), "连接服务器超时");
                        }
                    });//请求失败，在主线程中提示结果
                }
                //成功收到response,处理response
            }
        });//建立联系，call
    }//登陆操作

    private void getUserInfo() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Url.UserInfo + "?phone=" + phone).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();//通信完成，关闭加载框
                        Tools.showShortToast(getApplicationContext(), "连接服务器超时");
                    }
                });//请求失败，在主线程中提示结果
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    UserInfo.phone = jsonObject.getString("号码");
                    UserInfo.name = jsonObject.getString("用户名");
                    UserInfo.sex = jsonObject.getInt("性别");
                    UserInfo.uploadHead = jsonObject.getInt("是否上传头像");
                    UserInfo.articalNum=jsonObject.getInt("文章数");
                    UserInfo.leftEye=jsonObject.getString("左眼");
                    UserInfo.rightEye=jsonObject.getString("右眼");
                    UserInfo.motto=jsonObject.getString("签名");
                    UserInfo.recordNum=jsonObject.getInt("打卡次数");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();//通信完成，关闭加载框
                            editor.putBoolean("ISLOGIN", true);//登录状态
                            editor.putString("PHONE", phone);//保存账号
                            editor.putString("PASSWORD", password);//保存密码
                            editor.commit();
                            //Tools.showShortToast(getApplicationContext(), "登陆成功");
                            startActivity(intent.setClass(LoginActivity.this,MainActivity.class));
                            LoginActivity.this.finish();
                        }
                    });//获取用户信息成功，在主线程中提示结果，并跳转至主界面
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 找回（忘记）密码，按钮点击事件
     *
     * @param v
     */
    public void ForgetPassword(View v) {
        showProgressDialog(this, "请稍后...");
        if (Tools.NetworkAvailable(this)) {
            intent.setClass(LoginActivity.this, ForgetPwdActivity.class);
            startActivity(intent);
        } else
            Toast.makeText(getApplicationContext(), "网络连接不可用!请重试", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }//找回密码

    /**
     * 注册，按钮点击事件
     *
     * @param v
     */
    public void SignUp(View v) {
        showProgressDialog(this, "请稍后...");
        if (Tools.NetworkAvailable(this)) {
            intent.setClass(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        } else
            Toast.makeText(getApplicationContext(), "网络连接不可用!请重试", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }//注册

    /**
     * 加载框
     *
     * @param context
     * @param text
     */
    private void showProgressDialog(Context context, String text) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);//不可手动关闭
        progressDialog.show();
        progressDialog.setMessage(text);
    }//加载框
}
