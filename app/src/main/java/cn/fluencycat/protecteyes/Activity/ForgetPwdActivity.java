package cn.fluencycat.protecteyes.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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


public class ForgetPwdActivity extends Activity {
    private int lookPass = 0;
    private LinearLayout linear1, linear2, linear3;//三步界面
    private EditText phoneEdit, codeEdit, passwordEdit;//三个EditText
    private String phone, code, password;
    private ProgressDialog progressDialog;//加载框
    private EventHandler eventHandler;//sdk回调操作
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_pass);
        initData();//初始化
        // 创建EventHandler对象
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {//事件处理结果为成功
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                linear1.setVisibility(View.INVISIBLE);
                                linear2.setVisibility(View.VISIBLE);
                            }
                        });
                    }//获取验证码成功
                    else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                linear2.setVisibility(View.INVISIBLE);
                                linear3.setVisibility(View.VISIBLE);
                            }
                        });
                    }//提交验证码验证成功
                    else {
                        ;
                    }
                } else {//事件处理失败
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (Tools.NetworkAvailable(ForgetPwdActivity.this)) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "发送验证码失败，请检查手机号", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "发送验证码失败，请检查网络", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }//获取验证码失败
                    else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "验证码错误，请重试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        ;
                    }
                }
            }
        };

        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);
    }

    public void initData() {
        linear1 = findViewById(R.id.linear_forget_phone);
        linear2 = findViewById(R.id.linear_forget_code);
        linear3 = findViewById(R.id.linear_forget_submit);
        phoneEdit = findViewById(R.id.edit_forget_phone);
        codeEdit = findViewById(R.id.edit_forget_code);
        passwordEdit = findViewById(R.id.edit_forget_submit);
    }

    /**
     * 返回按钮
     *
     * @param v
     */
    public void forget_back(View v) {
        finish();
    }

    /**
     * 发送验证码
     *
     * @param v
     */
    public void forget_sendMessage(View v) {
        phone = phoneEdit.getText().toString().trim();
        if (phone.length() != 11 || (phone.toCharArray())[0] != '1')
        {
            Tools.showShortToast(this, "请输入正确的手机号码");
        }
        else {
            showProgressDialog(this, "发送验证码...");
            SMSSDK.getVerificationCode("86", phone);//请求发送验证码
        }
    }

    /**
     * 提交验证码
     *
     * @param v
     */
    public void forget_submitMessage(View v) {
        code = codeEdit.getText().toString().trim();
        if (code.length() != 4) {
            Tools.showShortToast(this, "请输入4位手机验证码");
        } else {
            showProgressDialog(this, "验证...");
            SMSSDK.submitVerificationCode("86", phone, code);//提交验证码
        }
    }

    /**
     * 修改密码
     *
     * @param v
     */
    public void editPassword(View v) {
        password=passwordEdit.getText().toString().trim();
        if(password.length()<6||password.length()>16||password.contains(" ")){
            Tools.showShortToast(this,"密码长度为6-16位且不包含特殊字符");
        }
        else{
            OkHttpClient okHttpClient=new OkHttpClient();
            FormBody formBody=new FormBody.Builder().add("phone",phone).add("password",Tools.MD5(password)).build();
            Request request=new Request.Builder().url(Url.EditPassword).post(formBody).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Tools.showShortToast(ForgetPwdActivity.this,"连接服务器超时");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String rcode=response.body().string().trim();
                    if(rcode.equals(ReturnCode.EDITPASSWORD_SUCCESSFUL+"")){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Tools.showShortToast(ForgetPwdActivity.this,"修改成功");
                                finish();
                            }
                        });
                    }
                    else if(rcode.equals(ReturnCode.EDITPASSWORD_FAULT+"")){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Tools.showShortToast(ForgetPwdActivity.this,"修改密码失败，该号码未注册");
                            }
                        });
                    }
                    else{
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Tools.showShortToast(ForgetPwdActivity.this,"修改密码失败");
                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * 密码可见
     *
     * @param v
     */
    public void forget_lookPass(View v) {
        if (lookPass == 0) {
            v.setBackgroundResource(R.mipmap.icon_eye_open);
            passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            lookPass = 1;
        }//不可见到可见
        else {
            v.setBackgroundResource(R.mipmap.icon_eye_close);
            passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            lookPass = 0;
        }//可见到不可见
    }

    /**
     * 显示加载框
     *
     * @param text 加载框文字
     */
    private void showProgressDialog(Context context, String text) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(text);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);//注销sdk
    }
}
