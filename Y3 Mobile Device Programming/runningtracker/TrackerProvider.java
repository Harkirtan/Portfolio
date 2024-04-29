package com.example.runningtracker;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.util.concurrent.Semaphore;


/**
 * Content Provider to allow other apps to access data in ROOM database
 */
public class TrackerProvider extends ContentProvider {

    RunDatabase runDatabase;
    RunDao runDao;
    UserDao userDao;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TrackerProviderContract.AUTHORITY, "run", 1);
        uriMatcher.addURI(TrackerProviderContract.AUTHORITY, "run/#", 2);
        uriMatcher.addURI(TrackerProviderContract.AUTHORITY, "user", 3);
        uriMatcher.addURI(TrackerProviderContract.AUTHORITY, "user/#", 4);
        uriMatcher.addURI(TrackerProviderContract.AUTHORITY, "*", 5);
    }

    @Override
    public boolean onCreate() {
        runDatabase = RunDatabase.getDatabase(this.getContext());
        runDao = runDatabase.runDao();
        userDao = runDatabase.userDao();
        return true;
    }

    @Override
    public String getType(Uri uri) {

        String contentType;

        if (uri.getLastPathSegment()==null) {
            contentType = TrackerProviderContract.CONTENT_TYPE_MULTIPLE;
        } else {
            contentType = TrackerProviderContract.CONTENT_TYPE_SINGLE;
        }

        return contentType;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Log.d("Provider", uri.toString() + " " + uriMatcher.match(uri));
        final int[] id = new int[1];
        final Cursor[] cursorHolder = new Cursor[1];
        final Semaphore semaphore = new Semaphore(0);

        switch (uriMatcher.match(uri)) {
            case 1:
                RunDatabase.databaseWriteExecutor.execute(() -> {
                    cursorHolder[0] = runDao.getOrderedRunsForProvider();
                    semaphore.release();

                });
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return cursorHolder[0];
            case 2:
                RunDatabase.databaseWriteExecutor.execute(() -> {
                    id[0] = Integer.parseInt(uri.getLastPathSegment());
                    cursorHolder[0] = runDao.getRunByIdForProvider(id[0]);
                    semaphore.release();

                });
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return cursorHolder[0];
            case 3:
                RunDatabase.databaseWriteExecutor.execute(() -> {
                    cursorHolder[0] = userDao.getOrderedUserEntries();
                    semaphore.release();

                });
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return cursorHolder[0];
            case 4:
                RunDatabase.databaseWriteExecutor.execute(() -> {
                    id[0] = Integer.parseInt(uri.getLastPathSegment());
                    cursorHolder[0] = userDao.getUserEntriesByIdForProvider(id[0]);
                    semaphore.release();

                });
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return cursorHolder[0];
            default:
                return null;
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int id = 0;

        switch(uriMatcher.match(uri)) {
            case 1:

                RunDatabase.databaseWriteExecutor.execute(() -> {
                    float dist = (float) values.get("distance");
                    long time = (long) values.get("time");
                    float speed = (float) values.get("speed");
                    long date = (long) values.get("date");
                    String tag = (String) values.get("tag");
                    String weather = (String) values.get("weather");
                    String notes = (String) values.get("notes");
                    byte[] image = (byte[]) values.get("image");

                    Run.Tag t = null;
                    if(tag.equals("Good"))
                        t = Run.Tag.Good;
                    else if(tag.equals("Bad"))
                        t = Run.Tag.Bad;
                    Run.Weather w = null;
                    switch (weather) {
                        case "Sunny":
                            w = Run.Weather.Sunny;
                            break;
                        case "Rain":
                            w = Run.Weather.Rain;
                            break;
                        case "Snow":
                            w = Run.Weather.Snow;
                            break;
                    }
                    if(t == null)
                        t = Run.Tag.Good;
                    if(w == null)
                        w = Run.Weather.Sunny;

                    runDao.insert(new Run(0, dist, time, speed, date, t, w, notes, image));
                });
                break;
            case 3:

                RunDatabase.databaseWriteExecutor.execute(() -> {
                    long u_date = (long) values.get("date");
                    double weight = (double) values.get("weight");
                    double height = (double) values.get("height");

                    userDao.insert(new User(0, u_date, weight, height));
                });
                break;
            default:
                break;
        }

        Uri nu = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(nu, null);

        return nu;
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int[] id = new int[1];
        int ids = 0;

        Log.d("Provider", uri.toString() + " " + uriMatcher.match(uri));

        if (uriMatcher.match(uri) == 2) {
            RunDatabase.databaseWriteExecutor.execute(() -> {
                id[0] = Integer.parseInt(uri.getLastPathSegment());
                String tag = (String) values.get("tag");
                String weather = (String) values.get("weather");
                String notes = (String) values.get("notes");
                byte[] image = (byte[]) values.get("image");

                Run.Tag t = null;
                if(tag.equals("Good"))
                    t = Run.Tag.Good;
                else if(tag.equals("Bad"))
                    t = Run.Tag.Bad;
                Run.Weather w = null;
                switch (weather) {
                    case "Sunny":
                        w = Run.Weather.Sunny;
                        break;
                    case "Rain":
                        w = Run.Weather.Rain;
                        break;
                    case "Snow":
                        w = Run.Weather.Snow;
                        break;
                }

                if(t == null)
                    t = Run.Tag.Good;
                if(w == null)
                    w = Run.Weather.Sunny;

                runDao.updateValues(id[0], t, w, notes);
                runDao.updateImage(id[0], image);

            });
        }
        else if(uriMatcher.match(uri) == 4){
            RunDatabase.databaseWriteExecutor.execute(() -> {
                id[0] = Integer.parseInt(uri.getLastPathSegment());
                double weight = (double) values.get("weight");
                double height = (double) values.get("height");

                userDao.updateValues(id[0], weight, height);
            });
        }
        else
            throw new UnsupportedOperationException("not implemented");

        Uri nu = ContentUris.withAppendedId(uri, ids);
        getContext().getContentResolver().notifyChange(nu, null);
        return ids;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        Log.d("Provider", uri.toString() + " " + uriMatcher.match(uri));

        final int[] id = new int[1];
        int ids = 0;
        switch(uriMatcher.match(uri)) {
            case 2:
                id[0] = Integer.parseInt(uri.getLastPathSegment());
                RunDatabase.databaseWriteExecutor.execute(() -> runDao.deleteById(id[0]));
                break;
            case 4:
                id[0] = Integer.parseInt(uri.getLastPathSegment());
                RunDatabase.databaseWriteExecutor.execute(() -> userDao.deleteById(id[0]));
                break;
            default:
                break;
        }
        return ids;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) {
        throw new UnsupportedOperationException("not implemented");
    }
}
