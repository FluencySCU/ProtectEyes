package cn.fluencycat.protecteyes.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import cn.fluencycat.protecteyes.Bean.Tools;
import cn.fluencycat.protecteyes.R;

/**
 * 测试页面的fragment
 */

public class TestFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ViewPager vp;
    private TextView text_eye, text_sanguang, text_color;
    private View line1, line2, line3;
    private ArrayList<Fragment> fragments;
    private Fragment eye, sanguang, color;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);
        initData();
        initViewPager();
        return view;
    }

    /**
     * 绑定控件，设置按钮监听器,初始化Fragment
     */
    private void initData() {
        text_eye = view.findViewById(R.id.text_test_eye);
        text_sanguang = view.findViewById(R.id.text_test_sanguang);
        text_color = view.findViewById(R.id.text_test_color);
        line1 = view.findViewById(R.id.view_test_1);
        line2 = view.findViewById(R.id.view_test_2);
        line3 = view.findViewById(R.id.view_test_3);
        text_eye.setOnClickListener(this);
        text_sanguang.setOnClickListener(this);
        text_color.setOnClickListener(this);
        eye = new Test_Eye();
        sanguang = new Test_Sanguang();
        color = new Test_Color();
        fragments = new ArrayList<Fragment>();
        fragments.add(eye);
        fragments.add(sanguang);
        fragments.add(color);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        vp = view.findViewById(R.id.vp_test);
        vp.setAdapter(new FragmentPagerAdapter(this.getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        vp.setOnPageChangeListener(new MyOnPageChangeListener());
        vp.setCurrentItem(0);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    resetLine();
                    line1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    resetLine();
                    line2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    resetLine();
                    line3.setVisibility(View.VISIBLE);
                    break;
            }
        }//切换后

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }//滑动

        @Override
        public void onPageScrollStateChanged(int state) {

        }//状态
    }

    private void resetLine() {
        line1.setVisibility(View.INVISIBLE);
        line2.setVisibility(View.INVISIBLE);
        line3.setVisibility(View.INVISIBLE);
    }

    /**
     * 顶部菜单点击监听器，跳转并改变下划线
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_test_eye:
                vp.setCurrentItem(0);
                resetLine();
                line1.setVisibility(View.VISIBLE);
                break;
            case R.id.text_test_sanguang:
                vp.setCurrentItem(1);
                resetLine();
                line2.setVisibility(View.VISIBLE);
                break;
            case R.id.text_test_color:
                vp.setCurrentItem(2);
                resetLine();
                line3.setVisibility(View.VISIBLE);
                break;
        }
    }

}
