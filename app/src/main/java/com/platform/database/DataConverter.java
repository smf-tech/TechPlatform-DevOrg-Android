package com.platform.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.models.common.Category;
import com.platform.models.common.Microservice;
import com.platform.models.forms.Components;
import com.platform.models.forms.FormResult;

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

    @TypeConverter // note this annotation
    public String fromMicroservice(Microservice microservice) {
        if (microservice == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Microservice>() {
        }.getType();
        return gson.toJson(microservice, type);
    }

    @TypeConverter // note this annotation
    public Microservice toMicroservice(String microservice) {
        if (microservice == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Microservice>() {
        }.getType();
        return gson.fromJson(microservice, type);
    }

}