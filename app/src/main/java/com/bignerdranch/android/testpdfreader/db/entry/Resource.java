package com.bignerdranch.android.testpdfreader.db.entry;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.db.converter.DateConverter;
import com.bignerdranch.android.testpdfreader.db.converter.ResourceTypeConverter;
import com.bignerdranch.android.testpdfreader.db.converter.UriConverter;
import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceType;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    @Ignore
    public Drawable drawable;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(uri, resource.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri);
    }
}
