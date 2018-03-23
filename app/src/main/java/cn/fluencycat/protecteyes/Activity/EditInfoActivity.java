package cn.fluencycat.protecteyes.Activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import cn.fluencycat.protecteyes.Bean.Tools;
import cn.fluencycat.protecteyes.Const.ReturnCode;
import cn.fluencycat.protecteyes.Const.Url;
import cn.fluencycat.protecteyes.Const.UserInfo;
import cn.fluencycat.protecteyes.PickImg.CacheManager;
import cn.fluencycat.protecteyes.PickImg.FrescoImageLoader;
import cn.fluencycat.protecteyes.R;
import cn.fluencycat.protecteyes.View.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EditInfoActivity extends Activity {

    private Handler handler;
    private CircleImageView head_img;
    public static final int REQUEST_CODE_AVATAR = 100;
    public static final String AVATAR_FILE_NAME = "temp.jpg";
    private RadioGroup rp;
    private RadioButton rb1, rb2;
    private EditText edit_1, edit_2;
    private int sex = 0;
    private String name, motto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_info_layout);
        SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
                .setImageLoader(new FrescoImageLoader())
                .setToolbaseColor(getResources().getColor(R.color.colorPrimary)).build());
        initData();
    }

    /**
     * 初始化
     */
    private void initData() {
        handler = new Handler();
        sex = UserInfo.sex;
        name = UserInfo.name;
        motto = UserInfo.motto;
        rp = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_1);
        rb2 = findViewById(R.id.radio_2);
        if (sex == 0)
            rb1.setChecked(true);
        else
            rb2.setChecked(true);
        rp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_1:
                        sex = 0;
                        break;
                    case R.id.radio_2:
                        sex = 1;
                        break;
                }
            }
        });
        edit_1 = findViewById(R.id.edit_editName);
        edit_1.setText(name);
        edit_2 = findViewById(R.id.edit_motto);
        edit_2.setText(motto);
        head_img = findViewById(R.id.img_edit_head);
        if (UserInfo.uploadHead == 1)//上传过,用用户自定义的
            getHeadIcon(Url.HeadIcon + UserInfo.phone + ".jpg");
        //打开图片选择器,选择头像图片
        head_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SImagePicker
                        .from(EditInfoActivity.this)
                        .pickMode(SImagePicker.MODE_AVATAR)
                        .showCamera(true)
                        .cropFilePath(
                                CacheManager.getInstance().getImageInnerCache()
                                        .getAbsolutePath(AVATAR_FILE_NAME))
                        .forResult(REQUEST_CODE_AVATAR);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            final ArrayList<String> pathList =
                    data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);//选择的图片路径集合
            Bitmap bitmap = BitmapFactory.decodeFile(pathList.get(0));//得到图片的bitmap
            upLoadHead(pathList.get(0));
            head_img.setImageBitmap(bitmap);
        }//图片选择且剪切成功
    }

    /**
     * 上传头像图片
     *
     * @param imgPath
     */
    private void upLoadHead(String imgPath) {
        OkHttpClient okHttpClient = new OkHttpClient();
        File file = new File(imgPath);
        MediaType type = MediaType.parse("image/jpeg;charset=utf-8");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone", UserInfo.phone)
                .addFormDataPart("upcode", UserInfo.uploadHead + "")
                .addFormDataPart("img", UserInfo.phone + ".jpg", RequestBody.create(type, file));
        RequestBody body = builder.build();
        Request request = new Request.Builder().post(body).url(Url.UploadHead).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Tools.showShortToast(EditInfoActivity.this, "修改失败，请检查网络");
                        setResult(1);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string().trim();
                if (result.equals(ReturnCode.UPLOADHEAD_SUCCESSFUL + "")) {
                    UserInfo.uploadHead = 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showShortToast(EditInfoActivity.this, "上传成功");
                            setResult(2);
                        }
                    });
                } else if (result.equals(ReturnCode.UPLOADHEAD_FAULT + "")) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showShortToast(EditInfoActivity.this, "修改头像失败");
                            setResult(1);
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showShortToast(EditInfoActivity.this, "修改头像失败");
                            setResult(1);
                        }
                    });
                }
            }
        });
    }

    /**
     * 获取用户头像
     *
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
                        Tools.showShortToast(EditInfoActivity.this, "获取头像失败");
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
                        head_img.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    /**
     * 返回
     *
     * @param v
     */
    public void editinfo_back(View v) {
        finish();
    }

    public void save_info(View v) {
        name = edit_1.getText().toString();
        motto = edit_2.getText().toString();
        if (name.length() == 0) {
            Tools.showShortToast(this, "请输入用户名");
            return;
        }
        if (motto.length() == 0) {
            Tools.showShortToast(this, "请输入签名");
            return;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody body = new FormBody.Builder().add("phone", UserInfo.phone).add("sex", sex + "").add("name", name).add("motto", motto).build();
        Request request = new Request.Builder().post(body).url(Url.EditInfo).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Tools.showShortToast(EditInfoActivity.this, "保存信息失败,请检查网络");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String code = response.body().string().trim();
                if (code.equals("" + ReturnCode.EDITINFO_SUCCESSFUL)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showShortToast(EditInfoActivity.this, "保存信息成功");
                            UserInfo.sex=sex;
                            UserInfo.name=name;
                            UserInfo.motto=motto;
                            finish();
                        }
                    });
                } else if (code.equals("" + ReturnCode.EDITINFO_FAULT)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showShortToast(EditInfoActivity.this, "保存信息失败");
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showShortToast(EditInfoActivity.this, "保存信息失败");
                        }
                    });
                }
            }
        });
    }

}
