package com.bignerdranch.android.testpdfreader.controller.resource.resource_view.page.service.impl;

import android.widget.TextView;

import com.bignerdranch.android.testpdfreader.model.components.SelectionAdaptedTextView;

public class OnParagraphSelectionChangedListener implements SelectionAdaptedTextView.OnSelectionChangListener {


    private TextView mTextView;
    private OnParagraphSelectedListenerImpl mOnParagraphSelectedListener;

    public OnParagraphSelectionChangedListener(TextView textView,
                                               OnParagraphSelectedListenerImpl onParagraphSelectedListener) {
        mTextView = textView;
        mOnParagraphSelectedListener = onParagraphSelectedListener;
    }

    @Override
    public void onSelectionChanged(int selStart, int selEnd) {
        if (selStart != selEnd) {
            mOnParagraphSelectedListener.onParagraphSelected(
                    mTextView.getText().toString().substring(selStart, selEnd));
        }
    }
}