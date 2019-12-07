package com.octopusbjsindia.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileList {

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("matrimonial_profile")
    @Expose
    private Matrimonial_profile matrimonial_profile;
    @SerializedName("matrimonial_meets")
    @Expose
    private List<String> matrimonial_meets = null;
    @SerializedName("user_status")
    @Expose
    private String user_status;
    @SerializedName("isApproved")
    @Expose
    private String isApproved;
    @SerializedName("isPremium")
    @Expose
    private boolean isPremium;

    public boolean isMarkAttendance() {
        return markAttendance;
    }

    public void setMarkAttendance(boolean markAttendance) {
        this.markAttendance = markAttendance;
    }

    public boolean isInterviewDone() {
        return interviewDone;
    }

    public void setInterviewDone(boolean interviewDone) {
        this.interviewDone = interviewDone;
    }

    public boolean isPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(boolean paymentDone) {
        this.paymentDone = paymentDone;
    }

    @SerializedName("markAttendance")
    @Expose
    private boolean markAttendance;

    @SerializedName("interviewDone")
    @Expose
    private boolean interviewDone;

    @SerializedName("paymentDone")
    @Expose
    private boolean paymentDone;

    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("createdDateTime")
    @Expose
    private int createdDateTime;
    @SerializedName("updatedDateTime")
    @Expose
    private int updatedDateTime;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Matrimonial_profile getMatrimonial_profile() {
        return matrimonial_profile;
    }

    public void setMatrimonial_profile(Matrimonial_profile matrimonial_profile) {
        this.matrimonial_profile = matrimonial_profile;
    }

    public List<String> getMatrimonial_meets() {
        return matrimonial_meets;
    }

    public void setMatrimonial_meets(List<String> matrimonial_meets) {
        this.matrimonial_meets = matrimonial_meets;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public boolean isIsPremium() {
        return isPremium;
    }

    public void setIsPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(int createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(int updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

}