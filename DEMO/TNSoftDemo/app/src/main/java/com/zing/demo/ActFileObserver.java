package com.zing.demo;

import android.os.Bundle;
import android.os.FileObserver;
import android.widget.EditText;

import com.zing.demo.utils.IOUtils;

import java.io.File;

public class ActFileObserver extends ActBase {

    EditText etFileObserverLog;

    String mPathToObserve;
    FileObserver mFileObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_file_observer);

        etFileObserverLog = (EditText) findViewById(R.id.etFileObserverLog);

        mPathToObserve = IOUtils.getExternalSdCardPath() + "/TNSoftFileObserverDemo";
        if(!IOUtils.checkFolderOrFileExist(mPathToObserve))
            new File(mPathToObserve).mkdir();

        // This observer is used to monitor activities that happened to the top folder.
        // It is NOT recursive, so anything happened in sub-directories cannot be tracked.
        mFileObserver = new FileObserver(mPathToObserve) {
            @Override
            public void onEvent(int event, String path) {

                // Event code:
                // 1    : Data was read from a file
                // 2    : Data was written to a file
                // 4    : Metadata (permissions, owner, timestamp) was changed explicitly
                // 8    : Someone had a file or directory open for writing, and closed it
                // 16   : Someone had a file or directory open read-only, and closed it
                // 32   : A file or directory was opened
                // 64   : A file or subdirectory was moved from the monitored directory
                // 128  : A file or subdirectory was moved to the monitored directory
                // 256  : A new file or subdirectory was created under the monitored directory
                // 512  : A file was deleted from the monitored directory
                // 1024 : The monitored file or directory was deleted; monitoring effectively stops
                // 2048 : The monitored file or directory was moved; monitoring continues

                String tmp = "";
                event &= FileObserver.ALL_EVENTS;
                switch (event) {
                    case FileObserver.CREATE:
                        tmp = "Create " + path + "\n";
                        break;
                    case FileObserver.DELETE:
                        tmp = "Delete " + path + "\n";
                        break;
                }

                final String value = tmp;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StringBuilder result = new StringBuilder(etFileObserverLog.getText().toString());
                        etFileObserverLog.setText(result.append(value).toString());
                        etFileObserverLog.setSelection(result.length());
                    }
                });
            }
        };

        etFileObserverLog.setText("Start watching folder: " + mPathToObserve + "\n");
        etFileObserverLog.setSelection(etFileObserverLog.getText().length());
        mFileObserver.startWatching();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFileObserver.stopWatching();
    }
}
