package cn.fluencycat.protecteyes.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.IOException;

import cn.fluencycat.protecteyes.Bean.Tools;
import cn.fluencycat.protecteyes.Const.ReturnCode;
import cn.fluencycat.protecteyes.Const.Url;
import cn.fluencycat.protecteyes.R;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RegisterActivity extends Activity {
    private EditText edit_username;//EditText
    private EditText edit_phone;
    private EditText edit_password;
    private EditText edit_code;

    private Button getCode;
    private Boolean getCodeSuccess=false;//是否已经成功获取到验证码

    private String username = "";//输入的信息
    private String phone = "";
    private String password = "";
    private String code = "";

    private EventHandler eventHandler;//sdk回调操作
    private Handler handler=new Handler();

    private ProgressDialog progressDialog;

    //private OnSendMessageHandler onSendMessageHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        initData();//初始化
    }

    /**
     * 1.初始化控件绑定
     * 2.注册sdk
     */
    private void initData() {
        edit_username = findViewById(R.id.edit_register_username);
        edit_phone = findViewById(R.id.edit_register_phone);
        edit_password = findViewById(R.id.edit_register_pwd);
        edit_code = findViewById(R.id.edit_register_code);
        getCode = findViewById(R.id.btn_register_getcode);
        // 创建EventHandler对象
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {//事件处理结果为成功
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        timer.start();
                        getCodeSuccess=true;
                    }//获取验证码成功
                    else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        Register_operation();
                    }//提交验证码验证成功
                    else{
                        ;
                    }
                } else{//事件处理失败
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(Tools.NetworkAvailable(RegisterActivity.this)){
                                    Toast.makeText(getApplicationContext(),"发送验证码失败，请检查手机号",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"发送验证码失败，请检查网络",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }//获取验证码失败
                    else if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"验证码错误，请重试",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        ;
                    }
                }
            }
        };

        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);
    }

    /**
     * 发送验证码的计时器
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            getCode.setText((millisUntilFinished / 1000) + "s");
            getCode.setEnabled(false);//不可点击
        }

        @Override
        public void onFinish() {
            getCode.setEnabled(true);//恢复点击
            getCode.setText("获取验证码");
        }
    };

    /**
     * 发送验证码，按钮点击事件
     * @param v
     */
    public void send_message(View v) {
        phone = edit_phone.getText().toString();
        if (phone.length() != 11 || (phone.toCharArray())[0] != '1')
            Toast.makeText(RegisterActivity.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
        else {
            SMSSDK.getVerificationCode("86", phone.trim());//请求发送验证码
        }
    }//发送验证码

    /**
     * 注册按钮，按钮点击事件
     * @param v
     */
    public void Register(View v) {
        //判断合法
        username=edit_username.getText().toString().trim();
        password=edit_password.getText().toString().trim();
        code=edit_code.getText().toString().trim();
        if(getCodeSuccess){
            if(username.length()==0||username.length()>16||username.contains(" ")){
                Tools.showShortToast(this,"用户名长度为1-16位且不含特殊字符");
                return ;
            }
            else if(password.length()<6||password.length()>16||password.contains(" ")){
                Tools.showShortToast(this,"密码长度为6-16位且不含特殊字符");
                return ;
            }
            showProgressDialog(this,"注册...");
            SMSSDK.submitVerificationCode("86",phone,code);
            //提交验证码
        }
        else{
            Toast.makeText(getApplicationContext(),"请先获取验证码",Toast.LENGTH_SHORT).show();
        }
    }//注册按钮

    /**
     * 注册操作网络通信
     */
    private void Register_operation(){
        OkHttpClient okHttpClient=new OkHttpClient();
        FormBody formBody=new FormBody.Builder()
                .add("name",username).add("phone",phone).add("password",Tools.MD5(password))
                .build();//表单，对密码加密
        final Request request=new Request.Builder()
                .post(formBody)
                .url(Url.Register)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();//通信完成，关闭加载框
                        Tools.showShortToast(getApplicationContext(),"连接服务器超时");
                    }
                });//请求失败，在主线程中提示结果
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result=response.body().string().trim();
                if(result.equals(ReturnCode.REGISTER_SUCCESSFUL+"")){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();//通信完成，关闭加载框
                            Tools.showShortToast(getApplicationContext(),"注册成功");
                            finish();//返回登录界面
                        }
                    });//注册成功，在主线程中提示结果
                }
                else if(result.equals(ReturnCode.REGISTER_ALREADY_EXISTS+"")){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();//通信完成，关闭加载框
                            Tools.showShortToast(getApplicationContext(),"该号码已经注册，请重试");
                        }
                    });//登陆失败，在主线程中提示结果
                }
                else if(result.equals(ReturnCode.REGISTER_FAULT+"")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();//通信完成，关闭加载框
                            Tools.showShortToast(getApplicationContext(), "注册失败");
                        }
                    });//请求失败，在主线程中提示结果
                }
                else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();//通信完成，关闭加载框
                            Tools.showShortToast(getApplicationContext(),"连接服务器超时");
                        }
                    });//请求失败，在主线程中提示结果
                }
            }
        });
    }//注册具体操作

    /**
     * 加载框
     * @param context
     * @param text
     */
    private void showProgressDialog(Context context,String text){
        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);//不可手动关闭
        progressDialog.show();
        progressDialog.setMessage(text);
    }//加载框

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);//注销sdk
    }
}
