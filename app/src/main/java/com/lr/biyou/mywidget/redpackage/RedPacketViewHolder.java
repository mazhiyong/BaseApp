package com.lr.biyou.mywidget.redpackage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.lr.biyou.R;
import com.lr.biyou.rongyun.im.message.RongRedPacketMessage;
import com.lr.biyou.ui.moudle2.activity.RedListActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author ChayChan
 * @description: 红包弹框
 */

public class RedPacketViewHolder {

    @BindView(R.id.iv_close)
    ImageView mIvClose;

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;

    @BindView(R.id.tv_name)
    TextView mTvName;

    @BindView(R.id.tv_msg)
    TextView mTvMsg;

    @BindView(R.id.iv_open)
    ImageView mIvOpen;

    private Context mContext;
    private OnRedPacketDialogClickListener mListener;

    private RongRedPacketMessage redPacketMessage;

    private int[] mImgResIds = new int[]{
            R.drawable.icon_open_red_packet1,
            R.drawable.icon_open_red_packet2,
            R.drawable.icon_open_red_packet3,
            R.drawable.icon_open_red_packet4,
            R.drawable.icon_open_red_packet5,
            R.drawable.icon_open_red_packet6,
            R.drawable.icon_open_red_packet7,
            R.drawable.icon_open_red_packet7,
            R.drawable.icon_open_red_packet8,
            R.drawable.icon_open_red_packet9,
            R.drawable.icon_open_red_packet4,
            R.drawable.icon_open_red_packet10,
            R.drawable.icon_open_red_packet11,
    };
    private FrameAnimation mFrameAnimation;

    public RedPacketViewHolder(Context context, View view) {
        mContext = context;
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.iv_close, R.id.iv_open})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                stopAnim();
                if (mListener != null) {
                    mListener.onCloseClick();
                }
                break;

            case R.id.iv_open:
                if (mFrameAnimation != null) {
                    //如果正在转动，则直接返回
                    return;
                }

                startAnim();

                if (mListener != null) {
                    mListener.onOpenClick();
                }
                break;
        }
    }

    public void setData(RongRedPacketMessage entity) {
        redPacketMessage = entity;
        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .circleCrop();

      /*  Glide.with(mContext)
                .load(entity.avatar)
                .apply(options)
                .into(mIvAvatar);*/

        mTvName.setText(entity.getUserid());
        mTvMsg.setText(entity.getMessage());
    }

    public void startAnim() {
        mFrameAnimation = new FrameAnimation(mIvOpen, mImgResIds, 125, true);
        mFrameAnimation.setAnimationListener(new FrameAnimation.AnimationListener() {
            @Override
            public void onAnimationStart() {
                Log.i("show", "start");
            }

            @Override
            public void onAnimationEnd() {
                Log.i("show", "end");
            }

            @Override
            public void onAnimationRepeat() {
                stopAnim();
                if (mListener != null) {
                    mListener.onCloseClick();
                }
                Intent intent = new Intent(mContext, RedListActivity.class);
                intent.putExtra("id",redPacketMessage.getId());
                mContext.startActivity(intent);

            }

            @Override
            public void onAnimationPause() {
                mIvOpen.setBackgroundResource(R.drawable.icon_open_red_packet1);
            }
        });
    }

    public void stopAnim() {
        if (mFrameAnimation != null) {
            mFrameAnimation.release();
            mFrameAnimation = null;
        }
    }

    public void setOnRedPacketDialogClickListener(OnRedPacketDialogClickListener listener) {
        mListener = listener;
    }
}
