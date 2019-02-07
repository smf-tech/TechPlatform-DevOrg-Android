package com.platform.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.platform.models.SavedForm;

import java.util.List;

@Dao
public interface SavedFormDao {
    @Query("SELECT * FROM savedform WHERE is_synced = 0")
    List<SavedForm> getAllNonSyncedForms();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SavedForm... savedForms);

    @Update
    void update(SavedForm savedForms);

    @Delete
    void delete(SavedForm savedForm);

}
