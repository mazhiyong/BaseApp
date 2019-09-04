package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.ShoushiPatternCallBack;
import com.lr.biyou.mywidget.view.ShouShiPatternView;
import com.lr.biyou.mywidget.view.ShouShiPatternViewSmall;
import com.lr.biyou.utils.secret.MD5;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 手势解锁（创建）界面
 */
public class DrawPatternActivity extends BasicActivity  implements ShoushiPatternCallBack{
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.shoushi_big)
    ShouShiPatternView mPatternViewBig;
    @BindView(R.id.shoushi_small)
    ShouShiPatternViewSmall mPatternViewSmall;
    @BindView(R.id.tv_tip)
    TextView mTextView;
    @BindView(R.id.bt_set_sucess)
    Button mButton;

    String first;
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }
    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(R.string.set_handpause);
        ShouShiPatternView.setCallBack(this);
    }
    @Override
    public int getContentView() {
        return R.layout.activity_draw_pattern;
    }
    @Override
    public void finsh(int i, List<Integer> str) {
        mPatternViewSmall.cellSeleced(str);
        mPatternViewSmall.invalidate();
        LogUtilDebug.i("show","密码的长度："+str.toString().length());
        switch (i){
            case 0:
                    mTextView.setText("请再次绘制手势密码图案");
                    mButton.setVisibility(View.INVISIBLE);
                    first=str.toString();
                break;
            case 1:
                    if(first!=null){
                        if(first.equals(str.toString())){
                            mPatternViewSmall.cellSeleced(str);
                            mTextView.setText("手势密码创建成功");
                            mButton.setVisibility(View.VISIBLE);
                            //对手势密码进行MD5加密
                            String reslult = MD5.getInstance().md5String(str.toString());
                            LogUtilDebug.i("show","MD5加密后的结果："+reslult);
                            //sharedPreferences 保存加密后的结果
                            SPUtils.put(DrawPatternActivity.this, MbsConstans.SharedInfoConstans.SHOUSHI_CODE, reslult);


                        }else {
                            mButton.setVisibility(View.INVISIBLE);
                            mTextView.setText("手势密码创建失败，请重新创建");

                        }

                    }
                break;
            case 2:
                mTextView.setText("请重新绘制图案，至少连接5个点");
                mButton.setVisibility(View.INVISIBLE);
                break;

        }
    }


    @OnClick({R.id.back_img,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
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
