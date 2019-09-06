package com.lr.biyou.ui.moudle2.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.flyco.dialog.utils.CornerUtils;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.DateSelectDialog;
import com.lr.biyou.ui.moudle.activity.IdCardEditActivity;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle1.activity.NoticeListActivity;
import com.lr.biyou.ui.moudle2.activity.AddFriendActivity;
import com.lr.biyou.ui.moudle2.activity.ChatNoticeListActivity;
import com.lr.biyou.ui.moudle4.adapter.MyViewPagerAdapter;
import com.lr.biyou.ui.moudle4.fragment.BBTradeFragment;
import com.lr.biyou.ui.moudle4.fragment.FBTradeFragment;
import com.lr.biyou.ui.temporary.adapter.TradeDialogAdapter;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.UserInfo;

@SuppressLint("ValidFragment")
public class ChatViewFragment extends BasicFragment implements RequestView {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_text_tv)
    TextView rightTextTv;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tab_layout)
    XTabLayout tabLayout;
    @BindView(R.id.notice_iv)
    ImageView noticeIv;
    @BindView(R.id.notice_title_tv)
    TextView noticeTitleTv;
    @BindView(R.id.notice_number_tv)
    TextView noticeNumberTv;
    @BindView(R.id.notice_layout)
    LinearLayout noticeLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    private boolean mIsback = false;

    private String mRequestTag = "";

    private List<Fragment> mFragments=new ArrayList<>();

    private AnimUtil mAnimUtil;

    public ChatViewFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_view;
    }

    @Override
    public void init() {
        setBarTextColor();
        //设置用户信息

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                return null;
            }
        },true);
        initView();
        mAnimUtil = new AnimUtil();

    }


    private void initView() {


        tabLayout.addTab(tabLayout.newTab().setText("近期聊天"));
        tabLayout.addTab(tabLayout.newTab().setText("我的好友"));
        tabLayout.addTab(tabLayout.newTab().setText("我的群组"));

        ConversationListFragment conListFragment=new ConversationListFragment();
        mFragments.add(conListFragment);



        vp.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(),mFragments));
        vp.addOnPageChangeListener(new XTabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });

    }

    public void setBarTextColor() {
        StatusBarUtil.setLightMode(getActivity());
    }





    @OnClick({R.id.right_img, R.id.right_lay,R.id.notice_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_lay:
                initPopupWindow();
                break;
            case R.id.right_img:
                initPopupWindow();
                //启动聊天
               /* if (RongIM.getInstance() != null){
                    RongIM.getInstance().startPrivateChat(getActivity(),"15561400223","渣女");
                }*/

                break;
            case R.id.notice_layout:
                Intent intent = new Intent(getActivity(), ChatNoticeListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType){
            case MethodUrl.CHAT_QRCODE:

                switch (tData.get("code")+""){
                    case "0": //请求成功

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        if (getActivity() != null){
                            getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }
    private View popView;
    private PopupWindow mConditionDialog;
    private boolean bright = false;

    private void initPopupWindow(){

        int nH = UtilTools.getNavigationBarHeight(getActivity());
        LinearLayout mNagView ;

        if (mConditionDialog == null) {

            popView = LayoutInflater.from(getActivity()).inflate(R.layout.chat_add_dialog,null);
            mConditionDialog = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mConditionDialog.setClippingEnabled(false);
            initConditionDialog(popView);


            //int screenWidth=UtilTools.getScreenWidth(getActivity());
            //int screenHeight=UtilTools.getScreenHeight(getActivity());
            mConditionDialog.setWidth(UtilTools.dip2px(getActivity(),130));
            mConditionDialog.setHeight(UtilTools.dip2px(getActivity(),150));

            //设置background后在外点击才会消失
            mConditionDialog.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), UtilTools.dip2px(getActivity(),5)));
            //mConditionDialog.setOutsideTouchable(true);// 设置可允许在外点击消失
            //自定义动画
            //mConditionDialog.setAnimationStyle(R.style.PopupAnimation);
            mConditionDialog.setAnimationStyle(android.R.style.Animation_Activity);//使用系统动画
            mConditionDialog.update();
            mConditionDialog.setTouchable(true);
            mConditionDialog.setFocusable(true);
            //popView.requestFocus();//pop设置不setBackgroundDrawable情况，把焦点给popView，添加popView.setOnKeyListener。可实现点击外部不消失，点击反键才消失
            //			mConditionDialog.showAtLocation(mCityTv, Gravity.TOP|Gravity.RIGHT, 0, 0); //设置layout在PopupWindow中显示的位置
            //mConditionDialog.showAtLocation(getActivity().getWindow().getDecorView(),  Gravity.TOP|Gravity.RIGHT, 0, 0);
            mConditionDialog.showAsDropDown(divideLine,-UtilTools.dip2px(getActivity(),20),0,Gravity.RIGHT);
            toggleBright();
            mConditionDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    toggleBright();
                }
            });
        }else {

            //mConditionDialog.showAtLocation(getActivity().getWindow().getDecorView(),  Gravity.TOP|Gravity.RIGHT, 0, 0);
            mConditionDialog.showAsDropDown(divideLine,-UtilTools.dip2px(getActivity(),20),0,Gravity.RIGHT);
            toggleBright();
        }
    }
    private void toggleBright() {
        //三个参数分别为： 起始值 结束值 时长 那么整个动画回调过来的值就是从0.5f--1f的
        mAnimUtil.setValueAnimator(0.7f, 1f, 300);
        mAnimUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                //此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                float bgAlpha = bright ? progress : (1.7f - progress);//三目运算，应该挺好懂的。
                //bgAlpha = progress;//三目运算，应该挺好懂的。
                bgAlpha(bgAlpha);//在此处改变背景，这样就不用通过Handler去刷新了。
            }
        });
        mAnimUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                //在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        mAnimUtil.startAnimator();
    }

    private void bgAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;// 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }


    LinearLayout group_chat_lay;
    LinearLayout add_friend_lay;
    LinearLayout sacn_lay;

    private void initConditionDialog(View view){
        group_chat_lay = view.findViewById(R.id.group_chat_lay);
        add_friend_lay = view.findViewById(R.id.add_friend_lay);
        sacn_lay = view.findViewById(R.id.scan_lay);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()){
                    case R.id.group_chat_lay:
                        mConditionDialog.dismiss();

                        break;
                    case R.id.add_friend_lay:
                        mConditionDialog.dismiss();
                        intent = new Intent(getActivity(), AddFriendActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.scan_lay:
                        mConditionDialog.dismiss();

                        break;
                }
            }
        };

        group_chat_lay.setOnClickListener(onClickListener);
        add_friend_lay.setOnClickListener(onClickListener);
        sacn_lay.setOnClickListener(onClickListener);
    }



}
