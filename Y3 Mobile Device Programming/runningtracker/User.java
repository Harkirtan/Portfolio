package com.example.runningtracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * User Entry entity
 */
@Entity(tableName = "user_table")
public class User {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "Date")
    private long date;

    @ColumnInfo(name = "Weight")
    private double weight;

    @ColumnInfo(name = "Height")
    private double height;

    public User(@NonNull int id, long date, double weight, double height) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }
}
