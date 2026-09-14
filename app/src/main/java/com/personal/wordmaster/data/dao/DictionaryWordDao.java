package com.personal.wordmaster.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.personal.wordmaster.data.entity.DictionaryWord;

import java.util.List;

@Dao
public interface DictionaryWordDao {

    @Insert
    long insert(DictionaryWord word);

    @Update
    void update(DictionaryWord word);

    @Query("SELECT * FROM dictionary_word WHERE word = :word LIMIT 1")
    DictionaryWord getByWord(String word);

    @Query("SELECT * FROM dictionary_word WHERE word LIKE '%' || :query || '%' ORDER BY word ASC LIMIT 100")
    LiveData<List<DictionaryWord>> searchWords(String query);

    @Query("SELECT * FROM dictionary_word ORDER BY word ASC")
    List<DictionaryWord> getAllSync();
}
