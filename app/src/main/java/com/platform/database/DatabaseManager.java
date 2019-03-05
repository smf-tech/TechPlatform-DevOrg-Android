package com.platform.database;

import androidx.room.Room;
import android.content.Context;
import android.util.Log;

import com.platform.dao.FormDataDao;
import com.platform.dao.FormResultDao;
import com.platform.dao.ModuleDao;
import com.platform.dao.ProcessDataDao;
import com.platform.dao.ReportsDataDao;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.models.home.Modules;
import com.platform.models.pm.ProcessData;
import com.platform.models.reports.ReportData;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;

import java.util.List;

public class DatabaseManager {
    private static final String TAG = DatabaseManager.class.getSimpleName();
    private static AppDatabase appDatabase;
    private static DatabaseManager databaseManager;

    public static DatabaseManager getDBInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, Constants.App.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }

        return databaseManager;
    }

    public List<FormResult> getNonSyncedPendingForms() {
        FormResultDao formResultDao = appDatabase.formResultDao();
        return formResultDao.getAllFormResults(SyncAdapterUtils.FormStatus.UN_SYNCED);
    }

    public List<FormResult> getAllPartiallySavedForms() {
        FormResultDao formResultDao = appDatabase.formResultDao();
        return formResultDao.getAllFormResults(SyncAdapterUtils.FormStatus.PARTIAL);
    }

    public FormResult getPartiallySavedForm(String formID) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        return formResultDao.getPartialForm(SyncAdapterUtils.FormStatus.PARTIAL, formID);
    }

    public void insertFormSchema(FormData... formData) {
        FormDataDao formDataDao = appDatabase.formDataDao();
        formDataDao.insertAll(formData);
    }

    public void updateFormSchema(FormData formData) {
        FormDataDao formDataDao = appDatabase.formDataDao();
        formDataDao.update(formData);
    }

    public FormData getFormSchema(final String processId) {
        FormDataDao formDataDao = appDatabase.formDataDao();
        return formDataDao.getFormSchema(processId);
    }

    public void deleteAllFormSchema() {
        FormDataDao formDataDao = appDatabase.formDataDao();
        formDataDao.deleteAllFormSchema();
    }

    public void insertProcessData(ProcessData... processData) {
        ProcessDataDao processDataDao = appDatabase.processDataDao();
        processDataDao.insertAll(processData);
    }

    public ProcessData getProcessData(final String processId) {
        ProcessDataDao processDataDao = appDatabase.processDataDao();
        return processDataDao.getProcessData(processId);
    }

    public void updateProcessSubmitCount(final String processId, final String count) {
        ProcessDataDao processDataDao = appDatabase.processDataDao();
        processDataDao.updateSubmitCount(processId, count);
    }

    public List<ProcessData> getAllProcesses() {
        ProcessDataDao processDataDao = appDatabase.processDataDao();
        return processDataDao.getAllProcesses();
    }

    public void deleteAllProcesses() {
        ProcessDataDao processDataDao = appDatabase.processDataDao();
        processDataDao.deleteAllProcesses();
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

    public List<String> getAllFormResults(String formId, int formStatus) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        return formResultDao.getAllFormResults(formId, formStatus);
    }

    public void insertFormResult(FormResult result) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        formResultDao.insertAll(result);
        Log.d(TAG, "insertFormResult");
    }

    public void updateFormResult(FormResult result) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        formResultDao.update(result);
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
