package com.lr.biyou.utils.tool;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;


public class ParseTextUtil{
	
	private final Context context;
	private final SimpleDateFormat mParseSimpleDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final SimpleDateFormat mParseSimpleDate = new SimpleDateFormat("yyyy-MM-dd");
	private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	private final DateFormat mDateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.CHINA);
	
	public ParseTextUtil(Context context) {
		this.context = context;
	}
	
	/**
	 * 解析并设置其字符串为指定的样式
	 * 
	 * 			要被设置的字符串数据
	 * @return 已经被设置完成的<CODE>{@link SpannableString}</CODE>对象
	 */
	public SpannableString parseValueColor(String content)
	{
		SpannableString mSpannableString = new SpannableString(content);
		List<Map<String, Object>> values = this.getBeginAndEnd(Pattern.compile("\\d+"), content);
		for(Map<String, Object> value : values)
		{
			int begin = Integer.parseInt(value.get("BEGIN").toString());
			int end = Integer.parseInt(value.get("END").toString());
			mSpannableString.setSpan(new AbsoluteSizeSpan(25, true), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			//mSpannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
			mSpannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.font_c)), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
		}
		return mSpannableString;
	}

	/**
	 * 解析并设置其字符串为指定的样式
	 *
	 * 			要被设置的字符串数据
	 * @return 已经被设置完成的<CODE>{@link SpannableString}</CODE>对象
	 */
	public Spannable parseValueColorNum(String content)
	{
		Spannable mSpan = new SpannableString(content);
		List<Map<String, Object>> values = this.getBeginAndEnd(Pattern.compile("\\d+"), content);
		for(Map<String, Object> value : values)
		{
			int begin = Integer.parseInt(value.get("BEGIN").toString());
			int end = Integer.parseInt(value.get("END").toString());


			//mSpan.setSpan(new AbsoluteSizeSpan(UtilTools.sp2px(context,16)), 0, begin, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			// mSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.gray_normal)), 0, begin, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
			mSpan.setSpan(new AbsoluteSizeSpan(UtilTools.sp2px(context,16)), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			mSpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
			mSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.black)), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
			//mSpan.setSpan(new AbsoluteSizeSpan(UtilTools.sp2px(context,16)), end, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			// mSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.gray_normal)), end, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
		}
		return mSpan;
	}
	/**
	 * 解析并设置其字符串为指定的样式
	 *
	 * 			要被设置的字符串数据
	 * @return 已经被设置完成的<CODE>{@link SpannableString}</CODE>对象
	 */
	public SpannableString parseValueColor(String content,int color)
	{
		SpannableString mSpannableString = new SpannableString(content);
		List<Map<String, Object>> values = this.getBeginAndEnd(Pattern.compile("\\d+"), content);
		for(Map<String, Object> value : values)
		{
			int begin = Integer.parseInt(value.get("BEGIN").toString());
			int end = Integer.parseInt(value.get("END").toString());
			//mSpannableString.setSpan(new AbsoluteSizeSpan(25, true), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			mSpannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
			mSpannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, color)), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
		}
		return mSpannableString;
	}

	/**
	 * 解析并设置其该字符串的指定位置为特定的样式
	 * 
	 *			要被设置的字符串数据
	 * @param begin
	 * 			要从该字符串的开始位置
	 * @parsm end
	 * 			要到该字符串的结束位置
	 * @return 已经被设置完成的<CODE>{@link SpannableString}</CODE>对象
	 */
	public SpannableString parseValueColor(String content, int begin, int end)
	{
		SpannableString mSpannableString = new SpannableString(content);
		mSpannableString.setSpan(new AbsoluteSizeSpan(15, true), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
		mSpannableString.setSpan(new ForegroundColorSpan(Color.rgb(141, 182, 205)), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return mSpannableString;
	}
	
	/**
	 * 解析并设置其该字符串的指定位置为特定的样式
	 * @param content
	 * 			要被设置的字符串数据
	 * @param begin
	 * 			要从该字符串的开始位置
	 * @param end
	 * 			要到该字符串的结束位置
	 * @param color
	 * 			指定要设置的最终颜色值
	 * @return 已经被设置完成的<CODE>{@link SpannableString}</CODE>对象
	 */
	public SpannableString parseValueColor(String content, int begin, int end, int color)
	{
		SpannableString mSpannableString = new SpannableString(content);
		mSpannableString.setSpan(new AbsoluteSizeSpan(15, true), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
		mSpannableString.setSpan(new ForegroundColorSpan(color), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return mSpannableString;
	}
	
	/**
	 * 解析并设置其该字符串的指定位置为特定的样式
	 * 
	 * @param content 
	 *			要被设置的字符串数据
	 * @param size 要设置的文字大小
	 * @param style 以何种字体风格显示，比如：{@code android.graphics.Typeface.BOLD}为粗体
	 * @param begin
	 * 			要从该字符串的开始位置
	 * @parsm end
	 * 			要到该字符串的结束位置
	 * @param color
	 * 			指定要设置的最终颜色值
	 * @return 已经被设置完成的<CODE>{@link SpannableString}</CODE>对象
	 */
	public SpannableString parseValueColor(String content, int size, int style, int begin, int end, int color)
	{
		SpannableString mSpannableString = new SpannableString(content);
		mSpannableString.setSpan(new AbsoluteSizeSpan(size, true), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpannableString.setSpan(new StyleSpan(style), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpannableString.setSpan(new ForegroundColorSpan(color), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return mSpannableString;
	}
	
	/**
	 * 解析一个有规定格式的日期和时间的字符串，被解析的结果将按照不同的风格返回：
	 * <UL>
	 * <LI>如果参数时间距离当前系统时间不超过1分钟，则返回：xx秒前</LI>
	 * <LI>如果参数时间距离当前系统时间不超过1小时，则返回：xx分钟前</LI>
	 * <LI>如果参数时间距离当前系统时间不超过1天，则返回：xx小时前</LI>
	 * <LI>如果参数时间距离当前该系统时间超过1天，则返回被格式化之后的日期简短形式：MM-dd HH:mm</LI>
	 * </UL>
	 * @param timeStr 具有规范化的日期字符串，该规范必须为：{@code yyyy-MM-dd HH:mm:ss}
	 * @return 返回被解析完成后的日期简短形式字符串
	 */
	public String parseDateTimeStr(String timeStr)
	{
		try
		{
			Calendar mCalendar = Calendar.getInstance();
			mCalendar.setTime(new Date()); // 系统当前时间
			int nowHourOfDay = mCalendar.get(Calendar.HOUR_OF_DAY); // 当前的小时
			int nowMinute = mCalendar.get(Calendar.MINUTE); // 当前的分
			int nowSecond = mCalendar.get(Calendar.SECOND); // 当前的秒
			Date createDateTime = mParseSimpleDateTime.parse(timeStr); // 新闻创建时间
			mCalendar.setTime(createDateTime); // 重新设置为新闻创建的时间
			Date createDate = mParseSimpleDate.parse(mDateFormat.format(createDateTime));
			Date nowDate = mParseSimpleDate.parse(mDateFormat.format(new Date()));
			int timeDifferenceHour = nowHourOfDay - mCalendar.get(Calendar.HOUR_OF_DAY);
			int timeDifferenceMinute = nowMinute - mCalendar.get(Calendar.MINUTE);
			int timeDifferenceSecond = nowSecond - mCalendar.get(Calendar.SECOND);
			if(createDate.before(nowDate))
				return mSimpleDateFormat.format(createDateTime);
			else if (timeDifferenceHour > 0)
				return timeDifferenceHour + "小时前";
			else if (timeDifferenceMinute > 0)
				return timeDifferenceMinute + "分钟前";
			else if (timeDifferenceSecond > 0)
				return timeDifferenceSecond + "秒前";
		} catch (ParseException e)
		{
			e.printStackTrace();
			return mSimpleDateFormat.format(new Date());
		}
		return timeStr;
	}

	public List<Map<String, Object>> getBeginAndEnd(Pattern pattern,
			String content)
	{
		List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
		Matcher matcher = pattern.matcher(content);
		while (matcher.find())
		{
			Map<String, Object> value = new HashMap<String, Object>();
			value.put("VALUE", matcher.group());
			value.put("BEGIN", Integer.valueOf(matcher.start()));
			value.put("END", Integer.valueOf(matcher.end()));
			values.add(value);
		}
		return values;
	}


	/**
	 * 解析并设置其字符串为指定的样式
	 *
	 *            要被设置的字符串数据
	 * @return 已经被设置完成的<CODE>{@link SpannableString}</CODE>对象
	 */
	public SpannableString parseValueColor2(String content) {
		SpannableString mSpannableString = new SpannableString(content);
		List<Map<String, Object>> values = this.getBeginAndEnd(
				Pattern.compile("\\d+\\.\\d%"), content);

		if (values == null || values.size() <= 0) {
			values = this.getBeginAndEnd(Pattern.compile("\\d%"), content);
		}

		for (Map<String, Object> value : values) {
			int begin = Integer.parseInt(value.get("BEGIN").toString());
			int end = Integer.parseInt(value.get("END").toString());
			// mSpannableString.setSpan(new AbsoluteSizeSpan(16, true), begin,
			// end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// mSpannableString.setSpan(new
			// StyleSpan(android.graphics.Typeface.BOLD), begin, end,
			// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //粗体
//			mSpannableString.setSpan(new ForegroundColorSpan(getResources()
//							.getColor(R.color.chongzhi_color)), begin, end,
//					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
		}
		return mSpannableString;
	}
	/**
	 * 解析并设置其字符串为指定的样式
	 *
	 *            要被设置的字符串数据
	 * @return 已经被设置完成的<CODE>{@link SpannableString}</CODE>对象
	 */
	public SpannableString parseValueColor3(String content,Context mContext) {
		SpannableString mSpannableString = new SpannableString(content);
		List<Map<String, Object>> values = this.getBeginAndEnd(
				Pattern.compile("《.*》"), content);

		if (values == null || values.size() <= 0) {
			values = this.getBeginAndEnd(Pattern.compile("《.*》"), content);
		}

		for (Map<String, Object> value : values) {
			int begin = Integer.parseInt(value.get("BEGIN").toString());
			int end = Integer.parseInt(value.get("END").toString());
			// mSpannableString.setSpan(new AbsoluteSizeSpan(16, true), begin,
			// end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// mSpannableString.setSpan(new
			// StyleSpan(android.graphics.Typeface.BOLD), begin, end,
			// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //粗体
			mSpannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.font_c)), begin, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
		}
		return mSpannableString;
	}


	public Spannable getDianType(String money){
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


	public SpannableString getTextSpan(String contentStr,String splitChar){
		int dian = contentStr.length();
		if (contentStr.contains(splitChar)) {
			dian = contentStr.indexOf(splitChar);
			dian = dian + 1;
		}else {
			dian = contentStr.length();
		}
		SpannableString mSpan = new SpannableString(contentStr);
		mSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.red)), dian, contentStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色

		return mSpan;
	}


}
