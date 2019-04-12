package com.bignerdranch.android.testpdfreader.model.storage.resource;

import java.sql.Timestamp;

public interface IMetaData {

    String getState();

    void setState(String state);

    Timestamp getTimeLastOpened();

    void setTimeLastOpened(Timestamp date);

}
