package com.lr.biyou.utils.tool;

import android.content.Context;
import android.content.res.AssetManager;

import com.lr.biyou.basic.BasicApplication;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mywidget.view.TipsToast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectDataUtil {

    private SelectDataUtil() {
    }

    private static Map<String,Object> mNameCode = new HashMap<>();

    //读取方法
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static List<Map<String,Object>> getPayWayValues(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("code","0");
        map.put("name","银行卡");
        list.add(map);
        map = new HashMap<>();
        map.put("code","1");
        map.put("name","支付宝");
        list.add(map);
        map = new HashMap<>();
        map.put("code","2");
        map.put("name","微信支付");
        list.add(map);

        return list;
    }


    public static List<Map<String,Object>> getTabValues(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("code","0");
        map.put("name","自选");
        list.add(map);
        map = new HashMap<>();
        map.put("code","1");
        map.put("name","USDT");
        list.add(map);
        map = new HashMap<>();
        map.put("code","2");
        map.put("name","BTC");
        list.add(map);
        map = new HashMap<>();
        map.put("code","3");
        map.put("name","ETH");
        list.add(map);
        map = new HashMap<>();
        map.put("code","4");
        map.put("name","涨幅榜");
        list.add(map);

        return list;
    }


    public static List<Map<String,Object>> getTabValues2(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("code","0");
        map.put("name","持仓");
        list.add(map);
        map = new HashMap<>();
        map.put("code","1");
        map.put("name","委托");
        list.add(map);
        map = new HashMap<>();
        map.put("code","2");
        map.put("name","成交");
        list.add(map);
        return list;
    }

    public static List<Map<String,Object>> getTabValues3(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("code","0");
        map.put("name","未完成委托");
        list.add(map);
        map = new HashMap<>();
        map.put("code","1");
        map.put("name","已完成委托");
        list.add(map);
        return list;
    }

    public static List<Map<String,Object>> getCondition(){
        List<Map<String,Object>> list = new ArrayList<>();
        //（top_up：充值，withdraw：提现，borrow：借款，repayment：还款，other：其他）
        Map<String,Object> map = new HashMap<>();
        map.put("code","borrow");
        map.put("name","借款");
        list.add(map);
        map = new HashMap<>();
        map.put("code","repayment");
        map.put("name","还款");
        list.add(map);
        map = new HashMap<>();
        map.put("code","top_up");
        map.put("name","充值");
        list.add(map);
        map = new HashMap<>();
        map.put("code","withdraw");
        map.put("name","提现");
        list.add(map);
        map = new HashMap<>();
        map.put("code","other");
        map.put("name","其他");
        list.add(map);

        return list;
    }




    public static List<Map<String,Object>> getCountry(){
        //国籍(CN:中国,HK:香港,MO:澳门,TW:台湾省,OTHER:其他)
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","中国");
        map.put("code","CN");
        list.add(map);

        map = new HashMap<>();
        map.put("name","香港");
        map.put("code","HK");
        list.add(map);

        map = new HashMap<>();
        map.put("name","澳门");
        map.put("code","MO");
        list.add(map);

        map = new HashMap<>();
        map.put("name","台湾省");
        map.put("code","TW");
        list.add(map);

        map = new HashMap<>();
        map.put("name","其他");
        map.put("code","OTHER");
        list.add(map);
        return list;
    }
    public static List<Map<String,Object>> getMarry(){
        //婚姻状况(1:单身,2:已婚,3:离婚,4:丧偶)
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","未婚");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","已婚");
        map.put("code","2");
        list.add(map);

        map = new HashMap<>();
        map.put("name","离婚");
        map.put("code","3");
        list.add(map);

        map = new HashMap<>();
        map.put("name","丧偶");
        map.put("code","4");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> getEducation(){
        //学历(1:小学程度或以下,2:中学程度,3:预科/大专程度,4:学士,5:硕士或以上)
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","小学程度或以下");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","中学程度");
        map.put("code","2");
        list.add(map);

        map = new HashMap<>();
        map.put("name","预科/大专程度");
        map.put("code","3");
        list.add(map);

        map = new HashMap<>();
        map.put("name","学士");
        map.put("code","4");
        list.add(map);

        map = new HashMap<>();
        map.put("name","硕士或以上");
        map.put("code","5");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> getHouse(){
        //现居所有权(1:自置（无抵押）,2:已按揭,3:亲属拥有,4:由雇主提供,5:租用,6:其它)
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","自置（无抵押）");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","已按揭");
        map.put("code","2");
        list.add(map);

        map = new HashMap<>();
        map.put("name","亲属拥有");
        map.put("code","3");
        list.add(map);

        map = new HashMap<>();
        map.put("name","由雇主提供");
        map.put("code","4");
        list.add(map);

        map = new HashMap<>();
        map.put("name","租用");
        map.put("code","5");
        list.add(map);

        map = new HashMap<>();
        map.put("name","其它");
        map.put("code","6");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> getJobType(){
        //工作性质(1:长期雇员,2:合约员工,3:非在职人士/临时工)
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","长期雇员");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","合约员工");
        map.put("code","2");
        list.add(map);

        map = new HashMap<>();
        map.put("name","非在职人士/临时");
        map.put("code","3");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> getHkZhouqi(){
        //还款周期 1：一次性 2：单周 3：双周 4：月 5：季 6：半年 7：年 8：其他
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","一次性");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","单周");
        map.put("code","2");
        list.add(map);

        map = new HashMap<>();
        map.put("name","双周");
        map.put("code","3");
        list.add(map);

        map = new HashMap<>();
        map.put("name","月");
        map.put("code","4");
        list.add(map);

        map = new HashMap<>();
        map.put("name","季");
        map.put("code","5");
        list.add(map);

        map = new HashMap<>();
        map.put("name","半年");
        map.put("code","6");
        list.add(map);

        map = new HashMap<>();
        map.put("name","年");
        map.put("code","7");
        list.add(map);

        map = new HashMap<>();
        map.put("name","其他");
        map.put("code","8");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> getLilvType(){
        //利率方式 0：浮动 1：固定
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","浮动");
        map.put("code","0");
        list.add(map);

        map = new HashMap<>();
        map.put("name","固定");
        map.put("code","1");
        list.add(map);


        return list;
    }
    public static List<Map<String,Object>> getDaikuanType(){
        //贷款种类 202010：个人-经营性贷款；101010：企业-流动资金贷款
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","个人-经营性贷款");
        map.put("code","202010");
        list.add(map);

        map = new HashMap<>();
        map.put("name","企业-流动资金贷款");
        map.put("code","101010");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> getDaikuanUse(){
        //贷款用途 0：个人经营 1：个人授信额度服务 2：个人综合消费 3：商品交易 4：资金周转
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","个人经营");
        map.put("code","0");
        list.add(map);

        map = new HashMap<>();
        map.put("name","个人授信额度服务");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","个人综合消费");
        map.put("code","2");
        list.add(map);

        map = new HashMap<>();
        map.put("name","商品交易");
        map.put("code","3");
        list.add(map);

        map = new HashMap<>();
        map.put("name","资金周转");
        map.put("code","4");
        list.add(map);
        return list;
    }
    public static List<Map<String,Object>> guanxiPeople(){
        //共同借款人与我的关系 0：配偶 1：父母 2：子女 3：其他
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","配偶");
        map.put("code","0");
        list.add(map);

        map = new HashMap<>();
        map.put("name","父母");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","子女");
        map.put("code","2");
        list.add(map);

        map = new HashMap<>();
        map.put("name","其他");
        map.put("code","3");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> jieKuanStatus(){
        //借款状态（1：放款中 2：还款中 3：已结清 4：已驳回）
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","放款中");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","已放款");
        map.put("code","2");
        list.add(map);

        map = new HashMap<>();
        map.put("name","已结清");
        map.put("code","3");
        list.add(map);
        map = new HashMap<>();
        map.put("name","已驳回");
        map.put("code","4");
        list.add(map);

        return list;
    }


    //应收凭证状态
    public static List<Map<String,Object>> pingZhengStatus(){
        //借款状态（1：放款中 2：还款中 3：已结清 4：已驳回）
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","正常");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","已融资");
        map.put("code","2");
        list.add(map);

        map = new HashMap<>();
        map.put("name","已核销");
        map.put("code","3");
        list.add(map);
        map = new HashMap<>();
        map.put("name","已到期");
        map.put("code","4");
        list.add(map);

        return list;
    }

    public static List<Map<String,Object>> jieKuanLimit(){
        //借款期限（1个月、3个月、6个月、12个月、24个月、36个月、50个月）
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","1个月");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","3个月");
        map.put("code","3");
        list.add(map);

        map = new HashMap<>();
        map.put("name","6个月");
        map.put("code","6");
        list.add(map);

        map = new HashMap<>();
        map.put("name","9个月");
        map.put("code","9");
        list.add(map);

        map = new HashMap<>();
        map.put("name","12个月");
        map.put("code","12");
        list.add(map);
        map = new HashMap<>();
        map.put("name","24个月");
        map.put("code","24");
        list.add(map);
        map = new HashMap<>();
        map.put("name","36个月");
        map.put("code","36");
        list.add(map);
        map = new HashMap<>();
        map.put("name","50个月");
        map.put("code","50");
        list.add(map);

        return list;
    }

    /**
     * 单笔借款期限单位(1:年,2:月,3:日)
     * @return
     */
    public static List<Map<String,Object>> getQixianDw(){
        //单笔借款期限单位(1:年,2:月,3:日)
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","年");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","月");
        map.put("code","2");
        list.add(map);

        map = new HashMap<>();
        map.put("name","日");
        map.put("code","3");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> gongsi(){
        //账户类型(1: 对公; 2: 对私
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","对公");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","对私");
        map.put("code","2");
        list.add(map);

        return list;
    }

    public static List<Map<String,Object>> getMaxQixian(int max,String type){//
        List<Map<String,Object>> maxList = new ArrayList<>();
        List<Map<String,Object>> list = jieKuanLimit();
        switch (type){
            case "1"://借款期限单位（1：年 2：月 3：日）
                list = qixianNian();
                break;
            case "2":
                list = SelectDataUtil.getNameCodeByType("loanLimit");
                break;
            case "3":
                list = new ArrayList<>();
                break;
        }
        for (Map<String,Object> map : list){
            String code = map.get("code")+"";
            int i = Integer.valueOf(code);
            if (i <= max){
                maxList.add(map);
            }
        }
        return maxList;
    }
    public static List<Map<String,Object>> qixianNian(){//
        //1年  2年  3年
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","1年");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","2年");
        map.put("code","2");
        list.add(map);

        map = new HashMap<>();
        map.put("name","3年");
        map.put("code","3");
        list.add(map);

        return list;
    }

    //-------------------------------------------预授信用到的--------------------------------------------------------------
    public static List<Map<String,Object>> getHkType(){//
        //还款方式( 1：利随本清 2：分期付息，到期还本，3：分期还本，分期付息，RPT-05：等额本息，RPT-06 等额本金
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","利随本清");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","分期付息，到期还本");
        map.put("code","2");
        list.add(map);

      /*  map = new HashMap<>();
        map.put("name","分期还本，分期付息");
        map.put("code","3");
        list.add(map);*/
        map = new HashMap<>();
        map.put("name","等额本息");
        map.put("code","RPT-05");
        list.add(map);
        map = new HashMap<>();
        map.put("name","等额本金");
        map.put("code","RPT-06");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> getDanbaoType(){//
        //担保类型 0:保证担保;1:信用;2:差额回购;3:优先权处置;4:差额退款;5:应收账款转让;6:应收账款质押;7:代偿;8:质押;9:无担保
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","保证担保");
        map.put("code","0");
        list.add(map);
        map = new HashMap<>();
        map.put("name","信用");
        map.put("code","1");
        list.add(map);
        map = new HashMap<>();
        map.put("name","差额回购");
        map.put("code","2");
        list.add(map);
        map = new HashMap<>();
        map.put("name","优先权处置");
        map.put("code","3");
        list.add(map);
        map = new HashMap<>();
        map.put("name","差额退款");
        map.put("code","4");
        list.add(map);
        map = new HashMap<>();
        map.put("name","应收账款转让");
        map.put("code","5");
        list.add(map);
        map = new HashMap<>();
        map.put("name","应收账款质押");
        map.put("code","6");
        list.add(map);
        map = new HashMap<>();
        map.put("name","代偿");
        map.put("code","7");
        list.add(map);
        map = new HashMap<>();
        map.put("name","质押");
        map.put("code","8");
        list.add(map);
        map = new HashMap<>();
        map.put("name","无担保");
        map.put("code","9");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> getHasZhuisuo(){//
        //有无追索权(0无;1有)
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","无");
        map.put("code","0");
        list.add(map);

        map = new HashMap<>();
        map.put("name","有");
        map.put("code","1");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> getBaoliType(){//
        //保理类型(1明保理;2暗保理)
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","明保理");
        map.put("code","1");
        list.add(map);

        map = new HashMap<>();
        map.put("name","暗保理");
        map.put("code","2");
        list.add(map);

        return list;
    }
    public static List<Map<String,Object>> getChuzhangType(){//
        //出账品种(0:流动资金贷款 1：银行承兑)
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        map.put("name","流动资金贷款");
        map.put("code","0");
        list.add(map);

        map = new HashMap<>();
        map.put("name","银行承兑");
        map.put("code","1");
        list.add(map);

        return list;
    }




    public static Map<String,Object> getMap(String code,  List<Map<String, Object>> list){

        Map<String,Object> empty = new HashMap<>();
        empty.put("name","");
        for (Map<String,Object> map:list){
            String c = map.get("code")+"";
            if (code.equals(c)){
                return map;
            }
        }
        return empty;
    }
    //  //根据name 获取相应的map  例如 "loanState":[{"1":"放款中"},{"2":"已放款"},{"3":"已结清"},{"4":"已驳回"},{"5":"已撤销"}]
    public static Map<String,Object> getMapByKey(String key,  List<Map<String, Object>> list){

        Map<String,Object> empty = new HashMap<>();
        empty.put(key,"");
        for (Map<String,Object> map:list){
           if (map.containsKey(key)){
                return  map;
           }
        }
        return empty;
    }


    public static Map<String,Object> getNameCodeData(){
        if (mNameCode == null || mNameCode.isEmpty()){
            Map<String,Object> mapData = new HashMap<>();
            String nameCode = SPUtils.get(BasicApplication.getContext(), MbsConstans.SharedInfoConstans.NAME_CODE_DATA,"")+"";
            if (!UtilTools.empty(nameCode)){
                mNameCode =   JSONUtil.getInstance().jsonMap(nameCode);
            }else {
                TipsToast.showToastMsg("配置文件丢失，请手动退出重新登录");
            }
            return mNameCode;
        }else {
            return mNameCode;
        }
    }

    //根据code 获取name
    public static List<Map<String,Object>> getNameCodeByType(String keyType){
        Map<String,Object> mapData = getNameCodeData();
        List<Map<String,Object>> mTypeList = new ArrayList<>();
        if (mapData != null && !mapData.isEmpty()){
            mTypeList =(List<Map<String,Object>>) mapData.get(keyType);
        }
        return mTypeList;
    }


    public static List<Map<String,Object>> getListByKeyList(List<Map<String, Object>> list){
        List<Map<String,Object>> list1 = new ArrayList<>();

        if (list == null || list.size() ==0){

            return list1;
        }
        for (Map<String,Object> map:list){
            Map<String,Object> resultMap = new HashMap<>();
            for (String key:map.keySet()) {
                resultMap.put("code",key);
                resultMap.put("name",map.get(key));
            }
            list1.add(resultMap);
        }
        return list1;
    }


    public static List<Map<String,Object>> getSetType(){//设置类型
        //账号主体（0：红涨绿跌，1红跌绿涨）
        List<Map<String,Object>> mDataList=new ArrayList<>();
        Map<String,Object> map1=new HashMap<>();

        map1.put("name","红跌绿涨");
        map1.put("code","0");
        mDataList.add(map1);

        map1=new HashMap<>();
        map1.put("name","红涨绿跌");
        map1.put("code","1");
        mDataList.add(map1);


        return mDataList;
    }



    public static List<Map<String,Object>> getHankuanAccountType(){//还款账户类型
        //账号主体（0：个人，1：企业）
        List<Map<String,Object>> mDataList=new ArrayList<>();
        Map<String,Object> map1=new HashMap<>();

        map1.put("name","结算账户还款");
        map1.put("code","1");
        mDataList.add(map1);

        map1=new HashMap<>();
        map1.put("name","资金账户还款");
        map1.put("code","2");
        mDataList.add(map1);


        return mDataList;
    }

    public static List<Map<String,Object>> getRegisterType(){////账户主体类型
        //账号主体（0：个人，1：企业）
        List<Map<String,Object>> mDataList=new ArrayList<>();
        Map<String,Object> map1=new HashMap<>();

        map1.put("name","个人");
        map1.put("code","0");
        mDataList.add(map1);

        map1=new HashMap<>();
        map1.put("name","企业");
        map1.put("code","1");
        mDataList.add(map1);


        return mDataList;
    }


    public static List<Map<String,Object>> getAccoutType(){//交易账户类型
        //（0：法币账户，1：币币账户 2：合约账户）
        List<Map<String,Object>> mDataList=new ArrayList<>();
        Map<String,Object> map1=new HashMap<>();
        map1.put("name","法币账户");
        map1.put("code","0");
        mDataList.add(map1);

        map1=new HashMap<>();
        map1.put("name","币币账户");
        map1.put("code","1");
        mDataList.add(map1);

        map1=new HashMap<>();
        map1.put("name","合约账户");
        map1.put("code","2");
        mDataList.add(map1);

        map1=new HashMap<>();
        map1.put("name","奖励金");
        map1.put("code","3");
        mDataList.add(map1);


        return mDataList;
    }

    public static List<Map<String,Object>> getBiType(){//交易账户类型
        List<Map<String,Object>> mDataList=new ArrayList<>();
        Map<String,Object> map1=new HashMap<>();
        map1.put("name","USDT");
        map1.put("code","0");
        mDataList.add(map1);

        map1=new HashMap<>();
        map1.put("name","BTC");
        map1.put("code","1");
        mDataList.add(map1);

        map1=new HashMap<>();
        map1.put("name","ETH");
        map1.put("code","2");
        mDataList.add(map1);

        return mDataList;
    }



    public static List<Map<String,Object>> getPriceType(){//类型
        List<Map<String,Object>> mDataList=new ArrayList<>();
        Map<String,Object> map1=new HashMap<>();


        map1=new HashMap<>();
        map1.put("name","限价");
        map1.put("code","1");
        mDataList.add(map1);

        map1=new HashMap<>();
        map1.put("name","计划委托");
        map1.put("code","2");
        mDataList.add(map1);

        return mDataList;
    }

    public static List<Map<String,Object>> getBBPriceType(){//类型
        List<Map<String,Object>> mDataList=new ArrayList<>();
        Map<String,Object> map1=new HashMap<>();
        map1.put("name","限价");
        map1.put("code","0");
        mDataList.add(map1);

        map1=new HashMap<>();
        map1.put("name","市价");
        map1.put("code","1");
        mDataList.add(map1);

        return mDataList;
    }
}
