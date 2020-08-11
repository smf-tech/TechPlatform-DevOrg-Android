package com.octopusbjsindia.matrimonyregistration.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MatrimonialProfile implements Serializable {
    @SerializedName("meet_id")
    @Expose
    private String meet_id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("firebase_id")
    @Expose
    private String firebaseId;
    @SerializedName("personal_details")
    @Expose
    private PersonalDetails personalDetails;
    @SerializedName("educational_details")
    @Expose
    private EducationalDetails educationalDetails;
    @SerializedName("occupational_details")
    @Expose
    private OccupationalDetails occupationalDetails;
    @SerializedName("family_details")
    @Expose
    private FamilyDetails familyDetails;
    @SerializedName("residential_details")
    @Expose
    private ResidentialDetails residentialDetails;
    @SerializedName("other_marital_information")
    @Expose
    private OtherMaritalInformation otherMaritalInformation;
    @SerializedName("isVerified")
    @Expose
    private Boolean isVerified;
    @SerializedName("is_active")
    @Expose
    private Integer isActive;
    @SerializedName("is_deleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("isBan")
    @Expose
    private Boolean isBan;
    @SerializedName("profile_visit_count")
    @Expose
    private Integer profileVisitCount;
    @SerializedName("idApproved")
    @Expose
    private Boolean idApproved;
    @SerializedName("educationApproved")
    @Expose
    private Boolean educationApproved;
    @SerializedName("isEducationVerified")
    @Expose
    private Boolean isEducationVerified;
    @SerializedName("isIdVerified")
    @Expose
    private Boolean isIdVerified;
    @SerializedName("contacts_unlock_credit")
    @Expose
    private Integer contactsUnlockCredit;
    @SerializedName("privacy_settings")
    @Expose
    private PrivacySettings privacySettings;

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public EducationalDetails getEducationalDetails() {
        return educationalDetails;
    }

    public void setEducationalDetails(EducationalDetails educationalDetails) {
        this.educationalDetails = educationalDetails;
    }

    public OccupationalDetails getOccupationalDetails() {
        return occupationalDetails;
    }

    public void setOccupationalDetails(OccupationalDetails occupationalDetails) {
        this.occupationalDetails = occupationalDetails;
    }

    public FamilyDetails getFamilyDetails() {
        return familyDetails;
    }

    public void setFamilyDetails(FamilyDetails familyDetails) {
        this.familyDetails = familyDetails;
    }

    public ResidentialDetails getResidentialDetails() {
        return residentialDetails;
    }

    public void setResidentialDetails(ResidentialDetails residentialDetails) {
        this.residentialDetails = residentialDetails;
    }

    public OtherMaritalInformation getOtherMaritalInformation() {
        return otherMaritalInformation;
    }

    public void setOtherMaritalInformation(OtherMaritalInformation otherMaritalInformation) {
        this.otherMaritalInformation = otherMaritalInformation;
    }

    public String getMeet_id() {
        return meet_id;
    }

    public void setMeet_id(String meet_id) {
        this.meet_id = meet_id;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsBan() {
        return isBan;
    }

    public void setIsBan(Boolean isBan) {
        this.isBan = isBan;
    }

    public Integer getProfileVisitCount() {
        return profileVisitCount;
    }

    public void setProfileVisitCount(Integer profileVisitCount) {
        this.profileVisitCount = profileVisitCount;
    }

    public Boolean getIdApproved() {
        return idApproved;
    }

    public void setIdApproved(Boolean idApproved) {
        this.idApproved = idApproved;
    }

    public Boolean getEducationApproved() {
        return educationApproved;
    }

    public void setEducationApproved(Boolean educationApproved) {
        this.educationApproved = educationApproved;
    }

    public Boolean getIsEducationVerified() {
        return isEducationVerified;
    }

    public void setIsEducationVerified(Boolean isEducationVerified) {
        this.isEducationVerified = isEducationVerified;
    }

    public Boolean getIsIdVerified() {
        return isIdVerified;
    }

    public void setIsIdVerified(Boolean isIdVerified) {
        this.isIdVerified = isIdVerified;
    }

    public Integer getContactsUnlockCredit() {
        return contactsUnlockCredit;
    }

    public void setContactsUnlockCredit(Integer contactsUnlockCredit) {
        this.contactsUnlockCredit = contactsUnlockCredit;
    }

    public PrivacySettings getPrivacySettings() {
        return privacySettings;
    }

    public void setPrivacySettings(PrivacySettings privacySettings) {
        this.privacySettings = privacySettings;
    }

    public class PrivacySettings implements Serializable {
        @SerializedName("is_contact_visible_for_mutual")
        @Expose
        boolean is_contact_visible_for_mutual = true;
        @SerializedName("is_contact_visible_for_premium")
        @Expose
        boolean is_contact_visible_for_premium = true;

        public boolean isIs_contact_visible_for_mutual() {
            return is_contact_visible_for_mutual;
        }

        public void setIs_contact_visible_for_mutual(boolean is_contact_visible_for_mutual) {
            this.is_contact_visible_for_mutual = is_contact_visible_for_mutual;
        }

        public boolean isIs_contact_visible_for_premium() {
            return is_contact_visible_for_premium;
        }

        public void setIs_contact_visible_for_premium(boolean is_contact_visible_for_premium) {
            this.is_contact_visible_for_premium = is_contact_visible_for_premium;
        }
    }
}
