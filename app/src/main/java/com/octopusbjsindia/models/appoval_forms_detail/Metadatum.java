package com.octopusbjsindia.models.appoval_forms_detail;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadatum {

    @SerializedName("form")
    @Expose
    private Form form;

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

}