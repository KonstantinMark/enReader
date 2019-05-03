package com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.android.testpdfreader.view.item.PdfMobilePageItemViewModel;

public class OnTranslateBtnClickListener implements View.OnClickListener {
    private PdfMobilePageItemViewModel mViewModel;
    private TextSelectionManager mTextSelectionManager;

    public OnTranslateBtnClickListener(PdfMobilePageItemViewModel viewModel, TextSelectionManager textSelectionManager) {
        mViewModel = viewModel;
        mTextSelectionManager = textSelectionManager;
    }

    @Override
    public void onClick(View v) {
        if (!mViewModel.isTranslationVisible()) {
            mViewModel.setTranslationLoadingAnimationVisibility(true);
            mTextSelectionManager.paragraphSelected(
                    mViewModel.getContent(), mViewModel);
        } else {
            mViewModel.setTranslationLoadingAnimationVisibility(false);
        }
//                newMarkerIntent(mMarker);
    }
}