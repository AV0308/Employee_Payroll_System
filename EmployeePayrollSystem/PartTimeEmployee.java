/**
 * An hourly-paid employee. Pay is based on hours worked, with a 1.5x
 * overtime multiplier once hours exceed the monthly threshold.
 * Part-time staff do not receive the fixed allowances that salaried
 * employees get, so calculateAllowances() returns 0.
 */
public class PartTimeEmployee extends Employee {

    private static final double OVERTIME_THRESHOLD_HOURS = 160.0;
    private static final double OVERTIME_MULTIPLIER = 1.5;

    private double hourlyRate;
    private double hoursWorked;

    public PartTimeEmployee(String name, Department department, String designation,
                             double hourlyRate, double hoursWorked) throws InvalidSalaryException {
        // basicSalary is set to 0 here and immediately recalculated below
        super(name, department, designation, 0);
        if (hourlyRate < 0 || hoursWorked < 0) {
            throw new InvalidSalaryException(
                    "Hourly rate and hours worked cannot be negative for employee: " + name);
        }
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.basicSalary = regularPay(); // used as the "basic" for PF purposes and reporting
    }

    private double regularPay() {
        double regularHours = Math.min(hoursWorked, OVERTIME_THRESHOLD_HOURS);
        return regularHours * hourlyRate;
    }

    private double overtimePay() {
        double overtimeHours = Math.max(0, hoursWorked - OVERTIME_THRESHOLD_HOURS);
        return overtimeHours * hourlyRate * OVERTIME_MULTIPLIER;
    }

    @Override
    public double calculateAllowances() {
        return 0.0;
    }

    @Override
    public double calculateGrossSalary() {
        return regularPay() + overtimePay();
    }

    @Override
    public String getEmployeeType() {
        return "Part-Time";
    }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
        this.basicSalary = regularPay();
    }
}
