package com.lr.biyou.ui.moudle5.activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyco.dialog.utils.CornerUtils;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.CallBackTotal;
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.DateSelectDialog;
import com.lr.biyou.mywidget.dialog.TipsDialog;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle5.adapter.AddressManageAdapter;
import com.lr.biyou.ui.moudle5.adapter.SwipeMenuAdapter;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

/**
 * 提币地址
 */
public class TibiAddressActivity extends BasicActivity implements RequestView, ReLoadingData, SelectBackListener, CallBackTotal {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.iv_shaixuan)
    ImageView mIvShaixuan;
    @BindView(R.id.search_result_list_view)
    LRecyclerView mLRecyclerView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.tv_kehu_mount)
    TextView mKehuMountTv;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.shuaixuan_lay)
    LinearLayout mShuaiXuanLay;
    @BindView(R.id.add_address_tv)
    TextView addAddressTv;


    private LRecyclerViewAdapter mAdapter;
    private AddressManageAdapter mAddressAdapter;
    private String mRequestTag = "";
    private List<Map<String, Object>> mDataList = new ArrayList<>();


    private Map<String, Object> mDeleteMap;

    private AnimUtil mAnimUtil;

    @Override
    public int getContentView() {
        return R.layout.activity_tibi_address;
    }

    @Override
    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText("提币地址");
        mTitleText.setCompoundDrawables(null, null, null, null);
        mRightImg.setVisibility(View.GONE);
        //mRightImg.setImageResource(R.drawable.add);

        mAnimUtil = new AnimUtil();


        mPageView.setContentView(mContent);
        mPageView.showEmpty();
        mPageView.setReLoadingData(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mLRecyclerView.setLayoutManager(manager);
        mLRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //请求客户列表数据
                mEtSearch.setText("");
                addressListAction();
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.toString().length()>0 && mAddressAdapter!=null){
//                    mAddressAdapter.setBackTotal(KeHuManageActivity.this);
//                    mAddressAdapter.getFilter().filter(s.toString());
//                }else {
//                    if (mDataList != null && mDataList.size() > 0) {
//                        responseData();
//                    }
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addressListAction();
        showProgressDialog();

    }




    private void addressListAction() {
        mRequestTag = MethodUrl.ADDRESS_LIST;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(TibiAddressActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.ADDRESS_LIST, map);
    }

    private void kehuDeleteAction() {
        mRequestTag = MethodUrl.ADDRESS_DELETE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(TibiAddressActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id",mDeleteMap.get("id")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.ADDRESS_DELETE, map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addressListAction();
        showProgressDialog();
    }



    private void responseData() {

        if (mAddressAdapter == null) {
            mAddressAdapter = new AddressManageAdapter(this);
            mAddressAdapter.addAll(mDataList);
            mAddressAdapter.setOnSwipeListener(new SwipeMenuAdapter.onSwipeListener() {
                @Override
                public void onDel(int pos) {
                    //删除

                /*//RecyclerView关于notifyItemRemoved的那点小事 参考：http://blog.csdn.net/jdsjlzx/article/details/52131528
                mSwipeMenuAdapter.getDataList().remove(pos);
                mSwipeMenuAdapter.notifyItemRemoved(pos);//推荐用这个
                if(pos != (mSwipeMenuAdapter.getDataList().size())){ // 如果移除的是最后一个，忽略 注意：这里的mDataAdapter.getDataList()不需要-1，因为上面已经-1了
                    mSwipeMenuAdapter.notifyItemRangeChanged(pos, mSwipeMenuAdapter.getDataList().size() - pos);
                }
                mSwipeMenuAdapter.notifyDataSetChanged();*/

                    mDataList.remove(pos);
                    responseData();

                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();

                }

                @Override
                public void onTop(int pos) {
                    //置顶
                }

                @Override
                public void onViewClick(Map<String, Object> myMap, int type) {
                    mDeleteMap = myMap;
                    showDeleteDialog();
                }
            });

            AnimationAdapter adapter = new SlideInBottomAnimationAdapter(mAddressAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(300);
            adapter.setInterpolator(new OvershootInterpolator(1f));

            mAdapter = new LRecyclerViewAdapter(mAddressAdapter);
            mLRecyclerView.setAdapter(mAdapter);
            mLRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mLRecyclerView.setHasFixedSize(true);
            mLRecyclerView.setNestedScrollingEnabled(false);
            mLRecyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mLRecyclerView.setPullRefreshEnabled(true);
            mLRecyclerView.setLoadMoreEnabled(false);

            mLRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mLRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);


            DividerDecoration divider2 = new DividerDecoration.Builder(this)
                    .setHeight(R.dimen.divide_hight)
                    .setPadding(R.dimen.dp_10)
                    .setColorResource(R.color.divide_line)
                    .build();
            mLRecyclerView.addItemDecoration(divider2);

        } else {
            mAddressAdapter.clear();
            mAddressAdapter.addAll(mDataList);
            mAddressAdapter.notifyDataSetChanged();
            mAdapter.notifyDataSetChanged();
        }

        mAddressAdapter.setSoorceList(mAddressAdapter.getDataList());
        //添加尾部布局
        mLRecyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mLRecyclerView.setNoMore(true);
        } else {
            mLRecyclerView.setNoMore(false);
        }

        if (mAddressAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

        mAddressAdapter.setmClickListener(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                Intent intent = new Intent();
                intent.putExtra("DATA", (Serializable) mParentMap);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private TipsDialog mTipsDialog;

    private void showDeleteDialog() {
        if (mTipsDialog == null) {
            mTipsDialog = new TipsDialog(this, "确定要删除吗？");
            mTipsDialog.setClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.bt_cancel:
                            mTipsDialog.dismiss();
                            break;
                        case R.id.bt_sure:
                            kehuDeleteAction();
                            //滑动删除
                            //showProgressDialog();
                            mTipsDialog.dismiss();
                            break;
                    }
                }
            });

            mTipsDialog.setCanceledOnTouchOutside(false);
            mTipsDialog.setCancelable(true);
        }
        mTipsDialog.show();

    }


    @Override
    public void showProgress() {
        //showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType) {
            case MethodUrl.ADDRESS_DELETE:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        showToastMsg(tData.get("msg") + "");
                        addressListAction();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        mPageView.showNetworkError();
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(TibiAddressActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case MethodUrl.ADDRESS_LIST:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (UtilTools.empty(tData.get("data") + "")) {
                            mPageView.showEmpty();
                        } else {
                            mDataList = (List<Map<String, Object>>) tData.get("data");
                            if (!UtilTools.empty(mDataList)) {
                                mPageView.showContent();
                                responseData();
                                mLRecyclerView.refreshComplete(10);

                            } else {
                                mPageView.showEmpty();
                            }
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        mPageView.showNetworkError();
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(TibiAddressActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;

        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        dealFailInfo(map, mType);
    }

    @OnClick({R.id.add_address_tv,R.id.back_img, R.id.right_img, R.id.iv_search, R.id.et_search, R.id.iv_shaixuan, R.id.left_back_lay, R.id.shuaixuan_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.shuaixuan_lay:
                showDialog();
                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.right_img: //添加地址
//                intent=new Intent(this,AddKeHuActivity.class);
//                intent.putExtra("TYPE",0);
//                startActivity(intent);
                break;
            case R.id.iv_search:
                break;
            case R.id.et_search:
                break;
            case R.id.iv_shaixuan://筛选
                showDialog();
                break;
            case R.id.add_address_tv:
                intent=new Intent(this,AddAddressActivity.class);
                startActivity(intent);
                break;
        }
    }

    private String mStartTime = "";
    private String mEndTime = "";
    private String mSearchStr = "";

    private String mSelectStartTime = "";
    private String mSelectEndTime = "";

    private LinearLayout mLayout;
    private TextView mOneTv;
    private TextView mThreeTv;
    private TextView mSetTimeTv;
    private TextView mStartTimeTv;
    private TextView mEndTimeTv;
    private EditText mEditText;
    private LinearLayout mTimeSelectLay;
    private Button mResetBut;
    private Button mSureBut;

    private DateSelectDialog mySelectDialog;
    private DateSelectDialog mySelectDialog2;

    private View popView;
    private PopupWindow mConditionDialog;
    private boolean bright = false;

    public void showDialog() {

        if (mConditionDialog == null) {
            mySelectDialog = new DateSelectDialog(this, true, "选择日期", 21);
            mySelectDialog.setSelectBackListener(this);
            mySelectDialog2 = new DateSelectDialog(this, true, "选择日期", 22);
            mySelectDialog2.setSelectBackListener(this);


            popView = LayoutInflater.from(this).inflate(R.layout.dialog_trade_condition, null);
            mConditionDialog = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //initConditionDialog(popView);

            int screenWidth = UtilTools.getScreenWidth(TibiAddressActivity.this);
            mConditionDialog.setWidth((int) (screenWidth * 0.8));
            mConditionDialog.setHeight(WindowManager.LayoutParams.MATCH_PARENT);

            mConditionDialog.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), UtilTools.dip2px(TibiAddressActivity.this, 5)));

            mConditionDialog.setAnimationStyle(R.style.PopupAnimation);

            mConditionDialog.update();
            mConditionDialog.setTouchable(true);
            mConditionDialog.setFocusable(true);
            //popView.requestFocus();//pop设置不setBackgroundDrawable情况，把焦点给popView，添加popView.setOnKeyListener。可实现点击外部不消失，点击反键才消失
            //			mConditionDialog.showAtLocation(mCityTv, Gravity.TOP|Gravity.RIGHT, 0, 0); //设置layout在PopupWindow中显示的位置
            mConditionDialog.showAtLocation(TibiAddressActivity.this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, 0, 0);
            toggleBright();
            mConditionDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    toggleBright();
                }
            });
        } else {
            mConditionDialog.showAtLocation(TibiAddressActivity.this.getWindow().getDecorView(), Gravity.TOP | Gravity.RIGHT, 0, 0);
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
        WindowManager.LayoutParams lp = TibiAddressActivity.this.getWindow().getAttributes();
        lp.alpha = alpha;// 0.0-1.0
        TibiAddressActivity.this.getWindow().setAttributes(lp);
    }



    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 21:
                //mSelectStartTime = map.get("date")+"";
                mSelectStartTime = map.get("year") + "" + map.get("month") + "01";
                mStartTimeTv.setText(UtilTools.getStringFromSting2(mSelectStartTime, "yyyyMMdd", "yyyy-MM"));
                break;
            case 22:
                //mSelectEndTime = map.get("date")+"";
                mSelectEndTime = UtilTools.getLastDayOfMonth(Integer.parseInt(map.get("year") + ""), Integer.parseInt(map.get("month") + ""));
                mEndTimeTv.setText(UtilTools.getStringFromSting2(mSelectEndTime, "yyyyMMdd", "yyyy-MM"));
                break;
        }
    }

    @Override
    public void reLoadingData() {

        mEtSearch.setText("");
        //本地搜索过滤为空时，点击重新加载，从本地加载
        if (mDataList != null && mDataList.size() > 0) {
            //responseData();
        } else {
            //网络条件查询搜索为空时，点击重新加载，从网络加载
            addressListAction();
            showProgressDialog();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void setTotal(int size) {
        mKehuMountTv.setText("共" + size + "个客户");
        if (size == 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }
    }


}
