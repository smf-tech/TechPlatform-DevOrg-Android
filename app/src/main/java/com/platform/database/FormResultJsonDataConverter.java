package com.platform.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;

public class FormResultJsonDataConverter implements Serializable {

    @TypeConverter // note this annotation
    public String fromComponents(JSONObject components) {
        if (components == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<JSONObject>() {
        }.getType();
        return gson.toJson(components, type);
    }

    @TypeConverter // note this annotation
    public JSONObject toComponents(String componentsString) {
        if (componentsString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<JSONObject>() {
        }.getType();
        return gson.fromJson(componentsString, type);
    }

}