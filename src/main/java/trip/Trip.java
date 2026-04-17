package trip;
import country.Country;
import exceptions.ActivityNotFoundException;
import exceptions.ExpenseNotFoundException;
import exceptions.TimeIntervalConflictException;
import expense.Expense;
import expense.ExpenseManagable;
import temporal.TimeInterval;
import utilities.BaseEntity;
import utilities.Copyable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import activity.Activity;

/**
 * Aggregate root representing a trip with schedule and cost details.
 *
 * <p>A trip owns {@link Activity} and {@link Expense} collections, references a
 * {@link Country}, and is coordinated by {@link TripManager} for lifecycle operations.</p>
 */
public class Trip extends BaseEntity implements TimeInterval, ExpenseManagable, Copyable<Trip> {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    private final List<Activity> activities = new ArrayList<>();

    private final List<Expense> expenses = new ArrayList<>();

    private Country country;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    /**
     * Creates a new instance.
     */
    public Trip(int id, String name, LocalDateTime startDateTime, LocalDateTime endDateTime, Country country) {
        super(id, name);
        setStartDateTime(startDateTime);
        setEndDateTime(endDateTime);
        setCountry(country);
    }

    /**
     * Creates a new instance.
     */
    public Trip(int id, String name, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this(id, name, startDateTime, endDateTime, new Country(0, "Unspecified"));
    }

