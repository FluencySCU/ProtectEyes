package cn.fluencycat.protecteyes.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.fluencycat.protecteyes.Activity.TestColorActivity;
import cn.fluencycat.protecteyes.R;

/**
 * 测试-色觉界面
 */

public class Test_Color extends Fragment{
    private Button start;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_test_color,container,false);
        initData();//初始化
        return view;
    }

    private void initData(){
        start=view.findViewById(R.id.button_start_color);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TestColorActivity.class));
            }
        });
    }
}
