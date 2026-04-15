package ui.control;

import activity.Activity;
import javafx.scene.control.ComboBox;
import location.Location;
import trip.Trip;
import ui.TripPage;

import java.util.List;

/**
 * Coordination contract exposed by the main window to child pages.
 *
 * <p>This layer reduces direct coupling to {@code MainWindow} and centralizes
 * navigation plus shared cross-page actions.</p>
 */
public interface MainWindowControl {
    /**
     * Navigate to the home page.
     */
    void showHomePage();

    /**
     * Navigate to the trip page for a selected trip.
     *
     * @param trip selected trip
     */
    void showTripPage(Trip trip);

    /**
     * Navigate to the activity page for a selected activity.
     *
     * @param activity selected activity
     * @param tripPage calling trip page
     */
    void showActivityPage(Activity activity, TripPage tripPage);

    /**
     * @return all known locations from lookup storage
     */
    List<Location> getAvailableLocations();

    /**
     * Launches the add-location flow.
     *
     * @return created location, or {@code null} if cancelled/failed
     */
    Location promptAddLocation();

    /**
     * Launches the edit-location flow.
     *
     * @param location selected location
     * @return edited location, or {@code null} if cancelled/failed
     */
    Location promptEditLocation(Location location);

    /**
     * Opens the edit-trip flow.
     *
     * @param trip selected trip
     * @return {@code true} when a save occurred
     */
    boolean promptEditTrip(Trip trip);

    /**
     * Deletes location from storage when safe.
     *
     * @param location location to delete
     * @param onDataChanged callback run after successful deletion
     * @return {@code true} if deleted
     */
    boolean deleteLocationFromUi(Location location, Runnable onDataChanged);

    /**
     * Adds a contextual delete action to location combo-box cells.
     *
     * @param locationCombo combo box to configure
     * @param onDataChanged callback run after successful deletion
     */
    void configureLocationComboForDelete(ComboBox<Location> locationCombo, Runnable onDataChanged);

    /**
     * Removes an expense from repository when it is orphaned.
     *
     * @param expenseId expense identifier
     */
    void cleanupExpenseIfOrphaned(int expenseId);

    /**
     * Refreshes global activity snapshot widgets.
     */
    void refreshHeaderActivitySummary();
}
