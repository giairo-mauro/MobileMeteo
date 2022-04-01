package ch.supsi.dti.meteoapp.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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

        /*RequestQueue requestQueue;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);
        // Start the queue
        requestQueue.start();
        Log.i("weatherTEST", url);
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        Log.i("weatherTEST", response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.i("weatherTEST", String.valueOf(error));
                    }
                });
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);*/

        /*HttpURLConnection conn = null;
        BufferedReader reader;
        String line;
        StringBuilder responseContent = new StringBuilder();
        try{
            URL url = new URL(weatherAPI +"q=London&appid="+ apiKey);
            conn = (HttpURLConnection) url.openConnection();

            // Request setup
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);// 5000 milliseconds = 5 seconds
            conn.setReadTimeout(5000);

            // Test if the response from the server is successful
            int status = conn.getResponseCode();

            if (status >= 300) {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
            }
            Log.i("weatherTEST", String.valueOf(responseContent));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }*/

        //Ask for location access or create list
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
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
