package com.bignerdranch.android.testpdfreader.model.translator;

import android.content.Context;

public interface Translator {

    void translateWord(String word, WordTranslateListener listener, Context context);

    void translatePhrase(String word, TranslationListener listener, Context context);

}
