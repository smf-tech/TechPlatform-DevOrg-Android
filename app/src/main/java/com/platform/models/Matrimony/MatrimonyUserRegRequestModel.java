package com.platform.models.Matrimony;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatrimonyUserRegRequestModel {

    @SerializedName("meet_id")
    @Expose
    private String meet_id;

    @SerializedName("personal_details")
    @Expose
    private Personal_details personal_details;
    @SerializedName("educational_details")
    @Expose
    private Educational_details educational_details;
    @SerializedName("occupational_details")
    @Expose
    private Occupational_details occupational_details;
    @SerializedName("family_details")
    @Expose
    private Family_details family_details;
    @SerializedName("residential_details")
    @Expose
    private Residential_details residential_details;
    @SerializedName("other_marital_information")
    @Expose
    private Other_maritial_information other_marital_information;

    public Personal_details getPersonal_details() {
        return personal_details;
    }

    public void setPersonal_details(Personal_details personal_details) {
        this.personal_details = personal_details;
    }

    public Educational_details getEducational_details() {
        return educational_details;
    }

    public void setEducational_details(Educational_details educational_details) {
        this.educational_details = educational_details;
    }

    public Occupational_details getOccupational_details() {
        return occupational_details;
    }

    public void setOccupational_details(Occupational_details occupational_details) {
        this.occupational_details = occupational_details;
    }

    public Family_details getFamily_details() {
        return family_details;
    }

    public void setFamily_details(Family_details family_details) {
        this.family_details = family_details;
    }

    public Residential_details getResidential_details() {
        return residential_details;
    }

    public void setResidential_details(Residential_details residential_details) {
        this.residential_details = residential_details;
    }

    public Other_maritial_information other_marital_information() {
        return other_marital_information;
    }

    public void setOther_maritial_information(Other_maritial_information other_marital_information) {
        this.other_marital_information = other_marital_information;
    }

    public String getMeet_id() {
        return meet_id;
    }

    public void setMeet_id(String meet_id) {
        this.meet_id = meet_id;
    }

    public static class Personal_details {

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
        @SerializedName("birth_date")
        @Expose
        private long birth_date;
        @SerializedName("sect")
        @Expose
        private String sect;
        /*@SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("pincode")
        @Expose
        private int pincode;*/
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
        private boolean match_patrika;
        @SerializedName("aadhar_number")
        @Expose
        private int aadhar_number;
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

        public long getBirth_date() {
            return birth_date;
        }

        public void setBirth_date(long birth_date) {
            this.birth_date = birth_date;
        }

        public String getSect() {
            return sect;
        }

        public void setSect(String sect) {
            this.sect = sect;
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

        public boolean isMatch_patrika() {
            return match_patrika;
        }

        public void setMatch_patrika(boolean match_patrika) {
            this.match_patrika = match_patrika;
        }

        public int getAadhar_number() {
            return aadhar_number;
        }

        public void setAadhar_number(int aadhar_number) {
            this.aadhar_number = aadhar_number;
        }

        public String getSpecial_case() {
            return special_case;
        }

        public void setSpecial_case(String special_case) {
            this.special_case = special_case;
        }



        public String getOwn_house() {
            return own_house;
        }

        public void setOwn_house(String own_house) {
            this.own_house = own_house;
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
    }

    public static class Educational_details {

        @SerializedName("education_level")
        @Expose
        private String education_level;
        @SerializedName("qualification_degree")
        @Expose
        private String qualification_degree;
        @SerializedName("income")
        @Expose
        private String income;

        public String getEducation_level() {
            return education_level;
        }

        public void setEducation_level(String education_level) {
            this.education_level = education_level;
        }

        public String getQualification_degree() {
            return qualification_degree;
        }

        public void setQualification_degree(String qualification_degree) {
            this.qualification_degree = qualification_degree;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

    }

    public static class Occupational_details {

        @SerializedName("occupation")
        @Expose
        private String occupation;
        @SerializedName("employer_company")
        @Expose
        private String employer_company;
        @SerializedName("business_description")
        @Expose
        private String business_description;

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public String getEmployer_company() {
            return employer_company;
        }

        public void setEmployer_company(String employer_company) {
            this.employer_company = employer_company;
        }

        public String getBusiness_description() {
            return business_description;
        }

        public void setBusiness_description(String business_description) {
            this.business_description = business_description;
        }

    }

    public static class Family_details {

        @SerializedName("family_type")
        @Expose
        private String family_type;
        @SerializedName("gotra")
        @Expose
        private Gotra gotra;
        @SerializedName("father_name")
        @Expose
        private String father_name;
        @SerializedName("father_occupation")
        @Expose
        private String father_occupation;
        @SerializedName("family_income")
        @Expose
        private String family_income;
        @SerializedName("mother_name")
        @Expose
        private String mother_name;
        @SerializedName("mother_occupation")
        @Expose
        private String mother_occupation;
        @SerializedName("brother_count")
        @Expose
        private String brother_count;
        @SerializedName("sister_count")
        @Expose
        private String sister_count;

        public String getFamily_type() {
            return family_type;
        }

        public void setFamily_type(String family_type) {
            this.family_type = family_type;
        }

        public Gotra getGotra() {
            return gotra;
        }

        public void setGotra(Gotra gotra) {
            this.gotra = gotra;
        }

        public String getFather_name() {
            return father_name;
        }

        public void setFather_name(String father_name) {
            this.father_name = father_name;
        }

        public String getFather_occupation() {
            return father_occupation;
        }

        public void setFather_occupation(String father_occupation) {
            this.father_occupation = father_occupation;
        }

        public String getFamily_income() {
            return family_income;
        }

        public void setFamily_income(String family_income) {
            this.family_income = family_income;
        }

        public String getMother_name() {
            return mother_name;
        }

        public void setMother_name(String mother_name) {
            this.mother_name = mother_name;
        }

        public String getMother_occupation() {
            return mother_occupation;
        }

        public void setMother_occupation(String mother_occupation) {
            this.mother_occupation = mother_occupation;
        }

        public String getBrother_count() {
            return brother_count;
        }

        public void setBrother_count(String brother_count) {
            this.brother_count = brother_count;
        }

        public String getSister_count() {
            return sister_count;
        }

        public void setSister_count(String sister_count) {
            this.sister_count = sister_count;
        }

        public static class Gotra {

            @SerializedName("self_gotra")
            @Expose
            private String self_gotra;
            @SerializedName("mama_gotra")
            @Expose
            private String mama_gotra;
            @SerializedName("dada_gotra")
            @Expose
            private String dada_gotra;
            @SerializedName("nana_gotra")
            @Expose
            private String nana_gotra;

            public String getSelf_gotra() {
                return self_gotra;
            }

            public void setSelf_gotra(String self_gotra) {
                this.self_gotra = self_gotra;
            }

            public String getMama_gotra() {
                return mama_gotra;
            }

            public void setMama_gotra(String mama_gotra) {
                this.mama_gotra = mama_gotra;
            }

            public String getDada_gotra() {
                return dada_gotra;
            }

            public void setDada_gotra(String dada_gotra) {
                this.dada_gotra = dada_gotra;
            }

            public String getNana_gotra() {
                return nana_gotra;
            }

            public void setNana_gotra(String nana_gotra) {
                this.nana_gotra = nana_gotra;
            }

        }
    }

    public  static class Residential_details {

        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("primary_phone")
        @Expose
        private String primary_phone;
        @SerializedName("secondary_phone")
        @Expose
        private String secondary_phone;
        @SerializedName("primary_email_address")
        @Expose
        private String primary_email_address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPrimary_mobile() {
            return primary_phone;
        }

        public void setPrimary_mobile(String primary_mobile) {
            this.primary_phone = primary_mobile;
        }

        public String getSecondary_mobile() {
            return secondary_phone;
        }

        public void setSecondary_mobile(String secondary_mobile) {
            this.secondary_phone = secondary_mobile;
        }

        public String getPrimary_email_address() {
            return primary_email_address;
        }

        public void setPrimary_email_address(String primary_email_address) {
            this.primary_email_address = primary_email_address;
        }

    }

    public static class Other_maritial_information {
        @SerializedName("about_me")
        @Expose
        private String about_me;
        @SerializedName("expectation_from_life_partner")
        @Expose
        private String expectation_from_life_partner;
        @SerializedName("profile_image")
        @Expose
        private String profile_image;

        @SerializedName("aadhar_url")
        @Expose
        private String aadhar_url;

        @SerializedName("educational_url")
        @Expose
        private String educational_url;

        @SerializedName("activity_achievements")
        @Expose
        private String activity_achievements;
        @SerializedName("other_remarks")
        @Expose
        private String other_remarks;

        public String getAbout_me() {
            return about_me;
        }

        public void setAbout_me(String about_me) {
            this.about_me = about_me;
        }

        public String getExpectation_from_life_partner() {
            return expectation_from_life_partner;
        }

        public void setExpectation_from_life_partner(String expectation_from_life_partner) {
            this.expectation_from_life_partner = expectation_from_life_partner;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public String getActivity_achievements() {
            return activity_achievements;
        }

        public void setActivity_achievements(String activity_achievements) {
            this.activity_achievements = activity_achievements;
        }

        public String getOther_remarks() {
            return other_remarks;
        }

        public void setOther_remarks(String other_remarks) {
            this.other_remarks = other_remarks;
        }

        public String getAadhar_url() {
            return aadhar_url;
        }

        public void setAadhar_url(String aadhar_url) {
            this.aadhar_url = aadhar_url;
        }

        public String getEducational_url() {
            return educational_url;
        }

        public void setEducational_url(String educational_url) {
            this.educational_url = educational_url;
        }
    }
}
