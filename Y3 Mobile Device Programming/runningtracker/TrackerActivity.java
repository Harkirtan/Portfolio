package com.example.runningtracker;


import androidx.appcompat.app.AppCompatActivity;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.runningtracker.databinding.ActivityTrackerBinding;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Activity to show the distance, time and calories of the current run
 * This is the activity which the service binds to
 */
public class TrackerActivity extends AppCompatActivity{

    private TrackerService.MyBinder myService = null;

    private DatabaseViewModel viewModel;

    private ActivityTrackerBinding binding;

    private double weight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tracker);
        binding.delete.setOnClickListener(this::onStop);
        binding.save.setOnClickListener(this::onSave);
        binding.playMusic.setOnClickListener(this::onPlayMusic);


        //Get weight for calorie calc
        viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(DatabaseViewModel.class);
        viewModel.getEntryByDate().observe(this, entries -> {
            if(!(entries.isEmpty())) {
                weight = entries.get(0).getWeight();
            }
            else
                weight = 80.0;
        });

        //Get intent which will specify if there is a max distance or time
        Intent optionsIntent = getIntent();
        Intent intent = new Intent(this, TrackerService.class);

        int value = optionsIntent.getIntExtra("distanceToRun", 0);
        if(value == 0){
            Log.d("STARTUP", "Empty value");
        }
        else{
            Log.d("STARTUP", "Value  " + value);
            intent.putExtra("maxDistance", value);
        }
        int value2 = optionsIntent.getIntExtra("timeTrial", 0);
        if(value2 != 0){
            Log.d("STARTUP", "Value  " + value2);
            intent.putExtra("maxTime", value2);
        }
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    /**
     * Connect to service and start tracking movement
     */
    private ServiceConnection serviceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("CONN", "Activity onServiceConnected");
            myService = (TrackerService.MyBinder) service;
            myService.registerCallback(callback);
            myService.startTracking();

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("CONN", "Activity onServiceDisconnected");
            myService.unregisterCallback(callback);
            myService = null;
        }
    };

    /**
     * Update UI based on callback, showing distance, time and calories
     */
    ICallback callback = (dist, time) -> {
        binding.setTime("Time: " + time + " s");
        binding.setDistanceTracker("Distance: " + String.format(Locale.ENGLISH, "%.02f", dist) + " metres");
        int met;
        if(dist/time < 3)
            met = 3;
        else if(dist/time > 5)
            met = 8;
        else
            met = 6;

        double burned = (time/60.0 * met * weight / 200);
        binding.setCaloriesTracker("Calories: " +  String.format(Locale.ENGLISH, "%.02f", burned));
    };


    /**
     * OnClick to pause/resume run logging
     */
    public void onStop(View v) {
        myService.pauseResumeTracking();
    }

    /**
     * OnClick to allow user to try and open a music app via an intent
     */
    public void onPlayMusic(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_MUSIC);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "No App Found For Music", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    Handler mainHandler = new Handler(Looper.myLooper());

    /**
     * OnClick to save the current run information by taking data from binder and launching the
     * singularEntryActivity to edit data
     */
    public void onSave(View v) {

        long date = new Date().getTime();
        byte[] image = new byte[1];
        image[0] = 1;
        Run run = new Run(0, myService.getDistance(), myService.getTime(), myService.getSpeed(), date, Run.Tag.Good, Run.Weather.Sunny, "NOTES", image);

        myService.stopTracking();


        viewModel.getAllRuns("distance").observe(this, runs -> {
            if(!runs.isEmpty()){
            if(myService.getDistance() > runs.get(0).getDistance()) {
                Toast toast = Toast.makeText(getApplicationContext(), "This run is the longest one yet! Your previous record was " + runs.get(0).getDistance(), Toast.LENGTH_LONG);
                toast.show();
            }
            }});
        viewModel.insert(run);

        writeGPX(date);

        Intent intent = new Intent(this, SingularEntryActivity.class);
        viewModel.getAllRuns("date").observe(this, runs -> {
            if(!runs.isEmpty()){
                intent.putExtra("item", runs.get(0).getId());
                startActivity(intent);
            }
        });



        finish();

    }

    /**
     * Method to use handler thread to write a GPX file to downloads folder
     * Used a thread to not block UI thread
     */
    private void writeGPX(long date) {
        new Thread(() -> mainHandler.post(() -> {
            String pattern = "yyyy-MM-dd HHmm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
            Date dateName = new Date(date);
            String dateString = simpleDateFormat.format(dateName);
            String gpx;
            gpx = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<gpx version=\"1.1\" creator=\"RunTracker\">" +
                    "<trk>" +
                    "<trkseg>" +
                    myService.getGpxContent() +
                    "</trkseg>" +
                    "</trk>" +
                    "</gpx>";

            dateString = dateString + ".gpx";

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), dateString);
            FileOutputStream stream = null;
            try {
                stream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                assert stream != null;
                stream.write(gpx.getBytes());
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        })).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MAIN", "MainActivity onDestroy");
        if (serviceConnection != null) {
            unbindService(serviceConnection);
            serviceConnection = null;
        }
    }
}