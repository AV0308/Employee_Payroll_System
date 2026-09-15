/**
 * Fixed set of departments an employee can belong to.
 * Using an enum (instead of a raw String) prevents typos like
 * "Sales" vs "sales" vs "SALES" from creating duplicate categories.
 */
public enum Department {
    HUMAN_RESOURCES("Human Resources"),
    INFORMATION_TECHNOLOGY("Information Technology"),
    FINANCE("Finance"),
    SALES("Sales"),
    OPERATIONS("Operations"),
    MARKETING("Marketing"),
    MANAGEMENT("Management");

    private final String label;

    Department(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
