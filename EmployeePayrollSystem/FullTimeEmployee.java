/**
 * A salaried employee with a fixed monthly basic salary plus standard
 * statutory/company allowances (HRA, DA, medical, conveyance).
 */
public class FullTimeEmployee extends Employee {

    private static final double HRA_PERCENT = 0.20;          // House Rent Allowance
    private static final double DA_PERCENT = 0.15;           // Dearness Allowance
    private static final double MEDICAL_ALLOWANCE = 1250.0;  // flat, per month
    private static final double CONVEYANCE_ALLOWANCE = 1600.0; // flat, per month

    public FullTimeEmployee(String name, Department department, String designation, double basicSalary)
            throws InvalidSalaryException {
        super(name, department, designation, basicSalary);
    }

    @Override
    public double calculateAllowances() {
        double hra = basicSalary * HRA_PERCENT;
        double da = basicSalary * DA_PERCENT;
        return hra + da + MEDICAL_ALLOWANCE + CONVEYANCE_ALLOWANCE;
    }

    @Override
    public double calculateGrossSalary() {
        return basicSalary + calculateAllowances();
    }

    @Override
    public String getEmployeeType() {
        return "Full-Time";
    }
}
