package com.platform.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.platform.models.notifications.Notifications;
import java.util.List;

@Dao
public interface NotificationsDao {
    @Query("SELECT * FROM Notifications")
    List<Notifications> getAllNotifications();

    @Query("DELETE FROM Notifications")
    void deleteAllNotifications();

    @Query("DELETE FROM Notifications WHERE id  = :id")
    void deleteNotification(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notifications notifications);

    @Update
    void update(Notifications notifications);

    @Query("SELECT * FROM Notifications WHERE isNew  = :unread")
    List<Notifications> getUnRearNotifications(String unread);
}
