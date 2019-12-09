package com.octopusbjsindia.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoleAccessList {

    @SerializedName("role_code")
    @Expose
    private Integer roleCode;
    @SerializedName("role_name")
    @Expose
    private String roleName;
    @SerializedName("role_access")
    @Expose
    private List<RoleAccessObject> roleAccess = null;

    public Integer getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(Integer roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<RoleAccessObject> getRoleAccess() {
        return roleAccess;
    }

    public void setRoleAccess(List<RoleAccessObject> roleAccess) {
        this.roleAccess = roleAccess;
    }
}
