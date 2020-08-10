package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Profile implements Serializable {
    @SerializedName("meet_id")
    @Expose
    private String meet_id;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("matrimonial_profile")
    @Expose
    private MatrimonialProfile matrimonialProfile;
    @SerializedName("user_status")
    @Expose
    private String userStatus;
    @SerializedName("isApproved")
    @Expose
    private String isApproved;
    @SerializedName("isPremium")
    @Expose
    private Boolean isPremium;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("isVerified")
    @Expose
    private boolean isVerified;
    @SerializedName("is_active")
    @Expose
    private int isActive;
    @SerializedName("is_deleted")
    @Expose
    private int isDeleted;
    @SerializedName("isBan")
    @Expose
    private boolean isBan;
    @SerializedName("profile_visit_count")
    @Expose
    private int profile_visit_count;
    @SerializedName("idApproved")
    @Expose
    private boolean idApproved;
    @SerializedName("educationApproved")
    @Expose
    private boolean educationApproved;
    @SerializedName("isEducationVerified")
    @Expose
    private boolean isEducationVerified;
    @SerializedName("isIdVerified")
    @Expose
    private boolean isIdVerified;

    public String getMeet_id() {
        return meet_id;
    }

    public void setMeet_id(String meet_id) {
        this.meet_id = meet_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MatrimonialProfile getMatrimonialProfile() {
        return matrimonialProfile;
    }

    public void setMatrimonialProfile(MatrimonialProfile matrimonialProfile) {
        this.matrimonialProfile = matrimonialProfile;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public Boolean getPremium() {
        return isPremium;
    }

    public void setPremium(Boolean premium) {
        isPremium = premium;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isBan() {
        return isBan;
    }

    public void setBan(boolean ban) {
        isBan = ban;
    }

    public int getProfile_visit_count() {
        return profile_visit_count;
    }

    public void setProfile_visit_count(int profile_visit_count) {
        this.profile_visit_count = profile_visit_count;
    }

    public boolean isIdApproved() {
        return idApproved;
    }

    public void setIdApproved(boolean idApproved) {
        this.idApproved = idApproved;
    }

    public boolean isEducationApproved() {
        return educationApproved;
    }

    public void setEducationApproved(boolean educationApproved) {
        this.educationApproved = educationApproved;
    }

    public boolean isEducationVerified() {
        return isEducationVerified;
    }

    public void setEducationVerified(boolean educationVerified) {
        isEducationVerified = educationVerified;
    }

    public boolean isIdVerified() {
        return isIdVerified;
    }

    public void setIdVerified(boolean idVerified) {
        isIdVerified = idVerified;
    }
}
