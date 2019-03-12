package com.platform.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.models.LocaleData;
import com.platform.models.common.Category;
import com.platform.models.common.Microservice;
import com.platform.models.forms.Components;
import com.platform.models.reports.ReportCategory;

import java.io.Serializable;
import java.lang.reflect.Type;

import androidx.room.TypeConverter;

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

    @TypeConverter // note this annotation
    public String fromLocaleData(LocaleData localeData) {
        if (localeData == null) {
            return (null);
        }

        Gson gson = new Gson();
        Type type = new TypeToken<LocaleData>() {
        }.getType();
        return gson.toJson(localeData, type);
    }

    @TypeConverter // note this annotation
    public LocaleData toLocaleData(String localeData) {
        if (localeData == null) {
            return (null);
        }

        Gson gson = new Gson();
        Type type = new TypeToken<LocaleData>() {
        }.getType();
        return gson.fromJson(localeData, type);
    }

    @TypeConverter // note this annotation
    public String fromReportCategory(ReportCategory category) {
        if (category == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ReportCategory>() {
        }.getType();
        return gson.toJson(category, type);
    }

    @TypeConverter // note this annotation
    public ReportCategory toReportCategory(String category) {
        if (category == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ReportCategory>() {
        }.getType();
        return gson.fromJson(category, type);
    }
}