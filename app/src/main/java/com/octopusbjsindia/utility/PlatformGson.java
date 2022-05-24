package com.octopusbjsindia.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.models.LocaleData;
import com.octopusbjsindia.view.adapters.LocaleDataAdapter;

public class PlatformGson {

    public static Gson getPlatformGsonInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocaleData.class, new LocaleDataAdapter());
        //builder.registerTypeAdapter(SubmittedFormsFragment.OID.class, new OIDAdapter());

        return builder.create();
    }
}
