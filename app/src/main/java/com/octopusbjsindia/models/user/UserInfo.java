package com.octopusbjsindia.models.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.profile.UserLocation;

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
    @SerializedName("email")
    @Expose
    private String userEmailId;
    @SerializedName("phone")
    @Expose
    private String userMobileNumber;
    @SerializedName("approve_status")
    @Expose
    private String approveStatus;
    @SerializedName("updatedDateTime")
    @Expose
    private Long updatedAt;
    @SerializedName("createdDateTime")
    @Expose
    private Long createdAt;
    @SerializedName("dob")
    @Expose
    private Long userBirthDate;
    @SerializedName("org_id")
    @Expose
    private JurisdictionType orgId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("role_id")
    @Expose
    private RoleData roleIds;
    @SerializedName("location")
    @Expose
    private UserLocation userLocation;
    @SerializedName("firebase_id")
    @Expose
    private String firebaseId;
    @SerializedName("project_id")
    @Expose
    private ArrayList<JurisdictionType> projectIds;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("gender")
    @Expose
    private String userGender;
    @SerializedName("approvers")
    @Expose
    private ArrayList<Approver> approvers = null;
    @SerializedName("device_id")
    @Expose
    private String device_id;
    @SerializedName("jurisdiction_type_id")
    @Expose
    private String jurisdictionTypeId;
    @SerializedName("multiple_location_level")
    @Expose
    private JurisdictionType multipleLocationLevel;
    @SerializedName("is_device_matched")
    @Expose
    private int isDeviceMatched;

    @SuppressWarnings("SameReturnValue")
    public static Creator<UserInfo> getCREATOR() {
        return CREATOR;
    }

    public UserInfo() {
    }

    public String getOrgId() {
        if (orgId != null) {
            return orgId.getId();
        }

        return "";
    }

    public String getOrgName() {
        return orgId.getName();
    }

    public void setOrgId(JurisdictionType orgId) {
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
        if (roleIds != null) {
            return roleIds.getId();
        }
        return "";
    }

    public String getRoleNames() {
        return roleIds.getName();
    }

    public void setRoleIds(RoleData roleIds) {
        this.roleIds = roleIds;
    }

    public int getRoleCode() {
        return roleIds.getRoleCode();
    }

    public ArrayList<JurisdictionType> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(ArrayList<JurisdictionType> projectIds) {
        this.projectIds = projectIds;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public ArrayList<Approver> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<Approver> approvers) {
        this.approvers = approvers;
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

    public Long getUserBirthDate() {
        return userBirthDate;
    }

    public void setUserBirthDate(Long userBirthDate) {
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

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getJurisdictionTypeId() {
        return jurisdictionTypeId;
    }

    public void setJurisdictionTypeId(String jurisdictionTypeId) {
        this.jurisdictionTypeId = jurisdictionTypeId;
    }

    public JurisdictionType getMultipleLocationLevel() {
        return multipleLocationLevel;
    }

    public void setMultipleLocationLevel(JurisdictionType multipleLocationLevel) {
        this.multipleLocationLevel = multipleLocationLevel;
    }

    public int getIsDeviceMatched() {
        return isDeviceMatched;
    }

    public void setIsDeviceMatched(int isDeviceMatched) {
        this.isDeviceMatched = isDeviceMatched;
    }

    private UserInfo(Parcel in) {
        id = in.readString();
        userFirstName = in.readString();
        userMiddleName = in.readString();
        userLastName = in.readString();
        userName = in.readString();
        userBirthDate = in.readLong();
        userMobileNumber = in.readString();
        userEmailId = in.readString();
        userGender = in.readString();
        approveStatus = in.readString();
        profilePic = in.readString();
        jurisdictionTypeId = in.readString();
        isDeviceMatched = in.readInt();
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
        parcel.writeLong(userBirthDate);
        parcel.writeString(userMobileNumber);
        parcel.writeString(userEmailId);
        parcel.writeString(userGender);
        parcel.writeString(approveStatus);
        parcel.writeString(profilePic);
        parcel.writeString(device_id);
        parcel.writeString(jurisdictionTypeId);
        parcel.writeInt(isDeviceMatched);
    }
}