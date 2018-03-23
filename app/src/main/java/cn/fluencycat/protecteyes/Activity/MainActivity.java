package cn.fluencycat.protecteyes.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.fluencycat.protecteyes.Bean.Tools;
import cn.fluencycat.protecteyes.Fragment.CommunityFragment;
import cn.fluencycat.protecteyes.Fragment.FindFragment;
import cn.fluencycat.protecteyes.Fragment.MeFragment;
import cn.fluencycat.protecteyes.Fragment.TestFragment;
import cn.fluencycat.protecteyes.R;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    //fragment
    private TestFragment testFragment;
    private FindFragment findFragment;
    private CommunityFragment communityFragment;
    private MeFragment meFragment;
    private Fragment nowFragment;//现在的fragment用于保存现在的fragment
    //fragment管理器
    private FragmentManager fragmentManager;
    //菜单按钮
    private LinearLayout test;
    private LinearLayout find;
    private LinearLayout community;
    private LinearLayout me;
    //按钮图片及文字
    private int nowButton=0;//0-3,指示当前页面按钮，点击后变化
    private ImageView[] imgArray=new ImageView[4];//ImageView数组
    private TextView[] textArray=new TextView[4];//TextView数组
    private int[] imgClickResorce=new int[]{R.mipmap.icon_test_click,R.mipmap.icon_find_click,R.mipmap.icon_community_click,R.mipmap.icon_me_click};//存放点击图片id
    private int[] imgNormalResorce=new int[]{R.mipmap.icon_test_normal,R.mipmap.icon_find_normal,R.mipmap.icon_community_normal,R.mipmap.icon_me_normal};//存放未点击图片id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();//初始化界面

    }

    /**
     * 初始化控件,和内容页,开启Fragment事务,按钮点击事件监听器
     */
    private void initViews() {
        imgArray[0]=findViewById(R.id.img_test_button);
        imgArray[1]=findViewById(R.id.img_find_button);
        imgArray[2]=findViewById(R.id.img_community_button);
        imgArray[3]=findViewById(R.id.img_me_button);
        textArray[0]=findViewById(R.id.text_test_button);
        textArray[1]=findViewById(R.id.text_find_button);
        textArray[2]=findViewById(R.id.text_community_button);
        textArray[3]=findViewById(R.id.text_me_button);
        test = findViewById(R.id.button_main_test);
        test.setOnClickListener(this);
        find = findViewById(R.id.button_main_find);
        find.setOnClickListener(this);
        community = findViewById(R.id.button_main_community);
        community.setOnClickListener(this);
        me = findViewById(R.id.button_main_me);
        me.setOnClickListener(this);
        testFragment = new TestFragment();
        findFragment = new FindFragment();
        communityFragment = new CommunityFragment();
        meFragment = new MeFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        initFragmentTransaction(fragmentTransaction);
        fragmentTransaction.show(testFragment);
        nowFragment=testFragment;
        fragmentTransaction.commit();
    }

    /**
     * 将所有fragment添加进事务并隐藏
     * @param fragmentTransaction
     */
    private void initFragmentTransaction(FragmentTransaction fragmentTransaction){
        fragmentTransaction.add(R.id.main_content, testFragment);
        fragmentTransaction.hide(testFragment);
        fragmentTransaction.add(R.id.main_content, findFragment);
        fragmentTransaction.hide(findFragment);
        fragmentTransaction.add(R.id.main_content,communityFragment);
        fragmentTransaction.hide(communityFragment);
        fragmentTransaction.add(R.id.main_content,meFragment);
        fragmentTransaction.hide(meFragment);
    }

    /**
     * 点击底部菜单，切换页面，改变图标和字体颜色
     * @param view
     */
    @Override
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.button_main_test:
                if(nowFragment==testFragment)
                    break;
                changeColor(0);
                changeFragment(fragmentTransaction,testFragment);
                break;//测试
            case R.id.button_main_find:
                if(nowFragment==findFragment)
                    break;
                changeColor(1);
                changeFragment(fragmentTransaction,findFragment);
                break;//发现
            case R.id.button_main_community:
                if(nowFragment==communityFragment)
                    break;
                changeColor(2);
                changeFragment(fragmentTransaction,communityFragment);
                break;//社区
            case R.id.button_main_me:
                if(nowFragment==meFragment)
                    break;
                changeColor(3);
                changeFragment(fragmentTransaction,meFragment);
                break;//我
        }
        fragmentTransaction.commit();//提交事务
    }

    /**
     * 隐藏当前fragment,显示选择的fragment,改变nowFragment为当前值
     * @param fragmentTransaction
     * @param fragment
     */
    private void changeFragment(FragmentTransaction fragmentTransaction,Fragment fragment){
        fragmentTransaction.hide(nowFragment);
        fragmentTransaction.show(fragment);
        nowFragment=fragment;
    }

    /**
     * 改变图标和字体颜色
     * @param id//0-3
     */
    private void changeColor(int id){
        imgArray[id].setBackgroundResource(imgClickResorce[id]);
        textArray[id].setTextColor(Color.parseColor("#554F5E"));//改为点击
        imgArray[nowButton].setBackgroundResource(imgNormalResorce[nowButton]);
        textArray[nowButton].setTextColor(Color.parseColor("#9A9A9A"));//改为普通状态
        nowButton=id;//现在点击的
    }

    /**
     * 2s内连按两次退出程序
     */
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Tools.showShortToast(this,"再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 广播接收器
     * 接受FinishActivity这个广播后
     * finish()
     */
    protected BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //在当前Activity中注册广播
        IntentFilter filter=new IntentFilter();
        filter.addAction("FinishActivity");
        this.registerReceiver(this.broadcastReceiver,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //
        this.unregisterReceiver(this.broadcastReceiver);
    }
}
