package com.octopusbjsindia.models.tm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubFilterset implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("name")
    @Expose
    private Name_ name;
    private boolean isSelected =true;
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Name_ getName() {
        return name;
    }

    public void setName(Name_ name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeParcelable(this.name, flags);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public SubFilterset() {
    }

    protected SubFilterset(Parcel in) {
        this._id = in.readString();
        this.name = in.readParcelable(Name_.class.getClassLoader());
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SubFilterset> CREATOR = new Parcelable.Creator<SubFilterset>() {
        @Override
        public SubFilterset createFromParcel(Parcel source) {
            return new SubFilterset(source);
        }

        @Override
        public SubFilterset[] newArray(int size) {
            return new SubFilterset[size];
        }
    };
}