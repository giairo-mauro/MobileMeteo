package ch.supsi.dti.meteoapp.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.supsi.dti.meteoapp.activities.MainActivity;

public class LocationsHolder {

    private static LocationsHolder sLocationsHolder;
    private List<Location> mLocations;

    public static LocationsHolder get(Context context) {
        if (sLocationsHolder == null)
            sLocationsHolder = new LocationsHolder(context);

        return sLocationsHolder;
    }

    private LocationsHolder(Context context) {
        mLocations = new ArrayList<>();
        mLocations.add(0, new Location());

        Thread t = new Thread(() -> {
            List<Location> locations = MainActivity.getDb().personDao().getLocations();
            for (int i = 0; i < locations.size(); i++) {
                mLocations.add(i+1, locations.get(i));
            }
        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {}
    }

    public List<Location> getLocations() {
        return mLocations;
    }

    public Location getLocation(UUID id) {
        for (Location location : mLocations) {
            if (location.getId().equals(id))
                return location;
        }

        return null;
    }
}