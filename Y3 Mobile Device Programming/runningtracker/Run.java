package com.example.runningtracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Run entity
 */
@Entity(tableName = "run_table")
public class Run {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "Distance")
    private Float distance;

    @ColumnInfo(name = "Time")
    private Long time;

    @ColumnInfo(name = "Speed")
    private Float speed;

    @ColumnInfo(name = "Date")
    private Long date;

    @ColumnInfo(name = "Tag")
    private Tag tag;

    @ColumnInfo(name = "Weather")
    private Weather weather;

    @ColumnInfo(name = "Notes")
    private String notes;

    @ColumnInfo(name = "Image")
    public byte[] image;

    public Run(@NonNull int id, Float distance, Long time, Float speed, Long date,
               Tag tag, Weather weather, String notes, byte[] image) {
        this.id = id;
        this.distance = distance;
        this.time = time;
        this.speed = speed;
        this.date = date;
        this.tag = tag;
        this.weather = weather;
        this.notes = notes;
        this.image = image;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public Float getDistance() {return distance;}

    public Long getTime() {return time;}

    public Float getSpeed() {return speed;}

    public Long getDate() {return date;}

    public Tag getTag() {return tag;}

    public Weather getWeather() {return weather;}

    public String getNotes() {return notes;}

    public byte[] getImage() {
        return image;
    }


    public enum Tag {
        Good,
        Bad
    }
    public static class UserTypeConverter {
        @TypeConverter
        public int fromTagType(Tag type) {
            return type.ordinal();
        }

        @TypeConverter
        public Tag toTagType(int ordinal) {
            return Tag.values()[ordinal];
        }
    }

    public enum Weather {
        Sunny,
        Rain,
        Snow
    }
    public static class WeatherTypeConverter {
        @TypeConverter
        public int fromWeatherType(Weather type) {
            return type.ordinal();
        }

        @TypeConverter
        public Weather toWeatherType(int ordinal) {
            return Weather.values()[ordinal];
        }
    }
}




