package com.bignerdranch.android.testpdfreader.model.storage.resource;

import java.sql.Timestamp;

public class MetaData implements IMetaData {
    private int mCurrentPage;
    private int mItemOnPage;
    private Timestamp mTime;

    @Override
    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    @Override
    public int getItemOnPage() {
        return mItemOnPage;
    }

    @Override
    public void setItemOnPage(int itemOnPage) {
        mItemOnPage = itemOnPage;
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
