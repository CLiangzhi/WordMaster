package com.personal.wordmaster;

import android.app.Application;

import com.personal.wordmaster.data.database.AppDatabase;
import com.personal.wordmaster.repository.WordRepository;

public class WordMasterApp extends Application {

    private static AppDatabase database;
    private static WordMasterApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = AppDatabase.getInstance(this);
        new WordRepository().backfillDictionary();
    }

    public static AppDatabase getDatabase() {
        return database;
    }

    public static WordMasterApp getInstance() {
        return instance;
    }
}
