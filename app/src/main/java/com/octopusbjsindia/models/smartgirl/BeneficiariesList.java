package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BeneficiariesList {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("markAttendance")
    @Expose
    private Boolean markAttendance;
    @SerializedName("interviewDone")
    @Expose
    private Boolean interviewDone;
    @SerializedName("preFeedBackStatus")
    @Expose
    private Boolean preFeedBackStatus;
    @SerializedName("postFeedBackStatus")
    @Expose
    private Boolean postFeedBackStatus;
    @SerializedName("isApproved")
    @Expose
    private String isApproved;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Boolean getPreFeedBackStatus() {
        return preFeedBackStatus;
    }

    public void setPreFeedBackStatus(Boolean preFeedBackStatus) {
        this.preFeedBackStatus = preFeedBackStatus;
    }

    public Boolean getPostFeedBackStatus() {
        return postFeedBackStatus;
    }

    public void setPostFeedBackStatus(Boolean postFeedBackStatus) {
        this.postFeedBackStatus = postFeedBackStatus;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}

/*
public class BeneficiariesList {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("profilePic")
    @Expose
    private Object profilePic;
    @SerializedName("markAttendance")
    @Expose
    private Boolean markAttendance;
    @SerializedName("interviewDone")
    @Expose
    private Boolean interviewDone;
    @SerializedName("isApproved")
    @Expose
    private String isApproved;
    */
/*@SerializedName("registeredOn")
    @Expose
    private RegisteredOn registeredOn;*//*


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Object profilePic) {
        this.profilePic = profilePic;
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

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

*/
/*    public RegisteredOn getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(RegisteredOn registeredOn) {
        this.registeredOn = registeredOn;
    }*//*


}
*/
