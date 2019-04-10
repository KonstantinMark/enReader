package com.bignerdranch.android.testpdfreader.model.word;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

public class PartOfSpeech implements Serializable {

    private static final PartOfSpeech NOUN = new PartOfSpeech(0, "noun", "сущ.", new ArrayList<String>());
    private static final PartOfSpeech VERB = new PartOfSpeech(1, "verb", "гл.", new ArrayList<String>());
    private static final PartOfSpeech ADJECTIVE = new PartOfSpeech(2, "adjective", "прил.", new ArrayList<String>());
    private static final PartOfSpeech NUMERAL = new PartOfSpeech(3, "numeral", "числ.", new ArrayList<String>());
    private static final PartOfSpeech PRONOUN = new PartOfSpeech(4, "pronoun", "мест.", new ArrayList<String>());
    private static final PartOfSpeech ADVERB = new PartOfSpeech(5, "adverb", "нар.", new ArrayList<String>());
    private static final PartOfSpeech ARTICLE = new PartOfSpeech(6, "article", "арт.", new ArrayList<String>());
    private static final PartOfSpeech PREPOSITION = new PartOfSpeech(7, "preposition", "предл.", new ArrayList<String>());
    private static final PartOfSpeech CONJUNCTION = new PartOfSpeech(8, "conjunction", "союз", new ArrayList<String>());
    private static final PartOfSpeech INTERJECTION = new PartOfSpeech(9, "interjection", "междом.", new ArrayList<String>());

    @SuppressLint("UseSparseArrays")
    private static HashMap<Integer, PartOfSpeech> mItems = new HashMap<>();

    static {
        mItems.put(NOUN.getId(), NOUN);
        mItems.put(VERB.getId(), VERB);
        mItems.put(ADJECTIVE.getId(), ADJECTIVE);
        mItems.put(NUMERAL.getId(), NUMERAL);
        mItems.put(PRONOUN.getId(), PRONOUN);
        mItems.put(ADVERB.getId(), ADVERB);
        mItems.put(ARTICLE.getId(), ARTICLE);
        mItems.put(PREPOSITION.getId(), PREPOSITION);
        mItems.put(CONJUNCTION.getId(), CONJUNCTION);
        mItems.put(INTERJECTION.getId(), INTERJECTION);

        NOUN.addReduction("n");
        VERB.addReduction("v");
        ADJECTIVE.addReduction("adj");
        NUMERAL.addReduction("num");
        PRONOUN.addReduction("pron");
        ADVERB.addReduction("adv");
        ARTICLE.addReduction("art");
        PREPOSITION.addReduction("prep");
        CONJUNCTION.addReduction("conj");
        INTERJECTION.addReduction("interj");
    }

    private int mId;
    private String mName;
    private String mRussianName;
    private List<String> mReductions;

    private PartOfSpeech() {
    }

    private PartOfSpeech(int id, String name, String russianName, List<String> reductions) {
        mId = id;
        mName = name;
        mRussianName = russianName;
        mReductions = reductions;
    }

    public static PartOfSpeech getPartOfSpeech(int id) {
        for (PartOfSpeech ps : mItems.values()) {
            if (ps.getId() == id) {
                return ps;
            }
        }
        return null;
    }

    public static PartOfSpeech definePartOfSpeech(String partOfSpeech) {
        for (PartOfSpeech ps : mItems.values()) {
            if (ps.getName().equals(partOfSpeech)) {
                return ps;
            }

            for (String reduction : ps.getReductions()) {
                if (reduction.equals(partOfSpeech)) {
                    return ps;
                }
            }
        }
        return null;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getRussianName() {
        return mRussianName;
    }

    public String getReduction() {
        return mReductions.get(0);
    }

    private List<String> getReductions() {
        return mReductions;
    }

    public void addReduction(String reduction) {
        mReductions.add(reduction);
    }

    @NonNull
    @Override
    public String toString() {
        return getReduction();
    }
}
