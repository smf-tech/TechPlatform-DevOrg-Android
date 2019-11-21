package com.platform.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MouDetails {

    @SerializedName("rate_details")
    @Expose
    private List<RateDetail> rateDetails = null;
    @SerializedName("MOU_images")
    @Expose
    private List<String> mOUImages = null;
    @SerializedName("date_of_signing")
    @Expose
    private long dateOfSigning;
    @SerializedName("mou_expiry_datetime")
    @Expose
    private long dateOfMouExpiry;
    @SerializedName("is_MOU_cancelled")
    @Expose
    private String isMOUCancelled;
    @SerializedName("status")
    @Expose
    private String status;

    public List<RateDetail> getRateDetails() {
        return rateDetails;
    }

    public void setRateDetails(List<RateDetail> rateDetails) {
        this.rateDetails = rateDetails;
    }

    public List<String> getMOUImages() {
        return mOUImages;
    }

    public void setMOUImages(List<String> mOUImages) {
        this.mOUImages = mOUImages;
    }

    public long getDateOfSigning() {
        return dateOfSigning;
    }

    public void setDateOfSigning(long dateOfSigning) {
        this.dateOfSigning = dateOfSigning;
    }

    public long getDateOfMouExpiry() {
        return dateOfMouExpiry;
    }

    public void setDateOfMouExpiry(long dateOfMouExpiry) {
        this.dateOfMouExpiry = dateOfMouExpiry;
    }

    public String getIsMOUCancelled() {
        return isMOUCancelled;
    }

    public void setIsMOUCancelled(String isMOUCancelled) {
        this.isMOUCancelled = isMOUCancelled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
