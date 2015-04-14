package com.zing.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zing.demo.utils.StringUtils;
import com.zing.demo.utils.ToastUtils;

public class ActSubsequenceEmoji extends ActBase {

    EditText editSource;
    EditText editIndex;
    Button btnCover;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_subsequence_emoji);
        setTitle("Subsequence Emoji");

        editSource = (EditText) findViewById(R.id.edit_source);
        editIndex = (EditText) findViewById(R.id.edit_index);
        btnCover = (Button) findViewById(R.id.btn_cover);
        txtResult = (TextView) findViewById(R.id.txt_result);
        btnCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String indexInput = editIndex.getText().toString();
                if(!TextUtils.isEmpty(indexInput)) {
                    try {
                        int index = Integer.parseInt(indexInput);
                        txtResult.setText(StringUtils.cutEmojiString(editSource.getText(), index));
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtils.showToast(v.getContext(), e.getMessage());
                    }
                }
            }
        });
    }
}
