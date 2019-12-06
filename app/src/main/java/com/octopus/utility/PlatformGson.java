package com.octopus.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopus.models.LocaleData;
import com.octopus.view.adapters.LocaleDataAdapter;
import com.octopus.view.adapters.OIDAdapter;
import com.octopus.view.fragments.SubmittedFormsFragment;

public class PlatformGson {

    public static Gson getPlatformGsonInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocaleData.class, new LocaleDataAdapter());
        builder.registerTypeAdapter(SubmittedFormsFragment.OID.class, new OIDAdapter());

        return builder.create();
    }
}
