package com.example.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.runningtracker.databinding.ActivityUserProfileBinding;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activity to display some statistics and allow user to add a new weight entry and edit goals
 */
public class UserProfileActivity extends AppCompatActivity {

    private DatabaseViewModel viewModel;
    private ActivityUserProfileBinding binding;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        binding.openProfileEditor.setOnClickListener(this::onOpenGoals);
        binding.openWeightMenu.setOnClickListener(this::onOpenWeight);

        viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(DatabaseViewModel.class);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);

        fragmentTransaction.replace(R.id.fragmentContainerGoals, AddWeightFragment.class, null);
        fragmentTransaction.commit();

        //Show user current weight / height
        viewModel.getEntryByDate().observe(this, entries -> {
            if(!(entries.isEmpty())) {
                binding.setWeightUser("WEIGHT: " + entries.get(0).getWeight() + " kg");
                binding.setHeightUser("HEIGHT: " + entries.get(0).getHeight() + " cm");

                //Show user current weight to goal
                SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                int weightGoal = sharedPreferences.getInt("user_weight_goal", 70);
                int diff = (int) (weightGoal - entries.get(0).getWeight());
                //Show user current weight to initial weight
                int diffFirst = (int) ((entries.get(0).getWeight()) - (entries.get(entries.size() - 1).getWeight()));

                binding.setWeightTrackUser("ONLY " + diff +"  AWAY FROM GOAL OF: " + weightGoal);

                binding.setFirstWeightTrack("YOU ARE " + diffFirst + " FROM INITIAL WEIGHT: " + (entries.get(entries.size() - 1).getWeight()));
            }
            else
                binding.setWeightTrackUser("PLEASE ADD ENTRIES TO SEE DATA");

        });

    }



    /**
     * OnCLick to replace the current fragment with goals editor
     */
    public void onOpenGoals(View v){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerGoals, EditGoalsFragment.class, null);
        fragmentTransaction.commit();
    }

    /**
     * OnCLick to replace the current fragment with weight and height add
     */
    public void onOpenWeight(View v){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerGoals, AddWeightFragment.class, null);
        fragmentTransaction.commit();
    }
}