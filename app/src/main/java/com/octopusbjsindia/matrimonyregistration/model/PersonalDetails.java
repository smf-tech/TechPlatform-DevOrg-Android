package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PersonalDetails implements Serializable {
    @SerializedName("profile_for")
    @Expose
    private String profileFor;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("complexion")
    @Expose
    private String complexion;
    @SerializedName("birth_city")
    @Expose
    private String birthCity;
    @SerializedName("birth_time")
    @Expose
    private String birthTime;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("birthDate")
    @Expose
    private long birthDate;
    @SerializedName("sect")
    @Expose
    private String sect;
    @SerializedName("blood_group")
    @Expose
    private String bloodGroup;
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
    private String isManglik;
    @SerializedName("marital_status")
    @Expose
    private String maritalStatus;
    @SerializedName("is_divorced_legal")
    @Expose
    private String isDivorcedLegal;
    @SerializedName("have_children")
    @Expose
    private String haveChildren;
    @SerializedName("children_count")
    @Expose
    private String childrenCount;
    @SerializedName("match_patrika")
    @Expose
    private boolean matchPatrika;
    @SerializedName("aadhar_number")
    @Expose
    private String aadharNumber;
    @SerializedName("special_case")
    @Expose
    private String specialCase;
    @SerializedName("sub_cast")
    @Expose
    private String subCast;
    @SerializedName("smoke")
    @Expose
    private String smoke;
    @SerializedName("drink")
    @Expose
    private String drink;
    @SerializedName("own_house")
    @Expose
    private String ownHouse;

    public String getProfileFor() {
        return profileFor;
    }

    public void setProfileFor(String profileFor) {
        this.profileFor = profileFor;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public String getBirthCity() {
        return birthCity;
    }

    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    public String getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(String birthTime) {
        this.birthTime = birthTime;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
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

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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

    public String getIsManglik() {
        return isManglik;
    }

    public void setIsManglik(String isManglik) {
        this.isManglik = isManglik;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getIsDivorcedLegal() {
        return isDivorcedLegal;
    }

    public void setIsDivorcedLegal(String isDivorcedLegal) {
        this.isDivorcedLegal = isDivorcedLegal;
    }

    public String getHaveChildren() {
        return haveChildren;
    }

    public void setHaveChildren(String haveChildren) {
        this.haveChildren = haveChildren;
    }

    public String getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(String childrenCount) {
        this.childrenCount = childrenCount;
    }

    public boolean getMatchPatrika() {
        return matchPatrika;
    }

    public void setMatchPatrika(boolean matchPatrika) {
        this.matchPatrika = matchPatrika;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getSpecialCase() {
        return specialCase;
    }

    public void setSpecialCase(String specialCase) {
        this.specialCase = specialCase;
    }

    public String getSubCast() {
        return subCast;
    }

    public void setSubCast(String subCast) {
        this.subCast = subCast;
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

    public String getOwnHouse() {
        return ownHouse;
    }

    public void setOwnHouse(String ownHouse) {
        this.ownHouse = ownHouse;
    }

}
