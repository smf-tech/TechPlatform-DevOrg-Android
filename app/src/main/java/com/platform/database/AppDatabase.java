package com.platform.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.platform.dao.SavedFormDao;
import com.platform.models.SavedForm;

@Database(entities = {SavedForm.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedFormDao formDao();
}
