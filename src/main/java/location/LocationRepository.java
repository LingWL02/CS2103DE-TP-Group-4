package location;

import activity.Activity;
import trip.Trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In-memory repository for selectable locations used by the UI forms.
 */
public class LocationRepository {

    private final List<Location> locations = new ArrayList<>();
    private int nextId = 1;

    public LocationRepository() {
        addIfAbsent(new Location(nextId++, "Singapore", null, "Singapore", "Singapore", null, null));
        addIfAbsent(new Location(nextId++, "Tokyo", null, "Tokyo", "Japan", null, null));
        addIfAbsent(new Location(nextId++, "London", null, "London", "UK", null, null));
    }

    public List<Location> getLocations() {
        return Collections.unmodifiableList(locations);
    }

    public Location addLocation(String name, String address, String city, String country,
                                Double latitude, Double longitude, String imagePath) {
        Location location = new Location(nextId++, name, address, city, country, latitude, longitude, imagePath);
        addIfAbsent(location);
        return location;
    }

    public void initializeFromTrips(List<Trip> trips) {
        if (trips == null) {
            return;
        }
        for (Trip trip : trips) {
            addIfAbsent(trip.getLocation());
            for (Activity activity : trip.getActivities()) {
                addIfAbsent(activity.getLocation());
            }
        }
    }

    private void addIfAbsent(Location location) {
        if (location == null) {
            return;
        }
        for (Location existing : locations) {
            if (isSameLocation(existing, location)) {
                return;
            }
        }
        locations.add(location);
        nextId = Math.max(nextId, location.getId() + 1);
    }

    private boolean isSameLocation(Location a, Location b) {
        String aName = normalize(a.getName());
        String bName = normalize(b.getName());
        String aCity = normalize(a.getCity());
        String bCity = normalize(b.getCity());
        String aCountry = normalize(a.getCountry());
        String bCountry = normalize(b.getCountry());
        return aName.equals(bName) && aCity.equals(bCity) && aCountry.equals(bCountry);
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }
}
