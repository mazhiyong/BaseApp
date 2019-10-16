package com.lr.biyou.ui.moudle2.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.view.RequestView;

import java.util.Map;

import butterknife.BindView;

public class ChatActivity extends BasicActivity implements RequestView {


    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.back_text)
    TextView backText;
    @BindView(R.id.left_back_lay)
    LinearLayout leftBackLay;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_text_tv)
    TextView rightTextTv;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.title_bar_view)
    LinearLayout titleBarView;
    @BindView(R.id.bt)
    Button bt;

    @Override
    public int getContentView() {
        return R.layout.activity_chat;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        titleText.setText("聊天界面");
        titleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);


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


}
