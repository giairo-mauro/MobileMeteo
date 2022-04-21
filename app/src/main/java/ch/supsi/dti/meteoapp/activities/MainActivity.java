package ch.supsi.dti.meteoapp.activities;

import android.Manifest;
import android.content.Context;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ch.supsi.dti.meteoapp.R;
import ch.supsi.dti.meteoapp.fragments.ListFragment;
import ch.supsi.dti.meteoapp.model.AppDatabase;
import ch.supsi.dti.meteoapp.model.LocationsHolder;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import ch.supsi.dti.meteoapp.model.Weather;

public class MainActivity extends AppCompatActivity implements OnDialogResultListener {
    private Fragment fragment;
    private FragmentManager fm;

    private static AppDatabase db;
    private static Location currentLoc;
    private static Geocoder geoLocation;

    private static Context context;

    public static AppDatabase getDb(){
        return db;
    }
    public static Location getCurrentLoc() {
        return currentLoc;
    }
    public static Geocoder getGeoLocation() {
        return geoLocation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        db = AppDatabase.getInstance(this);

        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            //Ask for location access or create list
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }else{
                locationGranted();
            }
        }
        else
            Toast.makeText(MainActivity.this, "Internet not available", Toast.LENGTH_SHORT).show();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "WEATHER_CHANNEL", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Weather informations");
            mNotificationManager.createNotificationChannel(channel);
        }

        PeriodicWorkRequest periodicRequest = new PeriodicWorkRequest.Builder(Weather.class, 15, TimeUnit.MINUTES).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("POLL WORK", ExistingPeriodicWorkPolicy.KEEP, periodicRequest);
    }

    private void locationGranted(){
        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(LocationAccuracy.LOW)
                .setDistance(0)
                .setInterval(5000);

        SmartLocation.with(this)
                .location()
                .continuous()
                .config(builder.build())
                .start(location -> {
                    //Set location
                    currentLoc = location;
                    geoLocation = new Geocoder(this, Locale.getDefault());
                    //Set list fragment
                    if(fragment == null){
                        fragment = new ListFragment();

                        fm.beginTransaction()
                                .add(R.id.list_fragment_container, fragment)
                                .commit();
                    }
                });
    }

    private void locationDenied(){
        //Set list fragment
        if(fragment == null){
            fragment = new ListFragment();

            fm.beginTransaction()
                    .add(R.id.list_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onDialogResult(String result) {
        Thread t = new Thread(() -> {
            ch.supsi.dti.meteoapp.model.Location location = new ch.supsi.dti.meteoapp.model.Location(result);
            //Create connection to check if API has the city
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + location.getCity() + "&appid=1ae729fe4329fe7bb3784f5931d6643b");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();
                db.personDao().insertLocation(location);
                LocationsHolder.get(this).getLocations().add(location);
            //Toast if city does not exists
            } catch (IOException e) {
                e.printStackTrace();MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "City nonexistent", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {}
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationGranted();
                } else {
                    locationDenied();
                }
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    public static Context getContext(){
        return context;
    }
}

