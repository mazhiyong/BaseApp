package com.lr.biyou.ui.temporary.activity;

/**
 *  开始指纹登录   界面
 */

import android.content.Intent;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
public class FingermentActivity extends BasicActivity {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.switch_finger_login)
    Switch mSwitch;
    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.set_title));
    }
    @Override
    public int getContentView() {
        return R.layout.activity_fingerment;
    }
    @OnClick({R.id.back_img,R.id.switch_finger_login,R.id.left_back_lay})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.switch_finger_login:
                 if(mSwitch.isChecked()){
                    //startActivity(new Intent(FingermentActivity.this,FingerRecognieActivity.class));
                    startActivity(new Intent(FingermentActivity.this,FingerCheckActivity.class));
                 }
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
