package com.octopusbjsindia.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

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
import com.octopusbjsindia.dao.UserCheckOutDao;
import com.octopusbjsindia.models.Operator.OperatorRequestResponseModel;
import com.octopusbjsindia.models.SavedForm;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.SujalamSuphalam.StructureBoundaryData;
import com.octopusbjsindia.models.SujalamSuphalam.StructureData;
import com.octopusbjsindia.models.SujalamSuphalam.StructurePripretionData;
import com.octopusbjsindia.models.SujalamSuphalam.StructureVisitMonitoringData;
import com.octopusbjsindia.models.attendance.AttendaceCheckOut;
import com.octopusbjsindia.models.attendance.AttendaceData;
import com.octopusbjsindia.models.content.ContentData;
import com.octopusbjsindia.models.forms.FormData;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.models.home.Modules;
import com.octopusbjsindia.models.notifications.NotificationData;
import com.octopusbjsindia.models.pm.ProcessData;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.reports.ReportData;

@Database(entities = {SavedForm.class, FormData.class, Modules.class, ReportData.class, FormResult.class,
        ProcessData.class, AttendaceData.class, AttendaceCheckOut.class, NotificationData.class, OperatorRequestResponseModel.class, SSMasterDatabase.class,
        StructureData.class, StructureVisitMonitoringData.class, StructurePripretionData.class, StructureBoundaryData.class, ContentData.class,
        JurisdictionLocationV3.class},
        version = 8)

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

    public abstract StructureBoundaryDao structureBoundaryDao();

    public abstract ContentDataDao contentDataDao();

    public abstract AccessibleLocationDataDao accessibleLocationDataDao();

}
