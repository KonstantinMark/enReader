package com.bignerdranch.android.testpdfreader.model.tools;

import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.TextSelector;

public class OnTranslationRemovedListenerAdapter implements OnTranslationNotShowedListener {
    private TextSelector mSelector;

    public OnTranslationRemovedListenerAdapter(TextSelector selector) {
        mSelector = selector;
    }

    @Override
    public void onNotShoved() {
        mSelector.unSelect();
    }
}