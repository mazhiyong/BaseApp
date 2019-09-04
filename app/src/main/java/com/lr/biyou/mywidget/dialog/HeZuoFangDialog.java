package com.lr.biyou.mywidget.dialog;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.SlideEnter.SlideBottomEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.widget.base.BaseDialog;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.HezuoListAdapter;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.listener.SelectBackListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HeZuoFangDialog extends BaseDialog {

    private TextView mCancleTv;
    private TextView mTitleTv;
    private TextView mSureTv;

    private Context context;

    private String mTitleStr = "";

    private int mType = 0;
    private SelectBackListener mSelectBackListener;

    private RecyclerView mRecyclerView;
    private HezuoListAdapter mHezuoListAdapter;

    private Map<String,Object> mSelectMap ;

    public SelectBackListener getSelectBackListener() {
        return mSelectBackListener;
    }

    public void setSelectBackListener(SelectBackListener selectBackListener) {
        mSelectBackListener = selectBackListener;
    }


    private List<Map<String,Object>> mList = new ArrayList<>();

    public HeZuoFangDialog(Context context) {
        super(context);
        this.context = context;
    }
    public HeZuoFangDialog(Context context, boolean isStyle, List<Map<String,Object>> list, String title, int  type) {
        super(context,isStyle);
        this.context = context;
        mList.clear();
        mList.addAll(list);
        mTitleStr = title;
        mType = type;
    }

    @Override
    public View onCreateView() {

        View view = View.inflate(context, R.layout.dialog_hezuo_layout, null);

        mRecyclerView = view.findViewById(R.id.hezuo_recyclerview);

        mSureTv = view.findViewById(R.id.sure_tv);
        mTitleTv = view.findViewById(R.id.title_tv);
        mCancleTv = view.findViewById(R.id.cancel_tv);
        initData();
        return  view;
    }

    @Override
    public void setUiBeforShow() {
        mTitleTv.setText(mTitleStr);
        mSureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectBackListener != null){
                    mSelectBackListener.onSelectBackListener(mSelectMap,mType);
                }
                dismiss();
            }
        });

        mCancleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


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
                .dismissAnim(bas_out);//

    }


    public void initData(){

        if(mHezuoListAdapter == null){
            mHezuoListAdapter = new HezuoListAdapter(context,mList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(mHezuoListAdapter);
            mHezuoListAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
                @Override
                public void OnMyItemClickListener(View view, int position) {
                    mSelectMap = mHezuoListAdapter.getDatas().get(position);
                    if (mSelectBackListener != null){
                        mSelectBackListener.onSelectBackListener(mSelectMap,mType);
                    }
                    dismiss();

                }
            });

        }else {
            mHezuoListAdapter.notifyDataSetChanged();
        }
    }
}
