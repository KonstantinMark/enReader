package com.bignerdranch.android.testpdfreader.controller.resource.resource_view.page.service.impl;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.android.testpdfreader.model.text.TextManager;
import com.bignerdranch.android.testpdfreader.controller.resource.resource_view.page.service.OnClickedWordListener;

public class OnClickedWordSelector implements View.OnClickListener {

    private OnClickedWordListener mOnClickedWordListener;

    public OnClickedWordSelector(OnClickedWordListener onClickedWordListener) {
        mOnClickedWordListener = onClickedWordListener;
    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView) v;
        String text = textView.getText().toString();
        TextManager textManager = new TextManager(text);
        int[] pos = textManager.getWordSelectionPositions(textView.getSelectionStart());

        mOnClickedWordListener.onWordClicked(text, pos[0], pos[1]);

    }
}
