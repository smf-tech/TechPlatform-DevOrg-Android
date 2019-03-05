package com.platform.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.platform.models.forms.FormData;

@Dao
public interface FormDataDao {
    @Query("SELECT * FROM formdata where id = :formId")
    FormData getFormSchema(String formId);

    @Query("DELETE FROM formdata")
    void deleteAllFormSchema();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FormData... formData);

    @Update
    void update(FormData formData);

    @Delete
    void delete(FormData formData);
}
