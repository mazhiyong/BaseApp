package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.activity.BankOpenActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QiyeCaPayAdapter extends RecyclerView.Adapter<QiyeCaPayAdapter.ViewHodler> {

    private Context mContext;
    private List<Map<String, Object>> mList;

    private OnMyItemClickListener mItemClickListener;

    public OnMyItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(OnMyItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    private int mSelectPosition = -1;

    public QiyeCaPayAdapter(Context context, List<Map<String, Object>> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_chose_payway, parent, false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        final Map<String, Object> item = mList.get(position);
        final ViewHodler viewHodler = (ViewHodler) holder;

        String type = item.get("type") + "";

        viewHodler.mCheckBox.setChecked(false);
        viewHodler.mTvTitle.setText(item.get("opnbnknm") + "");
        if (type.equals("100")) {
            GlideUtils.loadImage(mContext, "http://img.mp.sohu.com/upload/20170706/eba483641770409fbba6752a1c1c28c6_th.png", viewHodler.mIvIcon);
        } else {
            viewHodler.mIvIcon.setImageResource((Integer) item.get("icon"));
        }

        switch (type) {
            case "11":
                viewHodler.mMoneyTv.setText(UtilTools.getRMBMoney(0+""));
                viewHodler.mTypeTv.setText("");
                viewHodler.mIvIcon.setImageResource((Integer) item.get("icon"));

                break;
            case "12":
                viewHodler.mMoneyTv.setText("");
                viewHodler.mTypeTv.setText("");
                viewHodler.mIvIcon.setImageResource((Integer) item.get("icon"));
                break;
            case "13":
                viewHodler.mMoneyTv.setText("");
                viewHodler.mTypeTv.setText("");
                viewHodler.mIvIcon.setImageResource((Integer) item.get("icon"));
                break;
            case "14":
                viewHodler.mMoneyTv.setText("");
                viewHodler.mTypeTv.setText("");
                viewHodler.mIvIcon.setImageResource((Integer) item.get("icon"));
                break;
            case "15":
                viewHodler.mMoneyTv.setText("");
                viewHodler.mTypeTv.setText("");
                viewHodler.mIvIcon.setImageResource((Integer) item.get("icon"));
                break;
            case "100":
                viewHodler.mMoneyTv.setText("");
                viewHodler.mTypeTv.setText("快捷卡");
                GlideUtils.loadImage(mContext, "http://img.mp.sohu.com/upload/20170706/eba483641770409fbba6752a1c1c28c6_th.png", viewHodler.mIvIcon);
                break;

        }

        if (mSelectPosition == position) {
            viewHodler.mCheckBox.setChecked(true);
        } else {
            viewHodler.mCheckBox.setChecked(false);
        }

        viewHodler.mMoreBankcardLayout.setVisibility(View.GONE);


        viewHodler.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHodler.mLlLay.performClick();
            }
        });
        viewHodler.mLlLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectPosition = position;
                if (mItemClickListener != null) {
                    mItemClickListener.OnMyItemClickListener(viewHodler.mCheckBox, position);
                }
                notifyDataSetChanged();
            }
        });
        //选择更多客户
        viewHodler.mMoreBankcardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,BankOpenActivity.class);
                intent.putExtra("backtype","40");
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.check_box)
        CheckBox mCheckBox;
        @BindView(R.id.ll_lay)
        LinearLayout mLlLay;
        @BindView(R.id.type_tv)
        TextView mTypeTv;
        @BindView(R.id.money_tv)
        TextView mMoneyTv;
        @BindView(R.id.ll_more_bankcard)
        LinearLayout mMoreBankcardLayout;

        public ViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
