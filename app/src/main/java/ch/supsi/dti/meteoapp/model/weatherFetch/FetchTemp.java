package ch.supsi.dti.meteoapp.model.weatherFetch;

import android.os.AsyncTask;

import ch.supsi.dti.meteoapp.model.OnTaskCompleted;

public class FetchTemp extends AsyncTask<Void, Void, String> {
    private OnTaskCompleted listener;
    private String city;
    private String info;

    public FetchTemp(String city, OnTaskCompleted listener) {
        this.listener = listener;
        this.city = city;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return new WeatherAPI().fetchItems(city, "temp");
    }

    @Override
    protected void onPostExecute(String data) {
        listener.OnTempGet(data);
    }
}