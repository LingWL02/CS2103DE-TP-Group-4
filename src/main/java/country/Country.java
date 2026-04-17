package country;

import utilities.BaseEntity;

import java.util.Objects;

/**
 * Domain model representing a country used in trip planning.
 *
 * <p>This entity is referenced by {@link trip.Trip} and {@link location.Location}, and is
 * persisted and validated through the country repository and storage layer.</p>
 */
public class Country extends BaseEntity {

    private String continent;
    private String imagePath;

    /**
     * Creates a new instance.
     */
    public Country(int id, String name) {
        this(id, name, null, null);
    }

    /**
     * Creates a new instance.
     */
    public Country(int id, String name, String continent, String imagePath) {
        super(id, name);
        this.continent = normalizeOptional(continent);
        this.imagePath = normalizeOptional(imagePath);
    }

    /**
     * Returns the Continent value.
     */
    public String getContinent() {
        return continent;
    }

    /**
     * Updates the Continent value.
     */
    public void setContinent(String continent) {
        this.continent = normalizeOptional(continent);
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
        this.imagePath = normalizeOptional(imagePath);
    }

    /**
     * Returns a string representation of this object.
     */
    @Override
    public String toString() {
        if (continent == null) {
            return getName();
        }
        return getName() + " (" + continent + ")";
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
