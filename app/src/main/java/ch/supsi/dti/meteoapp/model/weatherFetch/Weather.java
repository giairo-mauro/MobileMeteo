package ch.supsi.dti.meteoapp.model.weatherFetch;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//Class that
public class Weather {

    private String weatherDataAPI = "https://api.openweathermap.org/data/2.5/weather?";
    private String weatherImgAPI = "https://openweathermap.org/img/wn/";
    private String img;
    private final String API_KEY = "1ae729fe4329fe7bb3784f5931d6643b";

    public Weather(){}

    //json.getJSONArray("weather").getJSONObject(0).getString("description");
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);

            int bytesRead;
            byte[] buffer = new byte[1024];

            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }

            out.close();

            return out.toByteArray();
        }
        catch (Exception e) {
            Log.e("weatherTEST", "Failed to get bytes items", e);
            return null;
        }
        finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public String fetchItems(String city, String info) {
        String result = "";
        try {
            String url = Uri.parse(weatherDataAPI)
                    .buildUpon()
                    .appendQueryParameter("q", city)
                    .appendQueryParameter("appid", API_KEY)
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONObject json = new JSONObject(jsonString);
            //Return the correct data
            switch (info){
                case "temp":
                    result = json.getJSONObject("main").getString("temp");
                    //Convert temperature in Celsius
                    result = Math.round(Float.parseFloat(result) - 273.15) +"°C";
                    break;
                case "country":
                    result = json.getJSONObject("sys").getString("country");
                    break;
                case "img":
                    //If we need the img we change the name
                    img = json.getJSONArray("weather").getJSONObject(0).getString("icon") +".png";
                    break;
                default:
                    result = json.getString("message");
                    break;
            }
        } catch (IOException ioe) {
            Log.e("weatherTEST", "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e("weatherTEST", "Failed to parse JSON", je);
        }
        return result;
    }

    public byte[] fetchImg(String city) {
        //Change the img name
        byte[] string = new byte[0];
        fetchItems(city, "img");
        String url = Uri.parse(weatherImgAPI + img)
                .buildUpon()
                .build().toString();
        try {
            if(getUrlBytes(url) == null) return null;
            string = getUrlBytes(url);
        } catch (IOException ioe) {
            Log.e("weatherTEST", "Failed to fetch items", ioe);
        }
        return string;
    }
}
