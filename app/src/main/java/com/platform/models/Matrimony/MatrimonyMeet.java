package com.platform.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MatrimonyMeet implements Serializable {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("meetType")
    @Expose
    private String meetType;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("venue")
    @Expose
    private String venue;
    @SerializedName("dateTime")
    @Expose
    private long dateTime;
    @SerializedName("meetStartTime")
    @Expose
    private String meetStartTime;
    @SerializedName("meetEndTime")
    @Expose
    private String meetEndTime;
    @SerializedName("isRegPaid")
    @Expose
    private Boolean isRegPaid;
    @SerializedName("regStartDateTime")
    @Expose
    private long regStartDateTime;
    @SerializedName("regEndDateTime")
    @Expose
    private long regEndDateTime;
    @SerializedName("regAmount")
    @Expose
    private Integer regAmount;
    @SerializedName("isOnlinePaymentAllowed")
    @Expose
    private Boolean isOnlinePaymentAllowed;
    @SerializedName("meetImageUrl")
    @Expose
    private String meetImageUrl;
    @SerializedName("meetOrganizers")
    @Expose
    private List<MatrimonyUserDetails> meetOrganizers = null;
    @SerializedName("meetReferences")
    @Expose
    private List<MatrimonyUserDetails> meetReferences = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMeetType() {
        return meetType;
    }

    public void setMeetType(String meetType) {
        this.meetType = meetType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getMeetStartTime() {
        return meetStartTime;
    }

    public void setMeetStartTime(String meetStartTime) {
        this.meetStartTime = meetStartTime;
    }

    public String getMeetEndTime() {
        return meetEndTime;
    }

    public void setMeetEndTime(String meetEndTime) {
        this.meetEndTime = meetEndTime;
    }

    public Boolean getIsRegPaid() {
        return isRegPaid;
    }

    public void setIsRegPaid(Boolean isRegPaid) {
        this.isRegPaid = isRegPaid;
    }

    public long getRegStartDateTime() {
        return regStartDateTime;
    }

    public void setRegStartDateTime(long regStartDateTime) {
        this.regStartDateTime = regStartDateTime;
    }

    public long getRegEndDateTime() {
        return regEndDateTime;
    }

    public void setRegEndDateTime(long regEndDateTime) {
        this.regEndDateTime = regEndDateTime;
    }

    public Integer getRegAmount() {
        return regAmount;
    }

    public void setRegAmount(Integer regAmount) {
        this.regAmount = regAmount;
    }

    public Boolean getIsOnlinePaymentAllowed() {
        return isOnlinePaymentAllowed;
    }

    public void setIsOnlinePaymentAllowed(Boolean isOnlinePaymentAllowed) {
        this.isOnlinePaymentAllowed = isOnlinePaymentAllowed;
    }

    public String getMeetImageUrl() {
        return meetImageUrl;
    }

    public void setMeetImageUrl(String meetImageUrl) {
        this.meetImageUrl = meetImageUrl;
    }

    public List<MatrimonyUserDetails> getMeetOrganizers() {
        return meetOrganizers;
    }

    public void setMeetOrganizers(List<MatrimonyUserDetails> meetOrganizers) {
        this.meetOrganizers = meetOrganizers;
    }

    public List<MatrimonyUserDetails> getMeetReferences() {
        return meetReferences;
    }

    public void setMeetReferences(List<MatrimonyUserDetails> meetReferences) {
        this.meetReferences = meetReferences;
    }

}
