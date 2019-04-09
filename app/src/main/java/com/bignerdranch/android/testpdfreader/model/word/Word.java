package com.bignerdranch.android.testpdfreader.model.word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Word implements Serializable {

    private String mWord;
    private String mTranscription;
    private HashMap<PartOfSpeech, List<Translation>> mTranslations;

    public Word() {
        mTranslations = new HashMap<>();
    }

    public String getWord() {
        return mWord;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public String getTranscription() {
        return mTranscription;
    }

    public void setTranscription(String transcription) {
        mTranscription = transcription;
    }

    public boolean addTranslation(Translation translation) {

        if (!mTranslations.containsKey(translation.getPartOfSpeech())) {
            mTranslations.put(translation.getPartOfSpeech(), new ArrayList<Translation>());
        }

        return Objects.requireNonNull(mTranslations.get(translation.getPartOfSpeech())).add(translation);
    }

    public HashMap<PartOfSpeech, List<Translation>> getTranslations() {
        return mTranslations;
    }

    @Override
    public String toString() {

        if (getWord() == null) return null;

        StringBuilder resp = new StringBuilder(getWord());

        if (getTranscription() != null && getTranscription().length() > 0) {
            resp.append(" [").append(getTranscription()).append("]");
        }

        if (getTranslations().size() > 0) {
            resp.append('\n');
        }

        for (PartOfSpeech pos : getTranslations().keySet()) {
            if (pos != null) {
                resp.append(pos.getRussianName()).append(": ");

                boolean addCommaFlag = false;

                for (Translation tr : Objects.requireNonNull(getTranslations().get(pos))) {
                    if (addCommaFlag) {
                        resp.append(", ");
                    }
                    resp.append(tr.toString());
                    addCommaFlag = true;
                }


                resp.append('\n');
            }
        }
        return resp.toString();
    }
}
