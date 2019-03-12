package com.platform.dao;

import com.platform.models.home.Modules;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
