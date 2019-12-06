package com.octopus.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MatrimonyRolesUsers implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("userDetails")
    @Expose
    private List<MatrimonyUserDetails> userDetails = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<MatrimonyUserDetails> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<MatrimonyUserDetails> userDetails) {
        this.userDetails = userDetails;
    }

}
