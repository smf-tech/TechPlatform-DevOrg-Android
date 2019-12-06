package com.octopus.models.tm;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterSet {

        @SerializedName("id")
        @Expose
        private int id;
        @SerializedName("filterType")
        @Expose
        private String filterType;
        @SerializedName("name")
        @Expose
        private Name name;
        @SerializedName("filterset")
        @Expose
        private List<SubFilterset> filterset = null;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFilterType() {
            return filterType;
        }

        public void setFilterType(String filterType) {
            this.filterType = filterType;
        }

        public Name getName() {
            return name;
        }

        public void setName(Name name) {
            this.name = name;
        }

        public List<SubFilterset> getFilterset() {
            return filterset;
        }

        public void setFilterset(List<SubFilterset> filterset) {
            this.filterset = filterset;
        }

    }