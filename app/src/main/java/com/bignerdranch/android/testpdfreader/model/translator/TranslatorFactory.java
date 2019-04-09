package com.bignerdranch.android.testpdfreader.model.translator;

import com.bignerdranch.android.testpdfreader.model.translator.YandexTranslator.YandexTranslator;

public class TranslatorFactory {

    public Translator newInstance() {
        YandexTranslator translator = new YandexTranslator();
        return translator;
    }

}
