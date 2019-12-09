package com.platform.models.planner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LeaveData {
    @SerializedName("total")
    @Expose
    private Float total;
    @SerializedName("used")
    @Expose
    private Float used;
    @SerializedName("balance")
    @Expose
    private Float balance;

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getUsed() {
        return used;
    }

    public void setUsed(Float used) {
        this.used = used;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

}
