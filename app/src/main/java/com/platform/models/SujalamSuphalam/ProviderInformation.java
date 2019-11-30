package com.platform.models.SujalamSuphalam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProviderInformation {

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("contact_number")
    @Expose
    private String contactNumber;
    @SerializedName("machine_id")
    @Expose
    private String machineId;
    @SerializedName("machine_meter_working")
    @Expose
    private String machineMeterWorking;

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    @SerializedName("ownership_type_id")
    @Expose
    private String ownership;
    @SerializedName("PAN_number")
    @Expose
    private String pANNumber;
    @SerializedName("trade_name")
    @Expose
    private String tradeName;
    @SerializedName("is_turnover")
    @Expose
    private String isTurnover;
    @SerializedName("GST_number")
    @Expose
    private String gSTNumber;
    @SerializedName("account_name")
    @Expose
    private String accountName;
    @SerializedName("account_no")
    @Expose
    private String accountNo;
    @SerializedName("bank_address")
    @Expose
    private String bankAddress;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("account_type")
    @Expose
    private String accountType;
    @SerializedName("IFSC")
    @Expose
    private String iFSC;
    @SerializedName("check_book_image")
    @Expose
    private String checkBookImage;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineMeterWorking() {
        return machineMeterWorking;
    }

    public void setMachineMeterWorking(String machineMeterWorking) {
        this.machineMeterWorking = machineMeterWorking;
    }

    public String getPANNumber() {
        return pANNumber;
    }

    public void setPANNumber(String pANNumber) {
        this.pANNumber = pANNumber;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getIsTurnover() {
        return isTurnover;
    }

    public void setIsTurnover(String isTurnover) {
        this.isTurnover = isTurnover;
    }

    public String getGSTNumber() {
        return gSTNumber;
    }

    public void setGSTNumber(String gSTNumber) {
        this.gSTNumber = gSTNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIFSC() {
        return iFSC;
    }

    public void setIFSC(String iFSC) {
        this.iFSC = iFSC;
    }

    public String getCheckBookImage() {
        return checkBookImage;
    }

    public void setCheckBookImage(String checkBookImage) {
        this.checkBookImage = checkBookImage;
    }
}
