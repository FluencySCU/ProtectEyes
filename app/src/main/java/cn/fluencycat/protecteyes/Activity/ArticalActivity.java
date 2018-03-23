package cn.fluencycat.protecteyes.Activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import cn.fluencycat.protecteyes.Bean.Tools;
import cn.fluencycat.protecteyes.Const.Url;
import cn.fluencycat.protecteyes.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArticalActivity extends Activity{
    private int[] imgViewId={R.id.artical_img_1,R.id.artical_img_2,R.id.artical_img_3,
            R.id.artical_img_4,R.id.artical_img_5,R.id.artical_img_6,
            R.id.artical_img_7,R.id.artical_img_8,R.id.artical_img_9};
    private ImageView[] imgView=new ImageView[9];
    private ImageView headIcon;
    private Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artical_page);
        initView();//初始化界面
    }

    private void initView() throws NumberFormatException{
        handler=new Handler();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String phone=bundle.getString("phone");//电话号码
        String titleId=bundle.getString("titleId");//文章id
        String name=bundle.getString("name");//用户名
        String title=bundle.getString("title");//文章标题
        String context=bundle.getString("context");//文章内容
        String date=bundle.getString("date");//文章日期
        int picNum=Integer.parseInt(bundle.getString("picNum"));//图片数量
        TextView TITLE=findViewById(R.id.artical_title);
        TITLE.setText(title);
        TextView NAME=findViewById(R.id.user_name);
        NAME.setText(name);
        TextView DATE=findViewById(R.id.artical_date);
        DATE.setText(date);
        TextView CONTEXT=findViewById(R.id.artical_context);
        CONTEXT.setText(context);
        headIcon=findViewById(R.id.user_headIcon);
        getHeadIcon(phone);
        getArticalImgs(picNum,phone,titleId);
    }

    /**
     * 获取用户头像
     * @param phone
     */
    private void getHeadIcon(String phone) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = Url.HeadIcon + phone+".jpg";
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Tools.showShortToast(ArticalActivity.this, "获取头像失败");
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
                        headIcon.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    /**
     * 获取文章图片
     * @param number
     * @param phone
     * @param titleId
     */
    private void getArticalImgs(int number,String phone,String titleId){
        for(int i=0;i<number;i++){
            imgView[i]=findViewById(imgViewId[i]);
            OkHttpClient okHttpClient = new OkHttpClient();
            String url = Url.ArticalImg + phone + "_" + titleId + "_"+(i+1)+".jpg";
            Request request = new Request.Builder().url(url).build();
            final int j=i;
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showShortToast(ArticalActivity.this, "获取图片失败");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final byte[] head_bt = response.body().bytes();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(head_bt, 0, head_bt.length);
                            imgView[j].setImageBitmap(bitmap);
                            imgView[j].setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        }
    }

    /**
     * 返回
     * @param v
     */
    public void artical_back(View v){
        finish();
    }
}
