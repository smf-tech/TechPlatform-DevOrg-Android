package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BusinessAdditionalInformation implements Serializable {
    @SerializedName("average_turnover")
    @Expose
    private String averageTurnover;
    @SerializedName("number_of_staffs")
    @Expose
    private String numberOfStaffs;
    @SerializedName("about_business")
    @Expose
    private String aboutBusiness;
    @SerializedName("about_yourSelf")
    @Expose
    private String aboutYourSelf;
    @SerializedName("about_your_vision")
    @Expose
    private String aboutYourVision;
    @SerializedName("expect_out_of_business")
    @Expose
    private String expectOutOfBusiness;

    public String getAverageTurnover() {
        return averageTurnover;
    }

    public void setAverageTurnover(String averageTurnover) {
        this.averageTurnover = averageTurnover;
    }

    public String getNumberOfStaffs() {
        return numberOfStaffs;
    }

    public void setNumberOfStaffs(String numberOfStaffs) {
        this.numberOfStaffs = numberOfStaffs;
    }

    public String getAboutBusiness() {
        return aboutBusiness;
    }

    public void setAboutBusiness(String aboutBusiness) {
        this.aboutBusiness = aboutBusiness;
    }

    public String getAboutYourSelf() {
        return aboutYourSelf;
    }

    public void setAboutYourSelf(String aboutYourSelf) {
        this.aboutYourSelf = aboutYourSelf;
    }

    public String getAboutYourVision() {
        return aboutYourVision;
    }

    public void setAboutYourVision(String aboutYourVision) {
        this.aboutYourVision = aboutYourVision;
    }

    public String getExpectOutOfBusiness() {
        return expectOutOfBusiness;
    }

    public void setExpectOutOfBusiness(String expectOutOfBusiness) {
        this.expectOutOfBusiness = expectOutOfBusiness;
    }

}
