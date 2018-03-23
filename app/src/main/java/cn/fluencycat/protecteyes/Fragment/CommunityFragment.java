package cn.fluencycat.protecteyes.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.fluencycat.protecteyes.Activity.ArticalActivity;
import cn.fluencycat.protecteyes.Activity.EditInfoActivity;
import cn.fluencycat.protecteyes.Activity.ForgetPwdActivity;
import cn.fluencycat.protecteyes.Activity.LoginActivity;
import cn.fluencycat.protecteyes.Activity.MyArticalActivity;
import cn.fluencycat.protecteyes.Activity.NewArticalActivity;
import cn.fluencycat.protecteyes.Activity.RegisterActivity;
import cn.fluencycat.protecteyes.Bean.Tools;
import cn.fluencycat.protecteyes.Const.Url;
import cn.fluencycat.protecteyes.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 社区页面的fragment
 */

public class CommunityFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Intent intent;
    private ImageView serch, myArtical, newAratical;//右上角三个按钮图标
    private ListView list;//列表
    private Handler handler;
    private static Bitmap bitmap;
    private static boolean finishFlag = false;
    private RefreshLayout refreshLayout;
    private JSONArray jsonArray;
    private int NEW_ARTICAL_CODE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_community, container, false);
        initData();
        return view;
    }

    /**
     * 初始化控件,绑定监听器
     */
    private void initData() {
        handler = new Handler();
        intent = new Intent();
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initList();
                refreshlayout.finishRefresh(3000);
            }
        });
        serch = view.findViewById(R.id.button_com_serch);
        myArtical = view.findViewById(R.id.button_com_my);
        newAratical = view.findViewById(R.id.button_com_new);
        serch.setOnClickListener(this);
        myArtical.setOnClickListener(this);
        newAratical.setOnClickListener(this);
        list = view.findViewById(R.id.list_all);
        initList();//列表
    }

    /**
     * 初始化列表
     */
    private void initList() {

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Url.AllArtical).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Tools.showShortToast(getActivity(), "请检查网络");//请求失败
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                if (!result.equals("null")&&!result.equals("[]"))
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            initListItem(result);
                        }
                    });
                else
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Tools.showShortToast(getActivity(), "还没有文章");
                        }
                    });
            }
        });

    }

    /**
     * 得到所有文章信息后初始化列表项
     *
     * @param mes jsonArray类型的string
     */
    private void initListItem(String mes) {
        try {
            ArrayList<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();//列表项的集合
            jsonArray = new JSONArray(mes);
            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, Object> listItem = new HashMap<String, Object>();//单个列表项
                Bitmap img = getArticalIcon(jsonObject.getString("phone"), jsonObject.getString("titleId"));
                listItem.put("headIcon", img);
                listItem.put("title", jsonObject.getString("title"));
                listItem.put("context", jsonObject.getString("context"));
                listItem.put("time", jsonObject.getString("date"));
                listItems.add(listItem);
            }
            //创建一个SimpleAdpter
            SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), listItems, R.layout.artical_item,
                    new String[]{"headIcon", "title", "context", "time"}
                    , new int[]{R.id.item_img, R.id.item_title, R.id.item_context, R.id.item_time});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView iv = (ImageView) view;
                        iv.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            list.setAdapter(simpleAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            articalPage(position);
                        }
                    });
                }
            });//列表项点击事件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转至帖子查看页面
     *
     * @param i
     */
    private void articalPage(int i) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - i - 1);
            Intent intent2=new Intent(getActivity(), ArticalActivity.class);
            intent2.putExtra("phone",jsonObject.getString("phone"));
            intent2.putExtra("name",jsonObject.getString("name"));
            intent2.putExtra("titleId",jsonObject.getString("titleId"));
            intent2.putExtra("title",jsonObject.getString("title"));
            intent2.putExtra("context",jsonObject.getString("context"));
            intent2.putExtra("picNum",jsonObject.getString("picNum"));
            intent2.putExtra("date",jsonObject.getString("date"));
            startActivity(intent2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文章第一张图片
     *
     * @param phone
     * @param titleID
     * @return
     */
    private Bitmap getArticalIcon(String phone, String titleID) {
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = Url.ArticalImg + phone + "_" + titleID + "_1.jpg";
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Tools.showShortToast(getActivity(), "获取图片失败");
                        bitmap = null;
                        finishFlag = true;
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final byte[] head_bt = response.body().bytes();
                bitmap = BitmapFactory.decodeByteArray(head_bt, 0, head_bt.length);
                finishFlag = true;
            }
        });
        while (!finishFlag) {
        }//相当于阻塞主线程，直到图片获取完成才释放
        finishFlag = false;
        return bitmap;
    }

    /**
     * 点击事件监听器
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_com_serch:
                //startActivity(intent.setClass(getActivity(), LoginActivity.class));
                Tools.showShortToast(getActivity(), "搜索帖子");
                break;
            case R.id.button_com_my:
                startActivity(intent.setClass(getActivity(), MyArticalActivity.class));
                break;
            case R.id.button_com_new:
                startActivityForResult(intent.setClass(getActivity(), NewArticalActivity.class), NEW_ARTICAL_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_ARTICAL_CODE) {
            if (resultCode == 1)
                initList();//如果发布成功，回来后刷新list
        }
    }
}
