package com.lr.biyou.utils.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  格式验证
 */
public class RegexUtil {

    private RegexUtil() {
    }

    /**
     *  判断是否是字母+数字 混合
     * @param str
     * @return
     */
    public static int isLetterDigit(String str){
        //是否含有数字
        boolean isDigit=false;
        //是否含有字母
        boolean isLetter = false;
        for (int i=0;i<str.length();i++){
            if(Character.isDigit(str.charAt(i))){
                isDigit=true;
            }
            if(Character.isLetter(str.charAt(i))){
                isLetter=true;
            }
        }
        //正则判断
        String regex="^[a-zA-Z0-9]{6,16}$";

        boolean b = str.matches(regex);
        if (!isDigit){
            return 1;
        }

        if (!isLetter){
            return 2;
        }

        if (!b){
            return 3;
        }

       return 0;
    }

    public static boolean isSiCard(String tel){//^[0-9]+(.[0-9]{1,3})?$  验证有1-3位小数的正实数   String regEx2 = "^[0-9_]+$";//纯数字
        Pattern p = Pattern.compile("^[0-9]{14,19}$"); //
        Matcher m = p.matcher(tel);
        LogUtilDebug.i("###############111   ",tel+" "+m.matches());

        return m.matches();
    }
    public static boolean isGongCard(String tel){//^[0-9]+(.[0-9]{1,3})?$  验证有1-3位小数的正实数   String regEx2 = "^[0-9_]+$";//纯数字
        LogUtilDebug.i("###############",tel);
        Pattern p = Pattern.compile("^[0-9]{4,32}$"); //
        Matcher m = p.matcher(tel);
        LogUtilDebug.i("###############222   ",tel+" "+m.matches());

        return m.matches();
    }
    public static boolean isPhone(String tel){
        Pattern p = Pattern.compile("^[1][0-9][0-9]{9}$"); // 验证手机号
        //		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[^4,\\D])|(17[0,8]))\\d{8}$");
        Matcher m = p.matcher(tel);
        return m.matches();
    }

    public static boolean validatePhonePass(String pass) {
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        return !UtilTools.empty(pass) && pass.matches(passRegex);
    }

    public static boolean isAmount(String tel){
        Pattern p = Pattern.compile("^(0(?:[.](?:[1-9]\\d?|0[1-9]))|[1-9]\\d*(?:[.]\\d{1,2}|$))$"); // 验证金额
        Matcher m = p.matcher(tel);
        return m.matches();
    }
    public static boolean isBankCard(String tel){
        Pattern p = Pattern.compile("^\\d{15,21}$"); // 验证银行卡信息
        Matcher m = p.matcher(tel);
        return m.matches();
    }

    public static boolean isCode(String tel){
        Pattern p = Pattern.compile("^\\d{6}$"); // 验证验证码
        Matcher m = p.matcher(tel);
        return m.matches();
    }


    //判断是否符合身份证号码的规范
    public static boolean isIDCard(String IDCard) {
        if (IDCard != null) {
            String IDCardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)";
            return IDCard.matches(IDCardRegex);
        }
        return false;
    }


    /**
     * 比较真实完整的判断身份证号码的工具
     *
     * @param idCard 用户输入的身份证号码
     * @return [true符合规范, false不符合规范]
     */
    public static boolean isRealIDCard(String idCard) {
        if (idCard != null) {
            IdCardUtil idCardUtil = new IdCardUtil(idCard);
            int correct = idCardUtil.isCorrect();
            String msg = idCardUtil.getErrMsg();
            if (0 == correct) {// 符合规范
                return true;
            }
        }
        return false;
    }


    public static  boolean isEmail(String email) {
        //判断是否为空邮箱
        int k = 0;
        if(email == null) {
            return false;
        }
         /*
          * 单引号引的数据 是char类型的
                                    双引号引的数据 是String类型的
                                    单引号只能引一个字符
                                    而双引号可以引0个及其以上*
          */

        //判断是否有仅有一个@且不能在开头或结尾
        if(email.indexOf("@") > 0 && email.indexOf('@') == email.lastIndexOf('@') && email.indexOf('@') < email.length()-1) {
            k++;
        }

        //判断"@"之后必须有"."且不能紧跟
        if(email.indexOf('.',email.indexOf('@')) > email.indexOf('@')+1 ) {
            k++;
        }
        //判断"@"之前或之后不能紧跟"."
        if(email.indexOf('.') < email.indexOf('@')-1 || email.indexOf('.') > email.indexOf('@')+1 ) {
            k++;
        }
        //@之前要有6个字符
        if(email.indexOf('@') > 5 ) {
            k++;
        }

        if(email.endsWith("com") || email.endsWith("org") || email.endsWith("cn") ||email.endsWith("net")) {
            k++;
        }
        if(k == 5) {
            return true;
        }
        return false;

    }
}
