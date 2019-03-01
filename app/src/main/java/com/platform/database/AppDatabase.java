package com.platform.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.platform.dao.FormDataDao;
import com.platform.dao.FormResultDao;
import com.platform.dao.ModuleDao;
import com.platform.dao.ReportsDataDao;
import com.platform.dao.SavedFormDao;
import com.platform.models.SavedForm;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.models.home.Modules;
import com.platform.models.reports.ReportData;

@Database(entities = {SavedForm.class, FormData.class, Modules.class, ReportData.class, FormResult.class}, version = 2)

public abstract class AppDatabase extends RoomDatabase {

    public abstract SavedFormDao formDao();

    public abstract FormDataDao formDataDao();

    public abstract ModuleDao homeDao();

    public abstract ReportsDataDao reportDao();

    public abstract FormResultDao formResultDao();
}
