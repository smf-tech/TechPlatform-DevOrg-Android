package com.octopusbjsindia.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DonationDetails {

    @SerializedName("donation_entity_list") //todo update key from backend
    @Expose
    private List<DonationEntity> donationEntityList;

    @SerializedName("total_donation_amount")
    @Expose
    private String totalDonationAmount;


    public List<DonationEntity> getDonationEntityList() {
        return donationEntityList;
    }

    public void setDonationEntityList(List<DonationEntity> donationEntityList) {
        this.donationEntityList = donationEntityList;
    }

    public String getTotalDonationAmount() {
        return totalDonationAmount;
    }

    public void setTotalDonationAmount(String totalDonationAmount) {
        this.totalDonationAmount = totalDonationAmount;
    }
}
