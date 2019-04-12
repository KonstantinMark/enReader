package com.bignerdranch.android.testpdfreader.model.storage.resource;

public class MetaDataManager {

    public void setLastOpenedDateCurrent(IResource resource) {
        IMetaData metaData = resource.getMetaData();
        metaData.setTimeLastOpened(new java.sql.Timestamp(new java.util.Date().getTime()));
    }

}
