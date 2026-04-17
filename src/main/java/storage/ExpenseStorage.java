package storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import expense.Expense;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistence adapter for reading and writing expense data as JSON.
 *
 * <p>This class is consumed by {@link expense.ExpenseRepository} and isolates Gson file
 * serialization concerns from expense business logic.</p>
 */
public class ExpenseStorage {

    private static final String DATA_DIRECTORY = "data";
    private static final String DATA_FILE = "expenses.json";

    private final Gson gson;
    private final Path dataFilePath;

    /**
     * Creates a new instance.
     */
    public ExpenseStorage() {
        this(Paths.get(DATA_DIRECTORY, DATA_FILE));
    }

    /**
     * Creates a new instance.
     */
    public ExpenseStorage(Path dataFilePath) {
        this.dataFilePath = dataFilePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Saves data from this component.
     */
    public void save(List<Expense> expenses) throws IOException {
        Path directory = dataFilePath.getParent();
        if (directory != null && !Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        try (Writer writer = Files.newBufferedWriter(dataFilePath)) {
            gson.toJson(expenses, writer);
        }
    }

    /**
     * Loads data into this component.
     */
    public List<Expense> load() throws IOException {
        if (!Files.exists(dataFilePath)) {
            return new ArrayList<>();
        }
        try (Reader reader = Files.newBufferedReader(dataFilePath)) {
            Type listType = new TypeToken<List<Expense>>() {}.getType();
            List<Expense> expenses = gson.fromJson(reader, listType);
            return expenses != null ? expenses : new ArrayList<>();
        } catch (JsonParseException e) {
            return new ArrayList<>();
        }
    }
}
