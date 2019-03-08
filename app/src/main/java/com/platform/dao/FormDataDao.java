package com.platform.dao;

import com.platform.models.forms.FormData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

}
