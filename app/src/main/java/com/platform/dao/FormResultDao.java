package com.platform.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.platform.models.forms.FormResult;

import java.util.List;

@SuppressWarnings("unused")
@Dao
public interface FormResultDao {

    @Query("SELECT * FROM formresult WHERE is_synced = 0")
    List<FormResult> getAllNonSyncedForms();

    @Query("SELECT result FROM formresult where form_id = :formId")
    List<String> getAllFormResults(String formId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FormResult result);

    @Update
    void update(FormResult formData);

    @Delete
    void delete(FormResult formData);

    @Query("DELETE FROM Modules")
    void deleteAllFormResults();

}
