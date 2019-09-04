package com.lr.biyou.basic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.lr.biyou.R;
import com.lr.biyou.di.component.DaggerPersenerComponent;
import com.lr.biyou.di.module.PersenerModule;
import com.lr.biyou.manage.ActivityManager;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.temporary.activity.ChoosePhoneActivity;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.api.ErrorHandler;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.api.RxApiManager;
import com.lr.biyou.mywidget.dialog.TipMsgDialog;
import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mywidget.view.LoadingWindow;
import com.lr.biyou.mywidget.view.TipsToast;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;



public abstract class BasicActivity extends AppCompatActivity implements BGASwipeBackHelper.Delegate,RequestView {
    // 装载Activity
    private ActivityManager activityManager;
    public  boolean mIsRefresh = false;
    //自动登录标示
    protected static boolean isAutoLogin = false;

    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private int i = 0;

    private LoadingWindow mLoadingWindow;


    public List<String> mRequestTagList = new ArrayList<>();


    public boolean mIsRefreshToken = false;

    @Inject
    public RequestPresenterImp mRequestPresenterImp;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            MbsConstans.ALPHA = 100;
            MbsConstans.TOP_BAR_COLOR = R.color.top_bar_bg;
        } else {
            MbsConstans.ALPHA = 0;
            MbsConstans.TOP_BAR_COLOR = R.color.top_bar_bg;
        }

        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        setContentView(getContentView());


        //StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.yellow), 0);
        ButterKnife.bind(this);
        //IntentFilter filter = new IntentFilter();
        //filter.addAction(MbsConstans.BroadcastReceiverAction.NET_CHECK_ACTION);
        //registerReceiver(receiver, filter);

//		new Handler().post(new Runnable() {
//			@Override
//			public void run() {

        activityManager = ActivityManager.getInstance();
        //压入当前Activity
        activityManager.pushActivity(BasicActivity.this);
        LogUtilDebug.i("show","CurrentActivity>>>:"+this.getLocalClassName());

        DaggerPersenerComponent.builder().persenerModule(new PersenerModule(this,this)).build().injectTo(this);

