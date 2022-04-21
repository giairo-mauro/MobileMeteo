package ch.supsi.dti.meteoapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.supsi.dti.meteoapp.activities.MainActivity;

public class LocationsHolder {

    private static LocationsHolder sLocationsHolder;
    private List<Location> mLocations;

    public static LocationsHolder get() {
        if (sLocationsHolder == null)
            sLocationsHolder = new LocationsHolder();

        return sLocationsHolder;
    }

    private LocationsHolder() {
        mLocations = new ArrayList<>();
        mLocations.add(0, new Location());
        //Create Locations list from db
        Thread t = new Thread(() -> {
            List<Location> locations = MainActivity.getDb().locationDAO().getLocations();
            for (int i = 0; i < locations.size(); i++) {
                mLocations.add(i+1, locations.get(i));
            }
        });
        t.start();

        try {
            t.join();
        } catch (InterruptedException ignored) {}
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