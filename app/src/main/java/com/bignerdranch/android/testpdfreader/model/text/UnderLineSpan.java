package com.bignerdranch.android.testpdfreader.model.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import org.jetbrains.annotations.NotNull;

public class UnderLineSpan extends ReplacementSpan {
    private static String TAG = "RoundedBackgroundSpan";

    private static int CORNER_RADIUS = 8;
    private static int PADDING_TOP = 5;
    private static int PADDING_BOTTOM = 3;

    private int mBgColor;
    private int mTextColor;

    public UnderLineSpan(int bgColor, int txtColor) {
        mBgColor = bgColor;
        mTextColor = txtColor;
    }

    @Override
    public void draw(@NotNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NotNull Paint paint) {
        RectF rect = new RectF(
                x,
                bottom - PADDING_BOTTOM,
                x + measureText(paint, text, start, end),
                bottom);

        paint.setColor(mBgColor);
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint);
        paint.setColor(mTextColor);
        canvas.drawText(text, start, end, x, y, paint);
    }

    @Override
    public int getSize(@NotNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }
}