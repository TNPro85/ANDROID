package com.zing.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zing.demo.data.Const;
import com.zing.demo.utils.IOUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActFolderAttribute extends ActBase {

    Button btnGetFolderInfo;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_folder_attribute);
        setTitle("Folder Attributes");

        // Create folder to check
        final String folderPath = Const.PATH;
        if(!IOUtils.checkFolderOrFileExist(folderPath))
            new File(folderPath).mkdir();

        final File file = new File(folderPath + "/test.tmp");
        try {
            if(!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvResult = (TextView) findViewById(R.id.tvResult);
        btnGetFolderInfo = (Button) findViewById(R.id.btnGetFolderInfo);
        btnGetFolderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = sdf.parse("31/08/2015");
                    file.setLastModified(date.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
