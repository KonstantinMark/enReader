package com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.impl;

import android.view.View;

import com.bignerdranch.android.testpdfreader.model.TextTranslationAction;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.TextTranslationActionListener;
import com.bignerdranch.android.testpdfreader.view.item.PdfMobilePageItemViewModel;

public class OnTranslateBtnClickListener implements View.OnClickListener {
    private PdfMobilePageItemViewModel mViewModel;
    private TextTranslationActionListener mTextSelectionManager;

    public OnTranslateBtnClickListener(PdfMobilePageItemViewModel viewModel, TextTranslationActionListener textSelectionManager) {
        mViewModel = viewModel;
        mTextSelectionManager = textSelectionManager;
    }

    @Override
    public void onClick(View v) {
        if (!mViewModel.isTranslationVisible()) {
            mViewModel.setTranslationLoadingAnimationVisibility(true);

            mTextSelectionManager.onNewAction(
                    new TextTranslationAction(TextTranslationAction.ActionType.ACTION_PARAGRAPH_SELECTED)
                .setText(mViewModel.getContent()).setTranslationListener(mViewModel));

        } else {
            mViewModel.setTranslationLoadingAnimationVisibility(false);
        }
    }
}