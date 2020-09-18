package com.octopusbjsindia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.octopusbjsindia.models.content.ContentData;

import java.util.List;

@Dao
public interface ContentDataDao {

    @Query("SELECT * FROM ContentData where project_id = :projectId")
    List<ContentData> getContentData(String projectId);

    @Query("SELECT DISTINCT category_name FROM ContentData where project_id = :projectId")
    List<String> getDistinctCategories(String projectId);

    @Query("SELECT * FROM ContentData where category_name = :categoryName  and project_id = :projectId")
    List<ContentData> getCategoryContent(String categoryName, String projectId);

    @Query("DELETE FROM ContentData")
    void deleteContentData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ContentData contentData);

    @Update
    void update(ContentData contentData);

}
