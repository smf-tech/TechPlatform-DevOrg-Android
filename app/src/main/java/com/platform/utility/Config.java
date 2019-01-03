package com.platform.utility;

public class Config {

    private static Config instance;

    private Config() {
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }

        return instance;
    }


    @SuppressWarnings("SameReturnValue")
    public String getAppMode() {
        return Constants.App.BJS_MODE;
    }
}
