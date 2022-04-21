package ch.supsi.dti.meteoapp.model.weatherFetch;

import android.os.AsyncTask;
import android.util.Log;

import ch.supsi.dti.meteoapp.model.OnTaskCompleted;

public class FetchImg extends AsyncTask<Void, Void, byte[]> {
    private OnTaskCompleted listener;
    private String city;

    public FetchImg(String city, OnTaskCompleted listener) {
        this.listener = listener;
        this.city = city;
    }

    @Override
    protected byte[] doInBackground(Void... voids) {
        return new Weather().fetchImg(city);
    }

    @Override
    protected void onPostExecute(byte[] data) {
        listener.onImageGet(data);
    }
}