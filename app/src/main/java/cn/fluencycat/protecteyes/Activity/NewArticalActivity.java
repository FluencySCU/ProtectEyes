package cn.fluencycat.protecteyes.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.fluencycat.protecteyes.Bean.Tools;
import cn.fluencycat.protecteyes.Const.ReturnCode;
import cn.fluencycat.protecteyes.Const.Url;
import cn.fluencycat.protecteyes.Const.UserInfo;
import cn.fluencycat.protecteyes.PickImg.FrescoImageLoader;
import cn.fluencycat.protecteyes.PickImg.PickAdapter;
import cn.fluencycat.protecteyes.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewArticalActivity extends Activity {
    private TextView percent;//显示输入字符百分比的textView
    private EditText text, title;//输入文字框，标题
    private TextView submit;//发布按钮
    private Button back, selectImg;//返回按钮
    private Handler handler;
    public static final int REQUEST_CODE_IMAGE = 101;
    private GridView gridView;
    private PickAdapter pickAdapter;
    private ArrayList<String> picPath = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_artical_layout);
        SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
                .setImageLoader(new FrescoImageLoader())
                .setToolbaseColor(getResources().getColor(R.color.colorPrimary)).build());
        initData();//初始化
    }

    /**
     * 初始化
     */
    private void initData() {
        handler = new Handler();
        pickAdapter = new PickAdapter(this);
        gridView = findViewById(R.id.image_grid);
        gridView.setAdapter(pickAdapter);
        percent = findViewById(R.id.text_newArtical_percent);//已输入的百分比
        title = findViewById(R.id.new_artical_title);//标题
        text = findViewById(R.id.new_artical_text);//输入框
        text.addTextChangedListener(new TextWatcher() {//文字框文字数量监听器
            int length = 0;
            int beforeLength = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                length = charSequence.length();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                percent.setText(length + "/1500");
                if (length == 1500)
                    percent.setTextColor(Color.RED);
                if (beforeLength == 1500)
                    percent.setTextColor(Color.parseColor("#8A000000"));
            }
        });
        back = findViewById(R.id.button_newArtical_back);//返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != picPath || !title.getText().toString().trim().equals("") || !text.getText().toString().trim().equals("")) //按了返回键后,若选择图片或有输入文字都弹出对话框
                    showAlertDialog();
                else
                    finish();//如果有字或者图片，提示框，是否退出，若没有，则直接退出
            }
        });
        selectImg = findViewById(R.id.button_newArtical_selectImg);//选择图片
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SImagePicker
                        .from(NewArticalActivity.this)
                        .maxCount(9)
                        .rowCount(3)
                        .showCamera(false)
                        .pickMode(SImagePicker.MODE_IMAGE)
                        .forResult(REQUEST_CODE_IMAGE);
            }
        });
        submit = findViewById(R.id.button_newArtical_submit);//发布按钮
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Submit();
            }
        });
    }

    /**
     * 提交文字和图片以及用户信息
     */
    private void Submit() {
        String titleText = title.getText().toString();
        if (titleText.equals("")) {
            Tools.showShortToast(this, "请输入标题");
            return;
        }
        String artical = text.getText().toString();
        if (artical.equals("")) {
            Tools.showShortToast(this, "内容不能为空");
            return;
        }
        if (null == picPath) {
            Tools.showShortToast(this, "请至少上传一张图片");
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);//不可手动关闭
        progressDialog.show();
        progressDialog.setMessage("请稍后...");
        OkHttpClient okHttpClient = new OkHttpClient();
        MediaType type = MediaType.parse("image/jpeg;charset=utf-8");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", UserInfo.phone)
                .addFormDataPart("name", UserInfo.name)
                .addFormDataPart("title", titleText)
                .addFormDataPart("text", artical)
                .addFormDataPart("titleId", (UserInfo.articalNum + 1) + "")
                .addFormDataPart("picNum", picPath.size() + "");
        for (int i = 1; i <= picPath.size(); i++) {
            File file = new File(picPath.get(i - 1));
            builder.addFormDataPart(i + "", i + ".jpg", RequestBody.create(type, file));
        }//添加图片
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().post(requestBody).url(Url.UploadArtical).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Tools.showShortToast(getApplicationContext(), "发布失败，请重试");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String code = response.body().string().trim();
                if (code.equals(ReturnCode.UPLOADARTICAL_SUCCESSFUL + "")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            UserInfo.articalNum++;
                            Tools.showShortToast(getApplicationContext(), "发布成功");
                            setResult(1);
                            finish();
                        }
                    });
                } else if (code.equals(ReturnCode.UPLOADARTICAL_FAULT + "")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Tools.showShortToast(getApplicationContext(), "发布失败，请重试");
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Tools.showShortToast(getApplicationContext(), "发布失败，请重试");
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            picPath =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);
            pickAdapter.setNewData(picPath);
        }
    }

    /**
     * 返回按钮监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && (null != picPath || !title.getText().toString().trim().equals("") || !text.getText().toString().trim().equals(""))) {//按了返回键后,若选择图片或有输入文字都弹出对话框
            showAlertDialog();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    //是否返回对话框
    private void showAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("还未发布，确定退出吗?")
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
    }
}
