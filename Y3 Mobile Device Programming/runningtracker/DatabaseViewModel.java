package com.example.runningtracker;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

/**
 * ViewModel to access the Run Repository
 */
public class DatabaseViewModel extends AndroidViewModel {

    private final RunRepository repository;

    public DatabaseViewModel(Application application) {
        super(application);
        repository = new RunRepository(application);

    }

    LiveData<List<Run>> getAllRuns(String sort) { return repository.getAllRuns(sort); }

    LiveData<List<Run>> getTodayRuns(long date) {return repository.getTodayRuns(date);}

    LiveData<List<Run>> getBestRuns() {return repository.getBestRuns();}

    LiveData<List<Run>> getRunsByType(Run.Tag t) {return repository.getRunByType(t);}

    LiveData<Run> getRunById(int id) {return repository.getRunById(id);}

    LiveData<List<Run>> getActivityRuns(String sort) {return repository.getActivityRuns(sort);}
    LiveData<List<Run>> getActivityJogs(String sort) {return repository.getActivityJogs(sort);}
    LiveData<List<Run>> getActivityWalks(String sort) {return repository.getActivityWalks(sort);}
    LiveData<List<Run>> getDistChallenge(int distance, String sort) {return repository.getDistChallenge(distance, sort);}
    LiveData<List<Run>> getTimedTrial(int time, String sort) {return repository.getTimedTrial(time, sort);}

    LiveData<List<User>> getAllEntries() {
        return repository.getAllEntries();
    }

    LiveData<List<User>> getEntryByDate() {
        return repository.getEntryByDate();
    }

    LiveData<User> getEntryById(int id) {return repository.getEntryById(id);}



    void insert(Run run) { repository.insertRun(run); }
    void delete(int id ) { repository.deleteRun(id); }

    void insertEntry(User user) {
        repository.insertUserEntry(user);
    }

    void updateValues(int id, Run.Tag tag, Run.Weather weather, String notes) {
        repository.updateValues(id, tag, weather, notes);
    }

    void updateImage(int id, byte[] image) {
        repository.updateImage(id, image);
    }
}

