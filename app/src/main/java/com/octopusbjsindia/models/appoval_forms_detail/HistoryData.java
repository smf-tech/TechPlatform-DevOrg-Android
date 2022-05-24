package com.octopusbjsindia.models.appoval_forms_detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryData {


        @SerializedName("metadata")
        @Expose
        private List<Metadatum> metadata = null;
        @SerializedName("values")
        @Expose
        private List<Value> values = null;

        public List<Metadatum> getMetadata() {
            return metadata;
        }

        public void setMetadata(List<Metadatum> metadata) {
            this.metadata = metadata;
        }

        public List<Value> getValues() {
            return values;
        }

        public void setValues(List<Value> values) {
            this.values = values;
        }

    }