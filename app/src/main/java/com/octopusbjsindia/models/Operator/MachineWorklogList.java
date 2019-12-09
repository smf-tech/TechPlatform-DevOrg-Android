package com.octopusbjsindia.models.Operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MachineWorklogList {

    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("startReading")
    @Expose
    private String startReading;
    @SerializedName("startMeterReadingImage")
    @Expose
    private String startMeterReadingImage;
    @SerializedName("endReading")
    @Expose
    private String endReading;
    @SerializedName("endMeterReadingImage")
    @Expose
    private String endMeterReadingImage;
    @SerializedName("totalHrsCunt")
    @Expose
    private String totalHrsCunt;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartReading() {
        return startReading;
    }

    public void setStartReading(String startReading) {
        this.startReading = startReading;
    }

    public String getStartMeterReadingImage() {
        return startMeterReadingImage;
    }

    public void setStartMeterReadingImage(String startMeterReadingImage) {
        this.startMeterReadingImage = startMeterReadingImage;
    }

    public String getEndReading() {
        return endReading;
    }

    public void setEndReading(String endReading) {
        this.endReading = endReading;
    }

    public String getEndMeterReadingImage() {
        return endMeterReadingImage;
    }

    public void setEndMeterReadingImage(String endMeterReadingImage) {
        this.endMeterReadingImage = endMeterReadingImage;
    }

    public String getTotalHrsCunt() {
        return totalHrsCunt;
    }

    public void setTotalHrsCunt(String totalHrsCunt) {
        this.totalHrsCunt = totalHrsCunt;
    }

}
