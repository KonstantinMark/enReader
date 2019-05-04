package com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.impl;

import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.OnClickedWordListener;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.TextSelector;
import com.bignerdranch.android.testpdfreader.model.TextTranslationAction;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.TextTranslationActionListener;

public class OnClickWordListenerImpl implements OnClickedWordListener {

    private TextSelector mSelector;
    private TextTranslationActionListener mTextSelectionManager;

    public OnClickWordListenerImpl(TextSelector selector,
                                   TextTranslationActionListener textSelectionManager) {
        mSelector = selector;
        mTextSelectionManager = textSelectionManager;
    }

    @Override
    public void onWordClicked(String fullText, int start, int end) {
        mSelector.set(start, end);
        mTextSelectionManager.onNewAction(new TextTranslationAction(TextTranslationAction.ActionType.ACTION_WORD_SELECTED)
                .setText(fullText.substring(start,end)).setTextSelector(mSelector));
    }
}
