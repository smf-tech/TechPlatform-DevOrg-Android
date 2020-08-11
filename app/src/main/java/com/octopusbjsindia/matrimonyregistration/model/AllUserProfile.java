package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllUserProfile {

    @SerializedName("current_page")
    @Expose
    private Integer current_page;
    @SerializedName("data")
    @Expose
    private List<Profile> allUserProfileLists = null;
    @SerializedName("first_page_url")
    @Expose
    private String first_page_url;
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("last_page")
    @Expose
    private Integer last_page;
    @SerializedName("last_page_url")
    @Expose
    private String last_page_url;
    @SerializedName("next_page_url")
    @Expose
    private String next_page_url;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("per_page")
    @Expose
    private Integer per_page;
    @SerializedName("prev_page_url")
    @Expose
    private String prev_page_url;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("total")
    @Expose
    private Integer total;

    public Integer getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(Integer current_page) {
        this.current_page = current_page;
    }

      /*  public List<AllUserProfileList> getData() {
            return data;
        }

        public void setData(List<AllUserProfileList> data) {
            this.data = data;
        }*/

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getLast_page() {
        return last_page;
    }

    public void setLast_page(Integer last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getPer_page() {
        return per_page;
    }

    public void setPer_page(Integer per_page) {
        this.per_page = per_page;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Profile> getAllUserProfileLists() {
        return allUserProfileLists;
    }

    public void setAllUserProfileLists(List<Profile> allUserProfileLists) {
        this.allUserProfileLists = allUserProfileLists;
    }
}
