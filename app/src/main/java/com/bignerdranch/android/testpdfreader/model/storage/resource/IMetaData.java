package com.bignerdranch.android.testpdfreader.model.storage.resource;

import java.sql.Timestamp;

public interface IMetaData {

    int getCurrentPage();

    void setCurrentPage(int currentPage);

    int getItemOnPage();

    void setItemOnPage(int itemOnPage);

    Timestamp getTimeLastOpened();

    void setTimeLastOpened(Timestamp date);

}
