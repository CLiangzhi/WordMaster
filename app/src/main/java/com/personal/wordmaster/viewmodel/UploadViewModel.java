package com.personal.wordmaster.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.personal.wordmaster.data.entity.WordBook;
import com.personal.wordmaster.data.entity.WordInfo;
import com.personal.wordmaster.repository.WordRepository;

import java.util.List;

public class UploadViewModel extends AndroidViewModel {

    private final WordRepository repository;
    public MutableLiveData<String> status = new MutableLiveData<>();
    public MutableLiveData<Boolean> success = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();

    public UploadViewModel(Application application) {
        super(application);
        repository = new WordRepository();
    }

    public void parseImages(List<Bitmap> bitmaps, String bookName) {
        repository.parseImagesAndSave(bitmaps, bookName, new WordRepository.ParseCallback() {
            @Override
            public void onProgress(String msg) {
                status.postValue(msg);
            }

            @Override
            public void onSuccess(WordBook book, List<WordInfo> words) {
                status.postValue("解析完成，共 " + words.size() + " 个单词");
                success.postValue(true);
            }

            @Override
            public void onError(String msg) {
                error.postValue(msg);
            }
        });
    }
}
