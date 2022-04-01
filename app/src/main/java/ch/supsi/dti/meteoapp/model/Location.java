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

    public void setId(UUID id) {
        this.id = id;
    }
}