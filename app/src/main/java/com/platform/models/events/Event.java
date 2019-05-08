package com.platform.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {
    //private String id;
    private String title;
    private String repeat;
    private String address;
    private String owner;
    private String ownerID;
//    private ArrayList<Participant> membersList = new ArrayList<>();
//    private ArrayList<TaskForm> formsList = new ArrayList<>();

    public Event() {

    }

    public Event(String id, String category, String title, Long startDate,Long eventEndDateTime , Recurrence recurrence, String description, String address, String organizer,
                 String ownerID, String status, ArrayList<Participant> membersList, ArrayList<TaskForm> formsList) {
        this.id = id;
        this.eventType = category;
        this.title = title;
        this.eventStartDateTime = startDate;
        this.eventEndDateTime = eventEndDateTime;
        this.recurrence = recurrence;
        this.eventDescription = description;
        this.address = address;
        this.organizer = organizer;
        this.ownerID = ownerID;
        this.status = status;
        this.membersList.addAll(membersList);
        if (formsList!=null) {
            this.formsList.addAll(formsList);
        }
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
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

//    public ArrayList<Participant> getMembersList() {
//        return membersList;
//    }
//
//    public void setMembersList(ArrayList<Participant> membersList) {
//        this.membersList = membersList;
//    }
//
//    public ArrayList<TaskForm> getFormsList() {
//        return formsList;
//    }
//
//    public void setFormsList(ArrayList<TaskForm> formsList) {
//        this.formsList = formsList;
//    }

    ///////////////////////////////////

    @SerializedName("eventStartDateTime")
    @Expose
    private Long eventStartDateTime;
    @SerializedName("eventEndDateTime")
    @Expose
    private Long eventEndDateTime;
    @SerializedName("recurrence")
    @Expose
    private Recurrence recurrence;

    public Long getEventStartDateTime() {
        return eventStartDateTime;
    }

    public void setEventStartDateTime(Long eventStartDateTime) {
        this.eventStartDateTime = eventStartDateTime;
    }

    public Long getEventEndDateTime() {
        return eventEndDateTime;
    }

    public void setEventEndDateTime(Long eventEndDateTime) {
        this.eventEndDateTime = eventEndDateTime;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;
    }





    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("eventDescription")
    @Expose
    private String eventDescription;
    @SerializedName("eventLocation")
    @Expose
    private EventLocation eventLocation;
    @SerializedName("eventName")
    @Expose
    private String eventName;
    @SerializedName("eventType")
    @Expose
    private String eventType;
    @SerializedName("formsList")
    @Expose
    private List<Object> formsList = null;
    @SerializedName("membersList")
    @Expose
    private ArrayList<Participant> membersList = null;
    @SerializedName("organizer")
    @Expose
    private String organizer;
    @SerializedName("participants")
    @Expose
    private List<Participant> participants = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("parent")
    @Expose
    private String parent;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("createdDateTime")
    @Expose
    private Integer createdDateTime;
    @SerializedName("updatedDateTime")
    @Expose
    private Integer updatedDateTime;
    @SerializedName("agenda")
    @Expose
    private String agenda;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public EventLocation getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(EventLocation eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<Object> getFormsList() {
        return formsList;
    }

    public void setFormsList(List<Object> formsList) {
        this.formsList = formsList;
    }

    public ArrayList<Participant> getMembersList() {
        return membersList;
    }

    public void setMembersList(ArrayList<Participant> membersList) {
        this.membersList = membersList;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Integer createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Integer getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(Integer updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }
}
