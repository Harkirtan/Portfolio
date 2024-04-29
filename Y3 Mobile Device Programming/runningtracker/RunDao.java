package com.example.runningtracker;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Database Access Object for Runs and relevant queries
 */
@Dao
public interface RunDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Run run);

    @Query("DELETE FROM run_table")
    void deleteAll();

    @Query("DELETE FROM run_table WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM run_table ORDER BY "+
            "CASE WHEN :order = 'date' THEN date END DESC, "+
            "CASE WHEN :order = 'distance' THEN distance END DESC, "+
            "CASE WHEN :order = 'time' THEN time END ASC")
    LiveData<List<Run>> getOrderedRuns(String order);

    @Query("SELECT * FROM run_table WHERE date / (86400000) = :today / 86400000")
    LiveData<List<Run>> getTodayRuns(long today);

    @Query("SELECT * FROM run_table ORDER BY distance DESC")
    LiveData<List<Run>> getBestRun();

    @Query("SELECT * FROM run_table WHERE tag = :type")
    LiveData<List<Run>> getRunsByTag(Run.Tag type);

    @Query("SELECT * FROM run_table WHERE id = :id")
    LiveData<Run> getRunById(int id);

    @Query("SELECT * FROM run_table WHERE speed > 5 ORDER BY "+
            "CASE WHEN :order = 'date' THEN date END DESC, "+
            "CASE WHEN :order = 'distance' THEN distance END DESC, "+
            "CASE WHEN :order = 'time' THEN time END ASC")
    LiveData<List<Run>> getActivityRuns(String order);

    @Query("SELECT * FROM run_table WHERE speed < 3 ORDER BY "+
            "CASE WHEN :order = 'date' THEN date END DESC, "+
            "CASE WHEN :order = 'distance' THEN distance END DESC, "+
            "CASE WHEN :order = 'time' THEN time END ASC")
    LiveData<List<Run>> getActivityWalks(String order);

    @Query("SELECT * FROM run_table WHERE speed < 5 AND speed > 3 ORDER BY "+
            "CASE WHEN :order = 'date' THEN date END DESC, "+
            "CASE WHEN :order = 'distance' THEN distance END DESC, "+
            "CASE WHEN :order = 'time' THEN time END ASC")
    LiveData<List<Run>> getActivityJogs(String order);

    @Query("SELECT * FROM run_table WHERE distance = :distance ORDER BY "+
            "CASE WHEN :order = 'date' THEN date END DESC, "+
            "CASE WHEN :order = 'distance' THEN distance END DESC, "+
            "CASE WHEN :order = 'time' THEN time END ASC")
    LiveData<List<Run>> getDistChallenge(int distance, String order);

    @Query("SELECT * FROM run_table WHERE time = :time ORDER BY "+
            "CASE WHEN :order = 'date' THEN date END DESC, "+
            "CASE WHEN :order = 'distance' THEN distance END DESC, "+
            "CASE WHEN :order = 'time' THEN time END ASC")
    LiveData<List<Run>> getTimedTrial(int time, String order);

    @Query("UPDATE run_table SET tag = :tag, weather = :weather, notes =:notes WHERE id = :id")
    void updateValues(int id, Run.Tag tag, Run.Weather weather, String notes);

    @Query("UPDATE run_table SET image = :image WHERE id = :id")
    void updateImage(int id, byte[] image);

    @Query("SELECT * FROM run_table ORDER BY id ASC")
    Cursor getOrderedRunsForProvider();

    @Query("SELECT * FROM run_table WHERE id = :id")
    Cursor getRunByIdForProvider(int id);

}
