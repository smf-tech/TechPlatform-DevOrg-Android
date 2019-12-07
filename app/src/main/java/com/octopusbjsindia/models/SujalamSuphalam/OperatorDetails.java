package com.octopusbjsindia.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OperatorDetails {

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("licence_number")
    @Expose
    private String licenceNumber;
    @SerializedName("contact_numnber")
    @Expose
    private String contactNumnber;
    @SerializedName("is_training_done")
    @Expose
    private String isTrainingDone;
    @SerializedName("is_app_installed")
    @Expose
    private String isAppInstalled;
    @SerializedName("operator_images")
    @Expose
    private List<String> operatorImages = null;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getContactNumnber() {
        return contactNumnber;
    }

    public void setContactNumnber(String contactNumnber) {
        this.contactNumnber = contactNumnber;
    }

    public List<String> getOperatorImages() {
        return operatorImages;
    }

    public void setOperatorImages(List<String> operatorImages) {
        this.operatorImages = operatorImages;
    }
    public String getIsTrainingDone() {
        return isTrainingDone;
    }

    public void setIsTrainingDone(String isTrainingDone) {
        this.isTrainingDone = isTrainingDone;
    }

    public String getIsAppInstalled() {
        return isAppInstalled;
    }

    public void setIsAppInstalled(String isAppInstalled) {
        this.isAppInstalled = isAppInstalled;
    }
}
