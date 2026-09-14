package com.personal.wordmaster.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_book")
public class WordBook {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "total_count")
    private int totalCount;

    @ColumnInfo(name = "mastered_count")
    private int masteredCount;

    @ColumnInfo(name = "unfamiliar_count")
    private int unfamiliarCount;

    @ColumnInfo(name = "created_time")
    private long createdTime;

    public WordBook(String name, int totalCount, long createdTime) {
        this.name = name;
        this.totalCount = totalCount;
        this.masteredCount = 0;
        this.unfamiliarCount = totalCount;
        this.createdTime = createdTime;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public int getMasteredCount() { return masteredCount; }
    public void setMasteredCount(int masteredCount) { this.masteredCount = masteredCount; }

    public int getUnfamiliarCount() { return unfamiliarCount; }
    public void setUnfamiliarCount(int unfamiliarCount) { this.unfamiliarCount = unfamiliarCount; }

    public long getCreatedTime() { return createdTime; }
    public void setCreatedTime(long createdTime) { this.createdTime = createdTime; }
}
