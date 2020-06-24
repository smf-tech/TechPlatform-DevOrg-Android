package com.octopusbjsindia.models.Matrimony;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {


    @SerializedName("title")
    @Expose
    private List<String> title = null;
    @SerializedName("male")
    @Expose
    private List<List<Male>> male = null;
    @SerializedName("female")
    @Expose
    private List<List<Female>> female = null;

    public List<List<Male>> getMale() {
        return male;
    }

    public void setMale(List<List<Male>> male) {
        this.male = male;
    }

    public List<List<Female>> getFemale() {
        return female;
    }

    public void setFemale(List<List<Female>> female) {
        this.female = female;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }
}
