package com.example.runningtracker;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

/**
 * Repository to access UserDao and RunDao from database
 */
public class RunRepository {

    private RunDao runDao;
    private UserDao userDao;

    RunRepository(Application application) {
        RunDatabase db = RunDatabase.getDatabase(application);
        runDao = db.runDao();
        userDao = db.userDao();
    }

    LiveData<List<Run>> getAllRuns(String sort) {
        return runDao.getOrderedRuns(sort);
    }

    LiveData<List<Run>> getTodayRuns(long date) {
        if(date == 0)
            return runDao.getTodayRuns(new Date().getTime());
        else
            return runDao.getTodayRuns(date);
    }

    LiveData<List<Run>> getBestRuns() {return runDao.getBestRun();}

    LiveData<List<Run>> getRunByType(Run.Tag t) {return runDao.getRunsByTag(t);}

    LiveData<Run> getRunById(int id) {return runDao.getRunById(id);}

    LiveData<List<Run>> getActivityRuns(String sort) {return runDao.getActivityRuns(sort);}
    LiveData<List<Run>> getActivityJogs(String sort) {return runDao.getActivityJogs(sort);}
    LiveData<List<Run>> getActivityWalks(String sort) {return runDao.getActivityWalks(sort);}
    LiveData<List<Run>> getDistChallenge(int distance, String sort) {return runDao.getDistChallenge(distance, sort);}
    LiveData<List<Run>> getTimedTrial(int time, String sort) {return runDao.getTimedTrial(time, sort);}


    LiveData<List<User>> getAllEntries() {
        return userDao.getOrderedUsers();
    }

    LiveData<List<User>> getEntryByDate() {
        return userDao.getLatestEntry();
    }

    LiveData<User> getEntryById(int id) {return userDao.getUserById(id);}


    void updateValues(int id, Run.Tag tag, Run.Weather weather, String notes) {
        RunDatabase.databaseWriteExecutor.execute(() -> {
        runDao.updateValues(id, tag, weather, notes);
    });}

    void updateImage(int id, byte[] image) {
        RunDatabase.databaseWriteExecutor.execute(() -> {
            runDao.updateImage(id, image);
        });}

    void insertRun(Run run) {
        RunDatabase.databaseWriteExecutor.execute(() -> {
            runDao.insert(run);

        });
    }

    void deleteRun(int id) {
        RunDatabase.databaseWriteExecutor.execute(() -> {
            runDao.deleteById(id);

        });
    }

    void insertUserEntry(User user) {
        RunDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insert(user);
        });
    }

}
