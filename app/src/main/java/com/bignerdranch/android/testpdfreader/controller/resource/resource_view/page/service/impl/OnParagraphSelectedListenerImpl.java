package com.bignerdranch.android.testpdfreader.controller.resource.resource_view.page.service.impl;

import com.bignerdranch.android.testpdfreader.model.translator.TranslationListener;
import com.bignerdranch.android.testpdfreader.controller.resource.resource_view.page.service.OnParagraphSelectedListener;
import com.bignerdranch.android.testpdfreader.model.TextTranslationAction;
import com.bignerdranch.android.testpdfreader.controller.resource.service.TextTranslationActionListener;
import com.bignerdranch.android.testpdfreader.view.item.PdfMobilePageItemViewModel;

public class OnParagraphSelectedListenerImpl implements OnParagraphSelectedListener {
    private TextTranslationActionListener mTextSelectionManager;
    private PdfMobilePageItemViewModel mViewModel;
    private TranslationListener mTranslationListener;

    public OnParagraphSelectedListenerImpl(TextTranslationActionListener textSelectionManager,
                                           PdfMobilePageItemViewModel viewModel,
                                           TranslationListener translationListener) {
        mTextSelectionManager = textSelectionManager;
        mViewModel = viewModel;
        mTranslationListener = translationListener;
    }

    @Override
    public void onParagraphSelected(String paragraph) {
        mTextSelectionManager.onNewAction(new TextTranslationAction(
                TextTranslationAction.ActionType.ACTION_PART_PARAGRAPH_SELECTED)
                .setText(paragraph)
                .setTranslationListener(mTranslationListener));

        mViewModel.setTranslationLoadingAnimationVisibility(true);
    }
}
