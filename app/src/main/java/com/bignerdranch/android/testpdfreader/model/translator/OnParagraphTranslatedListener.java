package com.bignerdranch.android.testpdfreader.model.translator;

public interface OnParagraphTranslatedListener extends TranslateErrorListener {

    void onParagraphTranslated(String paragraph);
}
