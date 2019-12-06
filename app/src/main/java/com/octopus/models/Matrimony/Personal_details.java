package com.octopus.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Personal_details {

    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("middle_name")
    @Expose
    private String middle_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("complexion")
    @Expose
    private String complexion;
    @SerializedName("birth_city")
    @Expose
    private String birth_city;
    @SerializedName("birth_time")
    @Expose
    private String birth_time;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("birthDate")
    @Expose
    private long birthDate;
    @SerializedName("sect")
    @Expose
    private String sect;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("blood_group")
    @Expose
    private String blood_group;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("is_manglik")
    @Expose
    private String is_manglik;
    @SerializedName("marital_status")
    @Expose
    private String marital_status;
    @SerializedName("match_patrika")
    @Expose
    private String match_patrika;
    @SerializedName("aadhar_number")
    @Expose
    private String aadhar_number;
    @SerializedName("special_case")
    @Expose
    private String special_case;
    @SerializedName("smoke")
    @Expose
    private String smoke;
    @SerializedName("drink")
    @Expose
    private String drink;
    @SerializedName("own_house")
    @Expose
    private String own_house;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public String getBirth_city() {
        return birth_city;
    }

    public void setBirth_city(String birth_city) {
        this.birth_city = birth_city;
    }

    public String getBirth_time() {
        return birth_time;
    }

    public void setBirth_time(String birth_time) {
        this.birth_time = birth_time;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public String getSect() {
        return sect;
    }

    public void setSect(String sect) {
        this.sect = sect;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIs_manglik() {
        return is_manglik;
    }

    public void setIs_manglik(String is_manglik) {
        this.is_manglik = is_manglik;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getMatch_patrika() {
        return match_patrika;
    }

    public void setMatch_patrika(String match_patrika) {
        this.match_patrika = match_patrika;
    }

    public String getAadhar_number() {
        return aadhar_number;
    }

    public void setAadhar_number(String aadhar_number) {
        this.aadhar_number = aadhar_number;
    }

    public String getSpecial_case() {
        return special_case;
    }

    public void setSpecial_case(String special_case) {
        this.special_case = special_case;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getOwn_house() {
        return own_house;
    }

    public void setOwn_house(String own_house) {
        this.own_house = own_house;
    }

}