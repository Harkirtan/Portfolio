package com.example.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.runningtracker.databinding.ActivityMainBinding;
import com.example.runningtracker.databinding.ActivityTrackerBinding;

import java.util.Date;

/**
 * Main Activity which allows user to access other activities
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.startFreeRun.setOnClickListener(this::onStartTracking);
        mainBinding.startDist.setOnClickListener(this::startDistanceTracking);
        mainBinding.startTimeT.setOnClickListener(this::startTimedTracking);
        mainBinding.openCalendar.setOnClickListener(this::viewCalendar);
        mainBinding.viewData.setOnClickListener(this::viewData);
        mainBinding.userData.setOnClickListener(this::viewProfile);

        //Day/night mode switch
        SwitchCompat dayNightSwitch = findViewById(R.id.nightSwitch);
        SharedPreferences sharedPreferencesNight = getSharedPreferences("nightPref", Context.MODE_PRIVATE);
        boolean mode = sharedPreferencesNight.getBoolean("mode", false);

        if(mode){
            dayNightSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        //Selection saved to a shared preference
        dayNightSwitch.setOnClickListener(v -> {
            if(mode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sharedPreferencesNight.edit()
                        .putBoolean("mode", false)
                        .apply();}
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sharedPreferencesNight.edit()
                        .putBoolean("mode", true)
                        .apply();}
        });


        //Check if permissions are granted
        checkPermissions();


        //Check how many days the user has logged in consecutively
        SharedPreferences sharedPreferences = getSharedPreferences("consecPref", Context.MODE_PRIVATE);

        long today = new Date().getTime();
        int daysToday = (int) (today/86400000);

        int yday = sharedPreferences.getInt("consecDay", 0);

        int consecCount = sharedPreferences.getInt("consecCount", 0);

        //Calculate if last login was yesterday
        if(yday == (daysToday-1)) {
            consecCount = consecCount + 1;
            sharedPreferences.edit()
                    .putInt("consecDay", daysToday)
                    .putInt("consecCount", consecCount)
                    .apply();
        }
        //If user returns to app on the same day
        else if(yday == daysToday){
            //do nothing
        }
        //Start the count from today otherwise
        else{
            sharedPreferences.edit()
                    .putInt("consecDay", daysToday)
                    .putInt("consecCount", 0)
                    .apply();}

        //If it has been more than 0 days then congratulate user
        if(yday == (daysToday-1)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Login");
            builder.setMessage("Great work! This is "+ consecCount +" days you have logged into this app in a row!");
            builder.setPositiveButton("Thank you", (dialog, which) -> {});
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


    }

    /**
     * OnClick to launch tracker activity
     */
    public void onStartTracking(View v){

        startActivity(new Intent(MainActivity.this, TrackerActivity.class));

    }

    /**
     * OnClick to launch tracker activity with an intent which contains the max distance
     */
    public void startDistanceTracking(View v){

        Intent intent = new Intent(MainActivity.this, TrackerActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Run Tracker - DISTANCE");
        final String[] options = {"100 M", "1000 M", "5000 M"};

        builder.setSingleChoiceItems(options, -1, (dialog, which) -> {
                String[] chosen = options[which].split(" ");
                intent.putExtra("distanceToRun", Integer.parseInt(chosen[0]));
                });

        builder.setPositiveButton("RUN", (dialog, which)
                -> startActivity(intent));
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * OnClick to launch tracker activity with an intent which contains the max time
     */
    public void startTimedTracking(View v){
        Intent intent = new Intent(MainActivity.this, TrackerActivity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Run Tracker - TIME");
        final String[] options = {"1 Min", "10 Min", "20 Min"};
        builder.setSingleChoiceItems(options, -1, (dialog, which) ->{
            String[] chosen = options[which].split(" ");
            intent.putExtra("timeTrial", (Integer.parseInt(chosen[0])*60));
        });
        builder.setPositiveButton("RUN", (dialog, which)
                -> startActivity(intent));
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /**
     * OnClick to launch data viewing activity
     */
    public void viewData(View v){
        startActivity(new Intent(MainActivity.this, DataActivity.class));
    }


    /**
     * OnClick to launch profile viewing activity
     */
    public void viewProfile(View v) {
        this.startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
    }

    /**
     * OnClick to launch calendar activity
     */
    public void viewCalendar(View v) {
        this.startActivity(new Intent(MainActivity.this, CalendarActivity.class));
    }




    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 1;

    /**
     * Method to check permissions for storage and location
     */
    public void checkPermissions() {
        String[] permissions = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(
                    this, permission) ==
                    PackageManager.PERMISSION_GRANTED) {
                Log.d("PERM", "granted");
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {

                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Run tracker needs access to storage and location to work");
                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, "OK", (dialog, which) ->
                        ActivityCompat.requestPermissions(this,
                                permissions,
                                MY_PERMISSIONS_REQUEST_STORAGE
                        ));
                alertDialog.show();

            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        MY_PERMISSIONS_REQUEST_STORAGE
                );
            }
        }
    }


}