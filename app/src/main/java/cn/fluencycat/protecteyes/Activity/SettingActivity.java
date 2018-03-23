package cn.fluencycat.protecteyes.Activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.fluencycat.protecteyes.R;

public class SettingActivity extends Activity implements View.OnClickListener{
    private Button back;
    private LinearLayout l1,l2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        initData();
    }

    /**
     * 初始化控件
     */
    private void initData(){
        back=findViewById(R.id.button_setting_back);//返回按钮
        l1=findViewById(R.id.linear_setting_edit);//修改密码
        l2=findViewById(R.id.linear_setting_logout);//退出登录
        back.setOnClickListener(this);
        l1.setOnClickListener(this);
        l2.setOnClickListener(this);
    }

    /**
     * 点击事件监听
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_setting_back:
                finish();
                break;
            case R.id.linear_setting_edit:
                startActivity(new Intent().setClass(SettingActivity.this,ForgetPwdActivity.class));
                break;
            case R.id.linear_setting_logout://退出登录
                AlertDialog dialog=new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定退出登录?")
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp;
                                SharedPreferences.Editor editor;
                                sp = SettingActivity.this.getSharedPreferences("INFO", MODE_PRIVATE);
                                editor = sp.edit();
                                editor.putBoolean("ISLOGIN", false);//登录状态
                                editor.putString("PASSWORD", "");//保存密码
                                editor.commit();
                                startActivity(new Intent().setClass(SettingActivity.this,LoginActivity.class));
                                myExit();//关闭MainActivity
                                SettingActivity.this.finish();
                            }
                        }).create();
                dialog.show();
                break;
        }
    }

    /**
     * 广播发送器
     * acticon:关闭Activity
     * 用这个函数向MainActivity发送广播以关闭页面
     */
    protected void myExit(){
        Intent intent=new Intent();
        intent.setAction("FinishActivity");
        this.sendBroadcast(intent);
    }
}
