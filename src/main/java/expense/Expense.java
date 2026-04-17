package expense;
import java.util.Locale;
import java.util.Objects;

import utilities.BaseEntity;
import utilities.Copyable;

/**
 * Domain model for a single monetary expense entry.
 *
 * <p>Expenses are attached to {@link trip.Trip} and {@link activity.Activity} objects and are
 * persisted through the expense repository and storage components.</p>
 */
public class Expense extends BaseEntity implements Copyable<Expense> {

    /**
     * Represents the enum Type.
     */
    public enum Type {
        FOOD,
        ACCOMMODATION,
        TRANSPORTATION,
        ENTERTAINMENT,
        OTHER
    }

    /**
     * Represents the enum Currency.
     */
    public enum Currency {
        SGD,
        USD,
        EUR,
        GBP,
        JPY,
        CNY
    }

    private float cost;

    private Currency currency;

    private Type type;

    private String imagePath;

    /**
     * Creates a new instance.
     */
    public Expense(int id, String name, float cost, Currency currency, Type type) {
        this(id, name, cost, currency, type, null);
    }

    /**
     * Creates a new instance.
     */
    public Expense(int id, String name, float cost, Currency currency, Type type, String imagePath) {
        super(id, name);
        setCost(cost);
        setCurrency(currency);
        setType(type);
        setImagePath(imagePath);
    }

    /**
     * Returns the Cost value.
     */
    public float getCost() {
        return cost;
    }

    /**
     * Updates the Cost value.
     */
    public void setCost(float cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("cost must be non-negative");
        }
        this.cost = cost;
    }

    /**
     * Returns the Currency value.
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Updates the Currency value.
     */
    public void setCurrency(Currency currency) {
        this.currency = Objects.requireNonNull(currency, "currency");
    }

    /**
     * Returns the Type value.
     */
    public Type getType() {
        return type;
    }

    /**
     * Updates the Type value.
     */
    public void setType(Type type) {
        this.type = Objects.requireNonNull(type, "type");
    }

    /**
     * Returns the ImagePath value.
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Updates the ImagePath value.
     */
    public void setImagePath(String imagePath) {
        if (imagePath == null) {
            this.imagePath = null;
            return;
        }
        String trimmed = imagePath.trim();
        this.imagePath = trimmed.isEmpty() ? null : trimmed;
    }

    /**
     * Creates and returns a copy of this object.
     */
    @Override
    public Expense copy() {
        Expense copy = new Expense(getId(), getName(), cost, currency, type, imagePath);
        copy.setDescription(getDescription());
        copy.setPriority(getPriority());
        copy.setImage(getImage());
        return copy;
    }

    /**
     * Returns a string representation of this object.
     */
    @Override
    public String toString() {
        return "Expense #" + getId() + ": " + getName()
                + " | " + String.format(Locale.US, "%.2f", getCost()) + " " + getCurrency()
                + " | Type: " + getType();
    }
}
