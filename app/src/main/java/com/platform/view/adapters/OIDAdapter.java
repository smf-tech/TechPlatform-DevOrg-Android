package com.platform.view.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.platform.utility.Constants;
import com.platform.view.fragments.SubmittedFormsFragment;

import java.io.IOException;

public class OIDAdapter extends TypeAdapter<SubmittedFormsFragment.OID> {
    public SubmittedFormsFragment.OID read(JsonReader reader) throws IOException {

        SubmittedFormsFragment.OID oid = null;
        try {
            reader.beginObject();
            String fieldName;

            while (reader.hasNext()) {
                JsonToken token = reader.peek();

                if (token.equals(JsonToken.NAME)) {
                    //get the current token
                    fieldName = reader.nextName();

                    if (Constants.FormDynamicKeys.OID.equals(fieldName)) {
                        //move to next token
                        String id = (reader.nextString());
                        oid = new SubmittedFormsFragment.OID(id);
                    }
                }
            }
            reader.endObject();
        } catch (IllegalStateException jse) {
            // We have to consume JSON document fully.
            String def = reader.nextString();
            oid = new SubmittedFormsFragment.OID(def);
        }
        return oid;

    }

    public void write(JsonWriter writer, SubmittedFormsFragment.OID oid) throws IOException {
        if (oid == null) {
            writer.value("");
        } else {
            writer.value(oid.getOID());
        }
    }
}
