package cn.fluencycat.protecteyes.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.fluencycat.protecteyes.Activity.TestEyeActivity;
import cn.fluencycat.protecteyes.R;

/**
 * 测试-视力界面
 */

public class Test_Eye extends Fragment{
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_test_eye,container,false);
        initData();
        return view;
    }

    private void initData(){
        Button button=view.findViewById(R.id.button_start_eye);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TestEyeActivity.class));
            }
        });
    }
}
