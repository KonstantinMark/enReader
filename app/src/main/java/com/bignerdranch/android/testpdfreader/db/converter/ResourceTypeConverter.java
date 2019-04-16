package com.bignerdranch.android.testpdfreader.db.converter;

import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceType;

import androidx.room.TypeConverter;

public class ResourceTypeConverter {

    @TypeConverter
    public ResourceType fromResourceType(String value) {
        return value == null ? null : ResourceType.valueOf(value);
    }

    @TypeConverter
    public String stringToResourceType(ResourceType type) {
        return type == null ? null : type.toString();
    }
}
