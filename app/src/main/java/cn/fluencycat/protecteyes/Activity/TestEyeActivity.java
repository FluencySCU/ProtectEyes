package cn.fluencycat.protecteyes.Activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

import cn.fluencycat.protecteyes.Bean.JsonParser;
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


public class TestEyeActivity extends Activity {
    private SpeechSynthesizer mTts;//语音合成(提示)
    private SpeechRecognizer mIat;//语音听写(识别)
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private LinearLayout ready;
    private LinearLayout test;
    private TextView text, tip;
    private ImageView img;

    private Handler handler;

    private String resultString = "";
    private int imgId[][] = {{R.drawable.e_1_1, R.drawable.e_1_2, R.drawable.e_1_3, R.drawable.e_1_4},
            {R.drawable.e_2_1, R.drawable.e_2_2, R.drawable.e_2_3, R.drawable.e_2_4},
            {R.drawable.e_3_1, R.drawable.e_3_2, R.drawable.e_3_3, R.drawable.e_3_4},
            {R.drawable.e_4_1, R.drawable.e_4_2, R.drawable.e_4_3, R.drawable.e_4_4},
            {R.drawable.e_5_1, R.drawable.e_5_2, R.drawable.e_5_3, R.drawable.e_5_4},
            {R.drawable.e_6_1, R.drawable.e_6_2, R.drawable.e_6_3, R.drawable.e_6_4},
            {R.drawable.e_7_1, R.drawable.e_7_2, R.drawable.e_7_3, R.drawable.e_7_4},
            {R.drawable.e_8_1, R.drawable.e_8_2, R.drawable.e_8_3, R.drawable.e_8_4},
            {R.drawable.e_9_1, R.drawable.e_9_2, R.drawable.e_9_3, R.drawable.e_9_4},
            {R.drawable.e_10_1, R.drawable.e_10_2, R.drawable.e_10_3, R.drawable.e_10_4},
            {R.drawable.e_11_1, R.drawable.e_11_2, R.drawable.e_11_3, R.drawable.e_11_4},};//图片id,11*4组
    private String sightPoint[] = {"5.0", "4.9", "4.8", "4.7", "4.6", "4.5", "4.4", "4.3", "4.2", "4.1", "4.0"};//视力
    private int sightHun[] = {0, 100, 150, 200, 250, 300, 400, 450, 500, 550, 650};//度数
    private String result[] = {"上", "下", "左", "右"};
    private String RightPoint = "", LeftPoint = "";//右眼，左眼视力
    private int RightHun = 0, LeftHun = 0;
    private int correct = 0;//4个中正确的个数,0-2,3,4分别对应不同的操作
    private int correct_pre = 2;//上一级对的个数。
    private int nowLevel = 0;
    private int nowNum = 0;
    private String nowResult = "";
    private int nowEye = 0;//0右,1左


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_eye);
        SpeechUtility.createUtility(TestEyeActivity.this, SpeechConstant.APPID + "=59f84278");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initSpeech();//初始化听与写
        initData();//初始化数据和控件
    }

    /**
     * 初始化数据、控件
     */
    private void initData() {
        handler = new Handler();
        ready = findViewById(R.id.linear_ready_test);
        test = findViewById(R.id.linear_start_test);
        text = findViewById(R.id.text_test);
        img = findViewById(R.id.test_img);
        tip = findViewById(R.id.text_tip);
    }

    /**
     * 初始化SpeechSynthesizer&SpeechRecognizer
     */
    private void initSpeech() {
        mTts = SpeechSynthesizer.createSynthesizer(TestEyeActivity.this, null);
        mIat = SpeechRecognizer.createRecognizer(TestEyeActivity.this, null);
    }

    /**
     * 机器人读出str
     *
     * @param str
     */
    private void Speak(String str) {
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        mTts.startSpeaking(str, mSynListener);//语音提示
    }

    private void Listen() {
        mIatResults.clear();
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        // 接收语言中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 接受的语言是普通话
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        // 设置听写引擎（云端）
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mIat.startListening(mRecoListener);
    }

    /**
     * 开始测试,关掉准备界面，打开测试界面
     *
     * @param v
     */
    public void start_test(View v) {
        ready.setVisibility(View.INVISIBLE);//隐藏提示界面开始测试
        test.setVisibility(View.VISIBLE);
        testEye();
    }

    private void testEye() {
        img.setImageResource(R.drawable.test_eye_back);
        nowNum = 0;
        if (nowEye == 0) {
            Speak("请遮住左眼，5秒后开始测试");
        } else {
            Speak("请遮住右眼，5秒后开始测试");
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                testLevel();
            }
        }, 8000);
    }


    private void testLevel() {
        tip.setText("");
        if (nowNum == 0)
            correct = 0;
        final int j = new Random().nextInt(4);
        img.setImageResource(imgId[nowLevel][j]);
        Speak("请指出方向");
        nowResult = result[j];
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Listen();
            }
        }, 2100);
    }

    /**
     * 语音合成监听
     */
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        // 会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
            if (error != null) {
                Log.d("complete code:", error.getErrorCode()
                        + "");
            } else {
                Log.d("complete code:", "0");
            }
        }

        // 缓冲进度回调
        // percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }

        // 开始播放
        public void onSpeakBegin() {
        }

        // 暂停播放
        public void onSpeakPaused() {
        }

        // 播放进度回调
        // percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        // 恢复播放回调接口
        public void onSpeakResumed() {
        }

        // 会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };

    /**
     * 语音听写监听
     */
    private RecognizerListener mRecoListener = new RecognizerListener() {
        // 听写结果回调接口(返回Json格式结果，用户可参见附录12.1)；
        // 一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        // 关于解析Json的代码可参见MscDemo中JsonParser类；
        // isLast等于true时会话结束。
        String t = "";

        public void onResult(RecognizerResult results, boolean isLast) {
            resultString = "";
            t = getString(results);
            text.setText(t);
        }

        // 会话发生错误回调接口
        public void onError(SpeechError error) {
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            if (error.getErrorCode() == 10118) {
                tip.setText("你好像没有说话哦");
            }
            resultString = "GG";
        }// 获取错误码描述}

        // 开始录音
        public void onBeginOfSpeech() {
            tip.setText("开始说话...");
        }

        // 结束录音
        public void onEndOfSpeech() {
            tip.setText("结束说话");
            resultString = t;
            if (resultString.contains(nowResult)) {
                correct++;
            }
            if (nowNum == 3) {
                Tools.showShortToast(getApplicationContext(), "正确率:" + correct + "/4");
                nowNum = 0;
                switch (correct) {
                    case 0:
                        ;
                    case 1:
                        ;
                    case 2:
                        correct_pre = 2;
                        nowLevel += 2;
                        if (nowLevel > 10) {
                            if (nowEye == 0) {
                                RightPoint = sightPoint[10] + "-";
                                RightHun = 800;
                                nowLevel = 0;
                                nowEye = 1;
                                correct_pre = 2;
                                testEye();
                            }//右眼，开始测试左眼
                            else {
                                LeftPoint = sightPoint[10] + "-";
                                LeftHun = 800;
                                end();
                            }//左眼，结束测试
                        } else
                            testLevel();
                        break;
                    case 3:
                        nowLevel++;
                        correct_pre = 3;
                        if (nowLevel > 10) {
                            if (nowEye == 0) {
                                RightPoint = sightPoint[10] + "-";
                                RightHun = 700;
                                nowLevel = 0;
                                nowEye = 1;
                                correct_pre = 2;
                                testEye();
                            }//右眼，开始测试左眼
                            else {
                                LeftPoint = sightPoint[10] + "-";
                                LeftHun = 700;
                                end();
                            }//左眼，结束测试
                        } else
                            testLevel();
                        break;
                    case 4:
                        if (nowLevel == 0) {
                            if (nowEye == 0) {
                                RightPoint = sightPoint[0] + "+";
                                RightHun = sightHun[0];
                                nowLevel = 0;
                                nowEye = 1;
                                correct_pre = 2;
                                testEye();
                            }//右眼，开始测试左眼
                            else {
                                LeftPoint = sightPoint[0] + "+";
                                LeftHun = sightHun[0];
                                end();
                            }//左眼，结束测试
                        } else {
                            if (nowEye == 0) {
                                if (correct_pre == 3) {
                                    RightHun = (sightHun[nowLevel] + sightHun[nowLevel - 1]) / 2;
                                } else {
                                    RightHun = sightHun[nowLevel];
                                }
                                RightPoint = sightPoint[nowLevel];
                                nowLevel = 0;
                                nowEye = 1;
                                correct_pre = 2;
                                testEye();
                            }//右眼，开始测试左眼
                            else {
                                if (correct_pre == 3) {
                                    LeftHun = (sightHun[nowLevel] + sightHun[nowLevel - 1]) / 2;
                                } else {
                                    LeftHun = sightHun[nowLevel];
                                }
                                LeftPoint = sightPoint[nowLevel];
                                end();
                            }//左眼，结束测试
                        }
                        break;
                }
            } else {
                nowNum++;
                testLevel();
            }
        }

        // 扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }

        //音量
        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            // TODO Auto-generated method stub
        }

    };

    /**
     * 测试结束
     */
    private void end(){
        Speak("测试结束");
        final String left=LeftPoint+" ("+LeftHun+"度)";
        final String right=RightPoint+" ("+RightHun+"度)";
        OkHttpClient okHttpClient=new OkHttpClient();
        FormBody body=new FormBody.Builder().add("phone", UserInfo.phone)
                .add("left",left)
                .add("right",right).build();
        Request request=new Request.Builder().post(body).url(Url.EditEye).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Tools.showShortToast(getApplicationContext(),"上传结果失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result=response.body().string().trim();
                if(result.equals(ReturnCode.EDITEYE_SUCCESSFUL+"")){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            UserInfo.leftEye=left;
                            UserInfo.rightEye=right;
                            Tools.showShortToast(getApplicationContext(),"上传结果成功");
                        }
                    });
                }
                else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showShortToast(getApplicationContext(),"上传结果失败");
                        }
                    });
                }
            }
        });
        AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("测试结果")
                .setMessage("右眼视力:"+RightPoint+" ("+RightHun+"度)\n"+"左眼视力:"+LeftPoint+" ("+LeftHun+"度)\n")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 解析用户说的话
     *
     * @param results
     * @return
     */
    private String getString(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        return resultBuffer.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTts.destroy();
        mIat.destroy();
        handler.removeCallbacksAndMessages(null);
    }

}
