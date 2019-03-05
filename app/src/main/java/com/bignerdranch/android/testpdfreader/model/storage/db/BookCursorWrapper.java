package com.bignerdranch.android.testpdfreader.model.storage.db;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;
import com.bignerdranch.android.testpdfreader.model.storage.resource.ResourceBuilder;
import com.bignerdranch.android.testpdfreader.model.storage.resource.ResourceType;

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
        String stringUri  = getString(getColumnIndex(BookDbSchema.BookTable.Cols.URI));
        return Uri.parse(stringUri);
    }
}
