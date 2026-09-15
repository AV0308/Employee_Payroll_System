import java.io.Serializable;

/**
 * Abstract base class for every kind of employee in the system.
 *
 * OOP concepts demonstrated here:
 *  - Abstraction     : calculateGrossSalary()/calculateAllowances()/getEmployeeType()
 *                       are left for concrete subclasses to define.
 *  - Encapsulation   : all fields are private/protected with controlled access.
 *  - Polymorphism    : compareTo() and generatePayslip() rely on the actual
 *                       runtime type's overridden calculateGrossSalary().
 *  - Interfaces      : implements Payable and Comparable.
 */
public abstract class Employee implements Payable, Comparable<Employee>, Serializable {

    private static final long serialVersionUID = 1L;
    private static int idCounter = 1000;

    protected final int employeeId;
    protected String name;
    protected Department department;
    protected String designation;
    protected double basicSalary;
    protected EmployeeStatus status;

    protected Employee(String name, Department department, String designation, double basicSalary)
            throws InvalidSalaryException {
        if (basicSalary < 0) {
            throw new InvalidSalaryException("Basic salary cannot be negative for employee: " + name);
        }
        this.employeeId = ++idCounter;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.basicSalary = basicSalary;
        this.status = EmployeeStatus.ACTIVE;
    }

    // ---- Abstract methods: every employee type supplies its own pay logic ----
    public abstract double calculateGrossSalary();
    public abstract double calculateAllowances();
    public abstract String getEmployeeType();

    // ---- Payable contract: identical for every subtype, so implemented once here ----
    @Override
    public SalarySlip generatePayslip(String payPeriod) {
        return new SalarySlip(this, payPeriod);
    }

    // ---- Encapsulated access ----
    public int getEmployeeId() { return employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) throws InvalidSalaryException {
        if (basicSalary < 0) {
            throw new InvalidSalaryException("Basic salary cannot be negative.");
        }
        this.basicSalary = basicSalary;
    }

    public EmployeeStatus getStatus() { return status; }
    public void setStatus(EmployeeStatus status) { this.status = status; }

    /** Keeps the shared ID counter ahead of any IDs restored from a save file. */
    static void syncIdCounterPastId(int usedId) {
        if (usedId > idCounter) {
            idCounter = usedId;
        }
    }

    // ---- Natural ordering: highest earner first, using polymorphic gross salary ----
    @Override
    public int compareTo(Employee other) {
        return Double.compare(other.calculateGrossSalary(), this.calculateGrossSalary());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Employee other)) return false;
        return employeeId == other.employeeId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(employeeId);
    }

    @Override
    public String toString() {
        return String.format("ID: %-6d | %-18s | %-24s | %-16s | %-11s | Basic: Rs.%,10.2f | %s",
                employeeId, name, department, designation, getEmployeeType(), basicSalary, status);
    }
}
