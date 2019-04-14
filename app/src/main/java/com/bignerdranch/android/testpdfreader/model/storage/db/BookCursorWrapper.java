package com.bignerdranch.android.testpdfreader.model.storage.db;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceType;

import java.sql.Timestamp;

public class BookCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public BookCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Uri getUri() {
        return Uri.parse(get(BookDbSchema.BookTable.Cols.URI));
    }

    public ResourceType getType() {
        return ResourceType.valueOf(get(BookDbSchema.BookTable.Cols.TYPE));
    }

    public int getCurrentPage() {
        return Integer.parseInt(get(BookDbSchema.BookTable.Cols.CURRENT_PAGE));
    }

    public int getScrollOnPage() {
        return Integer.parseInt(get(BookDbSchema.BookTable.Cols.ITEM_ON_PAGE));
    }
    public Timestamp getTime() {
        return Timestamp.valueOf(get(BookDbSchema.BookTable.Cols.DATE_LAST_OPENED));
    }

    private String get(String col) {
        return getString(getColumnIndex(col));
    }
}
