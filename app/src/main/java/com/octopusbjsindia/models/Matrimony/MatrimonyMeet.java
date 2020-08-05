package com.octopusbjsindia.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MatrimonyMeet implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("meetType")
    @Expose
    private String meetType;
    @SerializedName("location")
    @Expose
    private MeetLocation location;
    @SerializedName("venue")
    @Expose
    private String venue;
    @SerializedName("meetWebLink")
    @Expose
    private String meetWebLink;
    @SerializedName("schedule")
    @Expose
    private MeetSchedule schedule;
    @SerializedName("isRegPaid")
    @Expose
    private Boolean isRegPaid;
    @SerializedName("registrationSchedule")
    @Expose
    private RegistrationSchedule registrationSchedule;
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
    private ArrayList<MatrimonyUserDetails> meetReferences = null;
    @SerializedName("analytics")
    @Expose
    private ArrayList<MeetAnalytics> analytics = null;
    @SerializedName("is_published")
    @Expose
    private Boolean is_published;
    @SerializedName("isBadgeFanlize")
    @Expose
    private Boolean isBadgeFanlize;
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("is_archive")
    @Expose
    private Boolean isArchive;
    @SerializedName("is_allocate")
    @Expose
    private Boolean isAllocate;
    @SerializedName("meetCriteria")
    @Expose
    private MeetCriteria meetCriteria;
    @SerializedName("paymentInfo")
    @Expose
    private String paymentInfo;
    @SerializedName("note")
    @Expose
    private String note;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public MeetLocation getLocation() {
        return location;
    }

    public void setLocation(MeetLocation location) {
        this.location = location;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getMeetWebLink() {
        return meetWebLink;
    }

    public void setMeetWebLink(String meetWebLink) {
        this.meetWebLink = meetWebLink;
    }

    public MeetSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(MeetSchedule schedule) {
        this.schedule = schedule;
    }

    public Boolean getIsRegPaid() {
        return isRegPaid;
    }

    public void setIsRegPaid(Boolean isRegPaid) {
        this.isRegPaid = isRegPaid;
    }

    public RegistrationSchedule getRegistrationSchedule() {
        return registrationSchedule;
    }

    public void setRegistrationSchedule(RegistrationSchedule registrationSchedule) {
        this.registrationSchedule = registrationSchedule;
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

    public void setMeetReferences(ArrayList<MatrimonyUserDetails> meetReferences) {
        this.meetReferences = meetReferences;
    }

    public ArrayList<MeetAnalytics> getAnalytics() {
        return analytics;
    }

    public void setAnalytics(ArrayList<MeetAnalytics> analytics) {
        this.analytics = analytics;
    }

    public Boolean getIs_published() {
        return is_published;
    }

    public void setIs_published(Boolean is_published) {
        this.is_published = is_published;
    }

    public Boolean getBadgeFanlize() {
        return isBadgeFanlize;
    }

    public void setBadgeFanlize(Boolean badgeFanlize) {
        isBadgeFanlize = badgeFanlize;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Boolean getArchive() {
        return isArchive;
    }

    public void setArchive(Boolean archive) {
        isArchive = archive;
    }

    public Boolean getAllocate() {
        return isAllocate;
    }

    public void setAllocate(Boolean allocate) {
        isAllocate = allocate;
    }

    public MeetCriteria getMeetCriteria() {
        return meetCriteria;
    }

    public void setMeetCriteria(MeetCriteria meetCriteria) {
        this.meetCriteria = meetCriteria;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
