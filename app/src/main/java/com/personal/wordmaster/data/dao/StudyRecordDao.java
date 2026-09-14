package com.personal.wordmaster.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.personal.wordmaster.data.entity.StudyRecord;

@Dao
public interface StudyRecordDao {

    @Insert
    long insert(StudyRecord record);

    @Update
    void update(StudyRecord record);

    @Query("SELECT * FROM study_record WHERE study_date = :date LIMIT 1")
    StudyRecord getRecordByDate(String date);

    @Query("SELECT * FROM study_record WHERE study_date = :date LIMIT 1")
    LiveData<StudyRecord> getRecordByDateLive(String date);

    @Query("SELECT * FROM study_record ORDER BY study_date DESC LIMIT 7")
    LiveData<StudyRecord> getRecentRecords();
}
