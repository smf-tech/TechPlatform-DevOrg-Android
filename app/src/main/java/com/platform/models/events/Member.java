package com.platform.models.events;

import java.io.Serializable;

public class Member implements Serializable {
    private String id;
    private String name;
    private String role;
    private Boolean isMemberSelected;
    private Boolean isMemberAttended;


    public Member(String id, String name, String role, Boolean isMemberSelected, Boolean isMemberAttended) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.isMemberSelected = isMemberSelected;
        this.isMemberAttended = isMemberAttended;
    }

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

    public Boolean getMemberAttended() {
        return isMemberAttended;
    }

    public void setMemberAttended(Boolean memberAttended) {
        isMemberAttended = memberAttended;
    }

}
