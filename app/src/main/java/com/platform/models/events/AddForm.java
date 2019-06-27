package com.platform.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddForm {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private FormName name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FormName getName() {
        return name;
    }

    public void setName(FormName name) {
        this.name = name;
    }

    public class FormName{
        @SerializedName("default")
        @Expose
        private String _default;
        @SerializedName("mr")
        @Expose
        private String mr;
        @SerializedName("hi")
        @Expose
        private String hi;

        public String getDefault() {
            return _default;
        }

        public void setDefault(String _default) {
            this._default = _default;
        }

        public String getMr() {
            return mr;
        }

        public void setMr(String mr) {
            this.mr = mr;
        }

        public String getHi() {
            return hi;
        }

        public void setHi(String hi) {
            this.hi = hi;
        }
    }
}
