package com.octopusbjsindia.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.octopusbjsindia.dao.AccessibleLocationDataDao;
import com.octopusbjsindia.dao.ContentDataDao;
import com.octopusbjsindia.dao.FormDataDao;
import com.octopusbjsindia.dao.FormResultDao;
import com.octopusbjsindia.dao.ModuleDao;
import com.octopusbjsindia.dao.NotificationDataDao;
import com.octopusbjsindia.dao.OperatorRequestResponseModelDao;
import com.octopusbjsindia.dao.ProcessDataDao;
import com.octopusbjsindia.dao.ReportsDataDao;
import com.octopusbjsindia.dao.SSMasterDatabaseDao;
import com.octopusbjsindia.dao.StructureBoundaryDao;
import com.octopusbjsindia.dao.StructureDataDao;
import com.octopusbjsindia.dao.StructurePripretionDataDao;
import com.octopusbjsindia.dao.StructureVisitMonitoringDataDao;
import com.octopusbjsindia.dao.UserAttendanceDao;
import com.octopusbjsindia.models.forms.FormData;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.models.home.Modules;
import com.octopusbjsindia.models.pm.ProcessData;
import com.octopusbjsindia.models.reports.ReportData;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;

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
                    .addMigrations(MIGRATION_2_TO_3)
                    .addMigrations(MIGRATION_3_TO_4)
                    .addMigrations(MIGRATION_4_TO_5)
                    .addMigrations(MIGRATION_5_TO_6)
                    .addMigrations(MIGRATION_6_TO_7)
                    .addMigrations(MIGRATION_7_TO_8)
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

            database.execSQL("ALTER TABLE StructureData ADD COLUMN structureBoundary INTEGER NOT NULL DEFAULT 0");

            database.execSQL("CREATE TABLE IF NOT EXISTS `StructureBoundaryData`" +
                    " (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `structureId` TEXT, `structureBoundary` TEXT)");
        }
    };

    private static final Migration MIGRATION_2_TO_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE StructureData ADD COLUMN workStartDate TEXT");
            database.execSQL("ALTER TABLE StructureData ADD COLUMN workCompletedDate TEXT");

        }
    };

    private static final Migration MIGRATION_3_TO_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE StructurePripretionData ADD COLUMN beneficiary_id TEXT");
            database.execSQL("ALTER TABLE FormData ADD COLUMN api_url TEXT");
            database.execSQL("ALTER TABLE ProcessData ADD COLUMN api_url TEXT");
        }
    };

    private static final Migration MIGRATION_4_TO_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE FormData ADD COLUMN jurisdictions_ TEXT");

            database.execSQL("ALTER TABLE FormData ADD COLUMN location_required_level TEXT");

            database.execSQL("ALTER TABLE FormData ADD COLUMN location_required INTEGER");

            database.execSQL("CREATE TABLE IF NOT EXISTS `JurisdictionLocation`" +
                    " (`id` TEXT PRIMARY KEY NOT NULL, `name` TEXT, `parent_id` TEXT)");

            database.execSQL("ALTER TABLE ProcessData ADD COLUMN project_id TEXT");

            database.execSQL("ALTER TABLE FormResult ADD COLUMN rejection_reason TEXT");

            database.execSQL("CREATE TABLE IF NOT EXISTS ContentData (contentId TEXT PRIMARY KEY NOT NULL, category_id TEXT, category_name TEXT,content_title TEXT,file_type TEXT,file_size TEXT,downloadedFileName TEXT,languageDetailsString TEXT)");
        }
    };

    private static final Migration MIGRATION_5_TO_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("DROP TABLE JurisdictionLocation");
            database.execSQL("CREATE TABLE IF NOT EXISTS JurisdictionLocationV3 (autoId INTEGER PRIMARY KEY autoincrement NOT NULL, id TEXT, name TEXT,parent_id TEXT)");
        }
    };

    private static final Migration MIGRATION_6_TO_7 = new Migration(6, 7) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE SSMasterDatabase ADD COLUMN type TEXT");
        }
    };

    private static final Migration MIGRATION_7_TO_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

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
        return processDataDao.getAllProcesses(Util.getUserObjectFromPref().getProjectIds().get(0).getId());
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

    public List<FormResult> getFormResults(String formId, int formStatus) {
        FormResultDao formResultDao = appDatabase.formResultDao();
        return formResultDao.getFormResults(formId, formStatus);
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

    public UserAttendanceDao getAttendaceSchema() {
        UserAttendanceDao userAttendanceDao = appDatabase.userAttendanceDao();
        return userAttendanceDao;
    }

    public NotificationDataDao getNotificationDataDeo() {
        NotificationDataDao notificationDataDao = appDatabase.notificationsDataDao();
        return notificationDataDao;
    }

    public OperatorRequestResponseModelDao getOperatorRequestResponseModelDao() {
        OperatorRequestResponseModelDao notificationDataDao = appDatabase.operatorRequestResponseModelDao();
        return notificationDataDao;
    }

    public SSMasterDatabaseDao getSSMasterDatabaseDao() {
        SSMasterDatabaseDao ssMasterDatabaseDao = appDatabase.ssMasterDatabaseDao();
        return ssMasterDatabaseDao;
    }

    public StructureDataDao getStructureDataDao() {
        StructureDataDao structureDataDao = appDatabase.structureDataDao();
        return structureDataDao;
    }

    public StructureVisitMonitoringDataDao getStructureVisitMonitoringDataDao() {
        StructureVisitMonitoringDataDao structureVisitMonitoringDataDao = appDatabase.structureVisitMonitoringDataDao();
        return structureVisitMonitoringDataDao;
    }

    public StructurePripretionDataDao getStructurePripretionDataDao() {
        StructurePripretionDataDao structurePripretionDataDao = appDatabase.structurePripretionDataDao();
        return structurePripretionDataDao;
    }

    public StructureBoundaryDao getStructureBoundaryDao() {
        StructureBoundaryDao structureBoundaryDao = appDatabase.structureBoundaryDao();
        return structureBoundaryDao;
    }

    public ContentDataDao getContentDataDao() {
        ContentDataDao contentDataDao = appDatabase.contentDataDao();
        return contentDataDao;
    }

    public AccessibleLocationDataDao getAccessibleLocationData() {
        AccessibleLocationDataDao ssMasterDatabaseDao = appDatabase.accessibleLocationDataDao();
        return ssMasterDatabaseDao;
    }

}
