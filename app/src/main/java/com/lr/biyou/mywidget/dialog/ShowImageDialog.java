package com.lr.biyou.mywidget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lr.biyou.R;

public class ShowImageDialog  extends Dialog {

    private Bitmap showImage;
    private String title;

    public ShowImageDialog(Context context, Bitmap showImage,String title) {
        super(context, R.style.ShowImageDialog);
        this.showImage = showImage;
        this.title = title;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image);

        ImageView imageView = findViewById(R.id.imageView);
        TextView  titleView = findViewById(R.id.titleView);
        imageView.setImageBitmap(showImage);
        titleView.setText(title);
        setCanceledOnTouchOutside(true); // 设置点击屏幕或物理返回键，dialog是否消失
        Window w = getWindow();
        assert w != null;
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = 40;
        onWindowAttributesChanged(lp);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
    }




}
