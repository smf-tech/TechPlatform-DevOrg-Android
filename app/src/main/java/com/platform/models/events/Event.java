package com.platform.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Event implements Serializable {
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
    @SerializedName("startdatetime")
    @Expose
    private String startdatetime;
    @SerializedName("enddatetime")
    @Expose
    private String enddatetime;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("participants")
    @Expose
    private List<Participant> participants = null;
    @SerializedName("registration_required")
    @Expose
    private boolean registrationRequired;
    @SerializedName("registration_schedule")
    @Expose
    private RegistrationSchedule registrationSchedule = null;
    @SerializedName("is_mark_attendance_required")
    @Expose
    private boolean isMarkAttendanceRequired;


    private ArrayList<String> selectedForms;

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

    public List<Participant> getParticipants() {
        return participants;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public RegistrationSchedule getRegistrationSchedule() {
        return registrationSchedule;
    }

    public void setRegistrationSchedule(RegistrationSchedule registrationSchedule) {
        this.registrationSchedule = registrationSchedule;
    }

    public String getStartdatetime() {
        return startdatetime;
    }

    public void setStartdatetime(String startdatetime) {
        this.startdatetime = startdatetime;
    }

    public String getEnddatetime() {
        return enddatetime;
    }

    public void setEnddatetime(String enddatetime) {
        this.enddatetime = enddatetime;
    }

    public boolean isRegistrationRequired() {
        return registrationRequired;
    }

    public void setRegistrationRequired(boolean registrationRequired) {
        this.registrationRequired = registrationRequired;
    }

    public boolean isMarkAttendanceRequired() {
        return isMarkAttendanceRequired;
    }

    public void setMarkAttendanceRequired(boolean markAttendanceRequired) {
        isMarkAttendanceRequired = markAttendanceRequired;
    }

    public ArrayList<String> getSelectedForms() {
        return selectedForms;
    }

    public void setSelectedForms(ArrayList<String> selectedForms) {
        this.selectedForms = selectedForms;
    }

}
