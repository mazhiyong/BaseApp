package com.lr.biyou.utils.tool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.lr.biyou.basic.BasicApplication;
import com.lr.biyou.basic.MbsConstans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

import static android.view.View.NO_ID;

/**
 *

 * @Description: 辅助类用于一些常规方法

 * @author:刘英超

 * @time:2015-12-23 下午3:17:52

 * @email:646869341@qq.com
 */
public class UtilTools {

	/**
	 * 活动屏幕信息
	 */
	private static WindowManager wm;
	/**
	 * 获取真实屏幕高度
	 *
	 * @return
	 */
	public static int getRealHeight() {
		if (null == wm) {
			wm = (WindowManager)
					BasicApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
		}
		Point point = new Point();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			wm.getDefaultDisplay().getRealSize(point);
		} else {
			wm.getDefaultDisplay().getSize(point);
		}
		return point.y;
	}
	/**
	 * 获取状态栏高度
	 *
	 * @return
	 */
	public static int getStatusBarHeight() {
		int result = 0;
		int resourceId = BasicApplication.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = BasicApplication.getContext().getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 判断是否显示了导航栏
	 * (说明这里的context 一定要是activity的context 否则类型转换失败)
	 *
	 * @param context
	 * @return
	 */
	public static boolean isShowNavBar(Context context) {
		if (null == context) {
			return false;
		}
		/**
		 * 获取应用区域高度
		 */
		Rect outRect1 = new Rect();
		try {
			((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
		} catch (ClassCastException e) {
			e.printStackTrace();
			return false;
		}
		int activityHeight = outRect1.height();
		/**
		 * 获取状态栏高度
		 */
		int statuBarHeight = getStatusBarHeight();
		/**
		 * 屏幕物理高度 减去 状态栏高度
		 */
		int remainHeight = getRealHeight() - statuBarHeight;
		/**
		 * 剩余高度跟应用区域高度相等 说明导航栏没有显示 否则相反
		 */
		if (activityHeight == remainHeight) {
			return false;
		} else {
			return true;
		}

	}


	private static final String NAVIGATION= "navigationBarBackground";

	// 该方法需要在View完全被绘制出来之后调用，否则判断不了
	//在比如 onWindowFocusChanged（）方法中可以得到正确的结果
	public static  boolean isNavigationBarExist(@NonNull Activity activity){
		ViewGroup vp = (ViewGroup) activity.getWindow().getDecorView();
		if (vp != null) {
			for (int i = 0; i < vp.getChildCount(); i++) {
				vp.getChildAt(i).getContext().getPackageName();
				if (vp.getChildAt(i).getId()!= NO_ID && NAVIGATION.equals(activity.getResources().getResourceEntryName(vp.getChildAt(i).getId()))) {
					return true;
				}
			}
		}
		return false;
	}


	//获取虚拟按键的高度
	public static int getNavigationBarHeight(Context context) {
		int result = 0;
		if (isShowNavBar(context)) {
			Resources res = context.getResources();
			int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
			if (resourceId > 0) {
				result = res.getDimensionPixelSize(resourceId);
			}
		}
		return result;
	}




	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			//int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
			//listItem.measure(desiredWidth, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);

	}


	/**
	 * 获得状态栏的高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusHeight2(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 得到状态栏高度信息
	 * @param
	 * @return > 0 success; <= 0 fail
	 */
	public static int getStatusHeight(Context context){
		int statusHeight = 0;
		Rect localRect = new Rect();
		((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight){
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
				statusHeight = context.getResources().getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	public static int getScreenWidth(Context context) {
		if (context != null) {
			DisplayMetrics dm = new DisplayMetrics();
			dm = context.getResources().getDisplayMetrics();
			return dm.widthPixels;
		}else {
			return 0;
		}

	}

	public static int getScreenHeight(Context context) {
		if (context != null) {
			DisplayMetrics dm = new DisplayMetrics();
			dm = context.getResources().getDisplayMetrics();
			return dm.heightPixels;
		}else {
			return 0;
		}
	}

	public static float getScreenDensity(Context context,int i) {
		int dp=0;
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		dp=(int) (i*dm.density);
		return dp;
	}


	/**
	 * 判断是否有闪光灯
	 * @param context
	 * @return
	 */
	public static boolean isFlashLight(Context context){
		PackageManager pm= context.getPackageManager();
		FeatureInfo[]  features=pm.getSystemAvailableFeatures();
		for(FeatureInfo f : features)
		{
			if(PackageManager.FEATURE_CAMERA_FLASH.equals(f.name))   //判断设备是否支持闪光灯
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断一个对象是否为空
	 * @param o
	 * @return
	 */
	public static boolean empty(Object o) {
		return o == null
				|| "".equals(o.toString().trim())
				|| "null".equalsIgnoreCase(o.toString().trim())
				|| "nullnull".equalsIgnoreCase(o.toString().trim()) //判断地址内容（省市+详细地址）是否为空
				||  "NULL".equalsIgnoreCase(o.toString().trim())
				|| "undefined".equalsIgnoreCase(o.toString().trim())
		        || "[]".equals(o.toString().trim());
	}

	/**
	 * 判断输入框是否有输入，并提示信息
	 * @param w
	 * @param displayStr
	 * @return
	 */
	public static boolean isEmpty(TextView w, String displayStr) {
		if (empty(w.getText().toString().trim())) {
			displayStr = displayStr +"不能为空!";
			ForegroundColorSpan fgcspan = new ForegroundColorSpan(Color.RED);
			SpannableStringBuilder ssbuilder = new SpannableStringBuilder(displayStr);
			ssbuilder.setSpan(fgcspan, 0, displayStr.length(), 0);
			w.setError(ssbuilder);
			w.setFocusable(true);
			w.requestFocus();
			return true;
		}
		return false;
	}

	/**
	 *   提供精确的加法运算。   
	 *   @param   v1   被加数   
	 *   @param   v2   加数   
	 *   @return   两个参数的和
	 */
	public   static   double   add(double   v1,double   v2){
		BigDecimal   b1   =   new   BigDecimal(Double.toString(v1));
		BigDecimal   b2   =   new   BigDecimal(Double.toString(v2));
		return   b1.add(b2).doubleValue();
	}

	/**
	 *   提供精确的减法运算。   
	 *   @param   v1   被减数   
	 *   @param   v2   减数   
	 *   @return   两个参数的差
	 */

	public   static   double   sub(double   v1,double   v2){
		BigDecimal   b1   =   new   BigDecimal(Double.toString(v1));
		BigDecimal   b2   =   new   BigDecimal(Double.toString(v2));
		return   b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static double mul(double v1,double v2){
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供精确的除法运算。
	 */
	public static double divide(double v1,double v2){
		//注意需要使用BigDecimal(String val)构造方法
		BigDecimal bigDecimal = new BigDecimal(Double.toString(v1));
		BigDecimal bigDecimal2 = new BigDecimal(Double.toString(v2));
		int scale = 2;//保留2位小数
		BigDecimal bigDecimalDivide = bigDecimal.divide(bigDecimal2, scale, BigDecimal.ROUND_HALF_UP);
		double divide = bigDecimalDivide.doubleValue();
		return  divide;
	}


	/**
	 * 把double转化为带有二位小数点的字符串
	 * @param d 要转化的double，并保留小数点两位，多的按照四舍五入
	 * @return
	 */
	public static String fromDouble(double d){
		BigDecimal a = new BigDecimal(d);
		String str = a.setScale(2, BigDecimal.ROUND_HALF_UP)+"";
		return str;
	}
	public static String fromDouble4(double d){
		BigDecimal a = new BigDecimal(d);
		String str = a.setScale(4, BigDecimal.ROUND_HALF_UP)+"";
		return str;
	}
	public static String fromDouble6(double d){
		BigDecimal a = new BigDecimal(d);
		String str = a.setScale(6, BigDecimal.ROUND_HALF_UP)+"";
		return str;
	}

	public static int getIntFromStr(String str){
		try {
			int i = 0;
			i = Integer.valueOf(str);
			return i;
		}catch (Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	public static Double getDoubleFromStr(String str){
		try {
			double i = 0;
			i = Double.valueOf(str);
			return i;
		}catch (Exception e){
			e.printStackTrace();
			return 0.0;
		}
	}


	public static Spannable getDianType(Context context,String money){
		int dian = money.length();
		int ff = 0;
		if (money.contains(MbsConstans.RMB)){
			ff = money.indexOf(MbsConstans.RMB);
			ff = ff+1;
		}
		if (money.contains(".")) {
			dian = money.indexOf(".");
		}else {
			dian = money.length();
		}
		Spannable mSpan = new SpannableString(money);
		mSpan.setSpan(new AbsoluteSizeSpan(UtilTools.sp2px(context,26)), 0, ff, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpan.setSpan(new AbsoluteSizeSpan(UtilTools.sp2px(context,36)), ff, dian, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpan.setSpan(new AbsoluteSizeSpan(UtilTools.sp2px(context,16)), dian, money.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return mSpan;
	}
	public static Spannable getDianType2(Context context,String money){
		int dian = money.length();
		int ff = 0;
		if (money.contains(MbsConstans.RMB)){
			ff = money.indexOf(MbsConstans.RMB);
			ff = ff+1;
		}
		/*if (money.contains(".")) {
			dian = money.indexOf(".");
		}else {
			dian = money.length();
		}*/
		Spannable mSpan = new SpannableString(money);
		mSpan.setSpan(new AbsoluteSizeSpan(UtilTools.sp2px(context,20)), 0, ff, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpan.setSpan(new AbsoluteSizeSpan(UtilTools.sp2px(context,36)), ff, dian, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//mSpan.setSpan(new AbsoluteSizeSpan(UtilTools.sp2px(context,16)), dian, money.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return mSpan;
	}

	public static String getRMBMoney(String s){
		try {
			if (empty(s)){
				s = "0";
			}
			Double d = Double.valueOf(s);
			//Double dd = divide(d,100);

			DecimalFormat myformat = new DecimalFormat();
			myformat.applyPattern("##,###.00");
			String ss = myformat.format(d);
			if (ss.startsWith(".")){
				ss = "0"+ss;
			}else if (ss.startsWith("-.")){
				ss = ss.replace("-.","-0.");
			}
			String result = MbsConstans.RMB +" "+ss;
			return result;
		}catch (Exception e){
			e.printStackTrace();
			return "0.00";
		}
	}
	public static String getRMBMoneyZF(String s){
		try {
			if (empty(s)){
				s = "0";
			}
			Double d = Double.valueOf(s);
			Double dd = divide(d,100);

			DecimalFormat myformat = new DecimalFormat();
			myformat.applyPattern("##,###.00");
			String ss = myformat.format(dd);
			if (ss.startsWith(".")){
				ss = "+ "+MbsConstans.RMB+"0"+ss;
			}else if (ss.startsWith("-.")){
				ss = ss.replace("-.","- "+MbsConstans.RMB+"0.");
			}else if (ss.startsWith("-")){
				ss = ss.replace("-","- "+MbsConstans.RMB+"");
			}else {
				ss = "+ "+MbsConstans.RMB+""+ss;
			}
			String result = MbsConstans.RMB +" "+ss;
			return ss;
		}catch (Exception e){
			e.printStackTrace();
			return "0.00";
		}
	}
	public static String getMoney(String s){
		try {
			if (empty(s)){
				s = "0";
			}
			Double d = Double.valueOf(s);
			//Double dd = divide(d,100);

			DecimalFormat myformat = new DecimalFormat();
			myformat.applyPattern("##,###.00");
			String ss = myformat.format(d);
			if (ss.startsWith(".")){
				ss = "0"+ss;
			}
			return ss;
		}catch (Exception e){
			e.printStackTrace();
			return "0.00";
		}
	}
	public static String getNormalMoney(String s){
		try {

			if (empty(s)){
				s = "0";
			}
			Double d = Double.valueOf(s);
			DecimalFormat myformat = new DecimalFormat();
			myformat.applyPattern("##,###.00");
			String ss = myformat.format(d);
			if (ss.startsWith(".")){
				ss = "0"+ss;
			}
			return ss;
		}catch (Exception e){
			e.printStackTrace();
			return "0.00";
		}
	}

	public static String getNormalNumber(String s){
		try {

			if (empty(s)){
				s = "0";
			}
			Double d = Double.valueOf(s);
			DecimalFormat myformat = new DecimalFormat();
			myformat.applyPattern("##,###");
			String ss = myformat.format(d);
			if (ss.startsWith(".")){
				ss = "0"+ss;
			}
			return ss;
		}catch (Exception e){
			e.printStackTrace();
			return "0";
		}
	}

	public static String getShuziMoney(String s){
		try {
			if (empty(s)){
				s = "0";
			}
			Double d = Double.valueOf(s);
			Double dd = divide(d,100);
			String ss = fromDouble(dd);
			return ss;
		}catch (Exception e){
			e.printStackTrace();
			return "0.00";
		}
	}

	//(    [     {    /    ^    -    $     ¦    }    ]    )    ?    *    +    .
	//转义方法为字符前面加上"\\"，这样在split、replaceAll时就不会报错了；
	public static String getlilv(String s){

		try {
			if (empty(s)){
				s = "0";
			}
			Double d = Double.valueOf(s);
			String str = doubleTrans1(d);

			String lastStr = "";
			if (str.contains(".")){
				String[] shuzu = str.split("\\.");
				lastStr = shuzu[1];
			}
			String result ;
			if (lastStr.length()>2){
				result = str +"%";
			}else {
				result = fromDouble(d)+"%";
			}
			return result;
		}catch (Exception e){
			e.printStackTrace();
			return "0.00";
		}
	}


	public static String getlilvT(String s){
		try {
			if (empty(s)){
				s = "0";
			}
			Double d = Double.valueOf(s);
			//Double dd = divide(d,100);

			String result = fromDouble(d)+"%";
			return result;
		}catch (Exception e){
			e.printStackTrace();
			return "0";
		}
	}
	/**
	 * 去掉小数点后面无用的0
	 *
	 * @param num
	 * @return
	 */
	public static String doubleTrans1(double num) {
		if (num % 1.0 == 0) {
			return String.valueOf((long) num);
		}
		return String.valueOf(num);
	}


	public static String getPhoneXing(String s){
		if (!empty(s)){
			String maskNumber =  s.substring(0,3)+"****"+ s.substring(7, s.length());
			return  maskNumber;
		}else {
			return  "";
		}
	}

	/**
	 * 银行卡显示
	 * @param s
	 * @return
	 */
	public static String getIDXing(String s){
		String card = getShowBankIdCard(s);
		return card;
		/*if (!empty(s) && s.length()>5){
			String maskNumber =  s.substring(0,6)+"******"+ s.substring(s.length()-4, s.length());
			return  maskNumber;
		}else {
			return  "******";
		}*/
	}

	/**
	 * s身份证显示
	 * @param s
	 * @return
	 */
	public static String getIDCardXing(String s){
		if (!empty(s) && s.length()>5){
			String maskNumber =  s.substring(0,4)+"******"+ s.substring(s.length()-4, s.length());
			return  maskNumber;
		}else {
			return  "******";
		}
	}

	public static String getShowBankIdCard(String string){
		StringBuilder str = new StringBuilder(string.replace(" ",""));

		int i = str.length() / 4;
		int j = str.length() % 4;

		for (int x = (j == 0 ? i - 1 : i); x > 0; x--) {
			str = str.insert(x * 4," ");
		}

		return str.toString();
	}

	/**
	 * 隐藏银行卡号中间的字符串（使用*号），显示前四后四
	 */
	public static String getHideCardNo(String cardNo) {
		try {
			if (empty(cardNo)){
				return "";
			}
		/*if(StringUtils.isBlank(cardNo)) {
			return cardNo;
		}*/

			int length = cardNo.length();
			int beforeLength = 4;
			int afterLength = 4;
			//替换字符串，当前使用“*”
			String replaceSymbol = "*";
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<length; i++) {
				if(i < beforeLength || i >= (length - afterLength)) {
					sb.append(cardNo.charAt(i));
				} else {
					sb.append(replaceSymbol);
				}
			}

			return sb.toString();
		}catch (Exception e){
			return "";
		}


	}
	/**
	 * 后四
	 */
	public static String getCardNoFour(String cardNo) {
		/*if(StringUtils.isBlank(cardNo)) {
			return cardNo;
		}*/
		try {
			cardNo = cardNo.substring(cardNo.length()-4,cardNo.length());

			return cardNo;
		}catch (Exception e){
			return "";
		}

	}



	/**
	 * 得到日志的详细存放路径
	 * @param mContext
	 * @return
	 */
	public static String getLogPath(Context mContext){
		StringBuffer mLogFilePath = new StringBuffer();
		// 检测到如果系统中存在SD卡，则将Log文件写入SD卡路径
		// 否则，将Log文件写入程序内部存储路径：./data/data/com.hwttnew.xx.xx/files/
		mLogFilePath.append(getBaseLogPath(mContext));
		mLogFilePath.append(File.separator);
		mLogFilePath.append("LogsDay");
		mLogFilePath.append(File.separator);
		mLogFilePath.append(getStringFromDate(new Date(), "yyyy_MM_dd"));
		mLogFilePath.append(".log");

		return mLogFilePath.toString();
	}
	/**
	 * 根据手机配置 获取错误日志存放路径
	 */
	public static String getBaseLogPath(Context mContext){
		String mLogFilePath = MbsConstans.DATA_PATH;
		if(existsSD()){
			mLogFilePath= MbsConstans.BASE_PATH;
		} else{
			if(mContext!=null)
				mLogFilePath = mContext.getFilesDir().getAbsolutePath();
			else {
				mLogFilePath = MbsConstans.DATA_PATH;
			}
		}
		return mLogFilePath;
	}

	public static String getBaseCutPicPath(Context mContext){
		String mPicPath = MbsConstans.DATA_PATH;
		if(existsSD()){
			mPicPath= MbsConstans.BASE_PATH;
		} else{
			if(mContext!=null)
				mPicPath = mContext.getFilesDir().getAbsolutePath();
			else {
				mPicPath = MbsConstans.DATA_PATH;
			}
		}
		return mPicPath;
	}

	/**
	 * 判断是否存在Sdcard
	 * @return
	 */
	public static boolean existsSD(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * @Description::将date类型转换为字符串
	 * @param date 传入的date
	 * @param style 要变化成的字符串类型  比如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getStringFromDate(Date date,String style){
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(style);
			String dateStr = simpleDateFormat.format(date);
			return dateStr;
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @Description:将字符串转换为date类型的方法
	 * @param dateStr 要转换的字符串
	 * @param style 要转换的类型
	 * @return
	 */
	public static Date getDateFromString(String dateStr,String style){

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(style);
		Date date = null;
		try {
			date = simpleDateFormat.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static String getStringFromSting(String dateStr,String style){
		try {
			if (empty(dateStr)){
				return  "";
			}
			Date date = getDateFromString(dateStr,style);
			String s = getStringFromDate(date,"yyyy-MM-dd");
			return s;
		}catch (Exception e){
			e.printStackTrace();
			return  "";
		}
	}

	//yyyyMMddHHmmss   yyyy-MM-dd HH:mm:ss
	public static String getStringFromSting2(String dateStr,String style,String returnStyle){
		try {
			if (empty(dateStr)){
				return  "";
			}
			Date date = getDateFromString(dateStr,style);
			String s = getStringFromDate(date,returnStyle);
			return s;
		}catch (Exception e){
			return  "";
		}
	}


	/**
	 *获取一个月前的日期
	 * @param date 传入的日期
	 * @return
	 */
	public static String getMonthAgo(Date date,int i) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i);//-1
		String monthAgo = simpleDateFormat.format(calendar.getTime());
		return monthAgo;
	}

	/**
	 *获取i个周前的日期
	 * @param date 传入的日期
	 * @return
	 */
	public static String getWeekAgo(Date date,int i) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, i);//-1
		String monthAgo = simpleDateFormat.format(calendar.getTime());
		return monthAgo;
	}



	/**
	 * 根据手机app下载
	 */
	public static String getAppDownPath(Context mContext){
		String appPath = MbsConstans.DATA_PATH;
		if(existsSD()){
			appPath= MbsConstans.BASE_PATH+"apk";
		} else{
			if(mContext!=null)
				appPath = mContext.getFilesDir().getAbsolutePath();
			else {
				appPath = MbsConstans.DATA_PATH;
			}
		}

		return appPath;
	}

	/**
	 * 截取全屏的方法
	 * @param mContext
	 */
	public static void saveCurrentImage(Context mContext)
	{
		WindowManager windowManager = ((Activity)mContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();
		Bitmap Bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

		View decorview = ((Activity)mContext).getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		Bmp = decorview.getDrawingCache();

		String SavePath = getBaseCutPicPath(mContext)+ "/pic/";
		try
		{
			File path = new File(SavePath);

			String filepath = SavePath+new Date().getTime()+".png";
			File file = new File(filepath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (fos != null) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
				Toast.makeText(mContext, "截屏文件已保存至SDCard目录下",Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 跳到打电话的界面，但是没有拨出
	 * @param phoneNum
	 */
	public static void startTel(Context mContext,String phoneNum){
		Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phoneNum));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

	//判断手机格式是否正确
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^[1][0-9][0-9]{9}$"); // 验证手机号
		//		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[^4,\\D])|(17[0,8]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	//判断email格式是否正确
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/* 获得一个UUID
	 * @return String UUID
	 */
	public static String getUUID(){
		String s = UUID.randomUUID().toString();
		//去掉“-”符号 
		//		return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
		return s.substring(0,8)+s.substring(9,13)+s.substring(14,18);
	}

	//判断应用是否在运行
	public static boolean isRunning(Context context){
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		boolean isAppRunning = false;
		String MY_PKG_NAME = "cn.gagakeji.gagayi.activity";
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(MY_PKG_NAME) || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
				isAppRunning = true;
				break;
			}
		}
		return isAppRunning;
	}

	/**
	 * 是否包含中文
	 *
	 * @param str
	 * @return 找到了true
	 */
	public static boolean isContainsChinese(String str) {

		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);

		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find()) {
			flg = true;
		}
		return flg;
	}

	/**
	 * 是否全是中文
	 *
	 * @param str
	 * @return 找到了true
	 */
	public static boolean isAllChinese(String str) {

		String regEx = "[\u4e00-\u9fa5]+";

		if (!TextUtils.isEmpty(str)) {
			return str.matches(regEx);
		}

		return false;
	}

	private static boolean checkCameraFacing(final int facing) {
		if (getSdkVersion() < Build.VERSION_CODES.GINGERBREAD) {
			return false;
		}
		final int cameraCount = Camera.getNumberOfCameras();
		Camera.CameraInfo info = new Camera.CameraInfo();
		for (int i = 0; i < cameraCount; i++) {
			Camera.getCameraInfo(i, info);
			if (facing == info.facing) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasBackFacingCamera() {
		final int CAMERA_FACING_BACK = 0;
		return checkCameraFacing(CAMERA_FACING_BACK);
	}

	public static boolean hasFrontFacingCamera() {
		final int CAMERA_FACING_BACK = 1;
		return checkCameraFacing(CAMERA_FACING_BACK);
	}

	public static int getSdkVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}


	public static void showSoftInput(Context context, View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		//imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	public static void hideSoftInput(Context context, View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
	}

	public static boolean isShowSoftInput(Context context){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		//获取状态信息
		return imm.isActive();//true 打开
	}


	public static void saveMedioPic(String path){

		if (path == null || path.equals("")){
			return;
		}else{
			//String path  = Environment.getExternalStorageDirectory().getPath();
			MediaMetadataRetriever media = new MediaMetadataRetriever();
			media.setDataSource(path);
			Bitmap bitmap = media.getFrameAtTime();
			try
			{
				String SavePath =  Environment.getExternalStorageDirectory().getPath()+ "/viedoPic/";
				File saveFile = new File(SavePath);

				String filepath = SavePath+new Date().getTime()+".png";
				File file = new File(filepath);
				if (!saveFile.exists()) {
					saveFile.mkdirs();
				}
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fos = null;
				fos = new FileOutputStream(file);
				if (fos != null) {
					bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
					fos.flush();
					fos.close();
					//Toast.makeText(mContext, "截屏文件已保存至SDCard目录下", 0).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}


	/**
	 * 将时间戳转为代表"距现在多久之前"的字符串
	 * @param t   时间戳
	 * @return
	 */
	public static String getStandardDate(long t) {

		StringBuffer sb = new StringBuffer();
		long time = System.currentTimeMillis() - (t*1000);
		long mill = (long) Math.ceil(time /1000);//秒前

		long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前

		long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时

		long day = (long) Math.ceil(time/24/60/60/1000.0f);// 天前
		if (day - 1 > 0) {
			if (day>=10){
				return "";
			}
			sb.append(day + "天");
		} else if (hour - 1 > 0) {
			if (hour >= 24) {
				sb.append("1天");
			} else {
				sb.append(hour + "小时");
			}
		} else if (minute - 1 > 0) {
			if (minute == 60) {
				sb.append("1小时");
			} else {
				sb.append(minute + "分钟");
			}
		} else if (mill - 1 > 0) {
			if (mill == 60) {
				sb.append("1分钟");
			} else {
				sb.append(mill + "秒");
			}
		} else {
			sb.append("刚刚");
		}
		if (!sb.toString().equals("刚刚")) {
			sb.append("前");
		}
		return sb.toString();
	}

	/**
	 * 请求参数加密方法（先使用URLEncoder进行转码，后进行Base64加密）
	 * @param params
	 * @return
	 */
	public static String gagaJiami(String params){
		try {
			params = URLEncoder.encode(params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtilDebug.i("打印log日志",params);

		char[]chars = params.toCharArray();

		int l = chars.length;

		int key = 0x12;
		String s = "";
		for (int i = 0; i < l; i++) {
			s += (char)(((int)chars[i])^(key+(i%9)));
		}
		String strBase64 = new String(Base64.encode(s.getBytes(), Base64.DEFAULT));
		return strBase64;
	}

	/**
	 * 参数也可以解密
	 * @param params
	 * @return
	 */
	public static String gagaJiemi(String params){
		String strBase64 = new String(Base64.decode(params.getBytes(), Base64.DEFAULT));

		char[]chars = strBase64.toCharArray();

		int l = chars.length;

		int key = 0x12;
		String s = "";
		for (int i = 0; i < l; i++) {
			s += (char)(((int)chars[i])^(key+(i%9)));
		}
		try {
			s = URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}


	public static void setMoneyEdit(final EditText mMoneyEdit,final double maxNum){
		mMoneyEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//输入的内容包含小数点
				if (s.toString().contains(".")) {
					//小数点后面数字超过两位
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						mMoneyEdit.setText(s);
						mMoneyEdit.setSelection(s.length());
					}
				}
				//输入内容以0开头
				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						mMoneyEdit.setText(s.subSequence(0, 1));
						mMoneyEdit.setSelection(1);
						return;
					}
				}
				//输入内容以小数点开头
				if (s.toString().startsWith(".")){
					mMoneyEdit.setText("");
				}

				/*if (!empty(s) && maxNum != 0){
					double d = Double.valueOf(s.toString());
					if (d > maxNum){
						s = s.toString().subSequence(0, s.length()-1);
						mMoneyEdit.setText(s);
						mMoneyEdit.setSelection(s.length());
					}
				}*/

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}



	/**
	 * 获得该月第一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getFirstDayOfMonth(int year,int month){
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR,year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最小天数
		int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String firstDayOfMonth = sdf.format(cal.getTime());
		return firstDayOfMonth;
	}
	/**
	 * 获得该月第一天
	 * @return
	 */
	public static String getFirstDayOfMonthByStr(String str,String type){
		Date date = getDateFromString(str,type);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//获取某月最小天数
		int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String firstDayOfMonth = sdf.format(cal.getTime());
		return firstDayOfMonth;
	}
	/**
	 * 获得该月第一天
	 * @return
	 */
	public static String getFirstDayOfMonthByDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//获取某月最小天数
		int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String firstDayOfMonth = sdf.format(cal.getTime());
		return firstDayOfMonth;
	}

	/**
	 * 获得该月最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDayOfMonth(int year,int month){
		Calendar cal = Calendar.getInstance();
		//设置年份
		cal.set(Calendar.YEAR,year);
		//设置月份
		cal.set(Calendar.MONTH, month-1);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return lastDayOfMonth;
	}
	/**
	 * 获得该月最后一天
	 * @return
	 */
	public static String getLastDayOfMonthByStr(String str,String type){
		Date date = getDateFromString(str,type);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return lastDayOfMonth;
	}
	/**
	 * 获得该月最后一天
	 * @return
	 */
	public static String getLastDayOfMonthByDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		//设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		//格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String lastDayOfMonth = sdf.format(cal.getTime());
		return lastDayOfMonth;
	}

	/**
	 * 比较两个日期的大小，日期格式为yyyy-MM-dd
	 */
	public static boolean isDateOneBigger(String str1, String str2,String type) {
		boolean isBigger = false;
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		Date dt1 = null;
		Date dt2 = null;
		try {
			dt1 = sdf.parse(str1);
			dt2 = sdf.parse(str2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (dt1.getTime() > dt2.getTime()) {
			isBigger = true;
		} else if (dt1.getTime() < dt2.getTime()) {
			isBigger = false;
		}
		return isBigger;
	}




	public boolean isLongImage(int width,int height) {
		int realWidth = width;
		int realHeight = height;
		if (realWidth == 0 || realHeight == 0 || realWidth >= realHeight) {
			return false;
		}
		return (realHeight / realWidth) >= 4;
	}

	public static boolean isGif(String url) {
		return "gif".equals(getPathFormat(url));
	}
	public static String getPathFormat(String path) {
		if (!TextUtils.isEmpty(path)) {
			int lastPeriodIndex = path.lastIndexOf('.');
			if (lastPeriodIndex > 0 && lastPeriodIndex + 1 < path.length()) {
				String format = path.substring(lastPeriodIndex + 1);
				if (!TextUtils.isEmpty(format)) {
					return format.toLowerCase();
				}
			}
		}
		return "";
	}



	/**
	 * 1.现在目前市面的手机屏幕尺寸
	 *
	 * 			480*800       dp表示：   hdpi    密实系数：1.5
	 *
	 * 			720*1280     dp表示：   xhdpi    密实系数：2
	 *
	 * 			1080*1920   dp表示：   xxhdpi   密实系数：3  
	 *
	 * 			1440*2560   dp表示：   xxxhdpi   密实系数：3.5  
	 *
	 * 	但是有的UI真的是懒惰，为适配Android 和 ios 而做一套ios的规格尺寸（1242 x 2208）图片去适配两种系统，这种是最烦人的。怎么办？
	 *
	 * 	可以这样认为：1242 x 2208        密实系数：3.5
	 * 			---------------------
	 */

	/**
	 * * dp转px
	 * @param dp
	 * @return
	 */
	public static int dip2px(Context context,int dp)
	{
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (dp*density+0.5);
	}

	/** px转换dip */
	public static int px2dip(Context context,int px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}
	/** px转换sp */
	public static int px2sp(Context context,int pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}
	/** sp转换px */
	public static int sp2px(Context context,int spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}




	/*
	 * 将当前时间换为时间戳
	 */
	public static String dateToStamp() throws ParseException{
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s = simpleDateFormat.format(new Date());
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}


	/*
	 * 将时间换为时间戳
	 */
	public static String dateToStamp(String s) throws ParseException{
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}




	/*
	 * 将时间戳转换为时间
	 */
	public static String stampToDate(String s){
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = Long.valueOf(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}


	/**
	 * 检测输入金额是否有效的（至少包含1-9的任意数字）
	 * @param money
	 * @return
	 */
	public static boolean CheckMoneyValid(String money){

		for (int i=1;i<=9;i++){
			if(money.contains(i+"")){
				return true;
			}
		}
		return false;
	}

	/**
	 *  分转万元并保留两位小数
	 * @param money
	 * @return
	 */
	public static float fenToWanYuan(String money){
        if (empty(money)){
            money = "0";
        }
		BigDecimal bigDecimal = new BigDecimal(money);
		//转换为万元
		BigDecimal decimal = bigDecimal.divide(new BigDecimal(10000*100));
		//保留两位小数
		DecimalFormat format = new DecimalFormat("0.00");
		//四舍五入
		format.setRoundingMode(RoundingMode.HALF_UP);
		String formatNum = format.format(decimal);
		try {
			float i = Float.valueOf(formatNum);
			return i;
		}catch (Exception e){
			e.printStackTrace();
			return 0;
		}

	}


	/**
	 * 将20190101  格式日期转换成 “01月01日”
	 * @return
	 */
	public static String dateTypeTo(String strDate){
		try {
			Date date= getDateFromString(strDate,"yyyyMMdd");
			return getStringFromDate(date,"yyyy年MM月dd日").substring(5);
		}catch (Exception e){
			return "01月01日";
		}

	}


	/**
	 *  计算两个日期相差多少天
	 * @param startTime
	 * @param endTime
	 * @param format
	 * @return
	 */
	public static long dateDiff(String startTime, String endTime, String format) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long diff;
		long day = 0;
		try {
			// 获得两个时间的毫秒时间差异
			diff = sd.parse(endTime).getTime()
					- sd.parse(startTime).getTime();
			day = diff / nd;// 计算差多少天
			long hour = diff % nd / nh;// 计算差多少小时
			long min = diff % nd % nh / nm;// 计算差多少分钟
			long sec = diff % nd % nh % nm / ns;// 计算差多少秒
			// 输出结果
			System.out.println("时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");
			if (day>=1) {
				return day;
			}else {
				if (day==0) {
					return 1;
				}else {
					return 0;
				}

			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;

	}

	public static View parseLayout(Context context,int resLayout) {
		return View.inflate(context, resLayout, null);
	}

	@SuppressLint("StaticFieldLeak")
	private static DecimalFormat decimalFormat = new DecimalFormat();
	public static String formatNumber(Object number, String pattern,RoundingMode roundingMode) {
		try {
			decimalFormat.applyPattern(pattern);
			decimalFormat.setRoundingMode(roundingMode);
			if (number instanceof Double) {
				return decimalFormat.format((double) number);
			} else if (number instanceof Long) {
				return decimalFormat.format((long) number);
			} else if (number instanceof String) {
				if (!empty((String) number)) {
					if (((String) number).contains(".")) {
						Double aDouble = Double.valueOf((String) number);
						return formatNumber(aDouble, pattern);
					} else {
						Long aLong = Long.valueOf((String) number);
						return formatNumber(aLong, pattern);
					}
				}
				return "0";
			} else {
				return decimalFormat.format(number);
			}
		} catch (Exception e) {
			return "0";
		}
	}
	/**
	 * 格式化数字为指定格式
	 *
	 * @param number  需要格式化的数字
	 * @param pattern 指定格式
	 * @return 格式化完成后的字符串
	 */
	public static String formatNumber(Object number, String pattern) {
		try {
			decimalFormat.applyPattern(pattern);
			if (number instanceof Double) {
				return decimalFormat.format((double) number);
			} else if (number instanceof Long) {
				return decimalFormat.format((long) number);
			} else if (number instanceof String) {
				if (!empty((String) number)) {
					if (((String) number).contains(".")) {
						Double aDouble = Double.valueOf((String) number);
						return formatNumber(aDouble, pattern);
					} else {
						Long aLong = Long.valueOf((String) number);
						return formatNumber(aLong, pattern);
					}
				}
				return "0";
			} else {
				return decimalFormat.format(number);
			}
		} catch (Exception e) {
			return "0";
		}
	}

	private static StringBuffer stringBuffer = new StringBuffer();
	public static String formatDecimal(Object number, int pattern) {
		try {
			decimalFormat.setGroupingUsed(false);
			decimalFormat.setMaximumFractionDigits(pattern);
			stringBuffer.setLength(0);
			if (pattern > 0) {
				stringBuffer.append("0.");
				for (int i = 0; i < pattern; i++) {
					stringBuffer.append("0");
				}
			} else {
				stringBuffer.append("0");
			}
			decimalFormat.applyPattern(stringBuffer.toString());
			if (number instanceof Double) {
				return decimalFormat.format((double) number);
			} else if (number instanceof Long) {
				return decimalFormat.format((long) number);
			} else if (number instanceof String) {
				if (!empty((String) number)) {
					if (((String) number).contains(".")) {
						Double aDouble = Double.valueOf((String) number);
						return formatDecimal(aDouble, pattern);
					} else {
						Long aLong = Long.valueOf((String) number);
						return formatDecimal(aLong, pattern);
					}
				}
				return "0";
			} else {
				return decimalFormat.format(number);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

}
