package ch.supsi.dti.meteoapp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

import ch.supsi.dti.meteoapp.R;
import ch.supsi.dti.meteoapp.fragments.ListFragment;
import ch.supsi.dti.meteoapp.model.AppDatabase;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;

public class MainActivity extends AppCompatActivity implements OnDialogResultListener {
    private Fragment fragment;
    private FragmentManager fm;

    private static AppDatabase db;
    private static Location currentLoc;
    private static Geocoder geoLocation;

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
        db = AppDatabase.getInstance(this);

        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        //fragment = new Fragment[]{fm.findFragmentById(R.id.fragment_container)};
        fragment = fm.findFragmentById(R.id.fragment_container);


        //Ask for location access or create list
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            Log.i("locationsTest", "ASKING");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }else{
            locationGranted();
        }
    }

    private void locationGranted(){
        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(LocationAccuracy.HIGH)
                .setDistance(0)
                .setInterval(5000); // 5 sec

        SmartLocation.with(this)
                .location()
                .continuous()
                .config(builder.build())
                .start(location -> {
                    //Seti location
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
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                Log.i("locationsTest", "GETTING ANSWER");
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
}

