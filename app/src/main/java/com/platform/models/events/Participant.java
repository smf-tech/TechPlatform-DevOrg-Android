package com.platform.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Participant implements Serializable {

    private String role;
    private Boolean isMemberSelected=false;



    public Participant(String id, String name, String role, Boolean isMemberSelected, Boolean isMemberAttended) {
        this.participantId = id;
        this.name = name;
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

    @SerializedName("participantId")
    @Expose
    private String participantId;
    @SerializedName("attended")
    @Expose
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


    ///////////////////////////////////

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("project_id")
    @Expose
    private List<String> projectId = null;
    @SerializedName("role_id")
    @Expose
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getProjectId() {
        return projectId;
    }

    public void setProjectId(List<String> projectId) {
        this.projectId = projectId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}
