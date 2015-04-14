package com.zing.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class ActXml2Java extends ActBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_xml2java_main);
        setTitle("Java 2 Xml Demo");

        findViewById(R.id.btnNormal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActXml2Java.this, ActXml2JavaNormal.class);
                ActXml2Java.this.startActivity(intent);
            }
        });

        findViewById(R.id.btnStyle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActXml2Java.this, ActXml2JavaStyle.class);
                ActXml2Java.this.startActivity(intent);
            }
        });
    }
}
