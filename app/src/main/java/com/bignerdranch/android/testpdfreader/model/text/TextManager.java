package com.bignerdranch.android.testpdfreader.model.text;

import java.util.ArrayList;
import java.util.List;

public class TextManager {
    private String mText;
    private static String PARAGRAPH_SPACE = "        ";
    private static int PARAGRAPH_MAX_CHAR_COUNT = 400;

    public TextManager(String text){
        mText = text;
    }

    private static boolean isLowerCaseLetter(char c) {
        return (c >= 'a' && c <= 'z');
    }

    private static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '’';
    }

    public String getNormalisedText(){
        String[] tmp = mText.split("\n");

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : tmp) {
            if (s.length() > 0) {
                if (isLowerCaseLetter(s.charAt(0))) {
                    stringBuilder.append(' ');
                } else {
                    stringBuilder.append('\n');
                }
                stringBuilder.append(s);
            }
        }


        if (stringBuilder.length() > 0 && stringBuilder.charAt(0) == '\n')
            stringBuilder.deleteCharAt(0);

        return stringBuilder.toString().replaceAll(";", ";\n");
    }

    public String getWordSelection(int startPosition) {
        String word = null;
        int pos[] = getWordSelectionPositions(startPosition);
        int wordStartPosition = pos[0];
        int wordEndPosition = pos[1];

        if (wordStartPosition < wordEndPosition) {
            word = mText.substring(wordStartPosition, wordEndPosition).replaceAll("\\s", "");

            if (word.length() == 0) {
                word = null;
            }
        }

        return word;
    }

    public int[] getWordSelectionPositions(int startPosition) {

        int wordStartPosition = startPosition;
        int wordEndPosition = startPosition;

        if (startPosition >= mText.length()) {
            return new int[]{0, 0};
        }

        try {
            if (isLetter(mText.charAt(wordStartPosition))) {
                while (isLetter(mText.charAt(wordStartPosition - 1))) {
                    wordStartPosition--;
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }


        do {
            wordEndPosition++;
        } while (mText.length() > wordEndPosition &&
                isLetter(mText.charAt(wordEndPosition)));

        if (wordStartPosition < wordEndPosition) {
            return new int[]{wordStartPosition, wordEndPosition};
        }

        return new int[]{0, 0};
    }

    public List<String> getParagraphs() {
        String text = getNormalisedText();
        ArrayList<String> result = new ArrayList<>();
        String paragraphs[] = text.split("\n");
        for (String prg : paragraphs) {
            if (prg.trim().length() > 0) result.add(prg);
        }
        return result;
    }
}
