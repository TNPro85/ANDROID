package com.tnpro.core.uicontrols;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.View;

public class PopupManager {
    private PopupMenu mPopupMenu;
    private volatile static PopupManager mInstance;

    public PopupManager() {}

    public static PopupManager getInstance() {
        synchronized (PopupManager.class) {
            if(mInstance == null) {
                mInstance = new PopupManager();
            }
        }

        return mInstance;
    }

    public PopupManager buildMenu(Context context, View anchor, int menuID, PopupMenu.OnMenuItemClickListener onMenuItemClickListener) {
        mPopupMenu = new PopupMenu(context, anchor);
        mPopupMenu.getMenuInflater().inflate(menuID, mPopupMenu.getMenu());
        mPopupMenu.setOnMenuItemClickListener(onMenuItemClickListener);

        return mInstance;
    }

    public void show() {
        try {
            if(mPopupMenu != null)
                mPopupMenu.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        if(mPopupMenu != null)
            mPopupMenu.dismiss();
    }
}
