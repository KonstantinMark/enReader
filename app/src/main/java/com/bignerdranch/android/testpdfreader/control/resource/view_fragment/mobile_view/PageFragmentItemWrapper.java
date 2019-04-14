package com.bignerdranch.android.testpdfreader.control.resource.view_fragment.mobile_view;

import java.io.Serializable;

public class PageFragmentItemWrapper implements Serializable {
    private String mContent;
    private Boolean isMarker = false;

    public PageFragmentItemWrapper(String content) {
        mContent = content;
    }

    public Boolean isMarker() {
        return isMarker;
    }

    public void setMarker(Boolean marker) {
        isMarker = marker;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
