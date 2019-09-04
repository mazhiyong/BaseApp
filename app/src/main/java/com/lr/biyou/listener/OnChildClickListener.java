package com.lr.biyou.listener;

import android.view.View;

import java.util.Map;

public interface OnChildClickListener {
        void onChildClickListener(View view, int position, Map<String, Object> mParentMap);
    }