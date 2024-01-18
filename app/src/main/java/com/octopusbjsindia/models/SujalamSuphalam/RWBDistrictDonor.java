package com.octopusbjsindia.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RWBDistrictDonor {

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Commitment getCommitment() {
        return commitment;
    }

    public void setCommitment(Commitment commitment) {
        this.commitment = commitment;
    }

    public List<DonationDetails> getDonationList() {
        return donationList;
    }

    public void setDonationList(List<DonationDetails> donationList) {
        this.donationList = donationList;
    }

    @SerializedName("_id")
    @Expose
    private String donorId;

    @SerializedName("full_name")
    @Expose
    private String fullName;

    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;

    @SerializedName("email_id")
    @Expose
    private String emailId;

    @SerializedName("commitment")
    @Expose
    private Commitment commitment;

    @SerializedName("donation_details")
    @Expose
    private List<DonationDetails> donationList;
}
