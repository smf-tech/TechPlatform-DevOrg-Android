package com.octopus.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

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
import com.octopus.dao.UserCheckOutDao;
import com.octopus.models.Operator.OperatorRequestResponseModel;
import com.octopus.models.SavedForm;
import com.octopus.models.SujalamSuphalam.SSMasterDatabase;
import com.octopus.models.SujalamSuphalam.StructureData;
import com.octopus.models.SujalamSuphalam.StructurePripretionData;
import com.octopus.models.SujalamSuphalam.StructureVisitMonitoringData;
import com.octopus.models.attendance.AttendaceCheckOut;
import com.octopus.models.attendance.AttendaceData;
import com.octopus.models.forms.FormData;
import com.octopus.models.forms.FormResult;
import com.octopus.models.home.Modules;
import com.octopus.models.notifications.NotificationData;
import com.octopus.models.pm.ProcessData;
import com.octopus.models.reports.ReportData;

@Database(entities = {SavedForm.class, FormData.class, Modules.class, ReportData.class, FormResult.class,
        ProcessData.class,AttendaceData.class, AttendaceCheckOut.class, NotificationData.class, OperatorRequestResponseModel.class, SSMasterDatabase.class,
        StructureData.class, StructureVisitMonitoringData.class, StructurePripretionData.class},
        version = 1)

public abstract class AppDatabase extends RoomDatabase {

    public abstract ProcessDataDao processDataDao();

    public abstract FormDataDao formDataDao();

    public abstract ModuleDao homeDao();

    public abstract ReportsDataDao reportDao();

    public abstract FormResultDao formResultDao();

    public abstract UserAttendanceDao userAttendanceDao();

    public abstract UserCheckOutDao userCheckOutDao();

    public abstract NotificationDataDao notificationsDataDao();

    public abstract OperatorRequestResponseModelDao operatorRequestResponseModelDao();

    public abstract SSMasterDatabaseDao ssMasterDatabaseDao();

    public abstract StructureDataDao structureDataDao();

    public abstract StructureVisitMonitoringDataDao structureVisitMonitoringDataDao();

    public abstract StructurePripretionDataDao structurePripretionDataDao();

}
