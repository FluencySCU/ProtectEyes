package cn.fluencycat.protecteyes.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;

import cn.fluencycat.protecteyes.Activity.EditInfoActivity;
import cn.fluencycat.protecteyes.Activity.LoginActivity;
import cn.fluencycat.protecteyes.Activity.RegisterActivity;
import cn.fluencycat.protecteyes.Activity.SettingActivity;
import cn.fluencycat.protecteyes.Bean.Tools;
import cn.fluencycat.protecteyes.Const.Url;
import cn.fluencycat.protecteyes.Const.UserInfo;
import cn.fluencycat.protecteyes.R;
import cn.fluencycat.protecteyes.View.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 我页面的fragment
 */

public class MeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Intent intent;
    private Handler handler = new Handler();
    private ImageView message, setting;//右上角两个按钮图标
    private ImageView sex;
    private LinearLayout editInfo;//性别和编辑个人信息
    private TextView userNameText;//用户名
    private CircleImageView head;//头像
    private TextView left,right,articalNum,recordNum;
    private int EDIT_INFO_CODE=101;
    private RefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        initData();
        initViews();
        return view;
    }

    /**
     * 初始化控件,绑定监听器,初始化界面
     */

    private void initData(){
        intent = new Intent();
        message = view.findViewById(R.id.button_me_message);
        setting = view.findViewById(R.id.button_me_setting);
        editInfo=view.findViewById(R.id.linear_me_editinfo);
        refreshLayout=view.findViewById(R.id.refreshLayout_me);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initViews();
                refreshlayout.finishRefresh(1500);
            }
        });
        message.setOnClickListener(this);
        setting.setOnClickListener(this);
        editInfo.setOnClickListener(this);
        head = view.findViewById(R.id.myHead_icon);
        userNameText=view.findViewById(R.id.text_username);
        sex=view.findViewById(R.id.img_sex);
        left=view.findViewById(R.id.text_leftEye);
        right=view.findViewById(R.id.text_rightEye);
        articalNum=view.findViewById(R.id.text_articalNum);
        recordNum=view.findViewById(R.id.text_recordNum);
        if (UserInfo.uploadHead == 1)//上传过,用用户自定义的
            getHeadIcon(Url.HeadIcon + UserInfo.phone+".jpg");
    }


    private void initViews() {
        if(UserInfo.sex==1)
            sex.setBackgroundResource(R.mipmap.icon_sex_woman);
        else
            sex.setBackgroundResource(R.mipmap.icon_sex_man);
        userNameText.setText(UserInfo.name);
        if(UserInfo.leftEye.equals("")){
            left.setText("左眼\n未测试");
            right.setText("右眼\n未测试");
        }
        else{
            left.setText("左眼\n"+UserInfo.leftEye);
            right.setText("右眼\n"+UserInfo.rightEye);
        }
        articalNum.setText("共发布"+UserInfo.articalNum+"篇帖子");
        recordNum.setText("已打卡护眼"+UserInfo.recordNum+"次");
    }

    /**
     * 获取用户头像
     * @param url
     */
    private void getHeadIcon(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Tools.showShortToast(getActivity(), "获取头像失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final byte[] head_bt = response.body().bytes();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(head_bt, 0, head_bt.length);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        head.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    /**
     * 点击事件监听器
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_me_message:
                Tools.showShortToast(getActivity(),"新消息");
                //startActivity(intent.setClass(getActivity(), LoginActivity.class));
                break;
            case R.id.button_me_setting:
                startActivity(intent.setClass(getActivity(), SettingActivity.class));
                break;
            case R.id.linear_me_editinfo:
                Intent intent1=new Intent(getActivity(), EditInfoActivity.class);
                startActivityForResult(intent1,EDIT_INFO_CODE);
                break;//编辑个人信息
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==EDIT_INFO_CODE)
        {
            if(resultCode==1)
                ;
            else if(resultCode==2)
                getHeadIcon(Url.HeadIcon + UserInfo.phone+".jpg");
        }
    }
}
