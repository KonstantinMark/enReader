package com.bignerdranch.android.testpdfreader.model.components;

import android.content.Context;
import android.util.AttributeSet;

public class SelectionAdaptedTextView extends androidx.appcompat.widget.AppCompatTextView {

    private OnSelectionChangListener mOnSelectionChangListener;

    public SelectionAdaptedTextView(Context context) {
        super(context);
    }

    public SelectionAdaptedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectionAdaptedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnSelectionChangListener(OnSelectionChangListener listener) {
        mOnSelectionChangListener = listener;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        selStart = getSelectionStart();
        selEnd = getSelectionEnd();
        if (mOnSelectionChangListener != null)
            mOnSelectionChangListener.onSelectionChanged(selStart, selEnd);
    }

    public interface OnSelectionChangListener {
        void onSelectionChanged(int selStart, int selEnd);
    }
}
