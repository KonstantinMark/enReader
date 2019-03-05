package com.bignerdranch.android.testpdfreader.model;

import android.net.Uri;

import java.io.Serializable;

public class ResourceDescriptor implements Serializable {
    public static final int DEFAULT_TYPE = 0;
    public static final int PDF_FULL_TYPE = 10;
    public static final int PDF_JS_TYPE = 11;
    public static final int PDF_MOBILE_TYPE = 15;
    public static final int TXT_TYPE = 20;

    private String mDescription;

    private int mType;

    public ResourceDescriptor(String description, int type) {
        mDescription = description;
        mType = type;
    }
    public ResourceDescriptor(String description){
        this(description, DEFAULT_TYPE);
    }

    public String getDescription() {
        return mDescription;
    }

    public int getType() {
        return mType;
    }
}
