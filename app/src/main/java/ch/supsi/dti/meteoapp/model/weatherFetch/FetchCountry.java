package ch.supsi.dti.meteoapp.model.weatherFetch;

import android.os.AsyncTask;

import ch.supsi.dti.meteoapp.model.OnTaskCompleted;

public class FetchCountry extends AsyncTask<Void, Void, String> {
    private OnTaskCompleted listener;
    private String city;
    private String info;

    public FetchCountry(String city, OnTaskCompleted listener) {
        this.listener = listener;
        this.city = city;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return new WeatherAPI().fetchItems(city, "country");
    }

    @Override
    protected void onPostExecute(String data) {
        listener.OnTextGet(data);
    }
}