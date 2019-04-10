package com.bignerdranch.android.testpdfreader.trash.textViewMod;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;
import android.util.Log;

public class RoundedBackgroundSpan extends ReplacementSpan {
    private static String TAG = "RoundedBackgroundSpan";

    private static int CORNER_RADIUS = 8;
    private static int PADDING_TOP = 5;
    private static int PADDING_BOTTOM = 3;

    private int mBgColor = Color.GRAY;
    private int mTextColor = Color.RED;

    public RoundedBackgroundSpan(int bgColor, int txtColor) {
        //super();
        Log.i(TAG, bgColor + " | " + txtColor);
        mBgColor = bgColor;
        mTextColor = txtColor;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        RectF rect = new RectF(
                x,
                top + PADDING_TOP,
                x + measureText(paint, text, start, end),
                bottom - PADDING_BOTTOM);
        paint.setColor(mBgColor);
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint);
        paint.setColor(mTextColor);
        canvas.drawText(text, start, end, x, y, paint);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}