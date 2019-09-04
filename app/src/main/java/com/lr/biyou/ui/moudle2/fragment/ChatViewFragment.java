package com.lr.biyou.ui.moudle2.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle4.adapter.MyViewPagerAdapter;
import com.lr.biyou.ui.moudle4.fragment.BBTradeFragment;
import com.lr.biyou.ui.moudle4.fragment.FBTradeFragment;
import com.lr.biyou.utils.tool.LogUtilDebug;

import java.util.ArrayList;
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


    private List<Fragment> mFragments=new ArrayList<>();

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


    @OnClick({R.id.right_img, R.id.notice_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_img:
                //启动聊天
                if (RongIM.getInstance() != null){
                    RongIM.getInstance().startPrivateChat(getActivity(),"15561400223","渣女");
                }

                break;
            case R.id.notice_layout:
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

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }


}
