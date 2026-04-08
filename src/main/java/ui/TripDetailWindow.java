package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import trip.Trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TripDetailWindow {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    @FXML
    private Label tripNameLabel;
    @FXML
    private Label tripDateLabel;
    @FXML
    private Label tripLocationLabel;
    @FXML
    private Button backButton;

    private Stage stage;
    private Trip trip;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
        tripNameLabel.setText(trip.getName());
        tripDateLabel.setText(formatDateTimeRange(trip.getStartDateTime(), trip.getEndDateTime()));
        tripLocationLabel.setText(trip.getLocation() != null ? trip.getLocation().toString() : "");
    }

    @FXML
    private void initialize() {
        backButton.setOnAction(e -> {
            if (stage != null) {
                stage.close();
            }
        });
    }

    private String formatDateTimeRange(LocalDateTime start, LocalDateTime end) {
        return formatDateTime(start) + " -> " + formatDateTime(end);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_FORMAT) : "?";
    }
}
