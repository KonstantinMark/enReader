package com.bignerdranch.android.testpdfreader.model.word;

import java.util.HashMap;
import java.util.List;

public class WordTool {

    private Word mWord;

    public WordTool(Word word) {
        mWord = word;
    }

    public String translationToStringLine() {
        StringBuilder translation = new StringBuilder();
        HashMap<PartOfSpeech, List<Translation>> translations = mWord.getTranslations();

        boolean isCommaNeeded_2 = false;
        for (List<Translation> trs : translations.values()) {

            for (Translation tr : trs) {
                if (isCommaNeeded_2) {
                    translation.append(", ");
                }

                translation.append(tr.toString());

                isCommaNeeded_2 = true;
            }
        }

        return translation.toString();
    }

}
