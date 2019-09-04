package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.ui.temporary.activity.PDFLookActivity;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class PayHistoryAdapter extends ListBaseAdapter {



    private LayoutInflater mLayoutInflater;

    public PayHistoryAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_pay_history, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;

        String status = item.get("checkstate")+"";
        String statusStr = "";
        switch (status){
            case "0":
                statusStr = "初始化";
                break;
            case "1":
                statusStr = "已申请";
                break;
            case "2":
                statusStr = "还款成功";
                break;
            case "3":
                statusStr = "还款失败";
                break;
        }

        List<Map<String, Object>> list = SelectDataUtil.getNameCodeByType("repayState");
        Map<String,Object> mm = SelectDataUtil.getMapByKey(status+"",list);
        statusStr = mm.get(status+"")+"";


        String backType = item.get("backtype")+"";
        String backTypeStr = "";
        //还款账户类型(1：客户结算账户还款;2：客户资金账户还款;3：核心企业资金账户代还;4：借款客户网银还款;5：核心企业网银代
        switch (backType){
            case "1":
                backTypeStr = "客户结算账户还款";
                break;
            case "2":
                backTypeStr = "客户资金账户还款";
                break;
            case "3":
                backTypeStr = "核心企业资金账户代还";
                break;
            case "4":
                backTypeStr = "借款客户网银还款";
                break;
            case "5":
                backTypeStr = "核心企业网银代";
                break;
        }

        List<Map<String, Object>> list2 = SelectDataUtil.getNameCodeByType("repayAcct");
        Map<String,Object> mm2 = SelectDataUtil.getMapByKey(backType+"",list2);
        backTypeStr = mm2.get(backType+"")+"";


        String benjin = item.get("backbejn")+"";
        String benjin2 = UtilTools.getRMBMoney(benjin);

        String backlixi = item.get("backlixi")+"";
        String backlixi2 = UtilTools.getRMBMoney(backlixi);


        viewHolder.mStatusTv.setText(statusStr);
        viewHolder.mTimeTv.setText(item.get("creatime")+"");
        viewHolder.mMoneyTv.setText("");
        viewHolder.mPayType.setText(backTypeStr);
        viewHolder.mPayBenjin.setText(benjin2);
        viewHolder.mPayLixi.setText(backlixi2);
        viewHolder.mPayNum.setText(item.get("rtnbillid")+"");

        String date = item.get("creatime")+"";
        Date d = UtilTools.getDateFromString(date,"yyyyMMddHHmmss");
        String s = UtilTools.getStringFromDate(d,"yyyy-MM-dd HH:mm:ss");
        viewHolder.mTimeTv.setText(s);

        viewHolder.mXiyiTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PDFLookActivity.class);
                intent.putExtra("id",item.get("repaypath")+"");
                mContext.startActivity(intent);
            }
        });


        String tip =  mContext.getResources().getString(R.string.look_pay_book);
        int dian = tip.length();
        if (tip.contains("《")) {
            dian = tip.indexOf("《");
        } else {
            dian = tip.length();
        }

       /* 用来标识在 Span 范围内的文本前后输入新的字符时是否把它们也应用这个效果
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)
        Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)
        Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)*/
        SpannableString ss = new SpannableString(tip);
       // ss.setSpan(new BankOpenXieyiActivity.TextSpanClick(false), dian, tip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.black)), 0, dian, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.blue1)), dian, tip.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.mXiyiTv.setText(ss);
        //添加点击事件时，必须设置
        viewHolder.mXiyiTv.setMovementMethod(LinkMovementMethod.getInstance());
    }


    //[{
    //	"backlixi": "1000",
    //	"repaypath": "",
    //	"backtype": "1",
    //	"creatime": "20171127160349",
    //	"rtnbillid": "1733400000109740",
    //	"zifangnme": "廊坊银行股份有限公司营业部",
    //	"checkstate": "2",  还款状态(0:初始化,1:已申请,2:还款成功,3:还款失败
    //	"crhkdtme": "20171127170102",
    //	"backbejn": "100000"
    //}]
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.status_tv)
        TextView mStatusTv;
        @BindView(R.id.time_tv)
        TextView mTimeTv;
        @BindView(R.id.money_tv)
        TextView mMoneyTv;
        @BindView(R.id.pay_benjin)
        TextView mPayBenjin;
        @BindView(R.id.pay_lixi)
        TextView mPayLixi;
        @BindView(R.id.fa_xi)
        TextView mFaXi;
        @BindView(R.id.pay_type)
        TextView mPayType;
        @BindView(R.id.pay_num)
        TextView mPayNum;
        @BindView(R.id.xieyi_tv)
        TextView mXiyiTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}