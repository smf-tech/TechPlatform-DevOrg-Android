package com.platform.database;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.platform.dao.SavedFormDao;
import com.platform.models.SavedForm;

import java.util.List;

public class DatabaseManager {
    private static AppDatabase appDatabase;
    private static DatabaseManager databaseManager;

    public static DatabaseManager getDBInstance(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "SMF").
                allowMainThreadQueries()
                .build();

        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public void insertFormObject(SavedForm... savedForms) {
        SavedFormDao savedFormDao = appDatabase.formDao();
        savedFormDao.insertAll(savedForms);
    }

    public void updateFormObject(SavedForm savedForms) {
        SavedFormDao savedFormDao = appDatabase.formDao();
        savedFormDao.update(savedForms);
    }

    public List<SavedForm> getNonSyncedPendingForms() {
        SavedFormDao savedFormDao = appDatabase.formDao();
        return savedFormDao.getAllNonSyncedForms();
    }
}
