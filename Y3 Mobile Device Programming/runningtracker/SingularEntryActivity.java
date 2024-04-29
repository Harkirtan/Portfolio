package com.example.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.runningtracker.databinding.ActivitySingularEntryBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Locale;


/**
 * Activity to show the user all of the data for a run and edit Tag, Weather, Notes, or take a picture
 */
public class SingularEntryActivity extends AppCompatActivity {

    private DatabaseViewModel viewModel;
    private int id;

    private ActivitySingularEntryBinding binding;

    EditText tagView;
    EditText weatherView;
    EditText notesView;

    int dist;
    int time;

    long date = 0L;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_singular_entry);

        binding.shareRun.setOnClickListener(this::onShare);
        binding.imagePicker.setOnClickListener(this::openImagePicker);
        binding.viewRoute.setOnClickListener(this::openRoute);
        binding.saveSingle.setOnClickListener(this::saveValues);
        binding.deleteSingle.setOnClickListener(this::deleteRun);

        Intent intent = getIntent();
        id = intent.getIntExtra("item", -1);

        viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(DatabaseViewModel.class);

        tagView = findViewById(R.id.tag_single);
        weatherView = findViewById(R.id.weather_single);
        notesView = findViewById(R.id.notes_single);

        viewModel.getRunById(id).observe(this, run -> {
            if(run != null){
                binding.setId("ID: " + run.getId());
                binding.setDist(run.getDistance() + " M");
                binding.setTime(run.getTime() + " S");
                String formattedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date(run.getDate()));
                binding.setDate(formattedDate);
                date = run.getDate();
                tagView.setText(run.getTag().toString());
                weatherView.setText(run.getWeather().toString());
                notesView.setText(run.getNotes());

                dist = (int) Math.ceil(run.getDistance());
                time = (int) Math.floor(run.getTime());
            }

        });
    }

    /**
     * Method to validate and save values by inserting to database
     */
    public void saveValues(View v){

        //Assert correct format
        Run.Tag t = null;
        if(tagView.getText().toString().equals("Good"))
            t = Run.Tag.Good;
        else if(tagView.getText().toString().equals("Bad"))
            t = Run.Tag.Bad;
        Run.Weather w = null;
        if(weatherView.getText().toString().equals("Sunny"))
            w = Run.Weather.Sunny;
        else if(weatherView.getText().toString().equals("Rain"))
            w = Run.Weather.Rain;
        else if(weatherView.getText().toString().equals("Snow"))
            w = Run.Weather.Snow;

        //Inform user to correct values
        if(t == null || w == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Invalid Tag or Weather, Please enter a valid value");

            builder.setPositiveButton("OK", (dialog, which)
                    -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else {
            viewModel.updateValues(id, t, w, notesView.getText().toString());
            finish();
        }

    }

    public void deleteRun(View v){
        viewModel.delete(id);
        finish();
    }

    /**
     * OnClick to start image picking activity with id
     */
    public void openImagePicker(View v){
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra("item", id);
        this.startActivity(intent);
    }

    /**
     * OnClick to allow user to share their run distance and time via an intent
     */
    public void onShare(View v){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String shareMessage = "Hey, check out my run! I did "+ dist  +"m in "+ time +"s, can you beat that?";
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    /**
     * OnClick to allow user to view their run if a relevant app is available
     * PS: Tested by using an app called Locus Maps as gpx files are not compatible with Google Maps
     * Any compatible gpx viewer should work ie. GPX Viewer, Locus Mops...
     */
    public void openRoute(View v){
        String pattern = "yyyy-MM-dd HHmm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date dateName = new Date(date);
        String dateString = simpleDateFormat.format(dateName);
        dateString = dateString + ".gpx";


        //Used a file provider to create uri
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File gpxFile = new File(downloadDir, dateString);
        Uri sharedFileUri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".provider",
                gpxFile);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(sharedFileUri, "application/gpx+xml");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "No App Found To Display Route", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}