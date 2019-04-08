package com.bignerdranch.android.testpdfreader.control.content.pdf_mobile_view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextManager {
    private String mText;

    public TextManager(String text){
        mText = text;
    }

    public String getNormalisedText(){
        mText = mText.replaceAll(" {2}", " ");
        mText = mText.replaceAll("[^.!?] \n", " ");
        mText = mText.replaceAll("[^.!?]\n", " ");
        mText = mText.replaceAll("\n", "\n        ");
        return mText;
    }

    public String getWordSelection(int startPosition) {
        return "text";
    }

    public List<String> getParagraphs() {
        String text = getNormalisedText();
        String paragraphs[] = text.split("\n");
        return new ArrayList<>(Arrays.asList(paragraphs));
    }
}
