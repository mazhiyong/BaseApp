package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;


public class BankCardAdapter2 extends ListBaseAdapter {


    private OnCheckedBankCardListener mOnCheckedBankCardListener;

    private Context mContext;
    private int mScreenW;
    private int mType = 0;

    private final int ITEM_TYPE_NORMAL = 0;
    private final int ITEM_TYPE_HEADER = 1;
    private final  int ITEM_TYPE_FOOTER = 2;
    private final int ITEM_TYPE_EMPTY = 3;
    private View mHeaderView;
    private View mFooterView; //底部添加布局
    private View mEmptyView; //内容为空布局

    public String getPatncode() {
        return mPatncode;
    }

    public void setPatncode(String patncode) {
        mPatncode = patncode;
    }

    private String mPatncode = "";

    private int mParentPosition;

    private LayoutInflater mLayoutInflater;

    public void setOnCheckedBankCardListener(OnCheckedBankCardListener onCheckedBankCardListener) {
        mOnCheckedBankCardListener = onCheckedBankCardListener;
    }

    public BankCardAdapter2(Context context, int mType) {
        super();
        this.mContext = context;
        this.mType = mType;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        int itemCount = mDataList.size();
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
        if (null != mEmptyView && mDataList.size() == 0){
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        MyHolder viewholder = (MyHolder) holder;
        final Map<String, Object> map = mDataList.get(position);

        if (type == ITEM_TYPE_HEADER) {
           /* holder.mBankBindTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("patncode",mPatncode);
                    if (mOnChangeBankCardListener != null){
                        mOnChangeBankCardListener.onButClickListener("1",map, BankCardAdapter2.this);
                    }
                }
            });*/
        }else if (type == ITEM_TYPE_FOOTER){

        }else if (type == ITEM_TYPE_EMPTY){

        }else {

            int co = getColorWithAlpha(0.7f,ContextCompat.getColor(mContext,R.color.bank_bg_dark));
            int colors [] ={co,ContextCompat.getColor(mContext,R.color.bank_bg_dark)};
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
            gd.setCornerRadius(UtilTools.dip2px(mContext,8));
            viewholder.mContentLay.setBackground(gd);

            RequestOptions options = new RequestOptions()
                    .placeholder(R.color.body_bg) //占位图
                    .error(R.color.body_bg);       //错误图


            if (UtilTools.empty(map.get("opnbnknm") + "")){
                viewholder.mBankNameTv.setText("暂无相关信息");
            }else {
                viewholder.mBankNameTv.setText(map.get("opnbnknm") + "");
            }
            viewholder.mBankCardValueTv.setText("**** **** **** "+UtilTools.getCardNoFour(map.get("accid") + "") );

            String mtype = map.get("accsn")+"";
            switch (mtype){
                case "1":
                    viewholder.mTypeTv.setText("提现卡");
                    break;
                case "A":
                    viewholder.mTypeTv.setText("充值卡");
                    break;
                case "D":
                    viewholder.mTypeTv.setText("线下转账");
                    break;
            }


            String kind = MbsConstans.USER_MAP.get("firm_kind") + "";//客户类型（0：个人，1：企业）
            if (kind.equals("1")) {
                viewholder.mTypeTv.setVisibility(View.GONE);
            }else {
                viewholder.mTypeTv.setVisibility(View.GONE);
            }


            Glide.with(mContext)
                    .load(map.get("logopath")+"")
                    .apply(options)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void  onLoadFailed(@Nullable Drawable errorDrawable){
                            viewholder.mCircleImageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.default_bank));
                            int co = getColorWithAlpha(0.7f,ContextCompat.getColor(mContext,R.color.bank_bg_dark));
                            int colors [] ={co,ContextCompat.getColor(mContext,R.color.bank_bg_dark)};
                            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
                            gd.setCornerRadius(UtilTools.dip2px(mContext,8));
                            viewholder.mContentLay.setBackground(gd);
//                            viewholder.mContentLay.setBackgroundColor(ContextCompat.getColor(mContext,R.color.bank_bg));

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
//                                        viewholder.mContentLay.setCardBackgroundColor((int)(0.9*maxSwatch.getRgb()));
                                        //viewholder.mContentLay.getBackground().setAlpha((int)(0.8*255));
                                        //设置渐变色
                                        int co = getColorWithAlpha(0.7f,maxSwatch.getRgb());
                                        int colors [] ={co,maxSwatch.getRgb()};
                                        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,colors);
                                        gd.setCornerRadius(UtilTools.dip2px(mContext,8));
                                        viewholder.mContentLay.setBackground(gd);


                                    }
                                }
                            });


                            ColorMatrix matrix = new ColorMatrix();
                            matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
                            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                            // holder.mShuiyinView.setColorFilter(filter);
                            //holder.mShuiyinView.setImageBitmap(grey(bm));


                            Bitmap smallBitmap = Bitmap.createBitmap(bm,0,0,bm.getWidth()*3/4,bm.getWidth()*3/4);
                            //将获取后的图像显示在ImageView组件中
                            Drawable drawable =new BitmapDrawable(mContext.getResources(),bm);
                            ColorFilter filter2 = new LightingColorFilter( ContextCompat.getColor(mContext,R.color.white),ContextCompat.getColor(mContext,R.color.white));
                            drawable.setColorFilter(filter2);

                            viewholder.mShuiyinView.setImageDrawable(drawable);

                            viewholder.mCircleImageView.setImageBitmap(bm);
                        }
                    });

            String show = map.get("isShow")+"";
            if (show.equals("1")){
                viewholder.mMoneyTv.setText(MbsConstans.RMB+" "+UtilTools.getNormalMoney(map.get("money")+""));
                viewholder.mShowMoneyBut.setSelected(true);
            }else {
                viewholder.mMoneyTv.setText("****");
                viewholder.mShowMoneyBut.setSelected(false);
            }

            //查询余额
            viewholder.mShowMoneyBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean b = viewholder.mShowMoneyBut.isSelected();
                    if (!b){
                        //请求余额 显示
                        if (mOnCheckedBankCardListener != null){
                            mOnCheckedBankCardListener.onButClickListener("6",map, BankCardAdapter2.this);
                        }
                    }else {
                        //隐藏余额
                        map.put("isShow","0");
                        BankCardAdapter2.this.notifyDataSetChanged();
                    }

                }
            });


            //变更
           viewholder.mModifyBankCardTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCheckedBankCardListener != null){
                        mOnCheckedBankCardListener.onButClickListener("5",map, BankCardAdapter2.this);
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_bank_card2, arg0,false);
            MyHolder holder = new MyHolder(view,ITEM_TYPE_NORMAL);
            return holder;
        }
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {
        TextView mBankNameTv;  //开户行
        TextView mModifyBankCardTv; //变更
        TextView mBankCardValueTv; //卡号
        TextView mBankBindTv; //绑卡
        TextView mQianYeWyTv; //签约网银
        TextView mMoneyTv;  //余额
        TextView mTypeTv; //类型
        ImageView mShowMoneyBut; //显示余额
        CircleImageView mCircleImageView; //银行卡图标
        LinearLayout mContentLay;
        ImageView mShuiyinView;


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
                    mTypeTv = view.findViewById(R.id.type_tv);
                    mShowMoneyBut = view.findViewById(R.id.toggle_money);
                    mShuiyinView = view.findViewById(R.id.shuiyin_view);

                    break;
            }
        }

    }


    public interface OnCheckedBankCardListener {
        /*void onAddNewBanCardListener(Map<String,Object> map);
        void onShowMoney(Map<String,Object> map);*/
        void onButClickListener(String type,Map<String,Object> map,BankCardAdapter2 bankCardChildAdapter);
    }



    /**
     * 对rgb色彩加入透明度
     * @param alpha     透明度，取值范围 0.0f -- 1.0f.
     * @param baseColor
     * @return a color with alpha made from base color
     */
    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
}