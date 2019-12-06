package com.octopus.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MatrimonyMasterRequestModel {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private List<DataList> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataList> getData() {
        return data;
    }

    public void setData(List<DataList> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class DataList {

        @SerializedName("_id")
        @Expose
        private String _id;
        @SerializedName("master_data")
        @Expose
        private List<Master_data> master_data = null;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public List<Master_data> getMaster_data() {
            return master_data;
        }

        public void setMaster_data(List<Master_data> master_data) {
            this.master_data = master_data;
        }

        public class Master_data {

            @SerializedName("key")
            @Expose
            private String key;
            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("values")
            @Expose
            private List<String> values = null;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<String> getValues() {
                return values;
            }

            public void setValues(List<String> values) {
                this.values = values;
            }

        }
    }
}