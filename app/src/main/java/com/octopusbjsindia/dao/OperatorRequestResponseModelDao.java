package com.octopusbjsindia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.octopusbjsindia.models.Operator.OperatorRequestResponseModel;

import java.util.List;

@Dao
public interface OperatorRequestResponseModelDao {
    @Query("SELECT * FROM OperatorRequestResponseModel")
    List<OperatorRequestResponseModel> getAllProcesses();

    @Query("DELETE FROM OperatorRequestResponseModel WHERE _id  = :id")
    void deleteSinglSynccedOperatorRecord(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OperatorRequestResponseModel operatorRequestResponseModel);

    @Query("SELECT * FROM operatorrequestresponsemodel WHERE machine_id=:machineId AND status = 'Working'")
    OperatorRequestResponseModel getLastWorkingRecord(String machineId);

    @Query("UPDATE operatorrequestresponsemodel SET status=:status, status_code=:status_code, " +
            "stopImage=:stopImage, stop_meter_reading=:stopMeterReading, lat=:latitude, _long=:longitude " +
            "WHERE machine_id =:machine_id AND meterReadingDate=:reading_date")
    void updateMachineRecord(String status, String status_code, String stopImage, String stopMeterReading,
                             String latitude, String longitude, String machine_id, String reading_date);
}
