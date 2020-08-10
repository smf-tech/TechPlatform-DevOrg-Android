package com.octopusbjsindia.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Other_marital_information implements Serializable {

    @SerializedName("about_me")
    @Expose
    private String about_me;
    @SerializedName("expectation_from_partner")
    @Expose
    private String expectation_from_life_partner;

    @SerializedName("profile_image")
    @Expose
    private ArrayList<String> profile_image;
    @SerializedName("activity_achievements")
    @Expose
    private String activity_achievements;
    @SerializedName("other_remarks")
    @Expose
    private String other_remarks;
    @SerializedName("aadhar_url")
    @Expose
    private String aadhar_url;
    @SerializedName("educational_url")
    @Expose
    private String educational_url;
    @SerializedName("support_doc")
    @Expose
    private String support_doc;
    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    /*public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }*/

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


    public String getEducational_url() {
        return educational_url;
    }

    public void setEducational_url(String educational_url) {
        this.educational_url = educational_url;
    }
    public String getSupport_doc() {
        return support_doc;
    }

    public void setSupport_doc(String support_doc) {
        this.support_doc = support_doc;
    }
    public ArrayList<String> getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(ArrayList<String> profile_image) {
        this.profile_image = profile_image;
    }

    public String getExpectation_from_life_partner() {
        return expectation_from_life_partner;
    }

    public void setExpectation_from_life_partner(String expectation_from_life_partner) {
        this.expectation_from_life_partner = expectation_from_life_partner;
    }
}