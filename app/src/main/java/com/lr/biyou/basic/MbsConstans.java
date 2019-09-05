package com.lr.biyou.basic;

import android.os.Environment;

import com.lr.biyou.R;

import java.util.Map;

public class MbsConstans {
	/**
	 * 
	 ******************************************* 参数设置信息开始 ******************************************
	 */

	public static final String SERVER_IP_PORT = "172.16.1.65:12170";
	public static final String WEBSOCKET_URL = "ws://"+SERVER_IP_PORT+"/appsvr/";

//	public static final String SERVER_URL = "http://172.16.1.107:8088/api/";
//	public static final String SERVER_URL = "http://172.16.1.173:8082/appserv/";
//	public static final String SERVER_URL = "http://nsh.51zhir.cn/app_api/";
//	public static final String SERVER_URL = "http://172.16.1.226:8080/app_api/";
	public static final String SERVER_URL = "http://by.qilinpz.com/index/";
//	public static final String SERVER_URL = "http://172.16.1.102:8086/appserv/";

	public static final String XIEYI_URL = "http://ys.zhir.cn/";
//	public static final String XIEYI_URL = "https://phb.cpcn.com.cn/";


	//WEBSOCKET BASE_URL
	public static final String WEBSOCKET_BASE_URL = "ws://47.244.40.226";
	// K线
	public static final String KLINE_WEBSOCKET_URL = WEBSOCKET_BASE_URL + ":1317/";
	// 深度
	public static final String DEPTH_LEVER = WEBSOCKET_BASE_URL + ":1318/";
	// 当前币价格信息
	public static final String CURRENT_PRICE_URL = WEBSOCKET_BASE_URL + ":1316/";
	// 火币websocket数据
	public static final String HUOBI_WEBSOCKET = "wss://api.hadax.com/ws";

	public static final String PIC_URL = SERVER_URL+"viewer/image?path=";

	public static final String DATABASE_NAME = "biyous.db";
	public static final String DATABASE_PATH = "/data/data/com.lr.biyou/databases";
	public static final String DATA_PATH = "/data/data/android/files";

	// SDCard路径
	public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

	public static final String UPDATE_CODE = "biyou";
	// 路径
	public static final String BASE_PATH = SD_PATH + "/biyou/";
	//保存图片的路径
	public static final String PIC_SAVE = SD_PATH + "/biyou/pic/";
	//头像路径
	public static final String HEAD_IMAGE_PATH = SD_PATH+ "/biyou/headImage/";
	//二维码
	public static final String IMAGE_CODE_PATH = SD_PATH+ "/biyou/ImageCode/";
	//身份证照保存路径
	public static final String IDCARD_IMAGE_PATH = SD_PATH+ "/biyou/idCard/";
	//身份证照片名字
	public static final String IDCARD_FRONT="idcard_front";
	public static final String IDCARD_BACK="idcard_back";

	//调用照相机路径 Environment.getExternalStorageDirectory()+ "/gagayi/Photo_LJ/"
	public static final String PHOTO_PATH =  SD_PATH + "/biyou/biyou_photo/";
	//上传图片压缩路径
	public static final String UPLOAD_PATH =  SD_PATH + "/biyou/upload/";
	// 缓存图片路径
	public static final String BASE_IMAGE_CACHE = BASE_PATH + "/cache/images/";
	//apk下载路径
	public static String APP_DOWN_PATH = BASE_PATH +"apk";
	/*
	 * 登录用户一些基本信息
	 */
	public static boolean IS_LOGIN = false;

	public static String TEMP_TOKEN = "";

	//{auth=1, firm_kind=1, head_pic=default, name=刘德华,
	// uscc=91370105123D5P8C9U, tel=158****0123, comname=超神学院有限公司, idno=410725****3616, cmpl_info=1}
	public static Map<String,Object> USER_MAP = null;
	public static Map<String,Object> MONEY_INFO = null;
	public static String ACCESS_TOKEN = "";
	public static String REFRESH_TOKEN = "";
	public static String PAY_CODE = "";

	public static boolean LOGIN_OUT = false;
	//记录列表更新时间的文件名称
	public static String UPDATETIME = "JIUYUNDA_UPDATETIME";
	//系统类型
	public static final String SYS_NAME = android.os.Build.BRAND;
	// 手机名称
	public static final String MOBILE_NAME = android.os.Build.MODEL;
	//手机系统版本
	public static final String SYS_VERSION = android.os.Build.VERSION.RELEASE;
	//页面条数
	public static int PAGE_SIZE = 10;
	//当前获得编码的秒
	public static int CURRENT_TIME = 0;
	//当前图片循环的时间
	public static double PHOTO_CHANGE = 5;

