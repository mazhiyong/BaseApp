package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.Map;

public class CartAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;
    private int mW = 0;
    public CartAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Map<String, Object> item = mDataList.get(position);

        final ViewHolder  viewHolder = (ViewHolder) holder;
            viewHolder.nameTextView.setText(mDataList.get(position).get("name")+"");


        if (needTitle(position) ) {
            if (position == 0){
                viewHolder.mShopLay.setVisibility(View.VISIBLE);
            }else {
                viewHolder.mShopLay.setVisibility(View.VISIBLE);
            }

            // 显示标题并设置内容
            viewHolder.mShopNameTv.setText(mDataList.get(position).get("shopName")+"");
            viewHolder.mShopNameTv.setVisibility(View.VISIBLE);
        } else {
            // 内容项隐藏标题
            viewHolder.mShopLay.setVisibility(View.GONE);
        }



        viewHolder.mDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // mCartActivity.showDialog(map,position);
            }
        });

       /* viewHolder.checkBox.setId(position);
        viewHolder.checkBox.setChecked(checkPosition.get(position));
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                int id = buttonView.getId();
                checkPosition.set(id,isChecked); //赋值
                getAllPrice();
            }

        });*/

        viewHolder.mCheckLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewHolder.checkBox.performClick();
            }
        });

        viewHolder.mAddView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // int temp = Integer.valueOf(map.get("goods_quantity")+"");
//                goodsNum = temp + 1;
//                mListEdit = viewHolder.mEditText;
//                listMorePrice = viewHolder.morePricetTextView;
//                updateMap = map;
//                mCartActivity.butAction(Integer.valueOf(map.get("id")+""),goodsNum,map.get("buyType")+"");
            }
        });
        viewHolder.mCutView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                int temp = Integer.valueOf(map.get("goods_quantity")+"");
//                if (temp <=1) {
//                    return;
//                }else{
//                    goodsNum = temp - 1;
//                    mListEdit = viewHolder.mEditText;
//                    listMorePrice = viewHolder.morePricetTextView;
//                    updateMap = map;
//                    mCartActivity.butAction(Integer.valueOf(map.get("id")+""),goodsNum,map.get("buyType")+"");
//                }
            }
        });

        viewHolder.mEditText.clearFocus();
        viewHolder.mEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    viewHolder.mEditText.setFocusable(true);
                    viewHolder.mEditText.setFocusableInTouchMode(true);
                    viewHolder.mEditText.requestFocus();
                    // viewHolder.mEditText.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                return false;
            }
        });


        (viewHolder.convertView).getRootView().getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        viewHolder.convertView.getWindowVisibleDisplayFrame(r);
                        int screenHeight = viewHolder.convertView.getRootView()
                                .getHeight();
                        int heightDifference = screenHeight - (r.bottom);
                        if (heightDifference > 200) {
                            //软键盘显示
                            mIsSoftKeyboardShowing = true;
                            Log.e("TAG", "显示");
                        } else {
                            //软键盘隐藏
                            Log.e("TAG", "隐藏");
                            mIsSoftKeyboardShowing = false;

                            viewHolder.mEditText.setFocusable(false);
                            viewHolder.mEditText.setFocusableInTouchMode(false);
                            viewHolder.mEditText.clearFocus();


                        }
                    }

                });

    }
    public boolean mIsSoftKeyboardShowing = false;
    public class ViewHolder extends RecyclerView.ViewHolder {


        public View convertView;
        public TextView nameTextView;
        public ImageView imageView;
        public TextView priceTextView;
        public CheckBox checkBox;
        public EditText mEditText;
        public ImageView mAddView,mCutView;
        public TextView morePricetTextView;
        public LinearLayout mCheckLayout;
        public ImageView mDeleteView;

        private LinearLayout mShopLay;
        private TextView mShopNameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            convertView = itemView;
            nameTextView = (TextView) itemView.findViewById(R.id.cart_item_name);
            priceTextView = (TextView) itemView.findViewById(R.id.cart_item_price);
            imageView = (ImageView) itemView.findViewById(R.id.cart_item_image);
            checkBox = (CheckBox) itemView.findViewById(R.id.cart_item_checkbox);
            mAddView = (ImageView) itemView.findViewById(R.id.cart_item_add);
            mCutView = (ImageView) itemView.findViewById(R.id.cart_item_des);
            mEditText = (EditText) itemView.findViewById(R.id.cart_item_edit);
            morePricetTextView = (TextView) itemView.findViewById(R.id.more_prcie);
            mCheckLayout = (LinearLayout) itemView.findViewById(R.id.check);
            mDeleteView = (ImageView) itemView.findViewById(R.id.delete_view);

            mShopLay = (LinearLayout) itemView.findViewById(R.id.shop_lay);
            mShopNameTv = (TextView) itemView.findViewById(R.id.shop_name);
        }
    }



    /**
     * 判断是否需要显示标题
     *
     * @param position
     * @return
     */
    private boolean needTitle(int position) {
        // 第一个肯定是分类
        if (position == 0) {
            return true;
        }

        // 异常处理
        if (position < 0) {
            return false;
        }

        // 当前  // 上一个
        Map<String,Object> currentEntity = (Map<String,Object>) mDataList.get(position);
        Map<String,Object> previousEntity = (Map<String,Object>) mDataList.get(position - 1);
        if (null == currentEntity || null == previousEntity) {
            return false;
        }

        String currentTitle = currentEntity.get("code")+"";
        String previousTitle = previousEntity.get("code")+"";
        if (null == previousTitle || null == currentTitle) {
            return false;
        }

        // 当前item分类名和上一个item分类名不同，则表示两item属于不同分类
        if (currentTitle.equals(previousTitle)) {
            return false;
        }
        return true;
    }



    private boolean isMove(int position) {
        // 获取当前与下一项
        Map<String,Object> currentEntity = (Map<String,Object>) mDataList.get(position);
        Map<String,Object> nextEntity = (Map<String,Object>) mDataList.get(position + 1);
        if (null == currentEntity || null == nextEntity) {
            return false;
        }

        // 获取两项header内容
        String currentTitle = currentEntity.get("code")+"";
        String nextTitle = nextEntity.get("code")+"";
        if (null == currentTitle || null == nextTitle) {
            return false;
        }

        // 当前不等于下一项header，当前项需要移动了
        if (!currentTitle.equals(nextTitle)) {
            return true;
        }

        return false;
    }
}
