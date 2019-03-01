package com.platform.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.models.common.Category;
import com.platform.models.common.Microservice;
import com.platform.models.forms.Components;

import java.io.Serializable;
import java.lang.reflect.Type;

public class DataCategoryConverter implements Serializable {

    @TypeConverter // note this annotation
    public String fromCategory(Category category) {
        if (category == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Category>() {
        }.getType();
        return gson.toJson(category, type);
    }

    @TypeConverter // note this annotation
    public Category toCategory(String category) {
        if (category == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Category>() {
        }.getType();
        return gson.fromJson(category, type);
    }
}