	public static final long SECOND_TIME_500 = 500;

	public static boolean isNet = false;

	public static String RMB = "¥";


	public static int ALPHA = 0;
	public static int TOP_BAR_COLOR = R.color.top_bar_bg;


	/**	Android官网不建议使用enums，占用内存多（Enums often require more than twice as much memory as static constants.）。
	Android中当你的App启动后系统会给App单独分配一块内存。App的DEX code、Heap以及运行时的内存分配都会在这块内存中。
	*/
	//短信验证码界面   分类   根据不同分类，在相同界面进行不同操作）
	public static final class CodeType{
		public static final String CODE_KEY = "code_type";
		public static final  int CODE_PHONE_CHANGE = 0;//更换手机号 老的手机号
		public static final  int CODE_NEW_PHONE_CHANGE = 1;// 更换手机号 新的手机号
		public static final int CODE_RESET_LOGIN_PASS = 2;//重置登录密码
		public static final int CODE_MODIFY_ORDER_PASS = 3;//修改交易密码
		public static final int CODE_INSTALL = 4;//安装安全证书需要短信验证码
		public static final int CODE_WANGYIN = 5;//安装安全证书需要短信验证码
		public static final int CODE_PHONE_REGIST = 6;//注册

		public static final int CODE_CARD_CHONGZHI = 7;//绑定充值卡

		public static final int CODE_CHONGZHI_MONEY = 8;//充值钱，获取验证码

		public static final int CODE_TRADE_PASS = 9;//获取短信验证码  忘记交易密码

	}

	//人脸识别   分类   （根据不同分类，在相同界面进行不同操作）
	public static final class FaceType{
		public static final String FACE_KEY = "face_type";
		public static final  int FACE_AUTH = 10;//人脸识别认证
		public static final  int FACE_INSTALL = 11;//安装安全证书需要人脸认证    实名校验
		public static final  int FACE_CHECK_HUANKUAN = 12;//    实名校验  还款
		public static final  int FACE_CHECK_APPLY = 13;//    实名校验  申请额度
		public static final  int FACE_CHECK_BANK_PASS = 14;//    实名校验  开户密码
		public static final  int FACE_CHECK_BANK_BIND = 15;//    实名校验  绑定二类户

		public static final int FACE_CHECK_INDEX = 16;//  实名校验 首页申请额度等操作前，需要安装证书

		public static final int FACE_SIGN_HETONG = 17;//  签署合同需要人脸识别
		public static final int FACE_PEOPLE_CHECK = 18;//  共同借款人审核需要人脸识别
		public static final int FACE_BORROW_MONEY = 19;//  借款需要人脸识别
		public static final int FACE_UPLOAD_USE= 20;//  上传贷款用途凭证需要人脸验证
	}

	//人脸识别   分类   （根据不同分类，在相同界面进行不同操作）
	public static final class ResultType{
		public static final String RESULT_KEY = "result_type";
		public static final  int RESULT_APPLY_MONEY = 20;//申请额度 成功
		public static final  int RESULT_PHONE_CHANGE = 21;//更换手机号成功
		public static final  int RESULT_JIEKUAN = 22;//借款申请成功
		public static final  int RESULT_HUANKUAN = 23;//还款申请成功
	}
	//企业证书 提交结果 分类   （根据不同分类，在相同界面进行不同操作）
	public static final class QiYeResultType{
		public static final String RESULT_KEY = "qiye_result_type";
		public static final  int QIYE_CA_CHECK= 20;//申请证书 审核中
		public static final  int QIYE_CA_FOUJUE = 21;//申请证书 否决
		public static final  int QIYE_CA_TUIHUI= 22;//申请证书 退回
	}


	/**--------------------------------------------------
	 * 指纹识别的信息类
	 */
	public static final class FingerRecognize{

		public static final int MSG_AUTH_SUCCESS = 100;
		public static final int MSG_AUTH_FAILED = 101;
		public static final int MSG_AUTH_ERROR = 102;
		public static final int MSG_AUTH_HELP = 103;
	}




	/**--------------------------------------------------
	 * app的基本信息
	 */
	public static final class UpdateAppConstans{

