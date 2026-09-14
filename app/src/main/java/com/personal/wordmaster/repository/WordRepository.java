package com.personal.wordmaster.repository;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import com.personal.wordmaster.WordMasterApp;
import com.personal.wordmaster.algorithm.ReviewAlgorithm;
import com.personal.wordmaster.data.dao.DictionaryWordDao;
import com.personal.wordmaster.data.dao.StudyRecordDao;
import com.personal.wordmaster.data.dao.WordBookDao;
import com.personal.wordmaster.data.dao.WordInfoDao;
import com.personal.wordmaster.data.database.AppDatabase;
import com.personal.wordmaster.data.entity.DictionaryWord;
import com.personal.wordmaster.data.entity.StudyRecord;
import com.personal.wordmaster.data.entity.WordBook;
import com.personal.wordmaster.data.entity.WordInfo;
import com.personal.wordmaster.network.service.DeepSeekService;
import com.personal.wordmaster.network.utils.OcrUtils;
import com.personal.wordmaster.utils.ApiKeyUtils;
import com.personal.wordmaster.utils.DateUtils;
import com.personal.wordmaster.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordRepository {

    private final WordBookDao bookDao;
    private final WordInfoDao wordDao;
    private final StudyRecordDao recordDao;
    private final DictionaryWordDao dictionaryDao;
    private DeepSeekService deepSeekService;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public WordRepository() {
        AppDatabase db = WordMasterApp.getDatabase();
        this.bookDao = db.wordBookDao();
        this.wordDao = db.wordInfoDao();
        this.recordDao = db.studyRecordDao();
        this.dictionaryDao = db.dictionaryWordDao();
    }

    // ===== 词书操作 =====

    public LiveData<List<WordBook>> getAllBooks() {
        return bookDao.getAllBooks();
    }

    public LiveData<WordBook> getBookById(long bookId) {
        return bookDao.getBookById(bookId);
    }

    public void deleteBook(long bookId) {
        executor.execute(() -> bookDao.deleteById(bookId));
    }

    public WordBook getBookByIdSync(long bookId) {
        return bookDao.getBookByIdSync(bookId);
    }

    // ===== 单词操作 =====

    public LiveData<List<WordInfo>> getWordsByBook(long bookId) {
        return wordDao.getWordsByBook(bookId);
    }

    public List<WordInfo> getWordsByBookSync(long bookId) {
        return wordDao.getWordsByBookSync(bookId);
    }

    public LiveData<List<WordInfo>> getWordsForReview(long bookId) {
        return wordDao.getWordsForReview(bookId, System.currentTimeMillis());
    }

    public List<WordInfo> getAllWordsForReviewSync() {
        return wordDao.getAllWordsForReviewSync(System.currentTimeMillis());
    }

    public void updateWordAfterKnown(WordInfo word) {
        executor.execute(() -> {
            ReviewAlgorithm.updateAfterKnown(word);
            wordDao.update(word);
            updateBookStats(word.getBookId());
            updateTodayStudyRecord(1, 0);
        });
    }

    public void updateWordAfterUnknown(WordInfo word) {
        executor.execute(() -> {
            ReviewAlgorithm.updateAfterUnknown(word);
            wordDao.update(word);
            updateBookStats(word.getBookId());
            updateTodayStudyRecord(2, 0);
        });
    }

    public void updateWordAfterReviewKnown(WordInfo word) {
        executor.execute(() -> {
            ReviewAlgorithm.updateAfterKnown(word);
            wordDao.update(word);
            updateBookStats(word.getBookId());
            updateTodayStudyRecord(0, 1);
        });
    }

    public void updateWordAfterReviewUnknown(WordInfo word) {
        executor.execute(() -> {
            ReviewAlgorithm.updateAfterUnknown(word);
            wordDao.update(word);
            updateBookStats(word.getBookId());
            updateTodayStudyRecord(0, 1);
        });
    }

    // ===== 图片解析 =====

    public interface ParseCallback {
        void onProgress(String status);
        void onSuccess(WordBook book, List<WordInfo> words);
        void onError(String message);
    }

    public void parseImagesAndSave(List<Bitmap> bitmaps, String bookName, ParseCallback callback) {
        String apiKey = ApiKeyUtils.getApiKey();
        if (apiKey == null || apiKey.trim().isEmpty()) {
            callback.onError("请先填写 API Key");
            return;
        }

        if (bitmaps == null || bitmaps.isEmpty()) {
            callback.onError("请先选择图片");
            return;
        }

        deepSeekService = new DeepSeekService(apiKey);
        executor.execute(() -> ocrAllBitmaps(bitmaps, 0, new StringBuilder(), bookName, callback));
    }

    private void ocrAllBitmaps(List<Bitmap> bitmaps, int index, StringBuilder allText,
                               String bookName, ParseCallback callback) {
        if (index >= bitmaps.size()) {
            String combined = allText.toString().trim();
            if (combined.isEmpty()) {
                callback.onError("所有图片均未识别到文字");
                return;
            }
            callback.onProgress("已识别 " + bitmaps.size() + " 张图片，正在解析单词…");
            deepSeekService.parseOcrText(combined, new DeepSeekService.ParseCallback() {
                @Override
                public void onSuccess(String jsonResult) {
                    executor.execute(() -> saveWordsFromJson(jsonResult, bookName, callback));
                }

                @Override
                public void onError(String message) {
                    callback.onError(message);
                }
            });
            return;
        }

        callback.onProgress("正在识别第 " + (index + 1) + "/" + bitmaps.size() + " 张图片…");
        OcrUtils.recognizeText(bitmaps.get(index), new OcrUtils.OcrCallback() {
            @Override
            public void onSuccess(String text) {
                allText.append(text).append("\n");
                ocrAllBitmaps(bitmaps, index + 1, allText, bookName, callback);
            }

            @Override
            public void onError(String message) {
                allText.append("\n");
                ocrAllBitmaps(bitmaps, index + 1, allText, bookName, callback);
            }
        });
    }

    private void saveWordsFromJson(String jsonResult, String bookName, ParseCallback callback) {
        try {
            List<JsonUtils.WordEntry> entries = JsonUtils.parseWordList(jsonResult);
            if (entries == null || entries.isEmpty()) {
                callback.onError("未识别到单词，请检查图片");
                return;
            }

            List<JsonUtils.WordEntry> unique = new ArrayList<>();
            java.util.Set<String> seen = new java.util.HashSet<>();
            for (JsonUtils.WordEntry e : entries) {
                if (seen.add(e.getWord().toLowerCase())) {
                    unique.add(e);
                }
            }

            long now = System.currentTimeMillis();
            WordBook book = new WordBook(bookName, unique.size(), now);
            long bookId = bookDao.insert(book);

            List<WordInfo> words = new ArrayList<>();
            for (JsonUtils.WordEntry e : unique) {
                words.add(new WordInfo(bookId, e.getWord(), e.getMeaning(), now));
            }
            wordDao.insertAll(words);
            addWordsToDictionary(words);

            callback.onSuccess(book, words);
        } catch (Exception e) {
            callback.onError("保存数据失败: " + e.getMessage());
        }
    }

    // ===== 单词库 =====

    public LiveData<List<DictionaryWord>> searchDictionary(String query) {
        return dictionaryDao.searchWords(query);
    }

    public void backfillDictionary() {
        executor.execute(() -> {
            List<WordInfo> all = wordDao.getAllWordsSync();
            if (all != null && !all.isEmpty()) {
                addWordsToDictionary(all);
            }
        });
    }

    private void addWordsToDictionary(List<WordInfo> words) {
        for (WordInfo w : words) {
            if (w.getWord() == null) continue;
            String key = w.getWord().trim().toLowerCase();
            if (key.isEmpty()) continue;
            String newMeaning = w.getMeaning() == null ? "" : w.getMeaning().trim();
            DictionaryWord existing = dictionaryDao.getByWord(key);
            if (existing == null) {
                dictionaryDao.insert(new DictionaryWord(key, newMeaning));
            } else {
                String merged = mergeMeaning(existing.getMeaning(), newMeaning);
                if (!merged.equals(existing.getMeaning())) {
                    existing.setMeaning(merged);
                    dictionaryDao.update(existing);
                }
            }
        }
    }

    private String mergeMeaning(String existing, String incoming) {
        if (existing == null || existing.isEmpty()) return incoming == null ? "" : incoming;
        if (incoming == null || incoming.isEmpty()) return existing;
        if (existing.contains(incoming)) return existing;
        if (incoming.contains(existing)) return incoming;
        return existing + "；" + incoming;
    }

    // ===== 学习统计 =====

    public LiveData<StudyRecord> getTodayRecord() {
        return recordDao.getRecordByDateLive(DateUtils.getTodayDate());
    }

    private void updateTodayStudyRecord(int newWords, int reviewWords) {
        String today = DateUtils.getTodayDate();
        long now = System.currentTimeMillis();
        StudyRecord record = recordDao.getRecordByDate(today);

        if (record == null) {
            record = new StudyRecord(today, 0, 0, now);
            if (newWords == 1 || newWords == 2) {
                record.setNewWordCount(1);
            }
            if (reviewWords > 0) {
                record.setReviewWordCount(1);
            }
            recordDao.insert(record);
        } else {
            if (newWords == 1 || newWords == 2) {
                record.setNewWordCount(record.getNewWordCount() + 1);
            }
            if (reviewWords > 0) {
                record.setReviewWordCount(record.getReviewWordCount() + 1);
            }
            record.setLastUpdateTime(now);
            recordDao.update(record);
        }
    }

    private void updateBookStats(long bookId) {
        int mastered = wordDao.getMasteredCountByBook(bookId);
        int unfamiliar = wordDao.getUnfamiliarCountByBook(bookId);
        AppDatabase db = WordMasterApp.getDatabase();
        WordBook book = db.wordBookDao().getBookByIdSync(bookId);
        if (book != null) {
            book.setMasteredCount(mastered);
            book.setUnfamiliarCount(unfamiliar);
            db.wordBookDao().update(book);
        }
    }
}
