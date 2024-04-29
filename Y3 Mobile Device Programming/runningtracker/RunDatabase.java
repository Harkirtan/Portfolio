package com.example.runningtracker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton database which holds the user and runs as separate tables
 */
@Database(entities = {Run.class, User.class}, version = 1, exportSchema = false)
@TypeConverters({Run.UserTypeConverter.class, Run.WeatherTypeConverter.class})
public abstract class RunDatabase extends RoomDatabase {

    //Generate a thread pool
    private static final int threadCount = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(threadCount);

    public abstract RunDao runDao();
    public abstract UserDao userDao();

    private static volatile RunDatabase instance;
    static RunDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (RunDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    RunDatabase.class, "run_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(createCallback)
                            .build();
                }
            }
        }
        return instance;
    }


    /**
     * First time creation of the database
     */
    private static RoomDatabase.Callback createCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("DATABASE", "onCreate");
        }
    };
}