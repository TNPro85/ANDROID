package com.zing.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class ActUniqueId extends ActBase {

    private EditText etInput;
    private Button btnAction;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_unique_id);
        setTitle("Unique Id Demo");

        etInput = (EditText) findViewById(R.id.etInput);
        etInput.requestFocus();
        tvResult = (TextView) findViewById(R.id.tvResult);
        btnAction = (Button) findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    long input = Long.parseLong(etInput.getText().toString());
                    Integer i = (int)input;

                    // Get hashcode of an integer after parsed it from Long
                    StringBuilder result = new StringBuilder();
                    result.append("Long: " + input + " / Int: " + i + " / Hashcode: " + i.hashCode());
                    result.append("\n");

                    // Get hashcode of an UUID;
                    UUID uniqueKey = UUID.randomUUID();
                    result.append("uniqueKey: " + uniqueKey + " / Hashcode: " + uniqueKey.hashCode());

                    tvResult.setText(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
