package com.personal.wordmaster.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.personal.wordmaster.data.entity.WordBook;

import java.util.List;

@Dao
public interface WordBookDao {

    @Insert
    long insert(WordBook book);

    @Update
    void update(WordBook book);

    @Delete
    void delete(WordBook book);

    @Query("SELECT * FROM word_book ORDER BY created_time DESC")
    LiveData<List<WordBook>> getAllBooks();

    @Query("SELECT * FROM word_book WHERE id = :bookId")
    LiveData<WordBook> getBookById(long bookId);

    @Query("SELECT * FROM word_book WHERE id = :bookId")
    WordBook getBookByIdSync(long bookId);

    @Query("DELETE FROM word_book WHERE id = :bookId")
    void deleteById(long bookId);
}
