package com.platform.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Participant implements Serializable {

    private String role;
    private Boolean isMemberSelected;

    public Participant(String id, String name, String role, Boolean isMemberSelected, Boolean isMemberAttended) {
        this.participantId = id;
//        this.name = name;
        this.role = role;
        this.isMemberSelected = isMemberSelected;
        this.attended = isMemberAttended;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getMemberSelected() {
        return isMemberSelected;
    }

    public void setMemberSelected(Boolean memberSelected) {
        isMemberSelected = memberSelected;
    }

    //////////////////////////////////

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("participantId")
    @Expose
    private String participantId;
    @SerializedName("attended")
    @Expose
    private Boolean attended;
    @SerializedName("accepted")
    @Expose
    private Boolean accepted;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("createdDateTime")
    @Expose
    private Integer createdDateTime;
    @SerializedName("updatedDateTime")
    @Expose
    private Integer updatedDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public Boolean getAttended() {
        return attended;
    }

    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Integer getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Integer createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Integer getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Integer updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }
}
