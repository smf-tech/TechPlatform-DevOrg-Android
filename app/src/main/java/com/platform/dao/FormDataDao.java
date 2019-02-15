package com.platform.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.platform.models.forms.FormData;

import java.util.List;

@SuppressWarnings("unused")
@Dao
public interface FormDataDao {
    @Query("SELECT * FROM formdata where id = :formId")
    List<FormData> getFormSchema(String formId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FormData... formData);

    @Update
    void update(FormData formData);

    @Delete
    void delete(FormData formData);
}
