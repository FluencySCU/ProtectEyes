package cn.fluencycat.protecteyes.Const;

/**
 * 用来存储返回的数字对应的信息
 */

public class ReturnCode {
    public static final int REGISTER_ALREADY_EXISTS=0;//注册，已经存在
    public static final int REGISTER_SUCCESSFUL=1;//注册，成功
    public static final int REGISTER_FAULT=2;//注册，失败
    public static final int LOGIN_SUCCESSFUL=3;//登陆，成功
    public static final int LOGIN_FAULT=4;//登陆，失败
    public static final int EDITPASSWORD_SUCCESSFUL=5;//修改密码，成功
    public static final int EDITPASSWORD_FAULT=6;//修改密码，失败,用户未注册
    public static final int UPLOADHEAD_SUCCESSFUL=7;//上传头像，成功
    public static final int UPLOADHEAD_FAULT=8;//上传头像，失败
    public static final int UPLOADARTICAL_SUCCESSFUL=9;//发布文章，成功
    public static final int UPLOADARTICAL_FAULT=10;//发布文章，失败
    public static final int RECORD_SUCCESSFUL=11;//护眼打卡，成功
    public static final int RECORD_FAULT=12;//护眼打卡，失败
    public static final int EDITINFO_SUCCESSFUL=13;//修改信息，成功
    public static final int EDITINFO_FAULT=14;//修改信息，失败
    public static final int EDITEYE_SUCCESSFUL=15;//修改眼睛，成功
    public static final int EDITEYE_FAULT=16;//修改眼睛，失败
    //...
}
