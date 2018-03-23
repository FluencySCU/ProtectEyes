package cn.fluencycat.protecteyes.Bean;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 工具类
 */
public class Tools {
    /**
     * @param context
     * @return true表示网络可用, false表示网络不可用
     */
    public static boolean NetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);//得到网络连接管理器
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();//得到网络连接信息
            if (networkInfo != null) {
                return networkInfo.isAvailable();//返回可用还是不可用
            }
        }
        return false;
    }

    /**
     * 短时间提示
     *
     * @param context,上下文
     * @param text,打印的文字
     */
    public static void showShortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 对字符串进行简单的MD5加密
     * @param str
     * @return
     */
    public static String MD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
