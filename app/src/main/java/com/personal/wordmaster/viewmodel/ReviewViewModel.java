package com.personal.wordmaster.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.personal.wordmaster.data.entity.WordInfo;
import com.personal.wordmaster.repository.WordRepository;

import java.util.List;

public class ReviewViewModel extends AndroidViewModel {

    private final WordRepository repository;
    private List<WordInfo> reviewList;
    private int currentIndex = 0;

    public MutableLiveData<WordInfo> currentWord = new MutableLiveData<>();
    public MutableLiveData<String> meaning = new MutableLiveData<>();
    public MutableLiveData<Boolean> showMeaning = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> complete = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> empty = new MutableLiveData<>(false);

    public ReviewViewModel(Application application) {
        super(application);
        repository = new WordRepository();
    }

    public void loadReviewWords() {
        new Thread(() -> {
            List<WordInfo> words = repository.getAllWordsForReviewSync();
            reviewList = words;
            if (words == null || words.isEmpty()) {
                empty.postValue(true);
            } else {
                currentIndex = 0;
                currentWord.postValue(words.get(0));
                showMeaning.postValue(false);
                meaning.postValue("");
                empty.postValue(false);
            }
        }).start();
    }

    public void onKnown() {
        WordInfo word = currentWord.getValue();
        if (word == null) return;
        repository.updateWordAfterReviewKnown(word);
        nextWord();
    }

    public void onUnknown() {
        WordInfo word = currentWord.getValue();
        if (word == null) return;
        repository.updateWordAfterReviewUnknown(word);
        nextWord();
    }

    private void nextWord() {
        if (reviewList == null) return;
        currentIndex++;
        if (currentIndex >= reviewList.size()) {
            complete.setValue(true);
            return;
        }
        currentWord.setValue(reviewList.get(currentIndex));
        showMeaning.setValue(false);
        meaning.setValue("");
    }
}
