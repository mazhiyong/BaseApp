package com.lr.biyou.chatry.ui.activity;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.chatry.ui.interfaces.SearchableInterface;

import java.util.Map;

public class SealSearchBaseActivity extends BasicActivity implements TextWatcher, SearchableInterface {
    private static final String TAG = "SealSearchBaseActivity";
    protected String search; //当前关键字

    private EditText etSearch;
    private ImageView ivSearch;
    @Override
    public int getContentView() {
        return R.layout.activity_select_content;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        etSearch = findViewById(R.id.et_search2);
        ivSearch = findViewById(R.id.iv_search);
        etSearch.addTextChangedListener(this);


       /* getTitleBar().setVisibility(View.GONE);
        getTitleBar().setType(SEARCH);
        getTitleBar().addSeachTextChangedListener(this);
        setContentView();
        getTitleBar().setOnBtnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
    }

    @Override
    public void search(String match) {
        //子类实现自己搜索
    }

    @Override
    public void clear() {
        //子类实现清空搜索
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                search = s.toString();
                if (TextUtils.isEmpty(search)) {
                    clear();
                } else {
                    search(search);
                }
            }
        }, 300);
    }
}
