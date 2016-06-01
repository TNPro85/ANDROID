package com.zing.demo;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

public class ActLockScreen extends ActBase {

    static final int RESULT_ENABLE = 1;

    DevicePolicyManager deviceManger;
    ComponentName compName;

    Button btnLock;
    SwitchCompat sLockEnable, sShortcut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_lockscreen);

        deviceManger = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);

        sLockEnable = (SwitchCompat) findViewById(R.id.sLockEnable);
        sLockEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "Additional text explaining why this needs to be added.");
                    startActivityForResult(intent, RESULT_ENABLE);
                }
                else {
                    deviceManger.removeActiveAdmin(compName);
                    updateStatus();
                }
            }
        });

        sShortcut = (SwitchCompat) findViewById(R.id.sShortcut);
        sShortcut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //Adding shortcut for MainActivity
                    //on Home screen
                    Intent shortcutIntent = new Intent(getApplicationContext(), ActMain.class);
                    shortcutIntent.putExtra("forceClose", true);
                    shortcutIntent.setAction(Intent.ACTION_MAIN);

                    Intent addIntent = new Intent();
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "LockScreen");
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                            Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                                    R.mipmap.ic_launcher));

                    addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                    getApplicationContext().sendBroadcast(addIntent);
                }
                else {
                    removeShortcut();
                }
            }
        });

        btnLock = (Button) findViewById(R.id.btnLock);
        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceManger.isAdminActive(compName)) {
                    deviceManger.lockNow();
                }
            }
        });
    }

    private void removeShortcut() {
        final Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setComponent(new ComponentName(ActLockScreen.this.getPackageName(), "LockScreen"));

        final Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        intent.setComponent(new ComponentName(ActLockScreen.this.getPackageName(), "LockScreen"));
        intent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");

        sendBroadcast(intent, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    void updateStatus() {
        boolean isEnable = deviceManger.isAdminActive(compName);
        boolean isSetShortcut = false;
        sLockEnable.setChecked(isEnable);
        sShortcut.setEnabled(isEnable);
//        sShortcut.setChecked(isSetShortcut);
        btnLock.setEnabled(isEnable);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                updateStatus();
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static class MyAdmin extends DeviceAdminReceiver {

        @Override
        public void onEnabled(Context context, Intent intent) {}

        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            return "Disabling this will prevent you using the following functions:\n- Lock Screen\n-Lock Screen shortcut";
        }

        @Override
        public void onDisabled(Context context, Intent intent) {}
    }
}
