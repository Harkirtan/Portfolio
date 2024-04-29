package com.example.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;


/**
 * Activity which shows user a SummaryFragment and allows for sorting, viewing of data using a recycler view
 */
public class DataActivity extends AppCompatActivity {

    private DatabaseViewModel viewModel;
    private SortViewModel sortModel;
    private RecyclerView recyclerView;
    private RunAdapter adapter;
    private String sortSelection;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        recyclerView = findViewById(R.id.recyclerView);
        setAdapter();

        sortModel = new ViewModelProvider(this).get(SortViewModel.class);
        viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(DatabaseViewModel.class);


        sortSelection = "time";
        final Observer<String> sortObserver = s -> sortSelection = s;
        sortModel.getSort().observe(this, sortObserver);

        SummaryFragment fragment = new SummaryFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();

        RadioGroup radioGroupType = findViewById(R.id.radioGroupType);
        Spinner spinnerFree = findViewById(R.id.spinnerfree);
        Spinner spinnerTime = findViewById(R.id.spinnertime);
        Spinner spinnerDist = findViewById(R.id.spinnerdist);

        //Radio Group for sorting options
        radioGroupType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.sortDate:
                    sortModel.setSort("date");
                    break;
                case R.id.sortDist:
                    sortModel.setSort("distance");
                    break;
                case R.id.sortTime:
                    sortModel.setSort("time");
                    break;
            }
            spinnerDist.setSelection(0);
            spinnerTime.setSelection(0);
            spinnerFree.setSelection(0);
        });

        radioGroupType.check(R.id.sortDate);


        //Spinner for free run options
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.freeValues, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFree.setAdapter(adapter1);
        spinnerFree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                switch (text){
                    case "FreeRun":
                        break;
                    case "All":
                        viewModel.getAllRuns(sortSelection).observe(DataActivity.this, runs -> adapter.setData(runs));
                        break;
                    case "Runs":
                        viewModel.getActivityRuns(sortSelection).observe(DataActivity.this, runs -> adapter.setData(runs));
                        break;
                    case "Jogs":
                        viewModel.getActivityJogs(sortSelection).observe(DataActivity.this, runs -> adapter.setData(runs));
                        break;
                    case "Walks":
                        viewModel.getActivityWalks(sortSelection).observe(DataActivity.this, runs -> adapter.setData(runs));
                        break;

                }
                spinnerDist.setSelection(0);
                spinnerTime.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Spinner for distance challenge run options
        ArrayAdapter<CharSequence> adapterDist = ArrayAdapter.createFromResource(this,
                R.array.distanceValues, android.R.layout.simple_spinner_item);
        adapterDist.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDist.setAdapter(adapterDist);
        spinnerDist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                try {
                    int dist = Integer.parseInt(text);
                    viewModel.getDistChallenge(dist, sortSelection).observe(DataActivity.this, runs -> adapter.setData(runs));
                    spinnerTime.setSelection(0);
                    spinnerFree.setSelection(0);
                } catch (NumberFormatException ignored) {
                }
            }

                @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerDist.setSelection(-1);
        spinnerDist.setPrompt(null);

        //Spinner for Timed Trial run options
        spinnerTime.setSelection(-1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.timeValues, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapter2);
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                try{int time = Integer.parseInt(text);
                viewModel.getTimedTrial(time, sortSelection).observe(DataActivity.this, runs -> adapter.setData(runs));
                spinnerDist.setSelection(0);
                spinnerFree.setSelection(0);}
                catch (NumberFormatException ignored) {}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerTime.setSelection(-1);
        spinnerTime.setPrompt(null);


        spinnerFree.setSelection(1);


    }

    /**
     * Set adapter for recycler view
     */
    private void setAdapter(){
        adapter = new RunAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}