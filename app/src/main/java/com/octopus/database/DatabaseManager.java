package com.octopus.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.octopus.dao.FormDataDao;
import com.octopus.dao.FormResultDao;
import com.octopus.dao.ModuleDao;
import com.octopus.dao.NotificationDataDao;
import com.octopus.dao.OperatorRequestResponseModelDao;
import com.octopus.dao.ProcessDataDao;
import com.octopus.dao.ReportsDataDao;
import com.octopus.dao.SSMasterDatabaseDao;
import com.octopus.dao.StructureDataDao;
import com.octopus.dao.StructurePripretionDataDao;
import com.octopus.dao.StructureVisitMonitoringDataDao;
import com.octopus.dao.UserAttendanceDao;
import com.octopus.models.forms.FormData;
import com.octopus.models.forms.FormResult;
import com.octopus.models.home.Modules;
import com.octopus.models.pm.ProcessData;
import com.octopus.models.reports.ReportData;
import com.octopus.syncAdapter.SyncAdapterUtils;
import com.octopus.utility.Constants;

import java.util.List;

public class DatabaseManager {
    private static final String TAG = DatabaseManager.class.getSimpleName();
    private static AppDatabase appDatabase;
    private static DatabaseManager databaseManager;

    public static DatabaseManager getDBInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, Constants.App.DATABASE_NAME)
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_OLD_TO_NEW)
                    .build();
        }

        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }

        return databaseManager;
    }

    private static final Migration MIGRATION_OLD_TO_NEW = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
          //database.execSQL("CREATE TABLE IF NOT EXISTS `${Notifications}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `text` TEXT, `toOpen` TEXT, `unread` INTEGER, `dateTime` TEXT)");
        }
    };

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

    public String getProcessSubmitCount(final String processId) {
        ProcessDataDao processDataDao = appDatabase.processDataDao();
        return processDataDao.getSubmitCount(processId);
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

    public void deleteAllReports() {
        ReportsDataDao formDataDao = appDatabase.reportDao();
        formDataDao.deleteAllReports();
    }

    public List<String> getAllFormResults(String formId, int formStatus) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        return formResultDao.getAllFormResults(formId, formStatus);
    }

    public List<String> getAllFormResults(String formId) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        return formResultDao.getAllFormResults(formId);
    }

    public FormResult getFormResult(String formId) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        return formResultDao.getFormResult(formId);
    }

    public void insertFormResult(FormResult result) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        formResultDao.insertAll(result);
        Log.d(TAG, "insertFormResult");
    }

    public void deleteFormResult(FormResult result) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        formResultDao.delete(result);
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

    public void deleteAllSyncedFormResults() {
        FormResultDao formResultDao = appDatabase.formResultDao();
        formResultDao.deleteAllSyncedFormResults(SyncAdapterUtils.FormStatus.SYNCED);
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

    public UserAttendanceDao getAttendaceSchema(){
        UserAttendanceDao userAttendanceDao=appDatabase.userAttendanceDao();
        return userAttendanceDao;
    }

    public NotificationDataDao getNotificationDataDeo(){
        NotificationDataDao notificationDataDao=appDatabase.notificationsDataDao();
        return notificationDataDao;
    }

    public OperatorRequestResponseModelDao getOperatorRequestResponseModelDao(){
        OperatorRequestResponseModelDao notificationDataDao=appDatabase.operatorRequestResponseModelDao();
        return notificationDataDao;
    }

    public SSMasterDatabaseDao getSSMasterDatabaseDao(){
        SSMasterDatabaseDao ssMasterDatabaseDao=appDatabase.ssMasterDatabaseDao();
        return ssMasterDatabaseDao;
    }

    public StructureDataDao getStructureDataDao(){
        StructureDataDao structureDataDao=appDatabase.structureDataDao();
        return structureDataDao;
    }

    public StructureVisitMonitoringDataDao getStructureVisitMonitoringDataDao(){
        StructureVisitMonitoringDataDao structureVisitMonitoringDataDao=appDatabase.structureVisitMonitoringDataDao();
        return structureVisitMonitoringDataDao;
    }

    public StructurePripretionDataDao getStructurePripretionDataDao(){
        StructurePripretionDataDao structurePripretionDataDao=appDatabase.structurePripretionDataDao();
        return structurePripretionDataDao;
    }

}
