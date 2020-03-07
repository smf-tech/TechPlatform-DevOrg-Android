package com.octopusbjsindia.models.smartgirl;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainerBachList {

    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("batch_id")
    @Expose
    private String batch_id;
    @SerializedName("batch_category_id")
    @Expose
    private String batch_category_id;
    @SerializedName("batch_category_name")
    @Expose
    private String batch_category_name;
    @SerializedName("state_id")
    @Expose
    private String state_id;
    @SerializedName("state_name")
    @Expose
    private String state_name;
    @SerializedName("district_id")
    @Expose
    private String district_id;
    @SerializedName("district_name")
    @Expose
    private String district_name;
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
    private BatchSchedule batchschedule;
    @SerializedName("created_by")
    @Expose
    private String created_by;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getBatch_category_name() {
        return batch_category_name;
    }

    public void setBatch_category_name(String batch_category_name) {
        this.batch_category_name = batch_category_name;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
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


    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
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

    public BatchSchedule getBatchschedule() {
        return batchschedule;
    }

    public void setBatchschedule(BatchSchedule batchschedule) {
        this.batchschedule = batchschedule;
    }
}
