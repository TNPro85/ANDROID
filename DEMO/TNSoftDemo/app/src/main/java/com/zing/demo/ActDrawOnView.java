package com.zing.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zing.demo.uicontrol.DrawImage;
import com.zing.demo.utils.ToastUtils;

import java.text.NumberFormat;

public class ActDrawOnView extends ActBase {

    private DrawImage imvDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_draw_on_view);
        setTitle("Draw on view");

        imvDemo = (DrawImage) findViewById(R.id.imvDemo);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvDemo.setImageResource(R.drawable.profile_icon_valentine);
                imvDemo.requestLayout();
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvDemo.setImageResource(R.drawable.profile_icon_halloween);
                imvDemo.requestLayout();
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imvDemo.setImageResource(R.drawable.profile_icon_women);
                imvDemo.requestLayout();
            }
        });
    }
}
