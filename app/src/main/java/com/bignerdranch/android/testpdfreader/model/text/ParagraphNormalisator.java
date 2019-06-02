package com.bignerdranch.android.testpdfreader.model.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParagraphNormalisator {
    private  static int MAX_PARAGRAPH_LENGTH = 300;

    public static List<String> normalise(String text){
        List<String> res = new ArrayList<>();

        text = text.trim();
        if(text.isEmpty()) return new ArrayList<>();

        if(text.length() > MAX_PARAGRAPH_LENGTH){
            res.addAll(split(text));
        } else {
            res.add(text);
        }

        return res;
    }

    private static Pattern pattern = Pattern.compile("[(...).!?:]");
    private static List<String> split(String text){
        int length = text.length();
        if(length > MAX_PARAGRAPH_LENGTH){
            Matcher matcher = pattern.matcher(text);
            if(matcher.find(MAX_PARAGRAPH_LENGTH/2)) {
                int point = matcher.start();
                if (point > 0 && point < text.length() - 5) {
                    ArrayList<String> res = new ArrayList<>();

                    res.addAll(split(text.substring(0, point + 1)));
                    res.addAll(split(text.substring(point + 1)));
                    return res;
                }
            }
        }
        return new ArrayList<>(Collections.singletonList(text));
    }

}
