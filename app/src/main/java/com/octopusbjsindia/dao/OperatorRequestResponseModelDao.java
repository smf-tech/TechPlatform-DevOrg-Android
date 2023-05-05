package com.octopusbjsindia.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.octopusbjsindia.models.Operator.OperatorRequestResponseModel;

import java.util.List;

@Dao
public interface OperatorRequestResponseModelDao {
    @Query("SELECT * FROM OperatorRequestResponseModel WHERE isSynced = 0")
    List<OperatorRequestResponseModel> getAllUnSyncedProcesses();

    @Query("DELETE FROM OperatorRequestResponseModel WHERE _id  = :id")
    void deleteSinglSynccedOperatorRecord(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(OperatorRequestResponseModel operatorRequestResponseModel);

    @Query("SELECT * FROM operatorrequestresponsemodel WHERE machine_id=:machineId AND status = 'Working'")
    OperatorRequestResponseModel getLastWorkingRecord(String machineId);

    @Query("UPDATE operatorrequestresponsemodel SET status=:status, status_code=:status_code, " +
            "stopImage=:stopImage, stop_meter_reading=:stopMeterReading, lat=:latitude, _long=:longitude, isSynced=:isSync " +
            "WHERE machine_id =:machine_id AND meterReadingDate=:reading_date")
    int updateMachineRecord(String status, String status_code, String stopImage, String stopMeterReading,
                            String latitude, String longitude, String machine_id, String reading_date, boolean isSync);

    @Query("SELECT * FROM operatorrequestresponsemodel WHERE machine_id=:machine_id AND status = 'Stop'" +
            "ORDER BY meterReadingTimestamp DESC LIMIT 1")
    OperatorRequestResponseModel getPreviousLatestStopRecord(String machine_id);

    @Query("SELECT * FROM operatorrequestresponsemodel WHERE machine_id=:machine_id " +
            "ORDER BY meterReadingTimestamp DESC LIMIT 1")
    OperatorRequestResponseModel getPreviousLatestRecord(String machine_id);

    @Query("SELECT * FROM operatorrequestresponsemodel WHERE machine_id=:machine_id AND " +
            "meterReadingTimestamp<:selectedTimestamp AND status = 'Stop' ORDER BY meterReadingTimestamp DESC LIMIT 1")
    OperatorRequestResponseModel getPreviousLatestRecord(String machine_id, Long selectedTimestamp);

    @Query("SELECT * FROM operatorrequestresponsemodel WHERE machine_id=:machine_id AND " +
            "meterReadingTimestamp>:selectedTimestamp ORDER BY meterReadingTimestamp ASC LIMIT 1")
    OperatorRequestResponseModel getPreviousLatestRecordAfter(String machine_id, Long selectedTimestamp);

    @Query("SELECT EXISTS(SELECT * FROM operatorrequestresponsemodel WHERE machine_id=:machine_id AND" +
            " meterReadingTimestamp=:nextDayTimeStamp)")
    boolean getRecordForGivenTimestamp(String machine_id, Long nextDayTimeStamp);

    @Delete
    void deleteMachineRecord(OperatorRequestResponseModel operatorRequestResponseModel);

    @Query("DELETE FROM OperatorRequestResponseModel WHERE machine_id  = :machine_id AND" +
            " meterReadingTimestamp<:latestReadingTimestamp AND isSynced = 1")
    void deletePreviousMachineRecord(String machine_id, long latestReadingTimestamp);

    @Query("DELETE FROM OperatorRequestResponseModel WHERE machine_id  = :machine_id AND " +
            "meterReadingTimestamp =:nextDayTimeStamp")
    void deleteSpecificMachineRecord(String machine_id, Long nextDayTimeStamp);
}
