package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProfileDetailData {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("isPaid")
    @Expose
    private Boolean isPaid;
    @SerializedName("matrimonial_profile")
    @Expose
    private MatrimonialProfile matrimonialProfile;
    @SerializedName("profile_image")
    @Expose
    private List<String> profileImage = null;
    @SerializedName("isShortlistedUser")
    @Expose
    private Boolean isShortlistedUser;
    @SerializedName("yourInterested")
    @Expose
    private Boolean yourInterested;
    @SerializedName("interestedInyou")
    @Expose
    private Boolean interestedInyou;
    @SerializedName("meetStatus")
    @Expose
    private String meetStatus;
    @SerializedName("badgeNumber")
    @Expose
    private int badgeNumber=0;
    @SerializedName("contactUnlocked")
    @Expose
    private Boolean contactUnlocked;
    @SerializedName("contactVisibleForPremium")
    @Expose
    private Boolean contactVisibleForPremium;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public MatrimonialProfile getMatrimonialProfile() {
        return matrimonialProfile;
    }

    public void setMatrimonialProfile(MatrimonialProfile matrimonialProfile) {
        this.matrimonialProfile = matrimonialProfile;
    }

    public List<String> getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(List<String> profileImage) {
        this.profileImage = profileImage;
    }

    public Boolean getIsShortlistedUser() {
        return isShortlistedUser;
    }

    public void setIsShortlistedUser(Boolean isShortlistedUser) {
        this.isShortlistedUser = isShortlistedUser;
    }

    public Boolean getYourInterested() {
        return yourInterested;
    }

    public void setYourInterested(Boolean yourInterested) {
        this.yourInterested = yourInterested;
    }

    public Boolean getInterestedInyou() {
        return interestedInyou;
    }

    public void setInterestedInyou(Boolean interestedInyou) {
        this.interestedInyou = interestedInyou;
    }

    public String getMeetStatus() {
        return meetStatus;
    }

    public void setMeetStatus(String meetStatus) {
        this.meetStatus = meetStatus;
    }

    public Boolean getContactUnlocked() {
        return contactUnlocked;
    }

    public void setContactUnlocked(Boolean contactUnlocked) {
        this.contactUnlocked = contactUnlocked;
    }

    public Boolean getContactVisibleForPremium() {
        return contactVisibleForPremium;
    }

    public int getBadgeNumber() {
        return badgeNumber;
    }
}
