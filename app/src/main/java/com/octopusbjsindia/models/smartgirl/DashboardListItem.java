package com.octopusbjsindia.models.smartgirl;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class DashboardListItem {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("dob")
    @Expose
    private Long dob;


    @SerializedName("batchCount")
    @Expose
    private String batchCount;

    @SerializedName("workshopCount")
    @Expose
    private String workshopCount;




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @SerializedName("gender")
    @Expose
    private String gender;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public String getWorkshopCount() {
        return workshopCount;
    }

    public void setWorkshopCount(String workshopCount) {
        this.workshopCount = workshopCount;
    }

    public String getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(String batchCount) {
        this.batchCount = batchCount;
    }
}
