package com.example.runningtracker;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


/**
 * Database Access Object for User weight/height and relevant queries
 */
@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("DELETE FROM user_table WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    LiveData<List<User>> getOrderedUsers();

    @Query("SELECT * FROM user_table ORDER BY date DESC")
    LiveData<List<User>> getLatestEntry();

    @Query("SELECT * FROM user_table WHERE id = :id")
    LiveData<User> getUserById(int id);

    @Query("UPDATE user_table SET weight = :weight, height = :height WHERE id = :id")
    void updateValues(int id, double weight, double height);

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    Cursor getOrderedUserEntries();

    @Query("SELECT * FROM user_table WHERE id = :id")
    Cursor getUserEntriesByIdForProvider(int id);



}
