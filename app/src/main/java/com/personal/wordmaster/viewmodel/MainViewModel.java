package com.personal.wordmaster.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.personal.wordmaster.data.entity.DictionaryWord;
import com.personal.wordmaster.data.entity.StudyRecord;
import com.personal.wordmaster.data.entity.WordBook;
import com.personal.wordmaster.repository.WordRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final WordRepository repository;
    private final LiveData<List<WordBook>> allBooks;
    private final LiveData<StudyRecord> todayRecord;

    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    public final LiveData<List<DictionaryWord>> searchResults;

    public MainViewModel(Application application) {
        super(application);
        repository = new WordRepository();
        allBooks = repository.getAllBooks();
        todayRecord = repository.getTodayRecord();
        searchResults = Transformations.switchMap(searchQuery, q -> {
            if (q == null || q.trim().isEmpty()) {
                MutableLiveData<List<DictionaryWord>> empty = new MutableLiveData<>();
                empty.setValue(null);
                return empty;
            }
            return repository.searchDictionary(q.trim().toLowerCase());
        });
    }

    public LiveData<List<WordBook>> getAllBooks() {
        return allBooks;
    }

    public LiveData<StudyRecord> getTodayRecord() {
        return todayRecord;
    }

    public void deleteBook(long bookId) {
        repository.deleteBook(bookId);
    }

    public void setSearchQuery(String q) {
        searchQuery.setValue(q == null ? "" : q);
    }
}
