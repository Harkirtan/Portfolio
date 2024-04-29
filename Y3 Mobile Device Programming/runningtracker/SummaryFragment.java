package com.example.runningtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.example.runningtracker.databinding.FragmentSummaryBinding;


/**
 * Fragment to show summary of statistics
 */
public class SummaryFragment extends Fragment {

    private FragmentSummaryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_summary, container, false);

        return binding.getRoot();}

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();
        View view = getView();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);

        DatabaseViewModel viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(DatabaseViewModel.class);
        viewModel.getTodayRuns(0).observe(this, runs -> {
            //Show the distance run today/goal distance and update progress accordingly
            if(view != null && !runs.isEmpty()){
                float distancetrav = 0F;
                for (Run temp : runs){
                    distancetrav = distancetrav + temp.getDistance();
                }
                int distanceTotal = Math.round(distancetrav);
                int distGoal = sharedPreferences.getInt("user_dist_goal", 100);
                binding.setTodayDistance("DISTANCE GOAL " + distanceTotal+ "M/"+ distGoal+"M");
                ProgressBar progressBar = view.findViewById(R.id.progressBarToday);
                progressBar.setMax(distGoal);
                progressBar.setProgress(Math.round(distancetrav));
                progressBar.setIndeterminate(false);
            }
        });
        //Show the best run
        viewModel.getBestRuns().observe(this, runs -> {
            if(view != null && !runs.isEmpty()){
                binding.setBestDistanceSummary("BEST DISTANCE " + runs.get(0).getDistance() + "M");
            }
            else
                binding.setBestDistanceSummary("PLEASE ADD ENTRIES TO SEE DATA");

        });
        //Show the best time for 100M
        viewModel.getDistChallenge(100, "time").observe(this, runs -> {
            if(!runs.isEmpty())
                binding.setBest100mSummary("FASTEST 100M SPRINT " + runs.get(0).getTime() + "S");
        });

        //Show the best distance for 60S
        viewModel.getTimedTrial(60, "distance").observe(this, runs -> {
            if(!runs.isEmpty())
                binding.setBest60s("FURTHEST 60S RUN " + runs.get(0).getTime() + "M");
        });


    }
}