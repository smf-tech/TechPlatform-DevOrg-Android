package com.octopus.view.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.octopus.models.LocaleData;
import com.octopus.utility.Constants;

import java.io.IOException;

public class LocaleDataAdapter extends TypeAdapter<LocaleData> {
    public LocaleData read(JsonReader reader) throws IOException {

        LocaleData localeData = null;
        try {
            reader.beginObject();
            String fieldName;

            while (reader.hasNext()) {
                JsonToken token = reader.peek();

                if (token.equals(JsonToken.NAME)) {
                    //get the current token
                    fieldName = reader.nextName();

                    if (Constants.App.LANGUAGE_DEFAULT.equals(fieldName)) {
                        //move to next token
                        String en = (reader.nextString());
                        localeData = new LocaleData(en);
                    }

                    if (Constants.App.LANGUAGE_HINDI.equals(fieldName)) {
                        //move to next token
                        String hi = (reader.nextString());
                        if (localeData != null) {
                            localeData.setHi(hi);
                        }
                    }

                    if (Constants.App.LANGUAGE_MARATHI.equals(fieldName)) {
                        //move to next token
                        String mr = (reader.nextString());
                        if (localeData != null) {
                            localeData.setMr(mr);
                        }
                    }
                }
            }
            reader.endObject();
        } catch (IllegalStateException jse) {
            // We have to consume JSON document fully.
            String def = reader.nextString();
            localeData = new LocaleData(def);
        }
        return localeData;

    }

    public void write(JsonWriter writer, LocaleData localeData) throws IOException {
        if (localeData == null) {
            writer.value("");
        } else {
            writer.value(localeData.getLocaleValue());
        }
    }
}
