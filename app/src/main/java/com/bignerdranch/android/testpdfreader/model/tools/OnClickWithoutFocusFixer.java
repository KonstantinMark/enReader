package com.bignerdranch.android.testpdfreader.model.tools;

import android.view.MotionEvent;
import android.view.View;

public class OnClickWithoutFocusFixer implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!v.isFocused()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            v.onTouchEvent(event);
                        }
                        v.setOnTouchListener(new OnClickWithoutFocusFixer());
                        return false;
                    }
                });
            }
        }
        return false;
    }
}
