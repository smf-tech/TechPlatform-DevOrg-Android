package com.platform.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class Participant implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("role_name")
    @Expose
    private String roleName;
    @SerializedName("attended")
    @Expose
    private String attended;
    @SerializedName("MemberSelected")
    @Expose
    private boolean memberSelected;

    public Participant(String id, String name, String roleName, boolean memberSelected) {
        this.id = id;
        this.name = name;
        this.roleName = roleName;
        this.memberSelected = memberSelected;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isMemberSelected() {
        return memberSelected;
    }

    public void setMemberSelected(boolean memberSelected) {
        this.memberSelected = memberSelected;
    }

    public String getAttended() {
        return attended;
    }

    public void setAttended(String attended) {
        this.attended = attended;
    }
}
