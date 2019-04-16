package com.bignerdranch.android.testpdfreader.model.storage.resource;

import com.bignerdranch.android.testpdfreader.db.entry.MetaData;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;

public class MetaDataManager {

    public static void setLastOpenedDateCurrent(MetaData metaData) {
        metaData.lastOpenedTime = new java.sql.Timestamp(new java.util.Date().getTime());
    }

    public static void setLastCurrentPageInformation(MetaData metaData, int page, int item) {
        metaData.currentPage = page;
        metaData.currentItem = item;
    }



}
