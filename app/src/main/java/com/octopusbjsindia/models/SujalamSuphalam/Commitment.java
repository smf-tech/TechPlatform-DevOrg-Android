package com.octopusbjsindia.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Commitment {

    public String getCommitmentId() {
        return commitmentId;
    }

    public void setCommitmentId(String commitmentId) {
        this.commitmentId = commitmentId;
    }

    public String getCommitmentAmount() {
        return commitmentAmount;
    }

    public void setCommitmentAmount(String commitmentAmount) {
        this.commitmentAmount = commitmentAmount;
    }

    @SerializedName("_id")
    @Expose
    private String commitmentId;

    @SerializedName("commitment_amount")
    @Expose
    private String commitmentAmount;
}
