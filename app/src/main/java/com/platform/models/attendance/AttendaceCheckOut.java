package com.platform.models.attendance;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "UserChheckOutAttendance")
public class AttendaceCheckOut {




        @PrimaryKey
        @NonNull
        public String uid;
        public double latitude;
        public double longitude;
        public String Address;
        public Long attendaceDate;
        public String attendanceType;

        public String getAttendanceFormattedDate() {
            return attendanceFormattedDate;
        }

        public void setAttendanceFormattedDate(String attendanceFormattedDate) {
            this.attendanceFormattedDate = attendanceFormattedDate;
        }

        public String attendanceFormattedDate;
        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String time;

        public Boolean getSync() {
            return isSync;
        }

        public void setSync(Boolean sync) {
            isSync = sync;
        }

        public Boolean isSync;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public Long getAttendaceDate() {
            return attendaceDate;
        }

        public void setAttendaceDate(Long attendaceDate) {
            this.attendaceDate = attendaceDate;
        }

        public String getAttendanceType() {
            return attendanceType;
        }

        public void setAttendanceType(String attendanceType) {
            this.attendanceType = attendanceType;
        }


    }



