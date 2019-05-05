package com.bignerdranch.android.testpdfreader.controller.resource.resource_view.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageParagraphWrapper implements Serializable {
    public int id;
    public String content;
    public Boolean isCurrent = false;

    public static List<PageParagraphWrapper> wrap(List<String> list){
        List<PageParagraphWrapper> wrappers = new ArrayList<>();

        int counter = 0;
        for (String s: list){
            PageParagraphWrapper wrapper = new PageParagraphWrapper();
            wrapper.id = counter++;
            wrapper.content = s;
            wrappers.add(wrapper);
        }
        return wrappers;
    }

}
