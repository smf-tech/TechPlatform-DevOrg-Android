package com.platform.models.appconfig;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class AppConfigResponse {

        @SerializedName("_id")
        @Expose
        private String _id;
        @SerializedName("appUpdate")
        @Expose
        private AppUpdate appUpdate;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public AppUpdate getAppUpdate() {
            return appUpdate;
        }

        public void setAppUpdate(AppUpdate appUpdate) {
            this.appUpdate = appUpdate;
        }

    }