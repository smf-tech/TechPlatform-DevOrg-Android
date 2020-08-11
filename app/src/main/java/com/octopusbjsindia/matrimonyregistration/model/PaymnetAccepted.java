package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymnetAccepted implements Serializable {
    @SerializedName("cash")
    @Expose
    private Boolean cash;
    @SerializedName("cheque")
    @Expose
    private Boolean cheque;
    @SerializedName("cards")
    @Expose
    private Boolean cards;

    public Boolean getCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }

    public Boolean getCheque() {
        return cheque;
    }

    public void setCheque(Boolean cheque) {
        this.cheque = cheque;
    }

    public Boolean getCards() {
        return cards;
    }

    public void setCards(Boolean cards) {
        this.cards = cards;
    }
}
