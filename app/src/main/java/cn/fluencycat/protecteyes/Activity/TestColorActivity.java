package cn.fluencycat.protecteyes.Activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import cn.fluencycat.protecteyes.Bean.Tools;
import cn.fluencycat.protecteyes.R;

public class TestColorActivity extends Activity implements View.OnClickListener {
    //图片id
    private int imgId[] = {R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5, R.drawable.c6, R.drawable.c7, R.drawable.c8, R.drawable.c9,
            R.drawable.c10, R.drawable.c11, R.drawable.c12, R.drawable.c13, R.drawable.c14, R.drawable.c15, R.drawable.c16, R.drawable.c17, R.drawable.c18,
            R.drawable.c19, R.drawable.c20, R.drawable.c21, R.drawable.c22, R.drawable.c23, R.drawable.c24, R.drawable.c25, R.drawable.c26, R.drawable.c27,
            R.drawable.c28, R.drawable.c29, R.drawable.c30, R.drawable.c31, R.drawable.c32, R.drawable.c33, R.drawable.c34, R.drawable.c35, R.drawable.c36};
    //图片答案
    private String answer[][] = {
            {"公鸡", "金鱼", "鸭子"}, {"五角星", "三角形", "圆形"}, {"公鸡", "羊", "牛"}, {"1327", "4234", "4327"}, {"1327", "4327", "4284"}, {"69", "689", "89"}, {"鸡", "鹅", "猫"}, {"30", "36", "38"}, {"鹿", "牛", "马"},
            {"4", "8", "6"}, {"蜻蜓", "蝴蝶", "燕子"}, {"方形", "圆形", "三角形"}, {"马", "鹿", "牛"}, {"蝴蝶", "燕子", "蜻蜓"}, {"9", "698", "6"}, {"27", "17", "22"}, {"9", "6", "5"}, {"8", "6", "7"},
            {"6", "9", "8"}, {"燕子", "蝴蝶", "蜻蜓"}, {"9", "8", "6"}, {"9088", "9668", "9068"}, {"806", "06", "86"}, {"鸭和兔", "鸭子", "兔子"}, {"69", "56", "869"}, {"52", "88", "89"}, {"PEACE", "902", "19812"},
            {"BEE", "BED", "RED"}, {"3", "7", "2"}, {"马", "拖拉机", "牛"}, {"809", "808", "888"}, {"6", "9", "8"}, {"66", "00", "88"}, {"鹿", "牛", "兔"}, {"890", "808", "809"}, {"888", "608", "628"}
    };
    //图片正确答案
    private String correct[] = {
            "金鱼", "五角星", "羊", "4234", "4284", "689", "鹅", "36", "马",
            "4", "蝴蝶", "圆形", "马", "蜻蜓", "698", "17", "5", "6",
            "6", "燕子", "6", "9068", "806", "鸭和兔", "869", "89", "PEACE",
            "RED", "3", "拖拉机", "809", "9", "66", "牛", "890", "628"
    };
    //随机数列
    private int sign[] = new int[36];
    //计数
    private int count = 0;
    //错误数量
    private int wrong = 0;
    ImageView img;//测试图
    Button select = null;//选择的答案按钮
    Button btn1, btn2, btn3, btn4, sure, back;//答案按钮和确认按钮,返回按钮
    private LinearLayout layout;//测试页面
    private RelativeLayout layout2;//结果页面
    private TextView text1, text2;//做题结果和测试结果

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_color);
        int i = 0;
        while (i < 35) {
            int num = new Random().nextInt(36);
            boolean flag = true;
            for (int j = 0; j < 36; j++) {
                if (num == sign[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                sign[i] = num;
                i++;
                if (i == 35)
                    continue;//最后一张是0,不加的话0进不去,需要改一下
            }
        }
        initData();//初始化控件
        initView(sign[count]);//初始化界面
    }

    /**
     * 初始化控件绑定点击事件
     */
    private void initData() {
        img = findViewById(R.id.img_color);
        btn1 = findViewById(R.id.answer1);
        btn2 = findViewById(R.id.answer2);
        btn3 = findViewById(R.id.answer3);
        btn4 = findViewById(R.id.answer4);
        sure = findViewById(R.id.button_color);
        back = findViewById(R.id.button_colorResult_back);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        sure.setOnClickListener(this);
        back.setOnClickListener(this);
        layout = findViewById(R.id.linear_colortest);
        layout2 = findViewById(R.id.rela_colortest);
        text1 = findViewById(R.id.text_colortest_re1);
        text2 = findViewById(R.id.text_colortest_re2);
    }

    /**
     * 重置界面
     *
     * @param num 标号
     */
    private void initView(int num) {
        img.setImageResource(imgId[num]);
        btn1.setText(answer[num][0]);
        btn2.setText(answer[num][1]);
        btn3.setText(answer[num][2]);
        if (count != 35)
            sure.setText("下一题(" + (count + 1) + "/36)");
        else
            sure.setText("提交(36/36)");
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (null != select)
            resetButton();
        switch (view.getId()) {
            case R.id.answer1:
                btn1.setBackgroundColor(Color.parseColor("#554F5E"));
                btn1.setTextColor(Color.WHITE);
                select = btn1;
                break;
            case R.id.answer2:
                btn2.setBackgroundColor(Color.parseColor("#554F5E"));
                btn2.setTextColor(Color.WHITE);
                select = btn2;
                break;
            case R.id.answer3:
                btn3.setBackgroundColor(Color.parseColor("#554F5E"));
                btn3.setTextColor(Color.WHITE);
                select = btn3;
                break;
            case R.id.answer4:
                btn4.setBackgroundColor(Color.parseColor("#554F5E"));
                btn4.setTextColor(Color.WHITE);
                select = btn4;
                break;
            case R.id.button_color:
                if (null == select)
                    Tools.showShortToast(this, "请先选择答案!");
                else if (count < 35) {
                    if (!select.getText().toString().trim().equals(correct[sign[count]]))
                        wrong++;//如果选择的答案和正确答案不同，错误数量加1
                    count++;
                    select = null;
                    initView(sign[count]);//加载下一题
                } else if (count == 35) {
                    if (!select.getText().toString().trim().equals(correct[sign[count]]))
                        wrong++;//如果选择的答案和正确答案不同，错误数量加1
                    count++;
                    select = null;
                    layout.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    text1.setText("共36题,正确" + (36 - wrong) + "题,错误" + wrong + "题");
                    if (wrong > 6) {
                        text2.setText("色觉异常");
                        text2.setTextColor(Color.parseColor("#F74461"));
                    } else {
                        text2.setText("色觉正常");
                        text2.setTextColor(Color.parseColor("#6BC235"));
                    }
                    //隐藏测试页面，显示结果页面
                } else {
                    select = null;
                }
                break;
            case R.id.button_colorResult_back:
                finish();
        }
    }

    /**
     * 恢复颜色
     */
    private void resetButton() {
        if (null == select)
            return;
        select.setBackgroundColor(Color.WHITE);
        select.setTextColor(Color.BLACK);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&count<36){
            AlertDialog alertDialog=new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("测试还未完成，确定退出吗?")
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ;
                        }
                    })
                    .create();
            alertDialog.show();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }
}