    /**
     * Returns the Activities value.
     */
    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    /**
     * Returns the Expenses value.
     */
    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(expenses);
    }

    /**
     * Updates the Expenses value.
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses.clear();
        if (expenses != null) {
            for (Expense expense : expenses) {
                this.expenses.add(Objects.requireNonNull(expense, "expense"));
            }
        }
    }

    /**
     * Returns the Country value.
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Updates the Country value.
     */
    public void setCountry(Country country) {
        this.country = Objects.requireNonNull(country, "country");
    }

    /**
     * Returns the StartDateTime value.
     */
    @Override
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Updates the StartDateTime value.
     */
    @Override
    public void setStartDateTime(LocalDateTime startDateTime) {
        if (startDateTime == null) {
            // Default to 00:00 of today if not provided
            startDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
        if (endDateTime != null && startDateTime.isAfter(endDateTime)) {
            throw new IllegalArgumentException("startDateTime must not be after endDateTime");
        }
        this.startDateTime = startDateTime;
    }

    /**
     * Returns the EndDateTime value.
     */
    @Override
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Updates the EndDateTime value.
     */
    @Override
    public void setEndDateTime(LocalDateTime endDateTime) {
        if (endDateTime == null) {
            // Default to 23:59 of today if not provided
            endDateTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(0).withNano(0);
        }
        if (startDateTime != null && endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("endDateTime must not be before startDateTime");
        }
        this.endDateTime = endDateTime;
    }

    /**
     * Adds a new item to this object.
     */
    public void addActivity(Activity activity) throws TimeIntervalConflictException {
        Objects.requireNonNull(activity, "activity");
        activities.add(activity);
    }

    /**
     * Removes an existing item from this object.
     */
    public void deleteActivityById(int id) throws ActivityNotFoundException {
        Activity activity = findActivityById(id);
        activities.remove(activity);
    }

    /**
     * Removes an existing item from this object.
     */
    public void deleteActivityByName(String name) throws ActivityNotFoundException {
        Activity activity = findActivityByName(name);
        activities.remove(activity);
    }

    /**
     * Returns the OverlappingActivities value.
     */
    public List<Activity> getOverlappingActivities() {
        List<Activity> result = new ArrayList<>();
        for (int i = 0; i < activities.size(); i++) {
            Activity current = activities.get(i);
            for (int j = i + 1; j < activities.size(); j++) {
                Activity other = activities.get(j);
                if (current.overlapsWith(other)) {
                    if (!result.contains(current)) {
                        result.add(current);
                    }
                    if (!result.contains(other)) {
                        result.add(other);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the OverlappingAcitivites value.
     */
    public List<Activity> getOverlappingAcitivites(LocalDateTime begin, LocalDateTime end) {
        Objects.requireNonNull(begin, "begin");
        Objects.requireNonNull(end, "end");
        if (end.isBefore(begin)) {
            throw new IllegalArgumentException("end must not be before begin");
        }
        List<Activity> result = new ArrayList<>();
        for (Activity activity : activities) {
            if (activity.getStartDateTime().isBefore(end) && activity.getEndDateTime().isAfter(begin)) {
                result.add(activity);
            }
        }
        return result;
    }

    /**
     * Returns the OverlappingActivities value.
     */
    public List<Activity> getOverlappingActivities(LocalDateTime begin, LocalDateTime end) {
        return getOverlappingAcitivites(begin, end);
    }

    /**
     * Adds a new item to this object.
     */
    @Override
    public void addExpense(Expense expense) {
        expenses.add(Objects.requireNonNull(expense, "expense"));
    }

    /**
     * Removes an existing item from this object.
     */
    @Override
    public void deleteExpenseById(int id) throws ExpenseNotFoundException {
        Expense expense = findExpenseById(id);
        expenses.remove(expense);
    }

    /**
     * Removes an existing item from this object.
     */
    @Override
    public void deleteExpenseByName(String name) throws ExpenseNotFoundException {
        Expense expense = findExpenseByName(name);
        expenses.remove(expense);
    }

    /**
     * Returns the ExpenseById value.
     */
    @Override
    public Expense getExpenseById(int id) throws ExpenseNotFoundException {
        return findExpenseById(id);
    }

    /**
     * Returns the ExpenseByName value.
     */
    @Override
    public Expense getExpenseByName(String name) throws ExpenseNotFoundException {
        return findExpenseByName(name);
    }

    /**
     * Returns the TotalCost value.
     */
    @Override
    public float getTotalCost(Expense.Currency currency) {
        Objects.requireNonNull(currency, "currency");
        float total = 0f;
        for (Expense expense : expenses) {
            if (expense.getCurrency() == currency) {
                total += expense.getCost();
            }
        }
        for (Activity activity : activities) {
            total += activity.getTotalCost(currency);
        }
        return total;
    }

    /**
     * Creates and returns a copy of this object.
     */
    @Override
    public Trip copy() {
        Trip copy = new Trip(getId(), getName(), startDateTime, endDateTime, country);
        copy.setDescription(getDescription());
        copy.setPriority(getPriority());
        copy.setImage(getImage());
        for (Expense expense : expenses) {
            copy.addExpense(expense.copy());
        }
        for (Activity activity : activities) {
            try {
                copy.addActivity(activity.copy());
            } catch (TimeIntervalConflictException e) {
                throw new IllegalStateException("Overlapping activity detected during copy", e);
            }
        }
        return copy;
    }

    private Activity findActivityById(int id) throws ActivityNotFoundException {
        for (Activity activity : activities) {
            if (activity.getId() == id) {
                return activity;
            }
        }
        throw new ActivityNotFoundException("Activity not found: id=" + id);
    }

    private Activity findActivityByName(String name) throws ActivityNotFoundException {
        for (Activity activity : activities) {
            if (Objects.equals(activity.getName(), name)) {
                return activity;
            }
        }
        throw new ActivityNotFoundException("Activity not found: name=" + name);
    }

    private Expense findExpenseById(int id) throws ExpenseNotFoundException {
        for (Expense expense : expenses) {
            if (expense.getId() == id) {
                return expense;
            }
        }
        throw new ExpenseNotFoundException("Expense not found: id=" + id);
    }

    private Expense findExpenseByName(String name) throws ExpenseNotFoundException {
        for (Expense expense : expenses) {
            if (Objects.equals(expense.getName(), name)) {
                return expense;
            }
        }
        throw new ExpenseNotFoundException("Expense not found: name=" + name);
    }

    /**
     * Returns a string representation of this object.
     */
    @Override
    public String toString() {
        return "Trip #" + getId() + ": " + getName()
                + " | " + formatDateTime(getStartDateTime()) + " -> " + formatDateTime(getEndDateTime())
            + " | Country: " + (getCountry() != null ? getCountry() : "No country")
                + " | Activities: " + activities.size()
                + " | Expenses: " + expenses.size();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_FORMAT) : "?";
    }
}
