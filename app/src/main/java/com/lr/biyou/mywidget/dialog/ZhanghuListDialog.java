package com.lr.biyou.mywidget.dialog;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.flyco.dialog.widget.popup.base.BasePopup;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.ZhangHuListAdapter;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.listener.SelectBackListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZhanghuListDialog extends BasePopup<ZhanghuListDialog> {


    private RecyclerView mRecyclerView;
    private ZhangHuListAdapter mHezuoListAdapter;

    public SelectBackListener getSelectBackListener() {
        return mSelectBackListener;
    }

    public void setSelectBackListener(SelectBackListener selectBackListener) {
        mSelectBackListener = selectBackListener;
    }

    private SelectBackListener mSelectBackListener;
    private int mType;
    private List<Map<String,Object>> mList = new ArrayList<>();
    public ZhanghuListDialog(Context context, List<Map<String,Object>> mlist, int  type) {
        super(context);
//            setCanceledOnTouchOutside(false);
        mList = mlist;
        mType = type;
    }

    @Override
    public View onCreatePopupView() {
        View inflate = View.inflate(mContext, R.layout.item_hezuo_popu, null);
        mRecyclerView = inflate.findViewById(R.id.hezuo_recyclerview);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        initData(null);
    }



    public void initData(List<Map<String,Object>> mdata){
        if (mdata != null){
            mList.clear();
            mList.addAll(mdata);
        }
        if(mHezuoListAdapter == null){
            mHezuoListAdapter = new ZhangHuListAdapter(mContext,mList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.setAdapter(mHezuoListAdapter);

            mHezuoListAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
                @Override
                public void OnMyItemClickListener(View view, int position) {
                    Map<String,Object> mSelectMap = mHezuoListAdapter.getDatas().get(position);
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