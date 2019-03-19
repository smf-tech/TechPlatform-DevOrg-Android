package com.platform.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.platform.models.LocaleData;
import com.platform.view.adapters.LocaleDataAdapter;
import com.platform.view.adapters.OIDAdapter;
import com.platform.view.fragments.SubmittedFormsFragment;

public class PlatformGson {

    public static Gson getPlatformGsonInstance() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocaleData.class, new LocaleDataAdapter());
        builder.registerTypeAdapter(SubmittedFormsFragment.OID.class, new OIDAdapter());

        return builder.create();
    }
}
