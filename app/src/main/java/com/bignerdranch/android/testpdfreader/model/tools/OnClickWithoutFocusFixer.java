package com.bignerdranch.android.testpdfreader.model.tools;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

public class OnClickWithoutFocusFixer implements View.OnTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!v.isFocused()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setOnTouchListener((v1, event1) -> {
                    if (event1.getAction() == MotionEvent.ACTION_UP) {
                        v1.onTouchEvent(event1);
                    }
                    v1.setOnTouchListener(new OnClickWithoutFocusFixer());
                    return false;
                });
            }
        }
        return false;
    }
}
