package com.bignerdranch.android.testpdfreader.model;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public enum  MessageSchower {
    DEFAULT;

    public static void schow(View view, int message, MessageSchower type) {
        switch (type){
            default:
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    public static void schow(View view, String message, MessageSchower type){
        switch (type){
            default:
                Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

}
