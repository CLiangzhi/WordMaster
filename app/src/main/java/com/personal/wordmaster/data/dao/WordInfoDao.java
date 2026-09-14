package com.personal.wordmaster.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.personal.wordmaster.data.entity.WordInfo;

import java.util.List;

@Dao
public interface WordInfoDao {

    @Insert
    void insertAll(List<WordInfo> words);

    @Insert
    long insert(WordInfo word);

    @Update
    void update(WordInfo word);

    @Query("SELECT * FROM word_info WHERE book_id = :bookId ORDER BY id ASC")
    LiveData<List<WordInfo>> getWordsByBook(long bookId);

    @Query("SELECT * FROM word_info WHERE book_id = :bookId ORDER BY id ASC")
    List<WordInfo> getWordsByBookSync(long bookId);

    @Query("SELECT * FROM word_info WHERE book_id = :bookId AND mastery_status = 0 " +
           "AND next_review_time <= :currentTime ORDER BY next_review_time ASC")
    LiveData<List<WordInfo>> getWordsForReview(long bookId, long currentTime);

    @Query("SELECT * FROM word_info WHERE mastery_status = 0 " +
           "AND next_review_time <= :currentTime ORDER BY next_review_time ASC")
    List<WordInfo> getAllWordsForReviewSync(long currentTime);

    @Query("SELECT COUNT(*) FROM word_info WHERE book_id = :bookId")
    int getWordCountByBook(long bookId);

    @Query("SELECT COUNT(*) FROM word_info WHERE book_id = :bookId AND mastery_status = 1")
    int getMasteredCountByBook(long bookId);

    @Query("SELECT COUNT(*) FROM word_info WHERE book_id = :bookId AND mastery_status = 0")
    int getUnfamiliarCountByBook(long bookId);

    @Query("UPDATE word_info SET mastery_status = 0 WHERE book_id = :bookId AND mastery_status = 1")
    void resetAllMasteredInBook(long bookId);

    @Query("SELECT * FROM word_info")
    List<WordInfo> getAllWordsSync();
}
