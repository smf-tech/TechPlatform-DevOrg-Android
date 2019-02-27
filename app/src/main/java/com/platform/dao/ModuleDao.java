package com.platform.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.platform.models.home.Modules;

import java.util.List;

@SuppressWarnings("unused")
@Dao
public interface ModuleDao {
    @Query("SELECT * FROM Modules where module = :status")
    List<Modules> getModulesOfStatus(String status);

    @Query("SELECT * FROM Modules")
    List<Modules> getAllModules();

    @Query("DELETE FROM Modules")
    void deleteAllModules();

    @Insert
    void insertAll(Modules... homes);

    @Update
    void update(Modules home);

    @Delete
    void delete(Modules home);
}
