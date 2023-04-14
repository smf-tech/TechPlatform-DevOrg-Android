package com.octopusbjsindia.models.Operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OperatorMachineCodeDataModel {

    @SerializedName("hour_of_day")
    @Expose
    private int hour_of_day;

    @SerializedName("minute_of_hour")
    @Expose
    private int minute_of_hour;

    @SerializedName("minute_of_pause")
    @Expose
    private int minute_of_pause;

    @SerializedName("structure_id")
    @Expose
    private String structure_id;

    @SerializedName("machine_id")
    @Expose
    private String machine_id;

    @SerializedName("machine_code")
    @Expose
    private String machine_code;

    public Long getCurrentTimeStamp() {
        return currentTimeStamp;
    }

    @SerializedName("current_time_stamp")
    @Expose
    private Long currentTimeStamp;
    @SerializedName("nonutilisationTypeData")
    @Expose
    private NonutilisationTypeDataList nonutilisationTypeData;

    public String getMachine_id() {
        return machine_id;
    }

    public void setMachine_id(String machine_id) {
        this.machine_id = machine_id;
    }

    public String getMachine_code() {
        return machine_code;
    }

    public void setMachine_code(String machine_code) {
        this.machine_code = machine_code;
    }

    public NonutilisationTypeDataList getNonutilisationTypeData() {
        return nonutilisationTypeData;
    }

    public void setNonutilisationTypeData(NonutilisationTypeDataList nonutilisationTypeData) {
        this.nonutilisationTypeData = nonutilisationTypeData;
    }

    public String getStructure_id() {
        return structure_id;
    }

    public void setStructure_id(String structure_id) {
        this.structure_id = structure_id;
    }

    public int getHour_of_day() {
        return hour_of_day;
    }

    public void setHour_of_day(int hour_of_day) {
        this.hour_of_day = hour_of_day;
    }

    public int getMinute_of_hour() {
        return minute_of_hour;
    }

    public void setMinute_of_hour(int minute_of_hour) {
        this.minute_of_hour = minute_of_hour;
    }

    public int getMinute_of_pause() {
        return minute_of_pause;
    }

    public void setMinute_of_pause(int minute_of_pause) {
        this.minute_of_pause = minute_of_pause;
    }
}