		//本地app版本号
		public static int VERSION_APP_CODE = 100;
		//本地app中的version_name  当前应用的版本名称
		public static String VERSION_APP_NAME = "";
		// 默认的网络最新程序版本号
		public static int VERSION_NET_CODE = -1;
		//下载的APK的对应Id
		public static String VERSION_APK_ID = "";
		//新版本apk名称
		public static String VERSION_NET_APK_NAME = "";
		//版本编号
		public static String VERSION_NET_CODE_NAME = "";
		//新版本的apk存放地址
		public static String VERSION_NET_APK_URL = "";
		//apk更新信息
		public static String VERSION_NET_UPDATE_MSG = "";
		//是否强制更新
		public static boolean VERSION_UPDATE_FORCE=false;
		public static String VERSION_MD5_CODE = "";

	}

	
	/**----------------------------------------------------
	 * 一些SharedPreferences配置文件信息

	 * 作者：刘英超

	 * 时间：2014年12月29日 下午3:56:02

	 * 电子邮件：646869341@qq.com

	 * QQ：646869341
	 */
	public static final class SharedInfoConstans{

		public static String ACCESS_TOKEN = "ACCESS_TOKEN";
		public static String REFRESH_TOKEN = "REFRESH_TOKEN";

		public static String PAY_CODE = "PAY_CODE";

		//记录列表更新时间的文件名称
		public static String UPDATETIME = "DELIVERY_UPDATETIME";
		//记录登录一些配置信息的文件名称
		public static String LOGIN_INFO = "LOGIN_INFO";
		//存放下拉列表存储的用户名称键
		public static String LOGIN_NAME_LIST = "LOGIN_NAME_LIST";
		public static String LOGIN_NAME_LIST2 = "LOGIN_NAME_LIST2";
		//记录是否正常退出的字段
		public static String LOGIN_OUT = "LOGIN_OUT";
		//登录用户账号
		public static String LOGIN_ACCOUNT = "LOGIN_ACCOUNT";
		//登录用户密码
		public static String LOGIN_PASSWORD = "LOGIN_PASSWORD";
		//登录用户头像
		public static String HEAD_IMG = "HEAD_IMG";
		//登录用到的userKey
		public static String LOGIN_USER_KEY = "LOGIN_USER_KEY";
		//用户的TAG
		public static String PUSH_TAG = "PUSH_TAG";
		//是否首次启动
		public static final String IS_FIRST_START = "IS_FIRST_START1";
		//新的界面加载信息
		public static final String LOGO_URL = "LOGO_URL";

		public static final String WEIXIN_INFO = "WEIXIN_INFO";
		//登录方式   1  微信登录   2  手机号码登录
		public static final String LOGIN_TYPE = "LOGIN_TYPE";
		//是否显示手势轨迹
		public static String SHOW_SHOUSHI = "SHOW_SHOUSHI";
		//手势密码加密后的结果
		public static String SHOUSHI_CODE = "SHOUSHI_CODE";

		//name code  json数据
		public static String NAME_CODE_DATA = "NAME_CODE_DATA";



	}

	//自定义广播
	public static final class BroadcastReceiverAction{
		public static String MAIN_ACTIVITY = "MAIN_ACTIVITY";
		public static String NET_CHECK_ACTION = "NET_CHECK_ACTION";
		public static String FILE_TIP_ACTION = "FILE_TIP_ACTION";//上传附件数据标志
		public static String WEIXIN_PAY_ACTION = "WEIXIN_PAY_ACTION";
		public static String ZHENGSHU_UPDATE = "ZHENGSHU_UPDATE";//证书信息更新
		public static String USER_INFO_UPDATE = "USER_INFO_UPDATE";//用户信息更新
		public static String DAIBAN_INFO_UPDATE = "DAIBAN_INFO_UPDATE";//首页 待办列表数据更新
		public static String SHOUXIN_UPDATE = "SHOUXIN_UPDATE";//首页 授信状态信息变化更新
		public static String JIE_HUAN_UPDATE = "JIE_HUAN_UPDATE";// 还款  借款 成功后更新数据
		public static String QIAN_YUE_WY = "QIAN_YUE_WY";// 签约网银信息
		public static String OPEN_BANK = "OPEN_BANK";// 开户成功后  开户界面关闭
		public static String CAPAY_SUC = "CAPAY_SUC";// 证书支付费用匹配成功后返回操作  支付页面也要销毁

		public static String MONEY_UPDATE = "MONEY_UPDATE";//余额更新
		public static String BANKUPDATE_UPDATE = "BANKUPDATE_UPDATE";//银行卡列表更新

		public static String HTONGUPDATE = "HTONGUPDATE";//合同列表更新

		public static String TRADE_PASS_UPDATE = "TRADE_PASS_UPDATE";//交易密码更新

	}

    public static final class MessageEventType{
		public static int DOWN_LOAD = 0;
		public static int UPDATE_USERINFO = 1;

	}

	public static String APP_ID = "jpc4fc50f01af247bf";
	public static String ZONE_CODE = "1";
	public static String USER_KEY = "";

}
