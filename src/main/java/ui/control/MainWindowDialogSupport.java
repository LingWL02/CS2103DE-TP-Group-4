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
 * Shared dialog/UI helper functions used by MainWindow sub-controllers.
 */
public final class MainWindowDialogSupport {
    private MainWindowDialogSupport() {
    }

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

    public static Button createAddButton(String text) {
        return createStyledButton(text, "add-button");
    }

    public static Button createEditButton(String text) {
        return createStyledButton(text, "edit-button");
    }

    public static Button createDeleteButton(String text) {
        return createStyledButton(text, "delete-button");
    }

    public static HBox createResponsiveActionRow(ComboBox<?> combo, Button addButton, Button editButton, Button deleteButton) {
        combo.setMaxWidth(Double.MAX_VALUE);
        combo.setPrefWidth(260);
        HBox.setHgrow(combo, Priority.ALWAYS);
        addButton.setMinWidth(84);
        editButton.setMinWidth(84);
        deleteButton.setMinWidth(84);
        return new HBox(8, combo, addButton, editButton, deleteButton);
    }

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

    public static StringConverter<Country> createCountryConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Country country) {
                return country == null ? "" : country.toString();
            }

            @Override
            public Country fromString(String string) {
                return null;
            }
        };
    }

    public static LocalTime parseTimeOrDefault(String text, LocalTime fallback) {
        try {
            return LocalTime.parse(text);
        } catch (Exception e) {
            return fallback;
        }
    }

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
