package com.platform.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.platform.dao.FormDataDao;
import com.platform.dao.FormResultDao;
import com.platform.dao.ModuleDao;
import com.platform.dao.NotificationDataDao;
import com.platform.dao.OperatorRequestResponseModelDao;
import com.platform.dao.ProcessDataDao;
import com.platform.dao.ReportsDataDao;
import com.platform.dao.SSMasterDatabaseDao;
import com.platform.dao.UserAttendanceDao;
import com.platform.dao.UserCheckOutDao;
import com.platform.models.Operator.OperatorRequestResponseModel;
import com.platform.models.SavedForm;
import com.platform.models.SujalamSuphalam.SSMasterDatabase;
import com.platform.models.attendance.AttendaceCheckOut;
import com.platform.models.attendance.AttendaceData;
import com.platform.models.forms.FormData;
import com.platform.models.forms.FormResult;
import com.platform.models.home.Modules;
import com.platform.models.notifications.NotificationData;
import com.platform.models.pm.ProcessData;
import com.platform.models.reports.ReportData;

@Database(entities = {SavedForm.class, FormData.class, Modules.class, ReportData.class, FormResult.class,
        ProcessData.class,AttendaceData.class, AttendaceCheckOut.class, NotificationData.class, OperatorRequestResponseModel.class, SSMasterDatabase.class},
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

}
