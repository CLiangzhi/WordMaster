package com.personal.wordmaster.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.personal.wordmaster.data.dao.DictionaryWordDao;
import com.personal.wordmaster.data.dao.StudyRecordDao;
import com.personal.wordmaster.data.dao.WordBookDao;
import com.personal.wordmaster.data.dao.WordInfoDao;
import com.personal.wordmaster.data.entity.DictionaryWord;
import com.personal.wordmaster.data.entity.StudyRecord;
import com.personal.wordmaster.data.entity.WordBook;
import com.personal.wordmaster.data.entity.WordInfo;

@Database(
    entities = {WordBook.class, WordInfo.class, StudyRecord.class, DictionaryWord.class},
    version = 2,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract WordBookDao wordBookDao();
    public abstract WordInfoDao wordInfoDao();
    public abstract StudyRecordDao studyRecordDao();
    public abstract DictionaryWordDao dictionaryWordDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `dictionary_word` (" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`word` TEXT, " +
                "`meaning` TEXT)");
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_dictionary_word_word` " +
                "ON `dictionary_word` (`word`)");
        }
    };

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "wordmaster.db"
                    )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
