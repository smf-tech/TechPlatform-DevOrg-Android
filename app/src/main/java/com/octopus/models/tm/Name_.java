package com.octopus.models.tm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Name_ implements Parcelable {

    @SerializedName("default")
    @Expose
    private String _default;
    @SerializedName("hi")
    @Expose
    private String hi;
    @SerializedName("mr")
    @Expose
    private String mr;

    protected Name_(Parcel in) {
        _default = in.readString();
        hi = in.readString();
        mr = in.readString();
    }

    public static final Creator<Name_> CREATOR = new Creator<Name_>() {
        @Override
        public Name_ createFromParcel(Parcel in) {
            return new Name_(in);
        }

        @Override
        public Name_[] newArray(int size) {
            return new Name_[size];
        }
    };

    public String getDefault() {
        return _default;
    }

    public void setDefault(String _default) {
        this._default = _default;
    }

    public String getHi() {
        return hi;
    }

    public void setHi(String hi) {
        this.hi = hi;
    }

    public String getMr() {
        return mr;
    }

    public void setMr(String mr) {
        this.mr = mr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_default);
        dest.writeString(hi);
        dest.writeString(mr);
    }
}