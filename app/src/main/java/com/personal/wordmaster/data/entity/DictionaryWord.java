package com.personal.wordmaster.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "dictionary_word",
    indices = {@Index(value = "word", unique = true)}
)
public class DictionaryWord {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "word")
    private String word;

    @ColumnInfo(name = "meaning")
    private String meaning;

    public DictionaryWord(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }
}
