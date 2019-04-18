package com.platform.models.events;

public class Event {
    String id;
    String tital;
    String startDate;
    String starTime;
    String endTime;
    String repeat;
    String description;
    String address;
    String owner;
    String ownerID;

    public Event(String id, String tital, String startDate, String starTime, String endTime, String repeat, String description, String address, String owner, String ownerID) {
        this.id = id;
        this.tital = tital;
        this.startDate = startDate;
        this.starTime = starTime;
        this.endTime = endTime;
        this.repeat = repeat;
        this.description = description;
        this.address = address;
        this.owner = owner;
        this.ownerID = ownerID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
