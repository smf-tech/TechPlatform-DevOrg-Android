package com.platform.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.platform.dao.FormDataDao;
import com.platform.dao.SavedFormDao;
import com.platform.models.SavedForm;
import com.platform.models.forms.FormData;

@Database(entities = {SavedForm.class, FormData.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedFormDao formDao();

    public abstract FormDataDao formDataDao();
}
