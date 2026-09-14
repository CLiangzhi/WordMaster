package com.personal.wordmaster.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.personal.wordmaster.data.dao.StudyRecordDao;
import com.personal.wordmaster.data.dao.WordBookDao;
import com.personal.wordmaster.data.dao.WordInfoDao;
import com.personal.wordmaster.data.entity.StudyRecord;
import com.personal.wordmaster.data.entity.WordBook;
import com.personal.wordmaster.data.entity.WordInfo;

@Database(
    entities = {WordBook.class, WordInfo.class, StudyRecord.class},
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract WordBookDao wordBookDao();
    public abstract WordInfoDao wordInfoDao();
    public abstract StudyRecordDao studyRecordDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "wordmaster.db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
