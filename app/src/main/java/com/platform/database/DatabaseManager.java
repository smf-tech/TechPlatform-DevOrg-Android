package com.platform.database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

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

import java.util.List;

public class DatabaseManager {
    private static final String TAG = DatabaseManager.class.getSimpleName();
    private static AppDatabase appDatabase;
    private static DatabaseManager databaseManager;

    public static DatabaseManager getDBInstance(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "SMF").
                allowMainThreadQueries()
/*                .addMigrations(new Migration(1, 2) {
                    @Override
                    public void migrate(@NonNull final SupportSQLiteDatabase database) {

                    }
                })*/
                .fallbackToDestructiveMigration()
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

    public void insertFormSchema(FormData... formData) {
        FormDataDao formDataDao = appDatabase.formDataDao();
        formDataDao.insertAll(formData);
        Log.d(TAG, "insertFormSchema");
    }

    public void updateFormSchema(FormData formData) {
        FormDataDao formDataDao = appDatabase.formDataDao();
        formDataDao.update(formData);
    }

    public FormData getFormSchema(final String processId) {
        FormDataDao formDataDao = appDatabase.formDataDao();
        return formDataDao.getFormSchema(processId);
    }

    public List<FormData> getAllFormSchema() {
        FormDataDao formDataDao = appDatabase.formDataDao();
        return formDataDao.getAllFormSchema();
    }

    public void updateFormSchemaSubmitCount(final String formID, final String count) {
        FormDataDao formDataDao = appDatabase.formDataDao();
        formDataDao.updateSubmitCount(formID, count);
    }

    public void deleteAllFormSchema() {
        FormDataDao formDataDao = appDatabase.formDataDao();
        formDataDao.deleteAllFormSchema();
    }

    public void deleteAllModules() {
        ModuleDao modulesDao = appDatabase.homeDao();
        modulesDao.deleteAllModules();
    }

    public void insertReportData(ReportData... formData) {
        ReportsDataDao formDataDao = appDatabase.reportDao();
        formDataDao.insertAll(formData);
        Log.d(TAG, "insertReportData");
    }

    public List<ReportData> getAllReports() {
        ReportsDataDao formDataDao = appDatabase.reportDao();
        return formDataDao.getAllReports();
    }

    public List<String> getAllFormResults(String formId) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        return formResultDao.getAllFormResults(formId);
    }

    public void insertFormResult(FormResult result) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        formResultDao.insertAll(result);
        Log.d(TAG, "insertFormResult");
    }

    public void deleteAllFormResults() {
        FormResultDao formResultDao = appDatabase.formResultDao();
        formResultDao.deleteAllFormResults();
    }

    public List<Modules> getAllModules() {
        ModuleDao modulesDao = appDatabase.homeDao();
        return modulesDao.getAllModules();
    }

    public List<Modules> getModulesOfStatus(String status) {
        ModuleDao modulesDao = appDatabase.homeDao();
        return modulesDao.getModulesOfStatus(status);
    }

    public void insertModule(Modules home) {
        ModuleDao modulesDao = appDatabase.homeDao();
        modulesDao.insertAll(home);
        Log.d(TAG, "insertModule");
    }

}
