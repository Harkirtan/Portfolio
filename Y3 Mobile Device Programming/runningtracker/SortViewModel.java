package com.example.runningtracker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel to hold current selection for the sort on data activity
 */
public class SortViewModel extends ViewModel {
    private MutableLiveData<String> sort;

    public MutableLiveData<String> getSort() {
        if (sort == null) {
            sort = new MutableLiveData<>();
            sort.setValue("date");
        }
        return sort;
    }

    public void setSort(String sort) {
        getSort().postValue(sort);
    }
}
