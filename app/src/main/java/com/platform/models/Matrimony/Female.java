package com.platform.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Female {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("markAttendance")
    @Expose
    private Boolean markAttendance;
    @SerializedName("interviewDone")
    @Expose
    private Boolean interviewDone;
    @SerializedName("paymentDone")
    @Expose
    private Boolean paymentDone;
    @SerializedName("isApproved")
    @Expose
    private String isApproved;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("badge")
    @Expose
    private String badge;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getMarkAttendance() {
        return markAttendance;
    }

    public void setMarkAttendance(Boolean markAttendance) {
        this.markAttendance = markAttendance;
    }

    public Boolean getInterviewDone() {
        return interviewDone;
    }

    public void setInterviewDone(Boolean interviewDone) {
        this.interviewDone = interviewDone;
    }

    public Boolean getPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(Boolean paymentDone) {
        this.paymentDone = paymentDone;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }


}
