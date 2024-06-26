package it.polimi.ingsw.view.gui.components.toast;

/**
 * This class represents a toast level for the GUI.
 */
public enum ToastLevels {
    /**
     * Represents a successful operation.
     * It is colored green.
     */
    SUCCESS("success"),

    /**
     * Represents an informational message.
     * It is colored brown.
     */
    INFO("info"),

    /**
     * Represents a warning message.
     * It is colored yellow.
     */
    WARNING("warning"),

    /**
     * Represents an error message.
     * It is colored red.
     */
    ERROR("error");

    /**
     * The CSS class name associated with this toast level.
     */
    private final String cssClassName;

    /**
     * Constructs a new ToastLevels enum with the specified CSS class name.
     *
     * @param cssClassName the CSS class name for this toast level
     */
    ToastLevels(String cssClassName) {
        this.cssClassName = cssClassName;
    }

    /**
     * Returns the CSS class name for this toast level.
     *
     * @return the CSS class name for this toast level
     */
    public String getCssClassName() {
        return cssClassName;
    }
}