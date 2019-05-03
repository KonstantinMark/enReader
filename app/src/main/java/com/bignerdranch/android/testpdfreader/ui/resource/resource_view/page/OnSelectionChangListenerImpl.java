package com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page;

import android.widget.TextView;

import com.bignerdranch.android.testpdfreader.model.components.SelectionAdaptedTextView;
import com.bignerdranch.android.testpdfreader.model.translator.OnParagraphTranslatedListener;
import com.bignerdranch.android.testpdfreader.view.PdfMobilePageFragmentViewModel;
import com.bignerdranch.android.testpdfreader.view.item.PdfMobileItemViewModel;
import com.bignerdranch.android.testpdfreader.view.item.PdfMobilePageItemViewModel;

public class OnSelectionChangListenerImpl implements SelectionAdaptedTextView.OnSelectionChangListener {

    TextSelectionManager mTextSelectionManager;
    private PdfMobilePageItemViewModel mViewModel;
    private TextView mTextView;

    public OnSelectionChangListenerImpl(TextSelectionManager textSelectionManager,
                                        PdfMobilePageItemViewModel viewModel,
                                        TextView textView) {
        mTextSelectionManager = textSelectionManager;
        mViewModel = viewModel;
        mTextView = textView;
    }

    @Override
            public void onSelectionChanged(int selStart, int selEnd) {
                if (selStart != selEnd) {
                    mTextSelectionManager.paragraphSelected(
                            mTextView.getText().toString().substring(selStart, selEnd),
                            mViewModel);
                    mViewModel.setTranslationLoadingAnimationVisibility(true);
                }
            }
        }