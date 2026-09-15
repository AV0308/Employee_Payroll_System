/**
 * Thrown when an employee is constructed or updated with financially
 * invalid data (negative salary, negative hours, negative hourly rate, etc.).
 * Being a checked exception forces every caller to explicitly deal with
 * bad input instead of letting it silently corrupt payroll figures.
 */
public class InvalidSalaryException extends Exception {
    public InvalidSalaryException(String message) {
        super(message);
    }
}
