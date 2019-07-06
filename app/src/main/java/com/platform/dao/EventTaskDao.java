package com.platform.dao;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.platform.models.events.Event;

public interface EventTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Event... eventTasks);

}
