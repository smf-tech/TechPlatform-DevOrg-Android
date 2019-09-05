package com.platform.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Other_marital_information {

    @SerializedName("about_me")
    @Expose
    private String about_me;
    @SerializedName("profile_image")
    @Expose
    private String profile_image;
    @SerializedName("activity_achievements")
    @Expose
    private String activity_achievements;
    @SerializedName("other_remarks")
    @Expose
    private String other_remarks;
    @SerializedName("aadhar_url")
    @Expose
    private String aadhar_url;
    @SerializedName("educational")
    @Expose
    private String educational;

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getActivity_achievements() {
        return activity_achievements;
    }

    public void setActivity_achievements(String activity_achievements) {
        this.activity_achievements = activity_achievements;
    }

    public String getOther_remarks() {
        return other_remarks;
    }

    public void setOther_remarks(String other_remarks) {
        this.other_remarks = other_remarks;
    }

    public String getAadhar_url() {
        return aadhar_url;
    }

    public void setAadhar_url(String aadhar_url) {
        this.aadhar_url = aadhar_url;
    }

    public String getEducational() {
        return educational;
    }

    public void setEducational(String educational) {
        this.educational = educational;
    }

}