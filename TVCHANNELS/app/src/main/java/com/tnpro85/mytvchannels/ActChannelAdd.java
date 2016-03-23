package com.tnpro85.mytvchannels;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.tnpro85.mytvchannels.data.Const;
import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.models.Channel;
import com.tnpro85.mytvchannels.models.Device;

public class ActChannelAdd extends ActBase {

    private EditText etChannelNum, etChannelName, etChannelDesc;
    private Device curDevice;
    private Channel curChannel;

    @Override
    protected void initUI(Bundle savedInstanceState) {
        super.initUI(savedInstanceState);
        setContentView(R.layout.act_channel_add);
        showHomeAsUpEnable(true);
        setTitle(getString(R.string.title_activity_act_channel_add));

        etChannelNum = (EditText) findViewById(R.id.etChannelNum);
        etChannelNum.requestFocus();
        etChannelName = (EditText) findViewById(R.id.etChannelName);
        etChannelDesc = (EditText) findViewById(R.id.etChannelDesc);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            curDevice = data.getParcelable(Const.EXTRA.DEVICE);
            curChannel = data.getParcelable(Const.EXTRA.CHANNEL);
        }

        if (curChannel != null) {
            etChannelNum.setText(curChannel.cNum + "");
            etChannelNum.setEnabled(false);
            etChannelName.setText(curChannel.cName);
            etChannelName.requestFocus();
            etChannelDesc.setText(curChannel.cDesc);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done) {
            try {
                etChannelNum.setError(null);
                etChannelName.setError(null);
                etChannelDesc.setError(null);

                String num = etChannelNum.getText().toString();
                String name = etChannelName.getText().toString();
                String desc = etChannelDesc.getText().toString();

                if (TextUtils.isEmpty(num)) {
                    etChannelNum.setError(getString(R.string.str_error_must_not_empty));
                    etChannelNum.requestFocus();
                    return true;
                } else if (TextUtils.isEmpty(name)) {
                    etChannelName.setError(getString(R.string.str_error_must_not_empty));
                    etChannelName.requestFocus();
                    return true;
                } else if (TextUtils.isEmpty(desc)) {
                    etChannelDesc.setError(getString(R.string.str_error_must_not_empty));
                    etChannelDesc.requestFocus();
                    return true;
                }

                Channel channel = new Channel(curDevice.dName, Integer.parseInt(num), name, desc);
                if (curChannel != null) {
                    if(name.equals(curChannel.cName) && desc.equals(curChannel.cDesc)) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                    else {
                        DBHelper.getInstance().updateChannel(channel);
                        Intent result = new Intent();
                        result.putExtra(Const.EXTRA.CHANNEL, channel);
                        setResult(RESULT_OK, result);
                        finish();
                    }
                } else {
                    DBHelper.getInstance().addChannel(channel);
                    Intent result = new Intent();
                    result.putExtra(Const.EXTRA.CHANNEL, channel);
                    setResult(RESULT_OK, result);
                    finish();
                }
            } catch (SQLiteConstraintException e) {
                e.printStackTrace();
                Toast.makeText(ActChannelAdd.this, getString(R.string.str_error_invalid_data), Toast.LENGTH_SHORT).show();
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
}
