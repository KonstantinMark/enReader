package com.bignerdranch.android.testpdfreader.model.translator;

public interface TranslationListener extends TranslateErrorListener {

    void onTranslated(String translation);
}
