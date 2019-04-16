package com.bignerdranch.android.testpdfreader.db.entry;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.db.converter.DateConverter;
import com.bignerdranch.android.testpdfreader.db.converter.ResourceTypeConverter;
import com.bignerdranch.android.testpdfreader.db.converter.UriConverter;
import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceType;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "resource")
@TypeConverters({DateConverter.class, UriConverter.class, ResourceTypeConverter.class})
public class Resource {
    @NonNull
    @PrimaryKey
    public Uri uri;
    public String name;
    public ResourceType type;

    @NonNull
    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public ResourceType getType() {
        return type;
    }
}
