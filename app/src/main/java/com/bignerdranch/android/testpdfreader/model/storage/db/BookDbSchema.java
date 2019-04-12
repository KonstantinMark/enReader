package com.bignerdranch.android.testpdfreader.model.storage.db;

public class BookDbSchema {

    public static final class BookTable {
        public static final String NAME = "book";

        public static final class Cols{
            public static final String URI = "uri";
            public static final String TYPE = "type";
            public static final String STATE = "state";
            public static final String DATE_LAST_OPENED = "date_last_opened";
        }
    }
}
