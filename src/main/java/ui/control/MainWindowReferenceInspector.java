package ui.control;

import activity.Activity;
import trip.Trip;
import trip.TripManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Read-only helpers for checking trip/activity references before deletion.
 */
public final class MainWindowReferenceInspector {
    private MainWindowReferenceInspector() {
    }

    public static List<String> findCountryReferences(TripManager tripManager, int countryId) {
        List<String> references = new ArrayList<>();
        for (Trip trip : tripManager.getTrips()) {
            if (trip.getCountry() != null && trip.getCountry().getId() == countryId) {
                references.add("Trip: " + trip.getName());
            }
        }
        return references;
    }

    public static List<String> findLocationReferences(TripManager tripManager, int locationId) {
        List<String> references = new ArrayList<>();
        for (Trip trip : tripManager.getTrips()) {
            for (Activity activity : trip.getActivities()) {
                if (activity.getLocation() != null && activity.getLocation().getId() == locationId) {
                    references.add("Activity: " + activity.getName() + " (Trip: " + trip.getName() + ")");
                }
            }
        }
        return references;
    }
}
