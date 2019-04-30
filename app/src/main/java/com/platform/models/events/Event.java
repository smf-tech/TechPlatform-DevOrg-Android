package com.platform.models.events;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {
    private String id;
    private String category;
    private String tital;
    private String startDate;
    private String starTime;
    private String endTime;
    private String repeat;
    private String description;
    private String address;
    private String owner;
    private String ownerID;
    private ArrayList<Member> membersList = new ArrayList<>();

    public Event(String id, String category, String tital, String startDate, String starTime,
                 String endTime, String repeat, String description, String address, String owner,
                 String ownerID, ArrayList<Member> membersList) {
        this.id = id;
        this.category = category;
        this.tital = tital;
        this.startDate = startDate;
        this.starTime = starTime;
        this.endTime = endTime;
        this.repeat = repeat;
        this.description = description;
        this.address = address;
        this.owner = owner;
        this.ownerID = ownerID;
        this.membersList.addAll(membersList);
    }

    public Event() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTital() {
        return tital;
    }

    public void setTital(String tital) {
        this.tital = tital;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStarTime() {
        return starTime;
    }

    public void setStarTime(String starTime) {
        this.starTime = starTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public ArrayList<Member> getMembersList() {
        return membersList;
    }

    public void setMembersList(ArrayList<Member> membersList) {
        this.membersList = membersList;
    }
}
