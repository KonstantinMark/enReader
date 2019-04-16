package com.bignerdranch.android.testpdfreader.model.tools;

import com.bignerdranch.android.testpdfreader.ui.resource.text_selection.TextSelector;

public class OnTranslationNotShowedListenerAdapter implements OnTranslationNotShowedListener {
    private TextSelector mSelector;

    public OnTranslationNotShowedListenerAdapter(TextSelector selector) {
        mSelector = selector;
    }

    @Override
    public void onNotShoved() {
        mSelector.unSelect();
    }
}