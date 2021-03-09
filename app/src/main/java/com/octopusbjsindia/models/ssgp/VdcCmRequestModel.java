package com.octopusbjsindia.models.ssgp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VdcCmRequestModel {
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("taluka_id")
    @Expose
    private String talukaId;
    @SerializedName("village_id")
    @Expose
    private String villageId;
    @SerializedName("date")
    @Expose
    private long date;
    @SerializedName("activity_type")
    @Expose
    private String activityType;
    @SerializedName("activity_purpose")
    @Expose
    private String activityPurpose;
    @SerializedName("participant_key1")
    @Expose
    private String participantKey1;
    @SerializedName("participant_key2")
    @Expose
    private String participantKey2;
    @SerializedName("participant_key3")
    @Expose
    private String participantKey3;
    @SerializedName("total_participant_no")
    @Expose
    private String totalParticipantNo;
    @SerializedName("activity_discussion")
    @Expose
    private String activityDiscussion;
    @SerializedName("activity_photo")
    @Expose
    private String activityPhoto;
    @SerializedName("meeting_photo")
    @Expose
    private String meetingPhoto;
    @SerializedName("attendance_meeting_photo")
    @Expose
    private String attendanceMeetingPhoto;
    @SerializedName("comment")
    @Expose
    private String comment;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(String talukaId) {
        this.talukaId = talukaId;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityPurpose() {
        return activityPurpose;
    }

    public void setActivityPurpose(String activityPurpose) {
        this.activityPurpose = activityPurpose;
    }

    public String getParticipantKey1() {
        return participantKey1;
    }

    public void setParticipantKey1(String participantKey1) {
        this.participantKey1 = participantKey1;
    }

    public String getParticipantKey2() {
        return participantKey2;
    }

    public void setParticipantKey2(String participantKey2) {
        this.participantKey2 = participantKey2;
    }

    public String getParticipantKey3() {
        return participantKey3;
    }

    public void setParticipantKey3(String participantKey3) {
        this.participantKey3 = participantKey3;
    }

    public String getTotalParticipantNo() {
        return totalParticipantNo;
    }

    public void setTotalParticipantNo(String totalParticipantNo) {
        this.totalParticipantNo = totalParticipantNo;
    }

    public String getActivityDiscussion() {
        return activityDiscussion;
    }

    public void setActivityDiscussion(String activityDiscussion) {
        this.activityDiscussion = activityDiscussion;
    }

    public String getActivityPhoto() {
        return activityPhoto;
    }

    public void setActivityPhoto(String activityPhoto) {
        this.activityPhoto = activityPhoto;
    }

    public String getMeetingPhoto() {
        return meetingPhoto;
    }

    public void setMeetingPhoto(String meetingPhoto) {
        this.meetingPhoto = meetingPhoto;
    }

    public String getAttendanceMeetingPhoto() {
        return attendanceMeetingPhoto;
    }

    public void setAttendanceMeetingPhoto(String attendanceMeetingPhoto) {
        this.attendanceMeetingPhoto = attendanceMeetingPhoto;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}

