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

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class ShouxinJkListAdapter extends ListBaseAdapter {

    private LayoutInflater mLayoutInflater;
    private int mW = 0;

    public ShouxinJkListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_shouxin_jk, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;

        //借款状态（1：放款中 2：还款中 3：已结清 4：已驳回）

        String str = item.get("loanstate") + "";
        int status ;
        if (UtilTools.empty(str)){
            status = -123;
        }else {
            status = Integer.valueOf(item.get("loanstate") + "");//loanstate
        }

        List<Map<String, Object>> list = SelectDataUtil.getNameCodeByType("loanState");
        Map<String,Object> mm = SelectDataUtil.getMapByKey(status+"",list);
        String statusStr = mm.get(status+"")+"";
       /* String statusStr = "";
        switch (status) {
            case 1:
                statusStr = "放款中";
                break;
            case 2:
                statusStr = "还款中";
                break;
            case 3:
                statusStr = "已结清";
                break;
            case 4:
                statusStr = "已驳回";
                break;
            default:
                statusStr = "";
                break;
        }*/

        viewHolder.mStatusTv.setText(statusStr);

        viewHolder.mJeikuanYueTv.setText(UtilTools.getRMBMoney(item.get("reqmoney")+""));

        viewHolder.mJiekuanCodeTv.setText(item.get("loansqid") + "");

        String dateTime = UtilTools.getStringFromSting2(item.get("flowdate") + "", "yyyyMMdd", "yyyy-MM-dd");
        viewHolder.mShengqingDateTv.setText(dateTime + "");

        String dateTime2 = UtilTools.getStringFromSting2(item.get("stopdate") + "", "yyyyMMdd", "yyyy-MM-dd");
        viewHolder.mHuankuanStopDate.setText(dateTime2 + "");

        String money = UtilTools.getRMBMoney(item.get("reqmoney") + "");
        viewHolder.mMoneyTv.setText(money);

    }

    //{
    //		"jixishum": "2",
    //		"reqmoney": "100000",
    //		"flowdate": "20171019",
    //		"loansqid": "1729200000092879",
    //		"loancode": "L07",
    //		"zifangnme": "廊坊银行股份有限公司营业部",
    //		"loanstate": "3",
    //		"stopdate": "20181014",
    //		"zifangbho": "BOLF8888"
    //	}                                             借款状态（1：放款中 2：还款中 3：已结清 4：已驳回）
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.money_tv)
        TextView mMoneyTv;
        @BindView(R.id.status_tv)
        TextView mStatusTv;
        @BindView(R.id.jeikuan_yue_tv)
        TextView mJeikuanYueTv;
        @BindView(R.id.shengqing_date_tv)
        TextView mShengqingDateTv;
        @BindView(R.id.huankuan_stop_date)
        TextView mHuankuanStopDate;
        @BindView(R.id.jiekuan_code_tv)
        TextView mJiekuanCodeTv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
