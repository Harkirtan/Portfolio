package com.example.runningtracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteCallbackList;
import android.util.Log;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Service which logs user movement
 */
public class TrackerService extends Service {
    private final IBinder binder = new MyBinder();
    private final String CHANNEL_ID = "100";
    int NOTIFICATION_ID = 1;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    private float distance = 0F;
    private long time = 0L;
    private float speed;

    private float totalSpeed = 0F;

    int maxDistance;
    int maxTime;

    PowerManager.WakeLock wakeLock;

    RemoteCallbackList<MyBinder> remoteCallbackList = new RemoteCallbackList<>();

    /**
     * Perform callback for UI updating distance and time
     */
    public void doCallbacks(float dist, long time) {
        final int n = remoteCallbackList.beginBroadcast();
        for (int i = 0; i < n; i++) {
            remoteCallbackList.getBroadcastItem(i).callback.distanceAndTime(dist, time);

        }
        remoteCallbackList.finishBroadcast();
    }

    public TrackerService() {}

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SERVICE", "service OnBind");
        return binder;
    }

    /**
     * Add a partial wake lock to ensure service stays active and create a new handler
     */
    @Override
    public void onCreate() {
        Log.d("SERVICE", "service onCreate");
        createNotificationChannel();

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tracker:WakeLock");

        handler = new Handler();

        super.onCreate();
    }

    /**
     * Check intents to identify if there is a max distance, max time or intent to stop tracking
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SERVICE", "service onStart");
        maxDistance = intent.getIntExtra("maxDistance", 0);
        Log.d("distanceMax", "" + maxDistance);

        maxTime = intent.getIntExtra("maxTime", 0);
        Log.d("timeMax", "" + maxTime);

        Log.d("end", "" + intent.getIntExtra("end", 0));
        if((intent.getIntExtra("end", 0)) == 1) {
            stopForeground(true);
            wakeLock.release();
            active = false;
            fusedLocationClient.removeLocationUpdates(locationCallback);
            fusedLocationClient = null;
            locationCallback = null;
            distance = 0;
            time = 0;
        }
        return START_NOT_STICKY;

    }

    /**
     * Release resources
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        wakeLock.release();
        active = false;
        fusedLocationClient.removeLocationUpdates(locationCallback);
        locationCallback = null;
        fusedLocationClient = null;
        distance = 0;
        time = 0;
        Log.d("SERVICE", "service onDestroy");

    }

    boolean active = false;
    boolean paused = false;
    Location oldLoc;
    String gpxContent = null;
    private Handler handler;

    public class MyBinder extends Binder implements IInterface {

        /**
         * Method to get location, update notification with distance, do callbacks
         * Write current gpx coordinates on a separate thread
         */
        void startTracking(){
            if(!active) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    showNotification();
                }
                wakeLock.acquire();
                if(!paused){
                    distance = 0;
                    time = 0;}

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(TrackerService.this);
                LocationRequest locationRequest = new
                        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build();
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        for (Location location : locationResult.getLocations()) {

                            speed = location.getSpeed();

                            if(oldLoc != null) {
                                float c_distance = location.distanceTo(oldLoc);
                                distance = distance + c_distance;

                                time = time + 1;

                                totalSpeed = totalSpeed + speed;
                            }

                            if (maxDistance != 0 && distance >= maxDistance) {
                                // Stop location updates when distance is reached
                                fusedLocationClient.removeLocationUpdates(this);
                                doCallbacks((float) maxDistance, time);
                                distance = maxDistance;
                                sendAlert();
                                break;
                            }
                            if (maxTime != 0 && time == maxTime) {
                                // Stop location updates when time is reached
                                fusedLocationClient.removeLocationUpdates(this);
                                doCallbacks(distance, (long) maxTime);
                                time = maxTime;
                                sendAlert();
                                break;
                            }

                            builder.setContentText("RunningTracker: " + String.format(Locale.ENGLISH,"%.02f", distance) + "M");
                            notificationManager.notify(NOTIFICATION_ID, builder.build());

                            oldLoc = location;

                            doCallbacks(distance, time);

                            //Asynchronous task put in a separate thread
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    String pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
                                    Calendar calendar = Calendar.getInstance();
                                    String timeString = simpleDateFormat.format(calendar.getTime());
                                    gpxContent = gpxContent + "<trkpt lat=\"" + location.getLatitude()+ "\" lon=\"" + location.getLongitude() + "\">" +
                                            "<ele>" + location.getAltitude() + "</ele>" +
                                            "<time>" + timeString + "</time>" +
                                            "</trkpt>";
                                }
                            });
                        }
                    }
                };
                try {
                    fusedLocationClient.requestLocationUpdates(locationRequest,
                            locationCallback,
                            Looper.getMainLooper());
                } catch (SecurityException ignored) {
                }

                active = true;
            }

        }

        /**
         * Method to stop tracking location and stopForeground
         */
        void stopTracking(){
            stopForeground(true);
            active = false;
            fusedLocationClient.removeLocationUpdates(locationCallback);
            fusedLocationClient = null;
            wakeLock.release();
        }

        /**
         * Method to pause/resume the location updates
         */
        void pauseResumeTracking() {
            if(!paused) {
                active = false;
                fusedLocationClient.removeLocationUpdates(locationCallback);
                oldLoc = null;
                paused = true;
                wakeLock.release();
            }
            else if(paused){
                startTracking();
                paused = false;
            }
        }

        float getDistance(){return distance;}
        long getTime(){return time;}
        //Using time as the count for how many speed measurements are taken, not as time itself
        float getSpeed(){return totalSpeed/time;}
        String getGpxContent(){return gpxContent;}


        /**
         * Method to send intent to Broadcast Receiver to sound alert if user has reached the max distance
         * or max time
         */
        void sendAlert(){
            Intent intent = new Intent(TrackerService.this, AlertReceiver.class);
            intent.setAction("MY_CUSTOM_BROADCAST");
            sendBroadcast(intent);
        }


        public void registerCallback(ICallback callback) {
            this.callback = callback;
            remoteCallbackList.register(MyBinder.this);
        }
        public void unregisterCallback(ICallback callback) {
            this.callback = callback;
            remoteCallbackList.unregister(MyBinder.this);
        }
        ICallback callback;


        @Override
        public IBinder asBinder() {
            return this;
        }
    }



    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    /**
     * Create a notification channel to send notifications on
     */
    private void createNotificationChannel() {
        notificationManager= (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CHANNEL";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name,
                    importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Show notification with an intent to stop the location updates
     * Foregrounds this service
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showNotification(){

        Intent intent = new Intent(this, TrackerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Intent stopIntent = new Intent(this, TrackerService.class);
        stopIntent.putExtra("end", 1);
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
                .setContentTitle("TRACKER")
                .setContentText("RunningTracker is TRACKING LOCATION")
                .addAction(R.drawable.ic_baseline_stop_24, "Stop", stopPendingIntent)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        startForeground(NOTIFICATION_ID, builder.build());
    }
}