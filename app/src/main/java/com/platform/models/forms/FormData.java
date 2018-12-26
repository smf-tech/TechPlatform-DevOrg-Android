package com.platform.models.forms;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormData implements Parcelable {

    public static final Creator<FormData> CREATOR = new Creator<FormData>() {

        public FormData createFromParcel(Parcel in) {
            FormData formData = new FormData();
            formData.type = ((String) in.readValue((String.class.getClassLoader())));
            formData.name = ((String) in.readValue((String.class.getClassLoader())));
            return formData;
        }

        public FormData[] newArray(int size) {
            return new FormData[size];
        }
    };
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(type);
        parcel.writeValue(name);
    }
}
