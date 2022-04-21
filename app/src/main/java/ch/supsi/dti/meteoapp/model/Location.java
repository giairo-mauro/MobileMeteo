package ch.supsi.dti.meteoapp.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

import ch.supsi.dti.meteoapp.model.weatherFetch.FetchTemp;
import ch.supsi.dti.meteoapp.model.weatherFetch.Weather;

@Entity
public class Location {
    @PrimaryKey
    @NonNull
    private UUID id;
    private String city;

    public Location(String city) {
        this.id = UUID.randomUUID();
        this.city = city;
    }
    @Ignore
    public Location() {
        this.id = UUID.randomUUID();
        this.city = null;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public UUID getId() {
        return id;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }
}