package com.bignerdranch.android.testpdfreader.textViewMod;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

@RequiresApi(api = Build.VERSION_CODES.M)
public class SelectActionCalback extends ActionMode.Callback2 {

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        //mode.hide(ActionMode.DEFAULT_HIDE_DURATION);
    }
}
