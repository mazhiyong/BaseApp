package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  开启消息通知（服务提醒）
 */
public class ServicesRemindActivity extends BasicActivity {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.switch_service_remind)
    Switch mRemindServiceSwitch;
    @BindView(R.id.ll_layout)
    LinearLayout mLayout;
    @BindView(R.id.bt_clear_all)
    Button mClearButton;

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.service_remind));
    }

    @Override
    public int getContentView() {
        return R.layout.activity_services_remind;
    }

    @OnClick({R.id.bt_clear_all,R.id.back_img,R.id.switch_service_remind,R.id.left_back_lay})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.switch_service_remind:
                if(mRemindServiceSwitch.isChecked()){
                    mLayout.setVisibility(View.VISIBLE);
                }else {
                    mLayout.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.bt_clear_all:
                Toast.makeText(this,"清空成功",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,ServicesRemindInfoActivity.class));
                break;

        }

    }

    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {

    }

    /**
     * @descriptoin 请求结束之后隐藏progress
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void disimissProgress() {

    }

    /**
     * @param tData 数据类型
     * @param mType
     * @descriptoin 请求数据成功
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

    }

    /**
     * @param map
     * @param mType
     * @descriptoin 请求数据错误
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }
}
