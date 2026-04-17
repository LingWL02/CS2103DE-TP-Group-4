package storage;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Gson type adapter that serializes {@link LocalDateTime} values as ISO strings.
 *
 * <p>This adapter is registered by storage components such as {@link JsonStorage} to keep
 * temporal values consistent across persistence and reconstruction flows.</p>
 */
public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Called when Gson needs to WRITE a LocalDateTime to JSON.
     * Converts the Java object into a string like "2026-04-03T09:00:00".
     */
    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.format(FORMATTER));
        }
    }

    /**
     * Called when Gson needs to READ a LocalDateTime from JSON.
     * Converts a string like "2026-04-03T09:00:00" back into a Java object.
     */
    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String dateTimeString = in.nextString();
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }
}