package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.Map;

public class MyAmountAdapter extends ListBaseAdapter {

    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FIRST = 0;

    private LayoutInflater mLayoutInflater;

    public MyAmountAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            Map<String,Object> map = mDataList.get(position);
            if (map.containsKey("viewType")){
                return TYPE_FIRST;
            }else {
                return TYPE_NORMAL;
            }
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FIRST) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_my_pre_sx, parent, false), TYPE_FIRST);
        } else {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.item_my_amount, parent, false), TYPE_NORMAL);
        }

        // return new ViewHolder(mLayoutInflater.inflate(R.layout.item_my_amount, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        int type = getItemViewType(position);

        switch (type){
            case TYPE_NORMAL:
                String money = UtilTools.getRMBMoney(item.get("creditmoney") + "");
                String useMoney = UtilTools.getRMBMoney(item.get("usingcredit") + "");
                String dongJieMoney = UtilTools.getRMBMoney(item.get("frzcredit") + "");
                String nianLilv = UtilTools.getlilv(item.get("daiklilv") + "");
                String status2 = item.get("creditstate") + "";
                String startTime = UtilTools.getStringFromSting2(item.get("begindate") + "", "yyyyMMdd", "yyyy-MM-dd");
                String endTime = UtilTools.getStringFromSting2(item.get("enddate") + "", "yyyyMMdd", "yyyy-MM-dd");
                viewHolder.mSxEdValueTv.setText(money);
                viewHolder.mHasUseValueTv.setText(useMoney);
                viewHolder.mNianLilvValueTv.setText(nianLilv);
                viewHolder.mSxHtValueTv.setText(item.get("creditfile") + "");
                viewHolder.mSxStopValueTv.setText(endTime);

                viewHolder.mDongJieValueTv.setText(dongJieMoney);
                viewHolder.mSxStartValueTv.setText(startTime);


                switch (status2){
                    case "0":
                        viewHolder.mStatusTv.setText("未开通");
                        break;
                    case "1":
                        viewHolder.mStatusTv.setText("已开通");
                        break;
                    case "2":
                        viewHolder.mStatusTv.setText("暂停");
                        break;
                    case "3":
                        viewHolder.mStatusTv.setText("终止");
                        break;
                    case "4":
                        viewHolder.mStatusTv.setText("已签署");
                        break;
                }

                break;
            case TYPE_FIRST:
                String shenqingMoney = item.get("creditmoney") + "";//申请金额
                String jiekuanQixian = item.get("singlelimit") + "";//借款期限
                String daikuanKind = item.get("reqloantp") + "";//贷款种类
                //Map<String,Object> daikuanKindMap = SelectDataUtil.getMap(daikuanKind,SelectDataUtil.getDaikuanType());
                Map<String,Object> daikuanKindMap = SelectDataUtil.getMap(daikuanKind,SelectDataUtil.getNameCodeByType("loanType"));
                String shenqingDate = item.get("sqdate") + "";//申请日期
                shenqingDate = UtilTools.getStringFromSting2(shenqingDate, "yyyyMMdd", "yyyy-MM-dd");
                viewHolder.mTitleTv.setText("币友");
                String status = item.get("creditstate")+"";
                if (status.equals("14")){
                    viewHolder.mStatusTv.setText("已生效");
                }else {
                    viewHolder.mStatusTv.setText("审核中");
                }
                viewHolder.mShenqingMoneyTv.setText(UtilTools.getRMBMoney(shenqingMoney));
                viewHolder.mJiekuanQixianTv.setText(jiekuanQixian+"月");
                viewHolder.mDaikuanKindTv.setText(daikuanKindMap.get("name") + "");
                viewHolder.mShenqingDateTv.setText(shenqingDate);


                break;
        }



    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleTv;
        TextView mShenqingMoneyTv;
        TextView mJiekuanQixianTv;
        TextView mDaikuanKindTv;
        TextView mShenqingDateTv;
        //--------
        TextView mStatusTv;
        TextView mSxEdValueTv;
        TextView mHasUseValueTv;
        TextView mDongJieValueTv;
        TextView mNianLilvValueTv;
        TextView mSxHtValueTv;
        TextView mSxStartValueTv;
        TextView mSxStopValueTv;

        public ViewHolder(View itemView, int type) {
            super(itemView);
            if (type == TYPE_FIRST) {
                mTitleTv = itemView.findViewById(R.id.title_tv);
                mStatusTv = itemView.findViewById(R.id.status_tv);
                mShenqingMoneyTv = itemView.findViewById(R.id.shenqing_money_tv);
                mJiekuanQixianTv = itemView.findViewById(R.id.jiekuan_qixian_tv);
                mDaikuanKindTv = itemView.findViewById(R.id.daikuan_kind_tv);
                mShenqingDateTv = itemView.findViewById(R.id.shenqing_date_tv);
            } else {
                mStatusTv = itemView.findViewById(R.id.status_tv);
                mSxEdValueTv = itemView.findViewById(R.id.sx_ed_value_tv);
                mHasUseValueTv = itemView.findViewById(R.id.has_use_value_tv);
                mDongJieValueTv = itemView.findViewById(R.id.dongjie_money_tv);
                mNianLilvValueTv = itemView.findViewById(R.id.nian_lilv_value_tv);
                mSxHtValueTv = itemView.findViewById(R.id.sx_ht_value_tv);
                mSxStartValueTv = itemView.findViewById(R.id.sx_start_value_tv);
                mSxStopValueTv = itemView.findViewById(R.id.sx_stop_value_tv);
            }

        }
    }


}
