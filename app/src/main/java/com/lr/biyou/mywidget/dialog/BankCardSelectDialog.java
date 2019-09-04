package com.lr.biyou.mywidget.dialog;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.BankCardSelectAdapter;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.listener.SelectBackListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankCardSelectDialog extends BaseDialog {
    private Context mContext;
    private Map<String,Object>  mSelectedMap;
    private List<Map<String,Object>> mDatas= new ArrayList<>();
    private int mType = 0;

    private BankCardSelectAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mAddTv;
    private TextView mCancelTv;
    private RelativeLayout mAllLay;

    private LinearLayout mAddCardLay;

    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    private View.OnClickListener mOnClickListener;


    private SelectBackListener mSelectBackListener;

    public SelectBackListener getSelectBackListener() {
        return mSelectBackListener;
    }

    public void setSelectBackListener(SelectBackListener selectBackListener) {
        mSelectBackListener = selectBackListener;
    }

    public BankCardSelectDialog(Context context) {
        super(context);
        mContext=context;
    }

    public BankCardSelectDialog(Context context, boolean isPopupStyle, List<Map<String, Object>> datas, int type) {
        super(context, isPopupStyle);
        mContext =context;
        mDatas = datas;
        mType = type;
    }

    @Override
    public View onCreateView() {
        View view=View.inflate(mContext, R.layout.dialong_bankcard_layout,null);
        mRecyclerView=view.findViewById(R.id.rcv_bankcard);
        mAddTv = view.findViewById(R.id.tv_add_card);
        mAddCardLay = view.findViewById(R.id.add_card_lay);
        mAllLay = view.findViewById(R.id.all_lay);
        mAllLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (mType == 30){
            mAddCardLay.setVisibility(View.GONE);
        }else {
            mAddCardLay.setVisibility(View.VISIBLE);
        }
        mCancelTv=view.findViewById(R.id.tv_cancel);
        heightScale(0.6f);
        init();
        return view;
    }


    private void init() {
        if(mAdapter==null){
            mAdapter=new BankCardSelectAdapter(mContext,mDatas);
            LinearLayoutManager manager=new LinearLayoutManager(mContext);
            manager.setOrientation(RecyclerView.VERTICAL);
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
                @Override
                public void OnMyItemClickListener(View view, int position) {
                    mSelectedMap = mAdapter.getDatas().get(position);
                    if(mSelectBackListener!=null){
                        mSelectBackListener.onSelectBackListener(mSelectedMap,mType);
                    }
                    dismiss();
                }
            });
        }else {
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void setUiBeforShow() {
        if (mOnClickListener != null){
            mAddCardLay.setOnClickListener(mOnClickListener);
            mCancelTv.setOnClickListener(mOnClickListener);

        }
        /*mAddTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"添加新卡",Toast.LENGTH_SHORT).show();
            }
        });
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
*/

        BaseAnimatorSet bas_in;
        BaseAnimatorSet bas_out;
        bas_in = new SlideBottomEnter();
        bas_in.duration(400);
        bas_out = new SlideBottomExit();
        bas_out.duration(200);

        widthScale(1f);
        dimEnabled(true);
        // baseDialog.heightScale(1f);
        showAnim(bas_in)
                .dismissAnim(bas_out);
    }
}
