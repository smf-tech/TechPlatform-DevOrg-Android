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
    private String userGender;

    public UserInfo() {

    }

    private UserInfo(Parcel in) {
        userFirstName = in.readString();
        userGender = in.readString();
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userFirstName);
        parcel.writeString(userGender);
    }
}
