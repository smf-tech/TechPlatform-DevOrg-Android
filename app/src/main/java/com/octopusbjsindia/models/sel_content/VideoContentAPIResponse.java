package com.octopusbjsindia.models.sel_content;

import java.io.Serializable;
import java.util.List;

public class VideoContentAPIResponse implements Serializable {
    private Integer status;
    private String message;
    private List<SELVideoContent> data = null;
    private final static long serialVersionUID = -6962406116611068768L;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SELVideoContent> getData() {
        return data;
    }

    public void setData(List<SELVideoContent> data) {
        this.data = data;
    }
}
