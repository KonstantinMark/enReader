package com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page;

import com.bignerdranch.android.testpdfreader.model.translator.OnParagraphTranslatedListener;
import com.bignerdranch.android.testpdfreader.ui.resource.text_selection.ITextSelectionListener;
import com.bignerdranch.android.testpdfreader.ui.resource.text_selection.TextSelector;

public class TextSelectionManager implements ITextSelectionListener{
    private ITextSelectionListener mITextSelectionListener;


    public TextSelectionManager(ITextSelectionListener ITextSelectionListener) {
        mITextSelectionListener = ITextSelectionListener;
    }

    private TextSelector currentSelected;

    public void notifyTextSelected(String text, TextSelector listener) {
        mITextSelectionListener.wordSelected(text, listener);
        if (currentSelected != listener) removeCurrentSelection();
        currentSelected = listener;
    }

    public void removeCurrentSelection() {
        if (currentSelected != null)
            currentSelected.unSelect();
    }

    @Override
    public void wordSelected(String text, TextSelector unselectListener) {
        mITextSelectionListener.wordSelected(text, unselectListener);
    }

    @Override
    public void paragraphSelected(String paragraph, OnParagraphTranslatedListener receiver) {
        mITextSelectionListener.paragraphSelected(paragraph, receiver);
    }

    @Override
    public void allUnSelected() {
        mITextSelectionListener.allUnSelected();
    }
}
