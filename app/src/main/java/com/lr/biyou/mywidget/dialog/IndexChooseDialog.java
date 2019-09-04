package com.lr.biyou.mywidget.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.lr.biyou.R;
import com.wanou.framelibrary.utils.UiTools;

import java.util.Objects;

public class IndexChooseDialog implements View.OnClickListener {
    private ConfirmClickListener confirmClickListener;
    private Activity activity;
    private AlertDialog dialog;
    private RadioButton maText, bollText, mainHide, macdText, kdjText, rsiText, wrText, subHide,
            fenText, kText;
    private View view;

    public IndexChooseDialog(Activity activity) {
        this.activity = activity;
    }

    public void getDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(activity).create();
            view = LayoutInflater.from(activity).inflate(R.layout.index_choose,null);
            RadioButton maText = view.findViewById(R.id.maText);
            RadioButton bollText = view.findViewById(R.id.bollText);
            RadioButton mainHide = view.findViewById(R.id.mainHide);
            RadioButton macdText = view.findViewById(R.id.macdText);
            RadioButton kdjText = view.findViewById(R.id.kdjText);
            RadioButton rsiText = view.findViewById(R.id.rsiText);
            RadioButton wrText = view.findViewById(R.id.wrText);
            RadioButton subHide = view.findViewById(R.id.subHide);
            RadioButton fenText = view.findViewById(R.id.fenText);
            RadioButton kText = view.findViewById(R.id.kText);
            RadioGroup rgMain = view.findViewById(R.id.rgMain);
            RadioGroup rgSub = view.findViewById(R.id.rgSub);
            RadioGroup rgThird = view.findViewById(R.id.rgThird);

            maText.setOnClickListener(this);
            bollText.setOnClickListener(this);
            mainHide.setOnClickListener(this);
            macdText.setOnClickListener(this);
            kdjText.setOnClickListener(this);
            rsiText.setOnClickListener(this);
            wrText.setOnClickListener(this);
            subHide.setOnClickListener(this);
            fenText.setOnClickListener(this);
            kText.setOnClickListener(this);
            maText.performClick();
            subHide.performClick();
            kText.performClick();

            dialog.setCancelable(true);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.shape_white_bg_round5);
        }
        dialog.show();
        int deviceHeight = UiTools.getDeviceHeight(activity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view, params);
        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.getWindow().setWindowAnimations(R.style.dialogRightToLeft);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        // dialog显示时背景的昏暗程度
        attributes.dimAmount = 0.5f;
        attributes.width = UiTools.getDeviceWidth(activity);
        dialog.getWindow().setAttributes(attributes);
    }

    @Override
    public void onClick(View v) {
        if (confirmClickListener != null) {
            switch (v.getId()) {
                case R.id.maText:
                    confirmClickListener.onMaClick();
                    break;
                case R.id.bollText:
                    confirmClickListener.onBollClick();
                    break;
                case R.id.mainHide:
                    confirmClickListener.onMainHideClick();
                    break;
                case R.id.macdText:
                    confirmClickListener.onMacdClick();
                    break;
                case R.id.kdjText:
                    confirmClickListener.onKDJClick();
                    break;
                case R.id.rsiText:
                    confirmClickListener.onRsiClick();
                    break;
                case R.id.wrText:
                    confirmClickListener.onWrClick();
                    break;
                case R.id.subHide:
                    confirmClickListener.onSubHideClick();
                    break;
                case R.id.fenText:
                    confirmClickListener.onFenClick();
                    break;
                case R.id.kText:
                    confirmClickListener.onKlineClick();
                    break;
                default:
            }
        }
    }

    public void setConfirmClickListener(ConfirmClickListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
    }

    public interface ConfirmClickListener {
        //        maText, bollText, mainHide, macdText, kdjText, rsiText, wrText, subHide,
//        fenText, kText;
        void onMaClick();

        void onBollClick();

        void onMainHideClick();

        void onMacdClick();

        void onKDJClick();

        void onRsiClick();

        void onWrClick();

        void onSubHideClick();

        void onFenClick();

        void onKlineClick();
    }
}
