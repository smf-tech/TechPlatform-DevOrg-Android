package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BusinessDetails implements Serializable {
    @SerializedName("business_category")
    @Expose
    private String businessCategory;
    @SerializedName("business_keywords")
    @Expose
    private String businessKeywords;
    @SerializedName("operations_hours")
    @Expose
    private String operationsHours;
    @SerializedName("paymnet_accepted")
    @Expose
    private PaymnetAccepted paymnetAccepted;
    @SerializedName("establishment_date")
    @Expose
    private String establishmentDate;
    @SerializedName("business_descriptions")
    @Expose
    private String businessDescriptions;
    @SerializedName("websites")
    @Expose
    private String websites;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("photos")
    @Expose
    private List<String> photos = null;

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    public String getBusinessKeywords() {
        return businessKeywords;
    }

    public void setBusinessKeywords(String businessKeywords) {
        this.businessKeywords = businessKeywords;
    }

    public String getOperationsHours() {
        return operationsHours;
    }

    public void setOperationsHours(String operationsHours) {
        this.operationsHours = operationsHours;
    }

    public PaymnetAccepted getPaymnetAccepted() {
        return paymnetAccepted;
    }

    public void setPaymnetAccepted(PaymnetAccepted paymnetAccepted) {
        this.paymnetAccepted = paymnetAccepted;
    }

    public String getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(String establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public String getBusinessDescriptions() {
        return businessDescriptions;
    }

    public void setBusinessDescriptions(String businessDescriptions) {
        this.businessDescriptions = businessDescriptions;
    }

    public String getWebsites() {
        return websites;
    }

    public void setWebsites(String websites) {
        this.websites = websites;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
