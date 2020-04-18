package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.models.events.Schedule;

import java.util.List;

public class WorkshopBachList {

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("workshop_category_id")
    @Expose
    private String workshop_category_id;
    @SerializedName("state_id")
    @Expose
    private String state_id;
    @SerializedName("district_id")
    @Expose
    private String district_id;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("venue")
    @Expose
    private String venue;
    @SerializedName("total_praticipants")
    @Expose
    private String total_praticipants;
    @SerializedName("additional_master_trainer")
    @Expose
    private Additional_master_trainer additional_master_trainer;
    @SerializedName("schedule")
    @Expose
    private WorkShopSchedule workShopSchedule;

    @SerializedName("created_by")
    @Expose
    private List<Created_by> created_by = null;

    @SerializedName("beneficiariesList")
    @Expose
    private List<BeneficiariesList> beneficiariesList = null;
    /*@SerializedName("created_by")
    @Expose
    private String created_by;*/
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_by")
    @Expose
    private String updated_by;
    @SerializedName("state")
    @Expose
    private State state;
    @SerializedName("district")
    @Expose
    private District district;

    @SerializedName("category")
    @Expose
    private Category category;

    @SerializedName("batch_id")
    @Expose
    private String batch_id;
    @SerializedName("batch_category_id")
    @Expose
    private String batch_category_id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getWorkshop_category_id() {
        return workshop_category_id;
    }

    public void setWorkshop_category_id(String workshop_category_id) {
        this.workshop_category_id = workshop_category_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTotal_praticipants() {
        return total_praticipants;
    }

    public void setTotal_praticipants(String total_praticipants) {
        this.total_praticipants = total_praticipants;
    }

    public Additional_master_trainer getAdditional_master_trainer() {
        return additional_master_trainer;
    }

    public void setAdditional_master_trainer(Additional_master_trainer additional_master_trainer) {
        this.additional_master_trainer = additional_master_trainer;
    }



    public List<BeneficiariesList> getBeneficiariesList() {
        return beneficiariesList;
    }

    public void setBeneficiariesList(List<BeneficiariesList> beneficiariesList) {
        this.beneficiariesList = beneficiariesList;
    }


    public List<Created_by> getCreated_by() {
        return created_by;
    }

    public void setCreated_by(List<Created_by> created_by) {
        this.created_by = created_by;
    }
    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getBatch_category_id() {
        return batch_category_id;
    }

    public void setBatch_category_id(String batch_category_id) {
        this.batch_category_id = batch_category_id;
    }

    public WorkShopSchedule getWorkShopSchedule() {
        return workShopSchedule;
    }

    public void setWorkShopSchedule(WorkShopSchedule workShopSchedule) {
        this.workShopSchedule = workShopSchedule;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
