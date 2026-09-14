package com.personal.wordmaster.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.personal.wordmaster.data.entity.StudyRecord;
import com.personal.wordmaster.data.entity.WordBook;
import com.personal.wordmaster.repository.WordRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final WordRepository repository;
    private final LiveData<List<WordBook>> allBooks;
    private final LiveData<StudyRecord> todayRecord;

    public MainViewModel(Application application) {
        super(application);
        repository = new WordRepository();
        allBooks = repository.getAllBooks();
        todayRecord = repository.getTodayRecord();
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
}
