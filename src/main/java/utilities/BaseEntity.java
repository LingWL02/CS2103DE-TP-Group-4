package utilities;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Base class for all entities with shared identity and descriptive fields.
 */
public abstract class BaseEntity {

    private int id;

    private String name;

    private String description;

    private int priority;

    // BufferedImage should remain runtime-only and never be serialized to JSON.
    private transient BufferedImage image;

    protected BaseEntity() {
        this(0, "Unnamed");
    }

    protected BaseEntity(int id, String name) {
        setId(id);
        setName(name);
        setPriority(0);
    }

    /**
     * Returns the Id value.
     */
    public int getId() {
        return id;
    }

    /**
     * Updates the Id value.
     */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("id must be non-negative");
        }
        this.id = id;
    }

    /**
     * Returns the Name value.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the Name value.
     */
    public void setName(String name) {
        String trimmed = Objects.requireNonNull(name, "name").trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = trimmed;
    }

    /**
     * Returns the Description value.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the Description value.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the Priority value.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Updates the Priority value.
     */
    public void setPriority(int priority) {
        if (priority < 0) {
            throw new IllegalArgumentException("priority must be non-negative");
        }
        this.priority = priority;
    }

    /**
     * Returns the Image value.
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Updates the Image value.
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

}
