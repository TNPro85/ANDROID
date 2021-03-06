package com.zing.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zing.demo.utils.StringUtils;
import com.zing.demo.utils.ToastUtils;

import java.text.NumberFormat;

public class ActConvertNumToKMB extends ActBase {

    EditText etInput;
    Button btnConvert;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_convert_num_to_kmb);
        setTitle("Convert number to K, M, B");

        etInput = (EditText) findViewById(R.id.etInput);
        tvResult = (TextView) findViewById(R.id.tvResult);
        btnConvert = (Button) findViewById(R.id.btnConvert);
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String indexInput = etInput.getText().toString();
                if(!TextUtils.isEmpty(indexInput)) {
                    try {
                        int num = Integer.parseInt(indexInput);
                        tvResult.setText(getCountByKString(num));
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showToast(v.getContext(), e.getMessage());
                    }
                }
            }
        });
    }

    private String getCountByKString(int count) {
        int B = 1000000000, M = 1000000, K = 1000;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);

        if(count < K)
            return count + "";
        else {
            double f = count;
            String result;
            if(f >= B) {
                result = f % B >= 100000000 ? nf.format(f/B) + "B" : (int)(f/B) + "B";
            }
            else if(f >= M) {
                result = f % M >= 100000 ? nf.format(f/M) + "M" : (int)(f/M) + "M";
            }
            else {
                result = f % K >= 100 ? nf.format(f/K) + "K" : (int)(f/K) + "K";
            }

            return result;
        }
    }
}
