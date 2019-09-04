package com.lr.biyou.ui.temporary.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.why168.LoopViewPagerLayout;
import com.github.why168.listener.OnBannerItemClickListener;
import com.github.why168.loader.OnDefaultImageViewLoader;
import com.github.why168.modle.BannerInfo;
import com.github.why168.modle.IndicatorLocation;
import com.github.why168.modle.LoopStyle;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.activity.TestActivity;
import com.lr.biyou.ui.temporary.adapter.GoodsAdapter;
import com.lr.biyou.ui.temporary.adapter.HomeGridViewAdapter;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.mywidget.view.IndexGridView;
import com.lr.biyou.mywidget.view.SampleHeader;
import com.lr.biyou.mywidget.viewpager.BannerViewPager;
import com.lr.biyou.mywidget.viewpager.ViewFactory;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

@SuppressLint("ValidFragment")
public class IndexFragment extends BasicFragment implements View.OnClickListener{

    @BindView(R.id.but_test)
    Button mButTest;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.cart_view)
    ImageView mCartView;
    @BindView(R.id.list_cart_num)
    TextView mListCartNum;
    @BindView(R.id.title_bar_view)
    LinearLayout mTitleBarView;

//    private Button testBut;
//    private View rootView;
//    private LRecyclerView mRecyclerView;
//    private ImageView cart_btn;
//    private TextView mCartNumTv;

    //头部布局文件
    private BannerViewPager mFlBanner;
    private LoopViewPagerLayout mLoopViewPagerLayout;
    private IndexGridView mIndexGridView;

    public boolean isRefresh = false;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private GoodsAdapter mDataAdapter;
    private String[] imageUrls = {"http://m.360buyimg.com/mobilecms/s720x350_jfs/t3058/134/987833711/92043/510bc5da/57c3e41dN557e42e4.jpg!q70.jpg",
            "http://m.360buyimg.com/mobilecms/s720x350_jfs/t3151/236/1017904659/76060/7a50f772/57c41ed5Nd132bb3f.jpg!q70.jpg"};
    // private String[] imageUrls = {"http://pic7.nipic.com/20100525/4796759_105030008376_2.jpg"};
    private List<ImageView> views = new ArrayList<ImageView>();
    private List<String> infos = new ArrayList<String>();

    private int mPage = 1;


    public IndexFragment() {
        // Required empty public constructor
    }

    public IndexFragment(ImageView cart_btn, TextView mCartNumTv) {
        this.mCartView = cart_btn;
        this.mListCartNum = mCartNumTv;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_goods;
    }

    @Override
    public void init() {
        initView();
    }


  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        rootView = inflater.inflate(R.layout.fragment_goods, (ViewGroup) getActivity().findViewById(R.id.food_manager_page), false);
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup p = (ViewGroup) rootView.getParent();
        if (p != null) {
            p.removeAllViewsInLayout();
        }
        return rootView;
    }*/

    private void initView() {

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) getActivity().getResources().getDimension(R.dimen.title_item_height) + UtilTools.getStatusHeight2(getActivity()));
        mTitleBarView.setLayoutParams(layoutParams);
        mTitleBarView.setPadding(0, UtilTools.getStatusHeight2(getActivity()), 0, 0);

        animation_viewGroup = createAnimLayout();
        mCartAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_collect);

        //cart_btn = rootView.findViewById(R.id.cart_view);
        //mCartNumTv = (TextView) rootView.findViewById(R.id.list_cart_num);
        mButTest.setOnClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        mRefreshListView.setLayoutManager(manager);
        mRefreshListView.setLScrollListener(mLScrollListener);
        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;

                requestData();
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
                requestData();
            }
        });

        mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                requestData();
            }
        });
        handler.sendEmptyMessageDelayed(1, 0);
        setBarTextColor();
    }
    public void setBarTextColor(){
        StatusBarUtil.setLightMode(getActivity());
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            list.clear();
            for (int i = 0; i < 10; i++) {
                Map<String, Object> map = new HashMap<>();
                list.add(map);
            }

            responseData();
        }
    };
    List<Map<String, Object>> list = new ArrayList<>();

    private void requestData() {

        handler.sendEmptyMessageDelayed(1, 1000);

    }


    private void responseData() {

        /*if (map == null){
            mPageView.showNetworkError();
            return;
        }else {
            mPageView.showContent();
        }*/
        if (mPage == 1 && mDataAdapter != null) {
            mDataAdapter.clear();
        }


        if (mDataAdapter == null) {
            mDataAdapter = new GoodsAdapter(getActivity());
            mDataAdapter.addAll(list);

            AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));


            mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
            SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            mLRecyclerViewAdapter.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);


            //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);


            mFlBanner = (BannerViewPager) headerView.findViewById(R.id.fl_banner);
            mLoopViewPagerLayout = (LoopViewPagerLayout) headerView.findViewById(R.id.mLoopViewPagerLayout);
            mIndexGridView = headerView.findViewById(R.id.gird_view);
            // RecyclerViewUtils.setHeaderView(mRecyclerView, new SampleHeader(getActivity()));

            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mDataAdapter.getDataList().get(position);
                   /* Intent intent = new Intent(getActivity(), ShowDetailPictrue.class);
                    intent.putExtra("jsonData",item.get("url")+"");
                    startActivity(intent);*/
                }

            });

            initViewPager();
            initViewPager2();

            initGridView();

        } else {
            mDataAdapter.addAll(list);
            mDataAdapter.notifyDataSetChanged();
        }
        //设置底部加载颜色
        mRefreshListView.setFooterViewColor(R.color.colorAccent, R.color.black, android.R.color.white);
        if (list == null || list.size() < 10) {
            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        } else {
            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        }

        mRefreshListView.refreshComplete(10);
        mDataAdapter.notifyDataSetChanged();


        /*mDataAdapter.SetOnSetHolderClickListener(new HolderClickListener(){
            @Override
            public void onHolderClick(Drawable drawable, int[] start_location, Map<String,Object> m) {
                // TODO Auto-generated method stub
                doAnim(drawable,start_location);
                //addCartAction(m);
            }

        });*/
    }

    private void initViewPager() {
        for (int i = 0; i < imageUrls.length; i++) {
            infos.add(imageUrls[i]);
        }
        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(getActivity(), infos.get(infos.size() - 1)));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(getActivity(), infos.get(i)));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory.getImageView(getActivity(), infos.get(0)));
        // 在加载数据前设置是否循环
        mFlBanner.setData(views, infos, new BannerViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {

            }
        });
        //开始轮播
        mFlBanner.setWheel(true);
        // 设置轮播时间，默认3000ms
        mFlBanner.setScrollTime(4000);
        //设置圆点指示图标组居中显示，默认靠右
        mFlBanner.setIndicatorCenter();
    }


    private void initViewPager2() {
        mLoopViewPagerLayout.setLoop_ms(2000);//轮播的速度(毫秒)
        mLoopViewPagerLayout.setLoop_duration(1000);//滑动的速率(毫秒)
        mLoopViewPagerLayout.setLoop_style(LoopStyle.Empty);//轮播的样式-默认empty
        mLoopViewPagerLayout.setIndicatorLocation(IndicatorLocation.Center);//指示器位置-中Center
        // mLoopViewPagerLayout.setNormalBackground(R.drawable.normal_background);//默认指示器颜色
        // mLoopViewPagerLayout.setSelectedBackground(R.drawable.selected_background);//选中指示器颜色

        mLoopViewPagerLayout.initializeData(getActivity());//初始化数据
        ArrayList<BannerInfo> bannerInfos = new ArrayList<>();
        bannerInfos.add(new BannerInfo<String>(imageUrls[0], "第一张图片"));
        bannerInfos.add(new BannerInfo<String>(imageUrls[1], "第二张图片"));
        bannerInfos.add(new BannerInfo<String>(imageUrls[0], "第三张图片"));
        bannerInfos.add(new BannerInfo<String>(imageUrls[1], "第四张图片"));
        bannerInfos.add(new BannerInfo<String>(imageUrls[0], "第五张图片"));
        mLoopViewPagerLayout.setOnLoadImageViewListener(new OnDefaultImageViewLoader() {
            @Override
            public void onLoadImageView(ImageView imageView, Object parameter) {
                Glide.with(getActivity())
                        .load(parameter)
                        .into(imageView);
            }

            @Override
            public ImageView createImageView(Context context) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                return imageView;
            }
        });//设置图片加载&自定义图片监听
        mLoopViewPagerLayout.setOnBannerItemClickListener(new OnBannerItemClickListener() {
            @Override
            public void onBannerClick(int index, ArrayList<BannerInfo> banner) {

            }
        });//设置监听
        mLoopViewPagerLayout.setLoopData(bannerInfos);//设置数据

    }

    private void initGridView() {

        List<Map<String, Object>> gridList = new ArrayList<>();
        Map<String, Object> gridMap = new HashMap<>();
        gridMap.put("url", "http://m.360buyimg.com/mobilecms/s80x80_jfs/t2941/340/2547425887/3566/4dc2eaf4/57b3b3e5N53d03f9f.png");
        gridMap.put("name", "购物");
        gridMap.put("code", "0001");
        gridList.add(gridMap);

        gridMap = new HashMap<>();
        gridMap.put("url", "http://m.360buyimg.com/mobilecms/s80x80_jfs/t2941/340/2547425887/3566/4dc2eaf4/57b3b3e5N53d03f9f.png");
        gridMap.put("name", "教育");
        gridMap.put("code", "0002");
        gridList.add(gridMap);

        gridMap = new HashMap<>();
        gridMap.put("url", "http://m.360buyimg.com/mobilecms/s80x80_jfs/t2941/340/2547425887/3566/4dc2eaf4/57b3b3e5N53d03f9f.png");
        gridMap.put("name", "救援");
        gridMap.put("code", "0003");
        gridList.add(gridMap);

        gridMap = new HashMap<>();
        gridMap.put("url", "http://m.360buyimg.com/mobilecms/s80x80_jfs/t2941/340/2547425887/3566/4dc2eaf4/57b3b3e5N53d03f9f.png");
        gridMap.put("name", "智能家具");
        gridMap.put("code", "0004");
        gridList.add(gridMap);

        gridMap = new HashMap<>();
        gridMap.put("url", "http://m.360buyimg.com/mobilecms/s80x80_jfs/t2941/340/2547425887/3566/4dc2eaf4/57b3b3e5N53d03f9f.png");
        gridMap.put("name", "便民服务");
        gridMap.put("code", "0005");
        gridList.add(gridMap);

        gridMap = new HashMap<>();
        gridMap.put("url", "http://m.360buyimg.com/mobilecms/s80x80_jfs/t2941/340/2547425887/3566/4dc2eaf4/57b3b3e5N53d03f9f.png");
        gridMap.put("name", "理财");
        gridMap.put("code", "0006");
        gridList.add(gridMap);

        gridMap = new HashMap<>();
        gridMap.put("url", "http://m.360buyimg.com/mobilecms/s80x80_jfs/t2941/340/2547425887/3566/4dc2eaf4/57b3b3e5N53d03f9f.png");
        gridMap.put("name", "微创业");
        gridMap.put("code", "0007");
        gridList.add(gridMap);

        gridMap = new HashMap<>();
        gridMap.put("url", "http://m.360buyimg.com/mobilecms/s80x80_jfs/t2941/340/2547425887/3566/4dc2eaf4/57b3b3e5N53d03f9f.png");
        gridMap.put("name", "慈善");
        gridMap.put("code", "0008");
        gridList.add(gridMap);

        gridMap = new HashMap<>();
        gridMap.put("url", "http://m.360buyimg.com/mobilecms/s80x80_jfs/t2941/340/2547425887/3566/4dc2eaf4/57b3b3e5N53d03f9f.png");
        gridMap.put("name", "吃喝玩乐");
        gridMap.put("code", "0009");
        gridList.add(gridMap);

        gridMap = new HashMap<>();
        gridMap.put("url", "http://m.360buyimg.com/mobilecms/s80x80_jfs/t2941/340/2547425887/3566/4dc2eaf4/57b3b3e5N53d03f9f.png");
        gridMap.put("name", "医疗");
        gridMap.put("code", "0010");
        gridList.add(gridMap);

        HomeGridViewAdapter gridViewAdapter = new HomeGridViewAdapter(getActivity(), gridList);
        mIndexGridView.setAdapter(gridViewAdapter);
        mIndexGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, Object> map = (Map<String, Object>) adapterView.getAdapter().getItem(i);
            }
        });

    }


    private LRecyclerView.LScrollListener mLScrollListener = new LRecyclerView.LScrollListener() {
        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onScrolled(int distanceX, int distanceY) {

        }

        @Override
        public void onScrollStateChanged(int state) {

        }
    };


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), TestActivity.class);
        startActivity(intent);
    }


    private Animation mCartAnim;
    //动画时间
    private int AnimationDuration = 1000;
    //正在执行的动画数量
    private int number = 0;
    //是否完成清理
    private boolean isClean = false;
    private FrameLayout animation_viewGroup;
    private Handler animHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //用来清除动画后留下的垃圾
                    try {
                        animation_viewGroup.removeAllViews();
                    } catch (Exception e) {

                    }

                    isClean = false;

                    break;
                default:
                    break;
            }
        }
    };

    private void doAnim(Drawable drawable, int[] start_location) {
        if (!isClean) {
            setAnim(drawable, start_location);
        } else {
            try {
                animation_viewGroup.removeAllViews();
                isClean = false;
                setAnim(drawable, start_location);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isClean = true;
            }
        }
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private FrameLayout createAnimLayout() {
        ViewGroup rootView = (ViewGroup) getActivity().getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(getActivity());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;

    }

    /**
     * @param vg       动画运行的层 这里是frameLayout
     * @param view     要运行动画的View
     * @param location 动画的起始位置
     * @return
     * @deprecated 将要执行动画的view 添加到动画层
     */
    private View addViewToAnimLayout(ViewGroup vg, View view, int[] location) {
        int x = location[0];
        int y = location[1];
        vg.addView(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                dip2px(getActivity(), 60), dip2px(getActivity(), 60));
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setPadding(5, 5, 5, 5);
        view.setLayoutParams(lp);

        return view;
    }

    /**
     * dip，dp转化成px 用来处理不同分辨路的屏幕
     *
     * @param context
     * @param dpValue
     * @return
     */
    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 动画效果设置
     *
     * @param drawable       将要加入购物车的商品
     * @param start_location 起始位置
     */
    private void setAnim(Drawable drawable, int[] start_location) {
        final ImageView iview = new ImageView(getActivity());
        iview.setImageDrawable(drawable);
        final View view = this.addViewToAnimLayout(animation_viewGroup, iview, start_location);
        view.setAlpha(0.6f);

        int[] end_location = new int[2];// 这是用来存储动画结束位置的X、Y坐标
        Random ra = new Random();
        mCartView.getLocationInWindow(end_location);
        // 计算位移
        int endX = 0 - start_location[0]
                + mCartView.getLeft();// 动画位移的X坐标
        int endY = end_location[1] - start_location[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        //translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0,
                0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        // translateAnimationX.setFillAfter(true);

        Animation mRotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        Animation mScaleAnimation = new ScaleAnimation(1.0f, 0.3f, 1.0f, 0.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // mScaleAnimation.setFillAfter(true);


        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(mRotateAnimation);
        set.addAnimation(mScaleAnimation);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(AnimationDuration);// 动画的执行时间


       /* view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }
        });*/


        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                number++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub

                number--;
                if (number == 0) {
                    isClean = true;
                    animHandler.sendEmptyMessage(0);
                }
                view.setVisibility(View.GONE);
                mListCartNum.startAnimation(mCartAnim);
                mCartView.startAnimation(mCartAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

        });
        view.startAnimation(set);

    }

    /**
     * 内存过低时及时处理动画产生的未处理冗余
     */
    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        isClean = true;
        try {
            animation_viewGroup.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isClean = false;
        super.onLowMemory();
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
