package com.platform.models.login;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginInfo implements Parcelable {

    private String mobileNumber;
    private String otp;

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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
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
}
