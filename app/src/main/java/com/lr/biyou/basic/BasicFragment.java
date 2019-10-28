package com.lr.biyou.basic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.lr.biyou.R;
import com.lr.biyou.api.ErrorHandler;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.di.component.DaggerPersenerComponent;
import com.lr.biyou.di.module.PersenerModule;
import com.lr.biyou.manage.ActivityManager;
import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.TipsToast;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.temporary.activity.ChoosePhoneActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BasicFragment extends Fragment implements RequestView {

	protected LayoutInflater mInflater;

	public View mRootView;

	public Unbinder mUnbinder;

	@Inject
	public RequestPresenterImp mRequestPresenterImp;

	private ActivityManager mActivityManager;

	public boolean mIsRefreshToken = false;
	public List<String> mRequestTagList = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		mActivityManager = ActivityManager.getInstance();

		this.mInflater = inflater;
		if(mRootView!=null){
			ViewGroup parent= (ViewGroup) mRootView.getParent();
			if(parent!=null){
				parent.removeView(mRootView);
			}
			mUnbinder =ButterKnife.bind(this,mRootView);
			return mRootView;
		}

		mRootView = inflater.inflate(getLayoutId(), container, false);
		mUnbinder =ButterKnife.bind(this,mRootView);

		DaggerPersenerComponent.builder().persenerModule(new PersenerModule(this,getActivity())).build().injectTo(this);

		init();

		return mRootView;
	}
	/*@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		this.mInflater = inflater;
		mRootView = inflater.inflate(getLayoutId(), container, false);
		mUnbinder =ButterKnife.bind(this,mRootView);
		init();
		return mRootView;
	}*/
	public abstract int getLayoutId();

	public abstract void init();

	/**
	 * 获取refreshToken方法
	 */
	public void getRefreshToken(){
		/*Map<String, Object> map = new HashMap<>();
		map.put("access_token", MbsConstans.ACCESS_TOKEN);
		Map<String, String> mHeaderMap = new HashMap<String, String>();
		mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.REFRESH_TOKEN, map);*/
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


	private void showTips( String msgResId) {
		TipsToast	tipsToast = TipsToast.makeText( msgResId, TipsToast.LENGTH_LONG);
		tipsToast.show();
		//tipsToast.setIcon(iconResId);
		tipsToast.setText(msgResId);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mUnbinder.unbind();
	}


	public void dealFailInfo(Map<String,Object> map,String mType){
		String msg = map.get("errmsg") + "";
		String errcodeStr = map.get("errcode") + "";
		int errorCode = -1;
		try {
			errorCode = Double.valueOf(errcodeStr).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			LogUtilDebug.i("打印log日志","这里出现异常了" + e.getMessage());
		}
		if (errorCode == ErrorHandler.REFRESH_TOKEN_DATE_CODE) {
			mRequestTagList.add(mType);
			System.out.println(mType+"###########################################################"+mRequestTagList);
			if (!mIsRefreshToken){
				mIsRefreshToken = true;
				LogUtilDebug.i("打印log日志","refreshToken过期重新请求refreshtoken接口");
				getRefreshToken();
			}else {
				LogUtilDebug.i("打印log日志","refreshToken过期重新请求refreshtoken接口，正在请求。不需要再请求了");

			}
		} else if (errorCode == ErrorHandler.ACCESS_TOKEN_DATE_CODE) {
			getActivity().finish();
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
			TipsToast.showToastMsg(getResources().getString(R.string.toast_login_again));
		} else if (errorCode == ErrorHandler.PHONE_NO_ACTIVE){//账号未激活
			mActivityManager.close();
			Intent intent = new Intent(getActivity(), ChoosePhoneActivity.class);
			startActivity(intent);
			TipsToast.showToastMsg(getResources().getString(R.string.toast_no_active));
		} else {
			TipsToast.showToastMsg(msg);
		}

		if (mType.equals(MethodUrl.REFRESH_TOKEN)){
			mIsRefreshToken = false;
		}

	}


}