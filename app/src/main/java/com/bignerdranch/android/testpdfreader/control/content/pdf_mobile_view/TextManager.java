package com.bignerdranch.android.testpdfreader.control.content.pdf_mobile_view;

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
}
