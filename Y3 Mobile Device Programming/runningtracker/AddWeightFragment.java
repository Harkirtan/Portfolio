package com.example.runningtracker;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.example.runningtracker.databinding.FragmentAddWeightHeightBinding;
import java.util.Date;

/**
 * Fragment which allows user to add a weight and height to database
 */
public class AddWeightFragment extends Fragment {

    private DatabaseViewModel viewModel;
    private FragmentAddWeightHeightBinding weightBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(DatabaseViewModel.class);
        weightBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_weight_height, container, false);

        weightBinding.saveUserDetails.setOnClickListener(this::addEntry);

        return weightBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    /**
     * OnClick to insert the weight and height data to the database
     */
    public void addEntry(View view){
        View v = getView();
        assert v != null;
        EditText w_entry = v.findViewById(R.id.weightEntry);
        EditText h_entry = v.findViewById(R.id.heightEntry);
        double weight = Double.parseDouble(w_entry.getText().toString());
        double height = Double.parseDouble(h_entry.getText().toString());
        long date = new Date().getTime();
        viewModel.insertEntry(new User(0, date, weight, height));

        Toast toast = Toast.makeText(getContext(), "Details Saved", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}