package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OtherMaritalInformation implements Serializable {
    @SerializedName("about_me")
    @Expose
    private String aboutMe;
    @SerializedName("profile_image")
    @Expose
    private List<String> profileImage;
    @SerializedName("activity_achievements")
    @Expose
    private String activityAchievements;
    @SerializedName("expectation_from_partner")
    @Expose
    private String expectation_from_partner ;
    @SerializedName("other_remarks")
    @Expose
    private String otherRemarks;
    @SerializedName("aadhar_url")
    @Expose
    private String aadharUrl;
    @SerializedName("educational_url")
    @Expose
    private String educationalUrl;
    @SerializedName("support_doc")
    @Expose
    private String support_doc;

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public List<String> getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(List<String> profileImage) {
        this.profileImage = profileImage;
    }

    public String getActivityAchievements() {
        return activityAchievements;
    }

    public void setActivityAchievements(String activityAchievements) {
        this.activityAchievements = activityAchievements;
    }

    public String getOtherRemarks() {
        return otherRemarks;
    }

    public void setOtherRemarks(String otherRemarks) {
        this.otherRemarks = otherRemarks;
    }

    public String getAadharUrl() {
        return aadharUrl;
    }

    public void setAadharUrl(String aadharUrl) {
        this.aadharUrl = aadharUrl;
    }

    public String getEducationalUrl() {
        return educationalUrl;
    }

    public void setEducationalUrl(String educationalUrl) {
        this.educationalUrl = educationalUrl;
    }

    public String getSupport_doc() {
        return support_doc;
    }

    public void setSupport_doc(String support_doc) {
        this.support_doc = support_doc;
    }

    public String getExpectation_from_partner() {
        return expectation_from_partner;
    }

    public void setExpectation_from_partner(String expectation_from_partner) {
        this.expectation_from_partner = expectation_from_partner;
    }
}
