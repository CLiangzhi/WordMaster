package com.personal.wordmaster.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "word_info",
    foreignKeys = @ForeignKey(
        entity = WordBook.class,
        parentColumns = "id",
        childColumns = "book_id",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("book_id")}
)
public class WordInfo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "book_id")
    private long bookId;

    @ColumnInfo(name = "word")
    private String word;

    @ColumnInfo(name = "meaning")
    private String meaning;

    @ColumnInfo(name = "known_count")
    private int knownCount;

    @ColumnInfo(name = "unknown_count")
    private int unknownCount;

    @ColumnInfo(name = "mastery_rate")
    private float masteryRate;

    @ColumnInfo(name = "mastery_status")
    private int masteryStatus;

    @ColumnInfo(name = "next_review_time")
    private long nextReviewTime;

    @ColumnInfo(name = "first_added_time")
    private long firstAddedTime;

    @ColumnInfo(name = "last_study_time")
    private long lastStudyTime;

    public WordInfo(long bookId, String word, String meaning, long firstAddedTime) {
        this.bookId = bookId;
        this.word = word;
        this.meaning = meaning;
        this.knownCount = 0;
        this.unknownCount = 0;
        this.masteryRate = 0.0f;
        this.masteryStatus = 0;
        this.nextReviewTime = firstAddedTime;
        this.firstAddedTime = firstAddedTime;
        this.lastStudyTime = firstAddedTime;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getBookId() { return bookId; }
    public void setBookId(long bookId) { this.bookId = bookId; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }

    public int getKnownCount() { return knownCount; }
    public void setKnownCount(int knownCount) { this.knownCount = knownCount; }

    public int getUnknownCount() { return unknownCount; }
    public void setUnknownCount(int unknownCount) { this.unknownCount = unknownCount; }

    public float getMasteryRate() { return masteryRate; }
    public void setMasteryRate(float masteryRate) { this.masteryRate = masteryRate; }

    public int getMasteryStatus() { return masteryStatus; }
    public void setMasteryStatus(int masteryStatus) { this.masteryStatus = masteryStatus; }

    public long getNextReviewTime() { return nextReviewTime; }
    public void setNextReviewTime(long nextReviewTime) { this.nextReviewTime = nextReviewTime; }

    public long getFirstAddedTime() { return firstAddedTime; }
    public void setFirstAddedTime(long firstAddedTime) { this.firstAddedTime = firstAddedTime; }

    public long getLastStudyTime() { return lastStudyTime; }
    public void setLastStudyTime(long lastStudyTime) { this.lastStudyTime = lastStudyTime; }
}
