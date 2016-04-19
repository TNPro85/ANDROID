package com.tnpro85.mytvchannels;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tnpro85.mytvchannels.data.Const;
import com.tnpro85.mytvchannels.db.DBHelper;
import com.tnpro85.mytvchannels.models.Channel;
import com.tnpro85.mytvchannels.models.Device;
import com.tnpro85.mytvchannels.utils.Utils;

public class ActChannelAdd extends ActBase {

    private EditText etChannelNum, etChannelName, etChannelDesc;
    private Device curDevice;
    private TextView tvDeviceCopyHeader;
    private View vDeviceCopyDivider;

    private Channel curChannel; // Use in editor mode
    private boolean mIsCopyingChannel;

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

        tvDeviceCopyHeader = (TextView) findViewById(R.id.tvDeviceCopyHeader);
        vDeviceCopyDivider = findViewById(R.id.vDeviceCopyDivider);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            curDevice = data.getParcelable(Const.EXTRA.DEVICE);
            curChannel = data.getParcelable(Const.EXTRA.CHANNEL);
            mIsCopyingChannel = data.getBoolean(Const.EXTRA.COPYING_CHANNEL, false);
        }

        if (curChannel != null) {
            String cNum = curChannel.cNum + "";
            etChannelNum.setText(cNum);
            etChannelNum.setEnabled(mIsCopyingChannel);
            etChannelName.setText(curChannel.cName);
            if(mIsCopyingChannel) {
                setTitle(getString(R.string.title_activity_act_channel_copy));
                String headerStr = String.format(getString(R.string.str_copy_to), curDevice.dName);
                tvDeviceCopyHeader.setVisibility(View.VISIBLE);
                tvDeviceCopyHeader.setText(headerStr);
                vDeviceCopyDivider.setVisibility(View.VISIBLE);
                etChannelNum.requestFocus();
                etChannelNum.setSelection(cNum.length());
            }
            else {
                etChannelName.requestFocus();
                etChannelName.setSelection(curChannel.cName.length());
            }
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
                Channel existChannel = DBHelper.getInstance().getChannel(curDevice.dName, channel.cNum);
                if (curChannel != null) {
                    // If copying a channel, channel exists -> show error message. Otherwise, add it into selected device.
                    if(mIsCopyingChannel) {
                        if(existChannel != null) {
                            Utils.showMsg(ActChannelAdd.this, R.string.str_error_channel_exist);
                            etChannelNum.setError(getString(R.string.str_error_duplicated));
                            etChannelNum.requestFocus();
                        }
                        else {
                            DBHelper.getInstance().addChannel(channel);
                            Intent result = new Intent();
                            result.putExtra(Const.EXTRA.CHANNEL, channel);
                            setResult(RESULT_OK, result);
                            finish();
                        }
                    }
                    // If editing a channel, data has been changed -> update channel. Otherwise, do nothing.
                    else {
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
                    }
                }
                // Adding a new channel
                else {
                    if(existChannel != null) {
                        Utils.showMsg(ActChannelAdd.this, R.string.str_error_channel_exist);
                        etChannelNum.setError(getString(R.string.str_error_duplicated));
                        etChannelNum.requestFocus();
                    }
                    else {
                        DBHelper.getInstance().addChannel(channel);
                        Intent result = new Intent();
                        result.putExtra(Const.EXTRA.CHANNEL, channel);
                        setResult(RESULT_OK, result);
                        finish();
                    }
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
