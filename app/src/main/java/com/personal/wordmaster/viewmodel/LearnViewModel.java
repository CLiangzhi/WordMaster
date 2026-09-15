package com.personal.wordmaster.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.personal.wordmaster.data.entity.WordInfo;
import com.personal.wordmaster.repository.WordRepository;

import java.util.List;

public class LearnViewModel extends AndroidViewModel {

    private static final String PREFS_NAME = "learn_progress";
    private static final String PREF_PREFIX = "learn_progress_";

    private final WordRepository repository;
    private List<WordInfo> wordList;
    private int currentIndex = 0;
    private long bookId = -1;

    public MutableLiveData<WordInfo> currentWord = new MutableLiveData<>();
    public MutableLiveData<String> meaning = new MutableLiveData<>();
    public MutableLiveData<Boolean> showMeaning = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> complete = new MutableLiveData<>(false);
    public MutableLiveData<Integer> progress = new MutableLiveData<>();

    public LearnViewModel(Application application) {
        super(application);
        repository = new WordRepository();
    }

    public void loadWords(long bookId) {
        this.bookId = bookId;
        new Thread(() -> {
            List<WordInfo> words = repository.getWordsByBookSync(bookId);
            wordList = words;
            if (words != null && !words.isEmpty()) {
                int saved = loadProgress(bookId);
                currentIndex = Math.min(Math.max(saved, 0), words.size() - 1);
                currentWord.postValue(words.get(currentIndex));
                showMeaning.postValue(false);
                meaning.postValue("");
                updateProgress();
            }
        }).start();
    }

    public void recordKnown() {
        WordInfo word = currentWord.getValue();
        if (word == null) return;
        repository.updateWordAfterKnown(word);
    }

    public void recordUnknown() {
        WordInfo word = currentWord.getValue();
        if (word == null) return;
        repository.updateWordAfterUnknown(word);
    }

    public void nextWord() {
        if (wordList == null) return;
        currentIndex++;
        if (currentIndex >= wordList.size()) {
            saveProgress(bookId, 0);
            complete.setValue(true);
            return;
        }
        saveProgress(bookId, currentIndex);
        currentWord.setValue(wordList.get(currentIndex));
        showMeaning.setValue(false);
        meaning.setValue("");
        updateProgress();
    }

    private void updateProgress() {
        progress.postValue(currentIndex + 1);
    }

    public int getTotalCount() {
        return wordList == null ? 0 : wordList.size();
    }

    private SharedPreferences prefs() {
        return getApplication().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private int loadProgress(long bookId) {
        return prefs().getInt(PREF_PREFIX + bookId, 0);
    }

    private void saveProgress(long bookId, int index) {
        prefs().edit().putInt(PREF_PREFIX + bookId, index).apply();
    }
}
