package com.bignerdranch.android.testpdfreader.model.storage.resource;

import java.sql.Timestamp;

public class MetaData implements IMetaData {
    private String mState = "";
    private Timestamp mTime;

    @Override
    public String getState() {
        return mState;
    }

    @Override
    public void setState(String state) {
        mState = state;
    }

    @Override
    public Timestamp getTimeLastOpened() {
        return mTime;
    }

    @Override
    public void setTimeLastOpened(Timestamp date) {
        mTime = date;
    }
}
