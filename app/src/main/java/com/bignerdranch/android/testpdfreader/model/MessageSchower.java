package com.bignerdranch.android.testpdfreader.model;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

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
