package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TradeProfile implements Serializable {
    @SerializedName("business_info")
    @Expose
    private BusinessInfo businessInfo;
    @SerializedName("business_details")
    @Expose
    private BusinessDetails businessDetails;
    @SerializedName("additional_information")
    @Expose
    private BusinessAdditionalInformation additionalInformation;

    public BusinessInfo getBusinessInfo() {
        return businessInfo;
    }

    public void setBusinessInfo(BusinessInfo businessInfo) {
        this.businessInfo = businessInfo;
    }

    public BusinessDetails getBusinessDetails() {
        return businessDetails;
    }

    public void setBusinessDetails(BusinessDetails businessDetails) {
        this.businessDetails = businessDetails;
    }

    public BusinessAdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(BusinessAdditionalInformation additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}
