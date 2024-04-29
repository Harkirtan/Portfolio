package com.example.runningtracker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

/**
 * ViewModel to hold current date selected for calendarView
 */
public class CalendarViewModel extends ViewModel {
    private MutableLiveData<Long> date;

    public MutableLiveData<Long> getDate() {
        if (date == null) {
            date = new MutableLiveData<>();
            setDate(new Date().getTime());
        }
        return date;
    }

    public void setDate(Long date) {
        getDate().postValue(date);
    }
}

