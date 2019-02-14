package com.platform.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.models.forms.Components;

import java.io.Serializable;
import java.lang.reflect.Type;

public class DataConverter implements Serializable {

    @TypeConverter // note this annotation
    public String fromComponents(Components components) {
        if (components == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Components>() {
        }.getType();
        return gson.toJson(components, type);
    }

    @TypeConverter // note this annotation
    public Components toComponents(String componentsString) {
        if (componentsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Components>() {
        }.getType();
        return gson.fromJson(componentsString, type);
    }

}