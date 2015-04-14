package com.zing.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.zing.demo.adapter.ListXml2JavaAdapter;
import com.zing.demo.components.ListItem;

import java.util.ArrayList;


public class ActXml2JavaNormal extends ActBase {

    ListView lvList;
    ListXml2JavaAdapter mAdapter;

    EditText etInput;
    Button btnAdd;

    ArrayList<ListItem> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_xml2java_sub);

        etInput = (EditText) findViewById(R.id.etInput);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etInput.getText().toString())) {
                    addDataToLv(etInput.getText().toString());
                    mAdapter.setData(data);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        mAdapter = new ListXml2JavaAdapter(this);
        lvList = (ListView) findViewById(R.id.lvList);

        for(int i = 0; i < 5; i++) {
            addDataToLv(System.currentTimeMillis() + "");
        }

        mAdapter.setData(data);
        lvList.setAdapter(mAdapter);
    }

    private void addDataToLv(String value) {
        ListItem item = new ListItem(value, "");
        data.add(item);
    }
}
