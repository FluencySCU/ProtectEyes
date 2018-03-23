package cn.fluencycat.protecteyes.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.fluencycat.protecteyes.Activity.TestSanguangActivity;
import cn.fluencycat.protecteyes.R;

/**
 * 测试-散光界面
 */

public class Test_Sanguang extends Fragment{
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_test_sanguang,container,false);
        Button button=view.findViewById(R.id.button_start_sanguang);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),TestSanguangActivity.class));
            }
        });
        return view;
    }
}
