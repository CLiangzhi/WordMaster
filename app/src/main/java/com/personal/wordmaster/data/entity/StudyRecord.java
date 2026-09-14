package com.personal.wordmaster.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "study_record")
public class StudyRecord {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "study_date")
    private String studyDate;

    @ColumnInfo(name = "new_word_count")
    private int newWordCount;

    @ColumnInfo(name = "review_word_count")
    private int reviewWordCount;

    @ColumnInfo(name = "last_update_time")
    private long lastUpdateTime;

    public StudyRecord(String studyDate, int newWordCount, int reviewWordCount, long lastUpdateTime) {
        this.studyDate = studyDate;
        this.newWordCount = newWordCount;
        this.reviewWordCount = reviewWordCount;
        this.lastUpdateTime = lastUpdateTime;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getStudyDate() { return studyDate; }
    public void setStudyDate(String studyDate) { this.studyDate = studyDate; }

    public int getNewWordCount() { return newWordCount; }
    public void setNewWordCount(int newWordCount) { this.newWordCount = newWordCount; }

    public int getReviewWordCount() { return reviewWordCount; }
    public void setReviewWordCount(int reviewWordCount) { this.reviewWordCount = reviewWordCount; }

    public long getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(long lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }
}
