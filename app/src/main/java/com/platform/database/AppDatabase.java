package com.platform.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.platform.dao.FormDataDao;
import com.platform.dao.FormResultDao;
import com.platform.dao.ModuleDao;
import com.platform.dao.ProcessDataDao;
import com.platform.dao.ReportsDataDao;
import com.platform.models.SavedForm;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.models.home.Modules;
import com.platform.models.pm.ProcessData;
import com.platform.models.reports.ReportData;

@Database(entities = {SavedForm.class, FormData.class, Modules.class, ReportData.class, FormResult.class, ProcessData.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {

    public abstract ProcessDataDao processDataDao();

    public abstract FormDataDao formDataDao();

    public abstract ModuleDao homeDao();

    public abstract ReportsDataDao reportDao();

    public abstract FormResultDao formResultDao();
}
