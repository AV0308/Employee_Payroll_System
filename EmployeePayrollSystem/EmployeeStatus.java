/**
 * Current employment status. TERMINATED / ON_LEAVE employees are kept in the
 * system (for record-keeping) but a real payroll run would normally skip them.
 */
public enum EmployeeStatus {
    ACTIVE,
    ON_LEAVE,
    TERMINATED
}
