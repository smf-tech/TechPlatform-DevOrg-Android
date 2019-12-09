package com.octopusbjsindia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.octopusbjsindia.models.notifications.NotificationData;

import java.util.List;

@Dao
public interface NotificationDataDao {
    @Query("SELECT * FROM NotificationData")
    List<NotificationData> getAllNotifications();

    @Query("DELETE FROM NotificationData")
    void deleteAllNotifications();

    @Query("DELETE FROM NotificationData WHERE id  = :id")
    void deleteNotification(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NotificationData notifications);

    @Update
    void update(NotificationData notifications);

    @Query("SELECT * FROM NotificationData WHERE unread  = :unread")
    List<NotificationData> getUnRearNotifications(Boolean unread);
}
