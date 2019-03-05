package com.platform.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.platform.models.forms.FormResult;

import java.util.List;

@SuppressWarnings("unused")
@Dao
public interface FormResultDao {

    @Query("SELECT * FROM formresult WHERE form_status = :sync")
    List<FormResult> getAllFormResults(int sync);

    @Query("SELECT result FROM formresult where form_id = :formId and form_status = :sync")
    List<String> getAllFormResults(String formId, int sync);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FormResult result);

    @Update
    void update(FormResult formData);

    @Delete
    void delete(FormResult formData);

    @Query("DELETE FROM formresult")
    void deleteAllFormResults();

    @Query("DELETE FROM formresult where form_status = :sync")
    void deleteAllNonSyncedFormResults(int sync);

    @Query("SELECT * FROM formresult WHERE form_status = :sync and result_id = :formId")
    FormResult getPartialForm(int sync, String formId);

}
