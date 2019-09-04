package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class PayPlanAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;

    public List<Boolean> getShowList() {
        return mShowList;
    }
    public void setShowList(List<Boolean> showList) {
        mShowList = showList;
    }
    private List<Boolean> mShowList = new ArrayList<>();

    public PayPlanAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_pay_plan, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;

        double benjin = Double.valueOf(item.get("bqyhbnjn")+"");
        double lixi = Double.valueOf(item.get("bqyhlixi")+"");
        double allm = UtilTools.add(benjin,lixi);

        String endStr = item.get("dangqend")+"";


        viewHolder.mStatusTv.setText("待还款");
        String endt = UtilTools.getStringFromSting2(endStr,"yyyyMMdd","yyyy-MM-dd");
        viewHolder.mTimeTv.setText(endt);
        viewHolder.mQishuTv.setText((position+1)+"/"+item.get("huankqis")+"期");
        viewHolder.mBenjinTv.setText(UtilTools.getRMBMoney(item.get("bqyhbnjn")+""));
        viewHolder.mLixiTv.setText(UtilTools.getRMBMoney(item.get("bqyhlixi")+""));
        viewHolder.mAllMoneyTv.setText(UtilTools.getRMBMoney(allm+""));

        if (position == 0) {
            viewHolder.mIvRouteIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logistics_track_point));
            //viewHolder.mContentLay.setBackgroundColor(Color.WHITE);
            viewHolder.mIconTopLine.setVisibility(View.INVISIBLE);
            viewHolder.mIconBottomLine.setVisibility(View.VISIBLE);

        } else if (position == mDataList.size() - 1) {
            viewHolder.mIvRouteIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logistics_track_point));
            //viewHolder.mContentLay.setBackgroundColor(Color.parseColor("#F4F5F5"));
            viewHolder.mIconTopLine.setVisibility(View.VISIBLE);
            viewHolder.mIconBottomLine.setVisibility(View.INVISIBLE);

        } else {
            viewHolder.mIvRouteIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.logistics_track_point));
            //viewHolder.mContentLay.setBackgroundColor(Color.parseColor("#F4F5F5"));
            viewHolder.mIconTopLine.setVisibility(View.VISIBLE);
            viewHolder.mIconBottomLine.setVisibility(View.VISIBLE);
        }

        String currentMonth = UtilTools.getStringFromDate(new Date(),"yyyyMM");
        if (endStr.startsWith(currentMonth)){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,UtilTools.dip2px(mContext,60));
            viewHolder.mIvRouteIcon.setLayoutParams(layoutParams);
            viewHolder.mIvRouteIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.current_month_icon));
        }else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(UtilTools.dip2px(mContext,16),UtilTools.dip2px(mContext,16));
            viewHolder.mIvRouteIcon.setLayoutParams(layoutParams);
            viewHolder.mIvRouteIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.current_green_circle));
        }

        boolean b = mShowList.get(position);
        if (b){
            viewHolder.mShowLay.setVisibility(View.VISIBLE);
            viewHolder.mArrowView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.android_list_down));
        }else {
            viewHolder.mShowLay.setVisibility(View.GONE);
            viewHolder.mArrowView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.android_list_idex));
        }

        viewHolder.mHeadLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.mShowLay.isShown()){
                    mShowList.set(position,false);
                    viewHolder.mShowLay.setVisibility(View.GONE);
                    viewHolder.mArrowView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.android_list_idex));
                }else {
                    mShowList.set(position,true);
                    viewHolder.mShowLay.setVisibility(View.VISIBLE);
                    viewHolder.mArrowView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.android_list_down));
                }
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.icon_top_line)
        View mIconTopLine;
        @BindView(R.id.iv_route_icon)
        ImageView mIvRouteIcon;
        @BindView(R.id.icon_bottom_line)
        View mIconBottomLine;
        @BindView(R.id.status)
        TextView mStatusTv;
        @BindView(R.id.time_tv)
        TextView mTimeTv;
        @BindView(R.id.qishu_tv)
        TextView mQishuTv;
        @BindView(R.id.benjin_tv)
        TextView mBenjinTv;
        @BindView(R.id.lixi_tv)
        TextView mLixiTv;
        @BindView(R.id.content_lay)
        LinearLayout mContentLay;
        @BindView(R.id.show_lay)
        LinearLayout mShowLay;
        @BindView(R.id.head_lay)
        LinearLayout mHeadLay;
        @BindView(R.id.all_money)
        TextView mAllMoneyTv;
        @BindView(R.id.arrow_view)
        ImageView mArrowView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}