package ch.supsi.dti.meteoapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class Location {
    @PrimaryKey
    @NonNull
    private UUID id;

    private String city;
    private String country;
    private String degrees;

    public Location(String city, String country, String degrees) {
        this.id = UUID.randomUUID();
        this.city = city;
        this.country = country;
        this.degrees = degrees;
    }
    @Ignore
    public Location() {
        this.id = UUID.randomUUID();
        this.city = null;
        this.country = null;
        this.degrees = null;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDegrees() {
        return degrees;
    }

    public void setDegrees(String degrees) {
        this.degrees = degrees;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}