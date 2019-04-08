package com.bignerdranch.android.testpdfreader.control.content;

public interface ITextSelectedReceiver {

    void textSelected(String text);

    void paragraphSelected(String paragraph, TranslationReceiver receiver);

}
