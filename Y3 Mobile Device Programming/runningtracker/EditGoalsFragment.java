package com.example.runningtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.runningtracker.databinding.FragmentEditGoalsBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Fragment which allows user to view and save details such as goals to internal shared preference
 */
public class EditGoalsFragment extends Fragment {

    private FragmentEditGoalsBinding goalsBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        goalsBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_edit_goals, container, false);

        goalsBinding.saveGoals.setOnClickListener(this::saveProfile);

        return goalsBinding.getRoot();}


    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("user_name", null);
        int userAge = sharedPreferences.getInt("user_age", 0);
        String sex = sharedPreferences.getString("user_sex", null);
        int dist = sharedPreferences.getInt("user_dist_goal", 100);
        int weight = sharedPreferences.getInt("user_weight_goal", 70);

        EditText name = getView().findViewById(R.id.nameUser);
        EditText dob = getView().findViewById(R.id.dobUser);
        EditText sexET = getView().findViewById(R.id.sexUser);
        EditText distGoal = getView().findViewById(R.id.distGoalUser);
        EditText weightGoal = getView().findViewById(R.id.weightGoalUser);


        name.setText(userName);
        dob.setText(String.valueOf(userAge));
        sexET.setText(sex);
        distGoal.setText(String.valueOf(dist));
        weightGoal.setText(String.valueOf(weight));

    }

    /**
     * OnClick to save the user details such as name, dob, gender, distance goal and weight goal
     * to a SharedPreference
     */
    public void saveProfile(View view) {
        View v = getView();
        assert v != null;
        EditText name = v.findViewById(R.id.nameUser);
        EditText dob = v.findViewById(R.id.dobUser);
        EditText sex = v.findViewById(R.id.sexUser);
        EditText distGoal = v.findViewById(R.id.distGoalUser);
        EditText weightGoal = v.findViewById(R.id.weightGoalUser);


        //Calculate age based off of dob
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        int age = 0;
        try {
            String temp = (dob.getText().toString());
            if(temp.equals("0"))
                temp = "00/00/0000";
            Date dateObject = format.parse(temp);
            Calendar today = Calendar.getInstance();
            Calendar dobCal = Calendar.getInstance();
            assert dateObject != null;
            dobCal.setTime(dateObject);
            age = today.get(Calendar.YEAR) - dobCal.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dobCal.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int dist = Integer.parseInt(distGoal.getText().toString());
        int weight = Integer.parseInt(weightGoal.getText().toString());

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString("user_name", name.getText().toString())
                .putInt("user_age", age)
                .putString("user_sex", sex.getText().toString())
                .putInt("user_dist_goal", dist)
                .putInt("user_weight_goal", weight)
                .apply();

        Toast toast = Toast.makeText(getContext(), "Details Saved", Toast.LENGTH_SHORT);
        toast.show();

    }
}