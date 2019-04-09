package com.bignerdranch.android.testpdfreader.model.translator;

import com.bignerdranch.android.testpdfreader.model.word.Word;

public interface WordTranslateListener extends TranslateErrorListener {

    void translateIsDone(Word word);
}
