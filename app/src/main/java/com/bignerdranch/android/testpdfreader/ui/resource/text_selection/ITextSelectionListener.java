package com.bignerdranch.android.testpdfreader.ui.resource.text_selection;

import com.bignerdranch.android.testpdfreader.model.translator.OnParagraphTranslatedListener;

public interface ITextSelectionListener {

    void wordSelected(String text, TextSelector unselectListener);

    void paragraphSelected(String paragraph, OnParagraphTranslatedListener receiver);

    void allUnSelected();

}
