package com.bignerdranch.android.testpdfreader.trash.textViewMod;

import android.graphics.Color;
import android.text.Spannable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelectionInfo {
    private static final String TAG = "SelectionInfo";

    private static int COLOR_SELECTED_BACKGROUND = Color.BLUE;
    private static int COLOR_SELECTED_TEXT = Color.WHITE;
    private static int COLOR_UNSELECTED_BACKGROUND = Color.GRAY;
    private static int COLOR_UNSELECTED_TEXT = Color.DKGRAY;

    public static final int SELECTED_WORD = 0;//new ForegroundColorSpan(Color.RED);
    public static final int UNSELECTED_WORD = 1;//new ForegroundColorSpan(Color.GRAY);

    private static String WORD_REG_EXPRESSION = "(\\w|-)+";
    private static String SENTENCE_REG_EXPRESSION = "[^.?!]+[.?!]";

    private List<Selection> mWordsSelections;
    private List<Sentence> mSentencesOffsets;

    private Spannable mSpannable;

    private int mColorSelectedBackground;
    private int mColorSelectedText;
    private int mColorUnSelectedBackground;
    private int mColorUnSelectedText;

    public SelectionInfo(Spannable text){
        mSpannable = text;
        setColorSelected(
                COLOR_SELECTED_BACKGROUND,
                COLOR_SELECTED_TEXT);
        setColorUnSelected(
                COLOR_UNSELECTED_BACKGROUND,
                COLOR_UNSELECTED_TEXT);
    }

    private void fillWordArray(){
        mWordsSelections = new ArrayList<>();
        mSentencesOffsets = new ArrayList<>();

        Pattern p = Pattern.compile(WORD_REG_EXPRESSION);
        Matcher m = p.matcher(mSpannable.toString());

        while (m.find()) {
            mWordsSelections.add(new Selection(m.start(), m.end(), UNSELECTED_WORD));
        }

        p = Pattern.compile(SENTENCE_REG_EXPRESSION);
        m = p.matcher(mSpannable.toString());

        Log.i(TAG, mSpannable.toString());
        while (m.find()){
            mSentencesOffsets.add(new Sentence(m.start(), m.end() - 1));
        }
    }

    public void changSelection(int offset){
        if(mWordsSelections == null)  fillWordArray();

        Selection s = findSelectionByOffset(offset);
        if(s == null) return;

        removeSpan(s);

        if (s.getSpanType() == SELECTED_WORD) {
            s.setSpanType(UNSELECTED_WORD);
        } else {
            s.setSpanType(SELECTED_WORD);
        }

        applySelection();
    }

    private boolean hasSelection(){
        if(mWordsSelections == null) return false;

        for (Selection s: mWordsSelections){
            if(s.getSpanType() == SELECTED_WORD) return true;
        }

        return false;
    }

    public void changSelectionSentence(int offset){
        if(mWordsSelections == null)  fillWordArray();

        Sentence s = findSentenceByOffset(offset);
        if(s == null) return;

        boolean isSentenceSelected = isSentenceSelected(s);

        if(!isSentenceSelected) removeSelection();

        for (Selection selection: mWordsSelections){
            if(s.belongs(selection.getStart())){
                mSpannable.removeSpan(selection.getSpan());
                if(isSentenceSelected){
                    selection.setSpanType(UNSELECTED_WORD);
                } else {
                    selection.setSpanType(SELECTED_WORD);
                }
            }
        }

        applySelection();

        if(isSentenceSelected) removeSelection();
    }

    private boolean isSentenceSelected(Sentence sentence){
        for (Selection s: mWordsSelections){
            if(sentence.belongs(s.getStart())
            && s.getSpanType() != SELECTED_WORD){
                return false;
            }
        }
        return true;
    }

    private void setSpan(Selection s){
        mSpannable.setSpan(s.getSpan(), s.getStart(), s.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void removeSpan(Selection s){
        mSpannable.removeSpan(s.getSpan());
    }

    private Selection findSelectionByOffset(int offset){
        for (Selection s: mWordsSelections){
            if(offset >= s.getStart() && offset <= s.getEnd()){
                return s;
            }
        }
        return null;
    }

    private Sentence findSentenceByOffset(int offset){
        for (Sentence s: mSentencesOffsets){
            if(s.belongs(offset)) return s;
        }
        return null;
    }

    public void applySelection(){
        if(!hasSelection()) {
            removeSelection();
            return;
        }

        for (Selection s: mWordsSelections){
                mSpannable.setSpan(
                        s.getSpan(),
                        s.getStart(),
                        s.getEnd(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void removeSelection(){
        if(mSpannable != null && mWordsSelections != null) {
            for (Selection s : mWordsSelections) {
                mSpannable.removeSpan(s.getSpan());
                s.setSpanType(UNSELECTED_WORD);
            }
        }
    }

    private Object newSpan(int type){
        switch (type){
            case SELECTED_WORD:
                return new RoundedBackgroundSpan(
                        mColorSelectedBackground,
                        mColorSelectedText);
            case UNSELECTED_WORD:
                return new RoundedBackgroundSpan(
                        mColorUnSelectedBackground,
                        mColorUnSelectedText);
            default:
                return null;
        }
    }

    public String getSelectedText(){
        StringBuilder sb = new StringBuilder();

        if(mWordsSelections != null) {
            for (Selection s : mWordsSelections) {
                if (s.getSpanType() == SELECTED_WORD) {
                    sb.append(mSpannable.subSequence(s.getStart(), s.getEnd())).append(' ');
                }
            }

            // remove last space: ' '
            if(sb.length() > 0 && sb.charAt(sb.length()-1) == ' ')
                sb.deleteCharAt(sb.length()-1);

        }
        return sb.toString();
    }

    public void setColorSelected(int bg, int txt){
        mColorSelectedBackground = bg;
        mColorSelectedText = txt;
    }

    public void setColorUnSelected(int bg, int txt){
        mColorUnSelectedBackground = bg;
        mColorUnSelectedText = txt;
    }


    private class Selection{
        int mStart;
        int mEnd;
        private int mSpanType;
        private Object mSpan;

        public Selection(int start, int end, int spanType) {
            this.mStart = start;
            this.mEnd = end;
            this.mSpanType = spanType;
            this.mSpan = newSpan(spanType);
        }

        public Object getSpan() {
            return mSpan;
        }

        private void setSpan(Object span) {
            mSpan = span;
        }

        public int getSpanType() {
            return mSpanType;
        }

        public void setSpanType(int spanType) {
            mSpanType = spanType;
            setSpan(newSpan(spanType));
        }

        public int getStart() {
            return mStart;
        }

        public void setStart(int start) {
            this.mStart = start;
        }

        public int getEnd() {
            return mEnd;
        }

        public void setEnd(int end) {
            this.mEnd = end;
        }
    }

    private class Sentence {
        private int mStar;
        private int mEnd;

        public Sentence(int star, int end) {
            mStar = star;
            mEnd = end;
        }

        public boolean belongs(int offset){
            return offset >= getStar()
                    && offset <= getEnd();
        }

        public int getStar() {
            return mStar;
        }

        public void setStar(int star) {
            mStar = star;
        }

        public int getEnd() {
            return mEnd;
        }

        public void setEnd(int end) {
            mEnd = end;
        }
    }

}
