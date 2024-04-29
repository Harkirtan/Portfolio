package com.example.runningtracker;

import android.net.Uri;

/**
 * Contract for Content Provider
 */
public class TrackerProviderContract {

    public static final String AUTHORITY = "com.example.runningtracker";

    public static final Uri RUN_URI = Uri.parse("content://"+AUTHORITY+"/run");
    public static final Uri USER_ENTRY_URI = Uri.parse("content://"+AUTHORITY+"/user");
    public static final Uri ALL_URI = Uri.parse("content://"+AUTHORITY+"/");

    public static final String _ID = "_id";

    public static final String DISTANCE = "distance";
    public static final String TIME = "time";
    public static final String SPEED = "speed";
    public static final String DATE = "date";
    public static final String TAG = "tag";
    public static final String WEATHER = "weather";
    public static final String NOTES = "notes";
    public static final String IMAGE = "image";

    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";



    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/TrackerProvider.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/TrackerProvider.data.text";
}
