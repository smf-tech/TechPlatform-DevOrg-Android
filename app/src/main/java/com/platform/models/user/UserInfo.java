package com.platform.models.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.models.profile.UserLocation;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class UserInfo implements Parcelable {

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("firstname")
    @Expose
    private String userFirstName;
    @SerializedName("middlename")
    @Expose
    private String userMiddleName;
    @SerializedName("lastname")
    @Expose
    private String userLastName;
    @SerializedName("name")
    @Expose
    private String userName;
    @SerializedName("dob")
    @Expose
    private String userBirthDate;
    @SerializedName("phone")
    @Expose
    private String userMobileNumber;
    @SerializedName("email")
    @Expose
    private String userEmailId;
    @SerializedName("gender")
    @Expose
    private String userGender;
    @SerializedName("approve_status")
    @Expose
    private String approveStatus;
    @SerializedName("org_id")
    @Expose
    private String orgId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("role_id")
    @Expose
    private String roleIds;
    @SerializedName("location")
    @Expose
    private UserLocation userLocation;
    @SerializedName("projects")
    @Expose
    private ArrayList<String> projectIds;

    @SuppressWarnings("SameReturnValue")
    public static Creator<UserInfo> getCREATOR() {
        return CREATOR;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public ArrayList<String> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(ArrayList<String> projectIds) {
        this.projectIds = projectIds;
    }

    public UserInfo() {

    }

    private UserInfo(Parcel in) {
        id = in.readString();
        userFirstName = in.readString();
        userMiddleName = in.readString();
        userLastName = in.readString();
        userName = in.readString();
        userBirthDate = in.readString();
        userMobileNumber = in.readString();
        userEmailId = in.readString();
        userGender = in.readString();
        approveStatus = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserMiddleName() {
        return userMiddleName;
    }

    public void setUserMiddleName(String userMiddleName) {
        this.userMiddleName = userMiddleName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserBirthDate() {
        return userBirthDate;
    }

    public void setUserBirthDate(String userBirthDate) {
        this.userBirthDate = userBirthDate;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(userFirstName);
        parcel.writeString(userMiddleName);
        parcel.writeString(userLastName);
        parcel.writeString(userName);
        parcel.writeString(userBirthDate);
        parcel.writeString(userMobileNumber);
        parcel.writeString(userEmailId);
        parcel.writeString(userGender);
        parcel.writeString(approveStatus);
    }
}