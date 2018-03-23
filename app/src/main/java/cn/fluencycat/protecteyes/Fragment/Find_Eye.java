package cn.fluencycat.protecteyes.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;

import cn.fluencycat.protecteyes.Activity.EyeExercActivity;
import cn.fluencycat.protecteyes.Activity.EyeExercTechActivity;
import cn.fluencycat.protecteyes.Activity.EyeTiredActivity;
import cn.fluencycat.protecteyes.Activity.TiredPicActivity;
import cn.fluencycat.protecteyes.Bean.Tools;
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

public class Find_Eye extends Fragment implements View.OnClickListener {
    private View view;
    private TextView text1, text2, text3, text4, text5;
    private static boolean record = false;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find_eye, container, false);
        initData();//初始化
        return view;
    }

    private void initData() {
        if (record) {
            text5.setText("今日已打卡");
            text5.setTextColor(Color.parseColor("#c9c9c9"));
        }
        handler = new Handler();
        text1 = view.findViewById(R.id.find_eye_text1);
        text1.setOnClickListener(this);
        text2 = view.findViewById(R.id.find_eye_text2);
        text2.setOnClickListener(this);
        text3 = view.findViewById(R.id.find_eye_text3);
        text3.setOnClickListener(this);
        text4 = view.findViewById(R.id.find_eye_text4);
        text4.setOnClickListener(this);
        text5 = view.findViewById(R.id.find_eye_text5);
        text5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_eye_text1:
                startActivity(new Intent(getActivity(), EyeExercActivity.class));
                break;
            case R.id.find_eye_text2:
                startActivity(new Intent(getActivity(), EyeExercTechActivity.class));
                break;
            case R.id.find_eye_text3:
                startActivity(new Intent(getActivity(), EyeTiredActivity.class));
                break;
            case R.id.find_eye_text4:
                startActivity(new Intent(getActivity(), TiredPicActivity.class));
                break;
            case R.id.find_eye_text5:
                if (record)
                    Tools.showShortToast(getActivity(), "今天已经打过一次卡啦");
                else
                    Record();
                break;
        }
    }

    private void Record() {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody body = new FormBody.Builder().add("phone", UserInfo.phone).add("num", UserInfo.recordNum + "").build();
        Request request = new Request.Builder().post(body).url(Url.Record).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Tools.showShortToast(getActivity(), "打卡失败，请检查网络");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string().trim();
                if (result.equals(ReturnCode.RECORD_SUCCESSFUL + "")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            text5.setText("今日已打卡");
                            text5.setTextColor(Color.parseColor("#c9c9c9"));
                            record = true;
                            UserInfo.recordNum++;
                        }
                    });
                } else if (result.equals(ReturnCode.RECORD_FAULT + "")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showShortToast(getActivity(), "打卡失败");
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showShortToast(getActivity(), "打卡失败");
                        }
                    });
                }
            }
        });
    }
}
