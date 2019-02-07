package com.platform.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.platform.models.SavedForm;

import java.util.List;

@Dao
public interface SavedFormDao {
    @Query("SELECT * FROM savedform")
    List<SavedForm> getAll();

    @Insert
    void insertAll(SavedForm... savedForms);

    @Delete
    void delete(SavedForm savedForm);

}
