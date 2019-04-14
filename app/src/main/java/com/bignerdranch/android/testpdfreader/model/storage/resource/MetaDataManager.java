package com.bignerdranch.android.testpdfreader.model.storage.resource;

public class MetaDataManager {

    public void setLastOpenedDateCurrent(IResource resource) {
        IMetaData metaData = resource.getMetaData();
        metaData.setTimeLastOpened(new java.sql.Timestamp(new java.util.Date().getTime()));
    }

    public void setLastCurrentPageInformation(IResource resource, int page, int item) {
        IMetaData metaData = resource.getMetaData();
        metaData.setCurrentPage(page);
        metaData.setItemOnPage(item);
    }



}
