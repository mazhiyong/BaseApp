package com.lr.biyou.mywidget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

@SuppressLint("AppCompatCustomView")
public class ToggleRadioButton extends RadioButton {
    public ToggleRadioButton(Context context) {
        super(context);
    }

    public ToggleRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
        if (!isChecked()){
            ((RadioGroup)getParent()).clearCheck();
        }
    }
}
