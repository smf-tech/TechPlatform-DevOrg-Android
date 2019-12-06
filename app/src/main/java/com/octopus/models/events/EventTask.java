package com.octopus.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class EventTask implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("ownerid")
    @Expose
    private String ownerid;

    @SerializedName("ownername")
    @Expose
    private String ownername;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("thumbnail_image")
    @Expose
    private String thumbnailImage;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("participants")
    @Expose
    private List<Participant> participants;

    @SerializedName("registration_required")
    @Expose
    private boolean registrationRequired;

    @SerializedName("registration_schedule")
    @Expose
    private Schedule registrationSchedule;

    @SerializedName("is_mark_attendance_required")
    @Expose
    private boolean isMarkAttendanceRequired;

    @SerializedName("schedule")
    @Expose
    private Schedule schedule;

    @SerializedName("required_forms")
    @Expose
    private ArrayList<AddForm> requiredForms;

    @SerializedName("event_status")
    @Expose
    private String eventStatus;

    @SerializedName("mark_complete")
    @Expose
    private Boolean markComplete;

    @SerializedName("participants_count")
    @Expose
    private Integer participantsCount;

    @SerializedName("attended_completed")
    @Expose
    private Integer attendedCompleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public boolean isRegistrationRequired() {
        return registrationRequired;
    }

    public void setRegistrationRequired(boolean registrationRequired) {
        this.registrationRequired = registrationRequired;
    }

    public Schedule getRegistrationSchedule() {
        return registrationSchedule;
    }

    public void setRegistrationSchedule(Schedule registrationSchedule) {
        this.registrationSchedule = registrationSchedule;
    }

    public boolean isMarkAttendanceRequired() {
        return isMarkAttendanceRequired;
    }

    public void setMarkAttendanceRequired(boolean markAttendanceRequired) {
        isMarkAttendanceRequired = markAttendanceRequired;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public ArrayList<AddForm> getRequiredForms() {
        return requiredForms;
    }

    public void setRequiredForms(ArrayList<AddForm> requiredForms) {
        this.requiredForms = requiredForms;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Boolean getMarkComplete() {
        return markComplete;
    }

    public void setMarkComplete(Boolean markComplete) {
        this.markComplete = markComplete;
    }

    public Integer getParticipantsCount() {
        return participantsCount;
    }

    public void setParticipantsCount(Integer participantsCount) {
        this.participantsCount = participantsCount;
    }

    public Integer getAttendedCompleted() {
        return attendedCompleted;
    }

    public void setAttendedCompleted(Integer attendedCompleted) {
        this.attendedCompleted = attendedCompleted;
    }
}
