package com.octopusbjsindia.models.login;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginInfo implements Parcelable {

    public static final Creator<LoginInfo> CREATOR = new Creator<LoginInfo>() {
        @Override
        public LoginInfo createFromParcel(Parcel in) {
            return new LoginInfo(in);
        }

        @Override
        public LoginInfo[] newArray(int size) {
            return new LoginInfo[size];
        }
    };

    private String mobileNumber;
    private String otp;
    private String device_id;

    public LoginInfo() {

    }

    private LoginInfo(Parcel in) {
        mobileNumber = in.readString();
        otp = in.readString();
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOneTimePassword() {
        return otp;
    }

    public void setOneTimePassword(String otp) {
        this.otp = otp;
    }

    public String getDeviceId() {
        return device_id;
    }

    public void setDeviceId(String deviceId) {
        this.device_id = deviceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mobileNumber);
        parcel.writeString(otp);
    }
}
