package cn.fluencycat.protecteyes.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.fluencycat.protecteyes.R;

/**
 * 发现-商城页面
 */


public class Find_Mall extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_find_mall,container,false);
        return view;
    }
}
