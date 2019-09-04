package com.lr.biyou.mywidget.view;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import androidx.cardview.widget.CardView;
import android.util.TypedValue;
import android.view.View;

import com.lr.biyou.R;


public class ScaleTransformer implements ViewPager.PageTransformer {
    private float MINALPHA = 0.8f;
    private Context context;
    private float elevation;

    public ScaleTransformer(Context context) {
        this.context = context;
        elevation = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                2, context.getResources().getDisplayMetrics());
    }

    /**
     * position取值特点：
     * 假设页面从0～1，则：
     * 第一个页面position变化为[0,-1]
     * 第二个页面position变化为[1,0]
     *
     * @param page
     * @param position
     */
    @Override
    public void transformPage(View page, float position) {
        CardView cardView  = page.findViewById(R.id.bank_card_lay2);
        if (position < -1 || position > 1) {
            page.setAlpha(MINALPHA);
        } else {
            //不透明->半透明
            if (position < 0) {//[0,-1]
                page.setAlpha(MINALPHA + (1 + position) * (1 - MINALPHA));
                cardView.setCardElevation((1 + position) * elevation);
            } else {//[1,0]
                //半透明->不透明
                page.setAlpha(MINALPHA + (1 - position) * (1 - MINALPHA));

                cardView.setCardElevation((1 - position) * elevation);
            }
        }
    }




}