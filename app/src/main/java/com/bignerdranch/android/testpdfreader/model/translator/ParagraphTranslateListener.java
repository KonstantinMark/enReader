package com.bignerdranch.android.testpdfreader.model.translator;

public interface ParagraphTranslateListener extends TranslateErrorListener {

    void translateIsDone(String paragraph);
}
