package com.platform.models.events;

import com.platform.models.profile.UserLocation;

import java.util.ArrayList;

public class ParametersFilterMember {
    ArrayList<String> organizationIds = new ArrayList<>();
    ArrayList<String> roleIds = new ArrayList<>();
    UserLocation userLocation = new UserLocation();

    public ArrayList<String> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(ArrayList<String> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public ArrayList<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(ArrayList<String> roleIds) {
        this.roleIds = roleIds;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }
}
