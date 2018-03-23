package cn.fluencycat.protecteyes.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.fluencycat.protecteyes.Activity.WebActivity;
import cn.fluencycat.protecteyes.R;

public class Find_Plan extends Fragment {
    private Button[] baidu_btn = new Button[4];
    private int[] baidu_id = {R.id.baidu_1, R.id.baidu_2, R.id.baidu_3, R.id.baidu_4};
    private final String[] baidu_url = {"https://m.baidu.com/sf?openapi=1&dspName=iphone&from_sf=1&pd=disease_kgb&resource_id=4553&word=%E8%BF%91%E8%A7%86%E7%9C%BC%E6%A6%82%E8%BF%B0&pgn=summary&top=%7B%22sfhs%22%3A11%7D&title=%E8%BF%91%E8%A7%86%E7%9C%BC&ext=%7B%22sf_tab_name%22%3A%22%E6%A6%82%E8%BF%B0%22%7D&frsrcid=4358&frorder=1&lid=13925873659748108122"
            , "https://m.baidu.com/sf?openapi=1&dspName=iphone&from_sf=1&pd=disease_kgb&resource_id=4553&word=%E6%95%A3%E5%85%89%E6%A6%82%E8%BF%B0&pgn=summary&top=%7B%22sfhs%22%3A11%7D&title=%E6%95%A3%E5%85%89&ext=%7B%22sf_tab_name%22%3A%22%E6%A6%82%E8%BF%B0%22%7D&frsrcid=4358&frorder=1&lid=8503740329711436744"
            , "https://m.baidu.com/sf?openapi=1&dspName=iphone&from_sf=1&pd=disease_kgb&resource_id=4553&word=%E8%89%B2%E8%A7%89%E5%BC%82%E5%B8%B8%E7%AE%80%E4%BB%8B&top=%7B%22sfhs%22%3A11%7D&title=%E8%89%B2%E8%A7%89%E5%BC%82%E5%B8%B8&pgn=summary&ext=%7B%22sf_tab_name%22%3A%22%E7%AE%80%E4%BB%8B%22%7D"
            , "https://m.baidu.com/sf?openapi=1&dspName=iphone&from_sf=1&pd=disease_kgb&resource_id=4553&word=%E6%96%9C%E8%A7%86%E6%A6%82%E8%BF%B0&top=%7B%22sfhs%22%3A11%7D&title=%E6%96%9C%E8%A7%86&pgn=summary&ext=%7B%22sf_tab_name%22%3A%22%E6%A6%82%E8%BF%B0%22%7D&lid=13050002873181043452&referlid=13050002873181043452&ms=1&frsrcid=4358&frorder=1"};
    private Button[] diet_btn = new Button[4];
    private int[] diet_id = {R.id.diet_1, R.id.diet_2, R.id.diet_3, R.id.diet_4};
    private final String[] diet_url = {"https://www.xinshipu.com/s/d4a4b7c0bdfccad3/",
            "https://www.xinshipu.com/chishenme/114350/",
            "https://www.xinshipu.com/chishenme/114331/",
            "https://www.xinshipu.com/s/d1dbc6a3c0cd/"
    };
    private TextView[] info_text = new TextView[15];
    private int[] info_id = {R.id.info_1, R.id.info_2, R.id.info_3, R.id.info_4, R.id.info_5,
            R.id.info_6, R.id.info_7, R.id.info_8, R.id.info_9, R.id.info_10,
            R.id.info_11, R.id.info_12, R.id.info_13, R.id.info_14, R.id.info_15};
    private final String[] info_url =
            {"https://m.baidu.com/mip/c/s/m.fh21.com.cn/jibing/mip/731154.html"
                    , "https://www.zhihu.com/question/27858385"
                    , "https://jingyan.baidu.com/article/cbcede070c1cac02f40b4d00.html"
                    , "https://wapbaike.baidu.com/item/%E8%BF%91%E8%A7%86%E7%9C%BC%E6%81%A2%E5%A4%8D/12635779?fr=aladdin"
                    , "https://m.sohu.com/a/24024502_209015/?pvid=000115_3w_a"
                    , "https://jingyan.baidu.com/article/37bce2be76b7a81002f3a2c1.html"
                    , "https://wapbaike.baidu.com/item/%E8%B4%9D%E8%8C%A8%E8%A7%86%E5%8A%9B%E8%AE%AD%E7%BB%83%E6%81%A2%E5%A4%8D%E6%B3%95/4249096"
                    , "https://xw.qq.com/js/20160325021603/JSC2016032502160300"
                    , "http://m.gaosan.com/gaokao/182985.html"
                    , "http://m.gaosan.com/gaokao/183180.html"
                    , "https://m.baidu.com/from=2001a/bd_page_type=1/ssid=1fcfceded3efb3a9b3a9436a/uid=0/pu=usm%402%2Csz%40320_1003%2Cta%40iphone_2_6.0_1_11.7/baiduid=4B61C7759B933B811F0CF70F60AAF5C9/w=50_10_/t=iphone/l=3/tc?ref=www_iphone&lid=13519612226968135712&order=3&fm=alop&tj=www_normal_3_50_10_title&vit=osres&m=8&srd=1&cltj=cloud_title&asres=1&title=%E8%B6%85%E9%AB%98%E5%BA%A6%E8%BF%91%E8%A7%86%E7%9C%BC%E6%80%8E%E4%B9%88%E6%81%A2%E5%A4%8D%E8%A7%86%E5%8A%9B%E8%BF%99%E4%BA%9B%E8%BF%90%E5%8A%A8%E5%8D%83%E4%B8%87%E5%88%AB%E5%81%9A-..._%E4%BA%BA%E6%B0%91%E7%BD%91&dict=20&w_qd=IlPT2AEptyoA_ykx5v1p8AKvE6dOpncpxEcXl3vVthO&sec=25310&di=7f2b0e2f92f99e26&bdenc=1&tch=124.234.267.506.1.536&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IFR0PRSlLAjKr95qshbrgHhEsRD7h2Sm5UZWccyPQpx9FwnOu0VB-7BN2tKhgvmgf98nkcffsbM8_U1e&eqid=bb9f4fb0c176a800100000005a046c6b&wd=&clk_info=%7B%22srcid%22%3A1599%2C%22tplname%22%3A%22www_normal%22%2C%22t%22%3A1510239352038%2C%22xpath%22%3A%22div-a-h3%22%7D&sfOpen=1"
                    , "https://jingyan.baidu.com/article/a681b0dedcc7523b1943467b.html"
                    , "https://m.baidu.com/mip/c/s/m.120ask.com/jingyan/bdmip/8lwm3lsk3lwmrf9yr8.html"
                    , "https://www.zhihu.com/question/49491280"
                    , "https://jingyan.baidu.com/article/c14654136e9cf10bfcfc4c8f.html"};

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find_plan, container, false);
        initData();//绑定控件
        initListener();//设置监听
        return view;
    }

    private void initData() {
        for (int i = 0; i < baidu_btn.length; i++) {
            baidu_btn[i] = view.findViewById(baidu_id[i]);
        }
        for (int i = 0; i < diet_btn.length; i++) {
            diet_btn[i] = view.findViewById(diet_id[i]);
        }
        for (int i = 0; i < info_text.length; i++) {
            info_text[i] = view.findViewById(info_id[i]);
        }
    }

    private void initListener() {
        for (int i = 0; i < baidu_btn.length; i++) {
            final int j = i;
            baidu_btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", baidu_url[j]);
                    startActivity(intent);
                }
            });
        }
        for (int i = 0; i < diet_btn.length; i++) {
            final int j = i;
            diet_btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", diet_url[j]);
                    startActivity(intent);
                }
            });
        }
        for (int i = 0; i < info_text.length; i++) {
            final int j = i;
            info_text[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("url", info_url[j]);
                    startActivity(intent);
                }
            });
        }
    }

}
