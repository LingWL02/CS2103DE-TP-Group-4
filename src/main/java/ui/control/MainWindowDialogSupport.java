package ui.control;

import country.Country;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.io.File;
import java.time.LocalTime;

/**
 * Shared UI utility methods for dialog styling, parsing, and form helpers.
 *
 * <p>This stateless helper is used by main-window CRUD controllers to keep repeated JavaFX
 * dialog behavior centralized and consistent.</p>
 */
public final class MainWindowDialogSupport {
    /**
     * Utility class; prevents instantiation.
     */
    private MainWindowDialogSupport() {
    }

    /**
     * Performs the applyDialogTheme operation.
     */
    public static void applyDialogTheme(Dialog<?> dialog, String styleClass, String cancelButtonStyleClass) {
        String stylesheet = MainWindowDialogSupport.class.getResource("/view/theme.css").toExternalForm();
        if (!dialog.getDialogPane().getStylesheets().contains(stylesheet)) {
            dialog.getDialogPane().getStylesheets().add(stylesheet);
        }
        if (styleClass != null && !styleClass.isBlank() && !dialog.getDialogPane().getStyleClass().contains(styleClass)) {
            dialog.getDialogPane().getStyleClass().add(styleClass);
        }

        applyDialogActionStyles(dialog, cancelButtonStyleClass);
        dialog.getDialogPane().getButtonTypes().addListener((ListChangeListener<ButtonType>) change ->
            applyDialogActionStyles(dialog, cancelButtonStyleClass));
    }

    /**
     * Creates and returns a new item.
     */
    public static Button createAddButton(String text) {
        return createStyledButton(text, "add-button");
    }

    /**
     * Creates and returns a new item.
     */
    public static Button createEditButton(String text) {
        return createStyledButton(text, "edit-button");
    }

    /**
     * Creates and returns a new item.
     */
    public static Button createDeleteButton(String text) {
        return createStyledButton(text, "delete-button");
    }

    /**
     * Creates and returns a new item.
     */
    public static HBox createResponsiveActionRow(ComboBox<?> combo, Button addButton, Button editButton, Button deleteButton) {
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setPrefWidth(260);
        HBox.setHgrow(combo, Priority.ALWAYS);
        addButton.setMinWidth(84);
        editButton.setMinWidth(84);
        deleteButton.setMinWidth(84);
        return new HBox(8, combo, addButton, editButton, deleteButton);
    }

    /**
     * Performs the chooseImagePath operation.
     */
    public static void chooseImagePath(Dialog<?> dialog, TextField targetField) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose Image");
        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.webp")
        );
        Window window = dialog.getDialogPane().getScene() != null ? dialog.getDialogPane().getScene().getWindow() : null;
        File file = chooser.showOpenDialog(window);
        if (file != null) {
            targetField.setText(file.getAbsolutePath());
        }
    }

    /**
     * Creates and returns a new item.
     */
    public static StringConverter<Country> createCountryConverter() {
        return new StringConverter<>() {
            /**
             * Returns a string representation of this object.
             */
            @Override
            public String toString(Country country) {
                return country == null ? "" : country.toString();
            }

            /**
             * Performs the fromString operation.
             */
            @Override
            public Country fromString(String string) {
                return null;
            }
        };
    }

    /**
     * Performs the parseTimeOrDefault operation.
     */
    public static LocalTime parseTimeOrDefault(String text, LocalTime fallback) {
        try {
            return LocalTime.parse(text);
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Performs the parseOptionalDouble operation.
     */
    public static Double parseOptionalDouble(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        return Double.parseDouble(text.trim());
    }

    private static Button createStyledButton(String text, String styleClass) {
        Button button = new Button(text);
        if (!button.getStyleClass().contains(styleClass)) {
            button.getStyleClass().add(styleClass);
        }
        return button;
    }

    private static void applyDialogActionStyles(Dialog<?> dialog, String cancelButtonStyleClass) {
        for (ButtonType buttonType : dialog.getDialogPane().getButtonTypes()) {
            if (!buttonType.getButtonData().isCancelButton()) {
                continue;
            }
            Node node = dialog.getDialogPane().lookupButton(buttonType);
            if (node instanceof Button button && cancelButtonStyleClass != null && !cancelButtonStyleClass.isBlank()) {
                if (!button.getStyleClass().contains(cancelButtonStyleClass)) {
                    button.getStyleClass().add(cancelButtonStyleClass);
                }
            }
        }
    }
}
