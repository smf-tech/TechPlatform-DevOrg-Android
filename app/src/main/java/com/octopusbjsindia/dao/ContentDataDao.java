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

    @Query("SELECT * FROM ContentData")
    List<ContentData> getContentData();

    @Query("SELECT DISTINCT category_name FROM ContentData")
    List<String> getDistinctCategories();

    @Query("SELECT * FROM ContentData where category_name = :categoryName")
    List<ContentData> getCategoryContent(String categoryName);

    @Query("DELETE FROM ContentData")
    void deleteContentData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ContentData contentData);

    @Update
    void update(ContentData contentData);

}
