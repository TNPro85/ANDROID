package com.tnpro85.mytvchannels;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.models.Channel;
import com.tnpro85.mytvchannels.models.Device;

public class ActChannelAdd extends ActBase {

    EditText etChannelNum, etChannelName, etChannelDesc;
    private Device curDevice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_channel_add);

        Bundle data = getIntent().getExtras();
        if(data != null) {
            curDevice = data.getParcelable("device");
        }

        etChannelNum = (EditText) findViewById(R.id.etChannelNum);
        etChannelNum.requestFocus();
        etChannelName = (EditText) findViewById(R.id.etChannelName);
        etChannelDesc = (EditText) findViewById(R.id.etChannelDesc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {
            try {
                Channel channel = checkAndAddChannel();
                if (channel != null) {
                    Intent result = new Intent();
                    result.putExtra("channel", channel);
                    setResult(RESULT_OK, result);
                    finish();
                }
                else
                    Toast.makeText(ActChannelAdd.this, "Data exists or invalid. Try again!", Toast.LENGTH_SHORT).show();
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                Toast.makeText(ActChannelAdd.this, "Data exists or invalid. Try again!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private Channel checkAndAddChannel() {
        etChannelNum.setError(null);
        etChannelName.setError(null);
        etChannelDesc.setError(null);

        String num = etChannelNum.getText().toString();
        String name = etChannelName.getText().toString();
        String desc = etChannelDesc.getText().toString();

        if (TextUtils.isEmpty(num)) {
            etChannelNum.setError("Must not empty");
            etChannelNum.requestFocus();
            return null;
        } else if (TextUtils.isEmpty(name)) {
            etChannelName.setError("Must not empty");
            etChannelName.requestFocus();
            return null;
        } else if (TextUtils.isEmpty(desc)) {
            etChannelDesc.setError("Must not empty");
            etChannelDesc.requestFocus();
            return null;
        }

        Channel channel = new Channel(curDevice.dName, Integer.parseInt(num), name, desc);
        DBHelper.getInstance().addChannel(channel);

        return channel;
    }
}