//			}
//		});
        init();
        setBarTextColor();
    }

    public void setBarTextColor(){
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            StatusBarUtil.setDarkMode(this);
        } else {
            StatusBarUtil.setLightMode(this);
        }*/

        StatusBarUtil.setLightMode(this);
    }
    public abstract int getContentView();
    public abstract void init();


    public void viewEnable(){

    }

    /**
     * 内存不够时
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_MODERATE) {
            //开始自杀，清场掉所有的activity
            //下面这个是自己写的方法　　
            closeAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    public void getMsgCodeAction(RequestPresenterImp mRequestPresenterImp) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.resetPassCode, map);
    }

    protected BGASwipeBackHelper mSwipeBackHelper;
    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getmContext().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(false);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(false);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
    }

    private void addInternetView(){
//		View view = LayoutInflater.from(this).inflate(R.layout.internet_set_view,null);
//		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//		layoutParams.setMargins(0, (int)getResources().getDimension(R.dimen.title_item_height) + UtilTools.getStatusHeight2(this), 0, 0);
//		view.setLayoutParams(layoutParams);
    }

    /**
     * 显示dialog
     */
    public void showProgressDialog() {
        if (mLoadingWindow == null) {
            mLoadingWindow = new LoadingWindow(this,R.style.Dialog);
            mLoadingWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    viewEnable();
                }
            });
        }
        mLoadingWindow.showView();
    }
    /**
     * 显示dialog
     */
    public void showProgressDialog(String text) {
        if (mLoadingWindow == null) {
            mLoadingWindow = new LoadingWindow(this,R.style.Dialog);
            mLoadingWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    viewEnable();
                }
            });

        }
        mLoadingWindow.setTipText(text);
        mLoadingWindow.showView();
    }

    /**
     * 隐藏dialog
     */
    public void dismissProgressDialog() {
        if (mLoadingWindow != null && mLoadingWindow.isShowing()) {
            mLoadingWindow.cancleView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityManager.removeActivity(this);
        RxApiManager.get().cancel(this);
        RxApiManager.get().cancelActivityAll(this);
    }


    /**
     * 返回到指定Activity
     * @param clazz		返回到的Activity
     */
    public  void backToAndAutoLogin(Class<?> clazz, boolean isAutoLogin){
        activityManager.backTo(clazz,false);
        BasicActivity.isAutoLogin = isAutoLogin;
    }

    /**
     * 返回到指定Activity
     * @param clazz		返回到的Activity
     * @param isRefresh	是否刷新数据
     */
    public  void backTo(Class<?> clazz, boolean isRefresh){
        activityManager.backTo(clazz,isRefresh);
    }

    public  void backToMainActivity(Class<?> clazz, int i){
        activityManager.backToMainActivity(clazz, i);
    }
    public  void closeAllActivity(){
        activityManager.close();
    }

    /**
     * 返回到指定Activity
     * @param clazz		返回到的Activity
     */
    public  void backTo(Context context, Class<?> clazz, int resId){
        activityManager.backTo(clazz,false);
        //BasicActivity.isMsg = true;
        //BasicActivity.resId = resId;
        //showToastMsg(context, resId);
    }

    /**
     * 提示消息
     * @param resId
     */
    public void showToastMsg( final int resId){
        mHandler.post(new Runnable(){
            @Override
            public void run() {
                //Toast.makeText(BasicActivity.this, resId, Toast.LENGTH_SHORT).show();
                showTips(resId);
            }
        });
    }

    /**
     * 提示消息
     */
    public void showToastMsg(final String msg){
//        mHandler.post(new Runnable(){
//            @Override
//            public void run() {
                //Toast.makeText(BasicActivity.this, msg, Toast.LENGTH_SHORT).show();
                showTips(msg);
//            }
//        });
    }

    private void showTips(int iconResId, int msgResId) {
        TipsToast	tipsToast = TipsToast.makeText(msgResId, TipsToast.LENGTH_SHORT);
        tipsToast.show();
        tipsToast.setIcon(iconResId);
        tipsToast.setText(msgResId);
    }
    private void showTips( int msgResId) {
        TipsToast	tipsToast = TipsToast.makeText(msgResId, TipsToast.LENGTH_SHORT);
        tipsToast.show();
        //tipsToast.setIcon(iconResId);
        tipsToast.setText(msgResId);
    }
    private void showTips( String msgResId) {
        TipsToast	tipsToast = TipsToast.makeText( msgResId, TipsToast.LENGTH_LONG);
        tipsToast.show();
        //tipsToast.setIcon(iconResId);
        tipsToast.setText(msgResId);
    }


    /**
     * 按键监听
     * @return
     * @see Activity#onKeyDown(int, KeyEvent)
     */
    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }

    protected void popActivity(Activity activity){
        activityManager.popActivity(activity);
    }

    protected void closeActivity(){
        activityManager.close();
    }

    public  ActivityManager getActivityManager() {
        if(activityManager==null)
            activityManager = ActivityManager.getInstance();
        return activityManager;
    }

    public void setActivityManager(ActivityManager activityManager) {
        activityManager = activityManager;
    }

    public boolean isActivityOnTop(Class<?> clazz){
        String ps = clazz.getName();
        return isActivityOnTop(ps);
    }

    public  boolean isActivityOnTop(String clazz){
        Activity mActivity = activityManager.peepActivity();

        if(mActivity!=null){
            if((mActivity.getPackageName()+"."+mActivity.getLocalClassName()).equalsIgnoreCase(clazz))
                return true;
        }
        return false;
    }

    public void popActivity(){
        activityManager.popActivity();
    }

    /**
     * activity销毁前触发的方法
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
    }
    /**
     * activity恢复时触发的方法
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }


        }
        try{
            return super.dispatchTouchEvent(ev);
        } catch(IllegalArgumentException ex) {
        }
        return false;
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


	/*//网络广播监听
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Bundle b = intent.getExtras();
			if (MbsConstans.BroadcastReceiverAction.NET_CHECK_ACTION.equals(action)) {
				if(MbsConstans.isNet){
					Toast.makeText(BasicActivity.this,"网络已经打开",Toast.LENGTH_SHORT).show();

				}else{
					Toast.makeText(BasicActivity.this,"网络已经关闭",Toast.LENGTH_SHORT).show();
				}
			}
		}
	};*/


    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }


    /**
     * 获取refreshToken方法
     */
    public void getRefreshToken(){
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.REFRESH_TOKEN, map);
    }
    public void dealFailInfo(Map<String,Object> map,String mType){
        String msg = map.get("errmsg")+"";
        String errcodeStr = map.get("errcode")+"";
        int errorCode = -1;
        try {
            errorCode = Double.valueOf(errcodeStr).intValue();
        }catch (Exception e){
            e.printStackTrace();
            LogUtilDebug.i("打印log日志","这里出现异常了"+e.getMessage());
        }
        if (errorCode == ErrorHandler.REFRESH_TOKEN_DATE_CODE){
            mRequestTagList.add(mType);
            if (!mIsRefreshToken){
                mIsRefreshToken = true;
                LogUtilDebug.i("打印log日志","refreshToken过期重新请求refreshtoken接口");
                getRefreshToken();
            }else {
                LogUtilDebug.i("打印log日志","refreshToken过期重新请求refreshtoken接口，正在请求。不需要再请求了");
            }
        }else if (errorCode == ErrorHandler.ACCESS_TOKEN_DATE_CODE){
            closeAllActivity();
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            showToastMsg(getResources().getString(R.string.toast_login_again));
        }else if(errorCode == ErrorHandler.PHONE_NO_ACTIVE){
            closeAllActivity();
            Intent intent = new Intent(this,ChoosePhoneActivity.class);
            startActivity(intent);
            showToastMsg(getResources().getString(R.string.toast_no_active));
        } else {
            switch (mType){
                case MethodUrl.repayConfig:
                    showBaseMsgDialog(msg,true);
                    break;
                default:
                    showToastMsg(msg);
                    break;
            }
        }
        if (mType.equals(MethodUrl.REFRESH_TOKEN)){
            mIsRefreshToken = false;
        }
    }


    public TipMsgDialog mZhangDialog;
    public void showBaseMsgDialog(Object msg,boolean isClose){
        if (mZhangDialog == null){
            mZhangDialog = new TipMsgDialog(this,true);
            mZhangDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
                        dialog.dismiss();
                        if (isClose){
                            finish();
                        }

                        return true;
                    }
                    else {
                        return false;
                    }
                }
            });
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.cancel:
                            mZhangDialog.dismiss();
                            if (isClose){
                                finish();
                            }
                            break;
                        case R.id.confirm:
                            mZhangDialog.dismiss();
                            break;
                        case R.id.tv_right:
                            mZhangDialog.dismiss();
                            if (isClose){
                                finish();
                            }
                            break;
                    }
                }
            };
            mZhangDialog.setCanceledOnTouchOutside(false);
            mZhangDialog.setCancelable(true);
            mZhangDialog.setOnClickListener(onClickListener);
        }
        mZhangDialog.initValue("温馨提示",msg);
        mZhangDialog.show();

    }

    /**
     * 每次启动activity都会调用此方法
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (checkDoubleClick(intent)) {
            super.startActivityForResult(intent, requestCode, options);

        }
    }
    private String mActivityJumpTag;        //activity跳转tag
    private long mClickTime;                //activity跳转时间

    /**
     * 检查是否重复跳转，不需要则重写方法并返回true
     */
    protected boolean checkDoubleClick(Intent intent) {

        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        }else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        }else {
            return true;
        }

        if (tag.equals(mActivityJumpTag) && mClickTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mClickTime = SystemClock.uptimeMillis();
        return result;
    }


}