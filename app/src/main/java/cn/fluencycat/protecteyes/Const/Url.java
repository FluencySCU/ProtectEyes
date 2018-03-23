package cn.fluencycat.protecteyes.Const;

/**
 * 存储请求的url地址
 */

public class Url {
    private static final String urlHead="http://114.215.83.236:8080/psServlet";//前缀
    public static final String Login=urlHead+"/login";//登陆
    public static final String UserInfo=urlHead+"/userinfo";//登陆成功后获取用户信息
    public static final String Register=urlHead+"/register";//注册
    public static final String EditPassword=urlHead+"/editpassword";//修改密码
    public static final String HeadIcon=urlHead+"/head_image/";//头像
    public static final String ArticalImg=urlHead+"/artical_image/";//文章图片
    public static final String UploadHead=urlHead+"/uphead";//上传头像
    public static final String UploadArtical=urlHead+"/upartical";//上传文章
    public static final String AllArtical=urlHead+"/allartical";//所有文章
    public static final String UserArtical=urlHead+"/userartical";//用户文章
    public static final String Record=urlHead+"/record";//打卡
    public static final String EditInfo=urlHead+"/editinfo";//修改信息
    public static final String EditEye=urlHead+"/editeye";//修改眼睛
}
