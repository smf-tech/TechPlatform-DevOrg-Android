package com.platform.database;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.platform.dao.FormDataDao;
import com.platform.dao.HomeDao;
import com.platform.dao.ReportsDataDao;
import com.platform.dao.SavedFormDao;
import com.platform.models.SavedForm;
import com.platform.models.forms.FormData;
import com.platform.models.home.Modules;
import com.platform.models.reports.ReportData;

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

    public List<FormData> getAllFormSchema() {
        FormDataDao formDataDao = appDatabase.formDataDao();
        return formDataDao.getAllFormSchema();
    }

    public void deleteForm(FormData formData) {
        FormDataDao formDataDao = appDatabase.formDataDao();
        formDataDao.delete(formData);
    }

    public void insertReportData(ReportData... formData) {
        ReportsDataDao formDataDao = appDatabase.reportDao();
        formDataDao.insertAll(formData);
    }

    public List<ReportData> getAllReports() {
        ReportsDataDao formDataDao = appDatabase.reportDao();
        return formDataDao.getAllReports();
    }


/*    public List<FormResult> getAllFormResults(String formId) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        return formResultDao.getAllFormSchema();
    }

    public void insertFormResult(FormResult result) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        formResultDao.insertAll(result);
    }*/

    public List<Modules> getAllModules() {
        HomeDao modulesDao = appDatabase.homeDao();
        return modulesDao.getAllModules();
    }

    public List<Modules> getModulesOfStatus(String status) {
        HomeDao modulesDao = appDatabase.homeDao();
        return modulesDao.getModulesOfStatus(status);
    }

    public void insertModule(Modules home) {
        HomeDao modulesDao = appDatabase.homeDao();
        modulesDao.insertAll(home);
    }

   /* public void deleteModule(Modules home) {
        HomeDao modulesDao = appDatabase.homeDao();
        modulesDao.delete(home);
    }*/

}
