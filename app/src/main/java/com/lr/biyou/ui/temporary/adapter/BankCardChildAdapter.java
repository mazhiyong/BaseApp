package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.palette.graphics.Palette;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.activity.BankQianyueActivity;
import com.lr.biyou.ui.temporary.activity.SelectBankListActivity;
import com.lr.biyou.mywidget.view.TipsToast;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.lr.biyou.mywidget.view.ScaleTransformer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class BankCardChildAdapter extends RecyclerView.Adapter<BankCardChildAdapter.MyHolder> {


    private BankCardAdapter.OnChangeBankCardListener mOnChangeBankCardListener;

    private Context mContext;
    private List<Map<String, Object>> mDatas;
    private int mScreenW;
    private int mType = 0;

    private final int ITEM_TYPE_NORMAL = 0;
    private final int ITEM_TYPE_HEADER = 1;
    private final  int ITEM_TYPE_FOOTER = 2;
    private final int ITEM_TYPE_EMPTY = 3;
    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;

    public String getPatncode() {
        return mPatncode;
    }

    public void setPatncode(String patncode) {
        mPatncode = patncode;
    }

    private String mPatncode = "";

    private int mParentPosition;

    private LayoutInflater mLayoutInflater;

    public BankCardChildAdapter(Context context, List<Map<String, Object>> mDatas, int mType,
                                BankCardAdapter.OnChangeBankCardListener mOnChangeBankCardListener,int position) {
        super();
        this.mContext = context;
        this.mDatas = mDatas;
        this.mType = mType;
        this.mOnChangeBankCardListener = mOnChangeBankCardListener;
        this.mParentPosition = position;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        if (null != mEmptyView && itemCount == 0) {
            itemCount++;
        }
        if (null != mHeaderView) {
            itemCount++;
        }
        if (null != mFooterView) {
            itemCount++;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (null != mHeaderView && position == 0) {
            return ITEM_TYPE_HEADER;
        }
        if (null != mFooterView
                && position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        }
        if (null != mEmptyView && mDatas.size() == 0){
            return ITEM_TYPE_EMPTY;
        }
        return ITEM_TYPE_NORMAL;
    }

    public void addHeaderView(View view) {
        mHeaderView = view;
        notifyItemInserted(0);
    }

    public void addFooterView(View view) {
        mFooterView = view;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
        notifyDataSetChanged();
    }



    @Override
    // 填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final MyHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == ITEM_TYPE_HEADER) {
            holder.mBankBindTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("patncode",mPatncode);
                    if (mOnChangeBankCardListener != null){
                        mOnChangeBankCardListener.onButClickListener("1",map,BankCardChildAdapter.this);
                    }
                }
            });
        }else if (type == ITEM_TYPE_FOOTER){

        }else if (type == ITEM_TYPE_EMPTY){

        }else {
            final Map<String, Object> map = mDatas.get(position);

            RequestOptions options = new RequestOptions()
                    .placeholder(R.color.body_bg) //占位图
                    .error(R.color.body_bg);       //错误图

            holder.mViewPager.setId(mParentPosition+1000);
            String showViewPager = map.get("bindShow")+"";
            if (showViewPager.equals("1")){
                holder.mViewPagerLay.setVisibility(View.VISIBLE);


                List<View> mViews = new ArrayList<View>();
                List<Map<String,Object>> mBindCardList ;
                if (map.get("bindCard") != null){
                    mBindCardList = (ArrayList<Map<String,Object>>)map.get("bindCard");
                    if (mBindCardList == null){
                        mBindCardList = new ArrayList<>();
                    }
                }else {
                    mBindCardList = new ArrayList<>();
                }
                for (int i = 0; i < mBindCardList.size(); i++) {
                    Map<String,Object> bindMap = mBindCardList.get(i);
                    //View view=LayoutInflater.from(mContext).inflate(R.layout.item_pay_plan, viewHolder.mViewPager,false);
                    View view=LayoutInflater.from(mContext).inflate(R.layout.item_card_viewpager, null);
                    CircleImageView mBindBankImgView = view.findViewById(R.id.image_view2);
                    TextView mBindBankNameTv = view.findViewById(R.id.bank_name_tv2);
                    TextView mBindCardNumTv = view.findViewById(R.id.bank_card_value_tv2);
                    TextView mBindCardTypeTv = view.findViewById(R.id.bank_type_tv2);
                    CardView mBindCardLay = view.findViewById(R.id.bank_card_lay2);
                    mBindCardLay.getBackground().setAlpha((int)(0.8*255));

                    String bindBankNum = bindMap.get("acctNo")+"";
                    String s = UtilTools.getShowBankIdCard(bindBankNum);

                    mBindBankNameTv.setText(bindMap.get("othrBnkNm")+"");
                    mBindCardNumTv.setText(s);

                    Glide.with(mContext)
                            .load(bindMap.get("logopath")+"")
                            .apply(options)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void  onLoadFailed(@Nullable Drawable errorDrawable){
                                    mBindBankImgView.setImageDrawable(errorDrawable);
                                }

                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    BitmapDrawable bd = (BitmapDrawable) resource;
                                    Bitmap bm = bd.getBitmap();
                                    Palette.from(bm).maximumColorCount(10).generate(new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(@NonNull Palette palette) {
                                            List<Palette.Swatch> list = palette.getSwatches();
                                            int colorSize = 0;
                                            Palette.Swatch maxSwatch = null;
                                            for (int i = 0; i < list.size(); i++) {
                                                Palette.Swatch swatch = list.get(i);
                                                if (swatch != null) {
                                                    int population = swatch.getPopulation();
                                                    if (colorSize < population) {
                                                        colorSize = population;
                                                        maxSwatch = swatch;
                                                    }
                                                }
                                            }
                                            if (maxSwatch != null){
                                                mBindCardLay.setCardBackgroundColor(maxSwatch.getRgb());
                                                mBindCardLay.getBackground().setAlpha((int)(0.8*255));

                                            }
                                        }
                                    });
                                    mBindBankImgView.setImageBitmap(bm);
                                }
                            });
                    mViews.add(view);
                }
                CardViewPagerAdapter adapter = new CardViewPagerAdapter(mContext,mViews);
                holder.mViewPager.setAdapter(adapter);
                holder.mViewPager.setPageMargin(UtilTools.dip2px(mContext,8));
                holder.mViewPager.setOffscreenPageLimit(3);
                holder.mViewPager.setCurrentItem(0);
                holder.mViewPager.setPageTransformer(false, new ScaleTransformer(mContext));
                //holder.mViewPager.setPageTransformer(false, new ScaleTransformer(this));

                holder.mViewPagerLay.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return  holder.mViewPager.dispatchTouchEvent(event);
                    }
                });


            }else {
                holder.mViewPagerLay.setVisibility(View.GONE);
            }

            holder.mContentLay.getBackground().setAlpha((int)(0.8*255));

            map.put("indexPos",mParentPosition);
            String wxStatus = map.get("secstatus")+"";
            if (UtilTools.empty(map.get("bankname") + "")){
                holder.mBankNameTv.setText("暂无相关信息");
            }else {
                holder.mBankNameTv.setText(map.get("bankname") + "");
            }

            holder.mBankCardValueTv.setText(UtilTools.getIDXing(map.get("accid") + "") );
            //GlideUtils.loadImage(mContext,map.get("logopath")+"",holder.mCircleImageView);

            Glide.with(mContext)
                    .load(map.get("logopath")+"")
                    .apply(options)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void  onLoadFailed(@Nullable Drawable errorDrawable){
                            holder.mCircleImageView.setImageDrawable(errorDrawable);
                        }
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            BitmapDrawable bd = (BitmapDrawable) resource;
                            Bitmap bm = bd.getBitmap();
                            Palette.from(bm).maximumColorCount(10).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(@NonNull Palette palette) {
                                    List<Palette.Swatch> list = palette.getSwatches();
                                    int colorSize = 0;
                                    Palette.Swatch maxSwatch = null;
                                    for (int i = 0; i < list.size(); i++) {
                                        Palette.Swatch swatch = list.get(i);
                                        if (swatch != null) {
                                            int population = swatch.getPopulation();
                                            if (colorSize < population) {
                                                colorSize = population;
                                                maxSwatch = swatch;
                                            }
                                        }
                                    }
                                    if (maxSwatch != null){
                                        holder.mContentLay.setCardBackgroundColor(maxSwatch.getRgb());
                                        holder.mContentLay.getBackground().setAlpha((int)(0.8*255));

                                    }
                                }
                            });
                            holder.mCircleImageView.setImageBitmap(bm);
                        }
                    });
            holder.mModifyBankCardTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SelectBankListActivity.class);
                    //intent.putExtra("DATA",(Serializable) map);
                    intent.putExtra("TYPE","1");
                    intent.putExtra("patncode",map.get("patncode")+"");
                    mContext.startActivity(intent);
                }
            });
            if (wxStatus.equals("2")){
                holder.mQianYeWyTv.setVisibility(View.VISIBLE);

                holder.mQianYeWyTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, BankQianyueActivity.class);
                        //intent.putExtra("DATA",(Serializable) map);
                        intent.putExtra("DATA",(Serializable) map);
                        mContext.startActivity(intent);
                    }
                });
            }else {
                holder.mQianYeWyTv.setVisibility(View.GONE);
            }

            String show = map.get("isShow")+"";
            if (show.equals("1")){
                holder.mMoneyTv.setText(MbsConstans.RMB+" "+UtilTools.getNormalMoney(map.get("money")+""));
                holder.mShowMoneyBut.setSelected(true);
            }else {
                holder.mMoneyTv.setText("****");
                holder.mShowMoneyBut.setSelected(false);
            }

            holder.mShowMoneyBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean b = holder.mShowMoneyBut.isSelected();
                    if (!b){
                        if (mOnChangeBankCardListener != null){
                            mOnChangeBankCardListener.onButClickListener("2",map,BankCardChildAdapter.this);
                        }
                    }else {
                        map.put("isShow","0");
                        BankCardChildAdapter.this.notifyDataSetChanged();
                        if (mOnChangeBankCardListener != null){
                           // mOnChangeBankCardListener.onButClickListener("3",map);
                        }
                    }

                }
            });

            holder.mShowLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!holder.mViewPagerLay.isShown()){
                        List<Map<String,Object>> mBindCardList ;
                        if (map.containsKey("bindCard")){
                            mBindCardList = (ArrayList<Map<String,Object>>)map.get("bindCard");
                            if (mBindCardList == null){
                                mBindCardList = new ArrayList<>();
                            }
                            if (mBindCardList.size() == 0){
                                TipsToast.showToastMsg(mContext.getResources().getString(R.string.bind_card_no));
                                holder.mViewPagerLay.setVisibility(View.GONE);
                                map.put("bindShow","0");
                            }else {
                                holder.mViewPagerLay.setVisibility(View.VISIBLE);
                                map.put("bindShow","1");
                                BankCardChildAdapter.this.notifyDataSetChanged();
                            }
                        }else {
                            if (mOnChangeBankCardListener != null){
                                mOnChangeBankCardListener.onButClickListener("4",map,BankCardChildAdapter.this);
                            }
                        }

                    }else {
                        holder.mViewPagerLay.setVisibility(View.GONE);
                        map.put("bindShow","0");
                        BankCardChildAdapter.this.notifyDataSetChanged();
                    }
                }
            });


        }
    }

    // Generate palette synchronously and return it

    /**
     *
     * @param bitmap
     * @return
     */
    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }

    // Generate palette asynchronously and use it on a different
    // thread using onGenerated()
    public void createPaletteAsync(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                // Use generated instance
            }
        });
    }

    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 填充布局
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bank_card_child, null);
//        MyHolder holder = new MyHolder(view);

        if (arg1 == ITEM_TYPE_HEADER) {
            return new MyHolder(mHeaderView,ITEM_TYPE_HEADER);
        } else if (arg1 == ITEM_TYPE_EMPTY) {
            return new MyHolder(mEmptyView,ITEM_TYPE_EMPTY);
        } else if (arg1 == ITEM_TYPE_FOOTER) {
            return new MyHolder(mFooterView,ITEM_TYPE_FOOTER);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_bank_card_child, arg0,false);
            MyHolder holder = new MyHolder(view,ITEM_TYPE_NORMAL);
            return holder;
        }
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {
        TextView mBankNameTv;
        TextView mModifyBankCardTv;
        TextView mBankCardValueTv;
        TextView mBankBindTv;
        TextView mQianYeWyTv;
        TextView mMoneyTv;
        ImageView mShowMoneyBut;
        CircleImageView mCircleImageView;
        CardView mContentLay;
        ViewPager mViewPager;
        LinearLayout mAllLay;
        LinearLayout mShowLay;
        RelativeLayout mViewPagerLay;

        public MyHolder(View view,int type) {
            super(view);
            switch (type){
                case ITEM_TYPE_HEADER:
                    mBankBindTv = view.findViewById(R.id.bind_tv);
                    break;
                case ITEM_TYPE_FOOTER:
                    break;
                case ITEM_TYPE_EMPTY:
                    break;
                case ITEM_TYPE_NORMAL:
                    mBankNameTv = view.findViewById(R.id.bank_name_tv);
                    mModifyBankCardTv = view.findViewById(R.id.modify_bank_tv);
                    mBankCardValueTv = view.findViewById(R.id.bank_card_value_tv);
                    mCircleImageView = view.findViewById(R.id.image_view);
                    mContentLay = view.findViewById(R.id.bank_card_lay);
                    mQianYeWyTv = view.findViewById(R.id.qianyue_tv);
                    mMoneyTv = view.findViewById(R.id.money_tv);
                    mShowMoneyBut = view.findViewById(R.id.toggle_money);
                    mViewPager = view.findViewById(R.id.viewpager);
                    mAllLay = view.findViewById(R.id.all_content_view);
                    mShowLay = view.findViewById(R.id.bind_account_lay);
                    mViewPagerLay = view.findViewById(R.id.viewPager_container);
                    break;
            }
        }

    }
}