package com.platform.models.Matrimony;

import java.io.Serializable;

public class MatrimonyMeet implements Serializable {
    private String meetTitle;
    private String meetDateTime;

    public String getMeetTitle() {
        return meetTitle;
    }

    public void setMeetTitle(String meetTitle) {
        this.meetTitle = meetTitle;
    }

    public String getMeetDateTime() {
        return meetDateTime;
    }

    public void setMeetDateTime(String meetDateTime) {
        this.meetDateTime = meetDateTime;
    }
}
