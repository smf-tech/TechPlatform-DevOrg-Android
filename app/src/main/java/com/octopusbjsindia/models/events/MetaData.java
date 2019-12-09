package com.octopusbjsindia.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class MetaData {

    @SerializedName("Per Page")
    @Expose
    private Integer perPage;
    @SerializedName("Total Pages")
    @Expose
    private Integer totalPages;
    @SerializedName("Total number of records")
    @Expose
    private Integer totalNumberOfRecords;

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalNumberOfRecords() {
        return totalNumberOfRecords;
    }

    public void setTotalNumberOfRecords(Integer totalNumberOfRecords) {
        this.totalNumberOfRecords = totalNumberOfRecords;
    }
}
