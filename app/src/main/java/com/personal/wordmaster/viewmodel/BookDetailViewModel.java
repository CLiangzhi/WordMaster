package com.personal.wordmaster.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.personal.wordmaster.data.entity.WordBook;
import com.personal.wordmaster.repository.WordRepository;

public class BookDetailViewModel extends AndroidViewModel {

    private final WordRepository repository;

    public BookDetailViewModel(Application application) {
        super(application);
        repository = new WordRepository();
    }

    public LiveData<WordBook> getBookById(long bookId) {
        return repository.getBookById(bookId);
    }
}
