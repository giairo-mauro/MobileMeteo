package ch.supsi.dti.meteoapp.model;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import java.util.concurrent.atomic.AtomicReference;

import ch.supsi.dti.meteoapp.activities.MainActivity;

//Class that
public class Weather {

    private String weatherAPI = "https://api.openweathermap.org/data/2.5/weather?";
    private String apiKey = "1ae729fe4329fe7bb3784f5931d6643b";

    public String getDescByCity(String city){
        //Request weather JSON
        String url = weatherAPI +"q="+ city +"&appid="+ apiKey;
        AtomicReference<String> desc = new AtomicReference<>("");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    Log.i("weatherTEST", "url: " + url);
                    Log.i("weatherTEST", "Response: " + response.toString());
                    try {                        Log.i("weatherTEST", "weather: " + response.getJSONArray("weather"));

                        Log.i("weatherTEST", "weather: " + response.getJSONArray("weather"));
                        Log.i("weatherTEST", response.getJSONArray("weather").getJSONObject(0).getString("description"));
                        desc.set(response.getJSONArray("weather").getJSONObject(0).getString("description"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("weatherTEST", "LOG");
                },
                        error -> Log.i("weatherTEST", error.toString()));
        Singleton.getInstance(MainActivity.getContext()).addToRequestQueue(jsonObjectRequest);
        return desc.get();
    }
}
