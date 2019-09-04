package com.lr.biyou.ui.temporary.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.widget.base.BaseDialog;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.MyAuthCallback;
import com.lr.biyou.utils.tool.CryptoObjectHelper;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.LogUtilDebug;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *  指纹识别界面
 */
public class FingerRecognieActivity extends BasicActivity {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;

    TextView tvshow;
    private CancellationSignal cancellationSignal = null;
    private FingerprintManager fingerprintManager = null;
    private MyAuthCallback myAuthCallback = null;

    private Handler handler = null;

    View view;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void init() {
        mTitleText.setText(getResources().getString(R.string.finger_login));
        //初始化
        fingerprintManager = (FingerprintManager)getSystemService(Context.FINGERPRINT_SERVICE);
        final BaseDialog dialog=new BaseDialog(this,true) {
            @Override
            public View onCreateView() {
                view=getLayoutInflater().inflate(R.layout.figner_recogni_dialog_layout,null);
                tvshow=view.findViewById(R.id.tvshow_finger_message);
                return view;
            }

            @Override
            public void setUiBeforShow() {

            }
        };
        dialog.setCanceledOnTouchOutside(false);
        dialog.widthScale(0.6f);
        dialog.showAtLocation(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MbsConstans.FingerRecognize.MSG_AUTH_SUCCESS:
                        setResultInfo("识别成功");
                        if(cancellationSignal!=null){
                            cancellationSignal.cancel();
                            cancellationSignal = null;
                        }

                        break;
                    case MbsConstans.FingerRecognize.MSG_AUTH_FAILED:
                        setResultInfo("识别失败");
                        cancellationSignal = null;
                        break;
                    case MbsConstans.FingerRecognize.MSG_AUTH_ERROR:
                        handleErrorCode(msg.arg1);
                        break;
                    case MbsConstans.FingerRecognize.MSG_AUTH_HELP:
                        handleHelpCode(msg.arg1);
                        break;
                }
            }
        };
        try {
            CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
            if (cancellationSignal == null) {
                cancellationSignal = new CancellationSignal();
            }
            myAuthCallback = new MyAuthCallback(handler);
            fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(),
                    cancellationSignal,0,myAuthCallback, null);

        } catch (Exception e) {
            e.printStackTrace();
            setResultInfo("识别失败");
        }

          //判断当前设备是否支持指纹解锁
        if (!fingerprintManager.isHardwareDetected()) {
            // no fingerprint sensor is detected, show dialog to tell user.
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("提示：");
            builder1.setMessage("当前手机设备不支持指纹解锁");
            builder1.setIcon(android.R.drawable.stat_sys_warning);
            builder1.setCancelable(false);
            builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder1.create().show();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            // 判断设备是否已有录入的指纹信息
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle("提示：");
            builder2.setMessage("当前手机设备未录入有效指纹");
            builder2.setIcon(android.R.drawable.stat_sys_warning);
            builder2.setCancelable(false);
            builder2.setNegativeButton("去设置指纹", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                }
            });
            builder2.create().show();
        } else {
            try {
                //弹出对话框
                dialog.show();

                view.findViewById(R.id.tvcancel_finger_recogn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FingerRecognieActivity.this,"取消指纹登录",Toast.LENGTH_SHORT).show();
                        cancellationSignal.cancel();
                        cancellationSignal = null;
                        dialog.dismiss();
                    }
                });
                myAuthCallback = new MyAuthCallback(handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_finger_recognie;
    }

    @OnClick({R.id.back_img,R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal=null;
        }
        if(handler!=null){
            handler=null;
        }
    }
    private void handleHelpCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
                setResultInfo("指纹传感器不可用");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
                setResultInfo("指纹传感器不可用");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
                setResultInfo("失败次数太多，请稍后再试");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
                setResultInfo("识别失败");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
                setResultInfo("长时间未检测到指纹");
                break;
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                setResultInfo("识别失败");
                break;
        }
    }

    private void handleErrorCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:

            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:

            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:

            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:

            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:

            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                setResultInfo("识别失败");
                break;
        }
    }

    private void setResultInfo(String str) {
        LogUtilDebug.i("show","识别结果："+str);
        if (str!= null) {
            if (str.equals("识别成功")){
                tvshow.setText("识别成功");
            } else  {
                tvshow.setText("识别失败，再试一次");
            }

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
