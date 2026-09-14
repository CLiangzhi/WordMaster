package com.personal.wordmaster.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.personal.wordmaster.data.entity.WordInfo;
import com.personal.wordmaster.repository.WordRepository;

import java.util.List;

public class LearnViewModel extends AndroidViewModel {

    private final WordRepository repository;
    private List<WordInfo> wordList;
    private int currentIndex = 0;

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
        new Thread(() -> {
            List<WordInfo> words = repository.getWordsByBookSync(bookId);
            wordList = words;
            if (words != null && !words.isEmpty()) {
                currentIndex = 0;
                currentWord.postValue(words.get(0));
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
            complete.setValue(true);
            return;
        }
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
}
