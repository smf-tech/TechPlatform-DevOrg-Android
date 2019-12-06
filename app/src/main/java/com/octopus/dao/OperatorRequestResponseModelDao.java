package com.octopus.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.octopus.models.Operator.OperatorRequestResponseModel;

import java.util.List;

@Dao
public interface OperatorRequestResponseModelDao {
    @Query("SELECT * FROM OperatorRequestResponseModel")
    List<OperatorRequestResponseModel> getAllProcesses();

    @Query("DELETE FROM OperatorRequestResponseModel WHERE _id  = :id")
    void deleteSinglSynccedOperatorRecord(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OperatorRequestResponseModel operatorRequestResponseModel);
}
