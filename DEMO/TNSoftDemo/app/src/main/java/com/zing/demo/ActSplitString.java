package com.zing.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zing.demo.utils.ToastUtils;

import java.text.NumberFormat;

public class ActSplitString extends ActBase {

    EditText etInput, etSeparator, etOmit;
    Button btnSplit, btnMerge;
    TextView tvResult, tvResultMerge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_split_string);
        setTitle("Convert number to K, M, B");

        etInput = (EditText) findViewById(R.id.etInput);
        etSeparator = (EditText) findViewById(R.id.etSeparator);
        etOmit = (EditText) findViewById(R.id.etOmit);
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvResultMerge = (TextView) findViewById(R.id.tvResultMerge);
        btnSplit = (Button) findViewById(R.id.btnSplit);
        btnSplit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etInput.getText().toString();
                String expression = etSeparator.getText().toString();
                if(expression.equals(".")) expression = "\\.";
                String[] resultSplit = TextUtils.split(str, expression);
                String result = "";
                for(int i = 0; i < resultSplit.length; i++) {
                    result += resultSplit[i] + (i < resultSplit.length - 1 ? "\n" : "");
                }

                tvResult.setText(result);
            }
        });

        btnMerge = (Button) findViewById(R.id.btnMerge);
        btnMerge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etInput.getText().toString();
                String omit = etOmit.getText().toString();
                String[] resultSplit = TextUtils.split(str, etSeparator.getText().toString());
                String result = "";
                for(int i = 0; i < resultSplit.length; i++) {
                    if(!resultSplit[i].equals(omit)) {
                        if (!TextUtils.isEmpty(result))
                            result += etSeparator.getText().toString();
                        result += resultSplit[i];
                    }
                }

                tvResultMerge.setText(result);
            }
        });
    }
}
