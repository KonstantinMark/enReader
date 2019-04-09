package com.bignerdranch.android.testpdfreader.model.word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Translation implements Serializable {
    private PartOfSpeech mPartOfSpeech;
    private List<String> mWordTranslations;

    public Translation() {
        mWordTranslations = new ArrayList<>();
    }

    public PartOfSpeech getPartOfSpeech() {
        return mPartOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        mPartOfSpeech = partOfSpeech;
    }

    public List<String> getWordTranslations() {
        return mWordTranslations;
    }

    public void addTranslation(String wordTranslation) {
        mWordTranslations.add(wordTranslation);
    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();

        for (String s : mWordTranslations) {

            if (result.length() == 0) {
            } else {
                result.append(", ");
            }

            result.append(s);
        }

        return result.toString();
    }
}