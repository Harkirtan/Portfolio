package com.example.runningtracker;


/**
 * Callback for the service which sends distance and time of run
 */
public interface ICallback {

    /**
     * Method to implement
     */
    void distanceAndTime(float dist, long time);
}
