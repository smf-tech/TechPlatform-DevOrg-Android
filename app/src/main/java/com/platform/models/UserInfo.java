package com.platform.models;

import android.os.Parcel;
import android.os.Parcelable;

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

    private String userFirstName;
    private String userMiddleName;
    private String userLastName;
    private String userBirthDate;
    private String userEmailId;
    private String userGender;

    public UserInfo() {

    }

    private UserInfo(Parcel in) {
        userFirstName = in.readString();
        userMiddleName = in.readString();
        userLastName = in.readString();
        userBirthDate = in.readString();
        userEmailId = in.readString();
        userGender = in.readString();
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

    public String getUserBirthDate() {
        return userBirthDate;
    }

    public void setUserBirthDate(String userBirthDate) {
        this.userBirthDate = userBirthDate;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userFirstName);
        parcel.writeString(userMiddleName);
        parcel.writeString(userLastName);
        parcel.writeString(userBirthDate);
        parcel.writeString(userEmailId);
        parcel.writeString(userGender);
    }
}
