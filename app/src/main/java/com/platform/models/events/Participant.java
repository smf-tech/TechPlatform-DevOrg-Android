package com.platform.models.events;

import java.io.Serializable;

public class Participant implements Serializable {

    private String name;
    private String role;
    private Boolean isMemberSelected;



    public Participant(String id, String name, String role, Boolean isMemberSelected, Boolean isMemberAttended) {
        this.participantId = id;
        this.name = name;
        this.role = role;
        this.isMemberSelected = isMemberSelected;
        this.attended = isMemberAttended;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    private String participantId;
    private Boolean attended;

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

}
