package com.octopusbjsindia.models.smartgirl;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainerList {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String name;
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
    /*@SerializedName("registeredOn")
    @Expose
    private RegisteredOn registeredOn;*/
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("mockTestDone")
    @Expose
    private Boolean mockTestDone;
    @SerializedName("mockTestGrade")
    @Expose
    private String mockTestGrade;
    /*@SerializedName("mockTestDate")
    @Expose
    private MockTestDate mockTestDate;
    @SerializedName("mockTestDateTiming")
    @Expose
    private MockTestDateTiming mockTestDateTiming;*/

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

/*    public RegisteredOn getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(RegisteredOn registeredOn) {
        this.registeredOn = registeredOn;
    }*/

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getMockTestDone() {
        return mockTestDone;
    }

    public void setMockTestDone(Boolean mockTestDone) {
        this.mockTestDone = mockTestDone;
    }

    public String getMockTestGrade() {
        return mockTestGrade;
    }

    public void setMockTestGrade(String mockTestGrade) {
        this.mockTestGrade = mockTestGrade;
    }

 /*   public MockTestDate getMockTestDate() {
        return mockTestDate;
    }

    public void setMockTestDate(MockTestDate mockTestDate) {
        this.mockTestDate = mockTestDate;
    }

    public MockTestDateTiming getMockTestDateTiming() {
        return mockTestDateTiming;
    }

    public void setMockTestDateTiming(MockTestDateTiming mockTestDateTiming) {
        this.mockTestDateTiming = mockTestDateTiming;
    }
*/
}
