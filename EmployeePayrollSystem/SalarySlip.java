import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * An immutable snapshot of one employee's pay computation for one pay period.
 * All figures (gross, tax, PF, net, etc.) are calculated once at construction
 * time so the slip always reflects a single consistent moment in time.
 */
public class SalarySlip implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String slipId;
    private final Employee employee;
    private final String payPeriod;
    private final LocalDateTime generatedOn;

    private final double basicSalary;
    private final double allowances;
    private final double grossSalary;

    private final double incomeTax;
    private final double providentFund;
    private final double professionalTax;
    private final double totalDeductions;

    private final double netSalary;

    public SalarySlip(Employee employee, String payPeriod) {
        this.employee = employee;
        this.payPeriod = payPeriod;
        this.generatedOn = LocalDateTime.now();
        this.slipId = "SLIP-" + employee.getEmployeeId() + "-" + payPeriod.replace(" ", "").toUpperCase();

        this.basicSalary = employee.getBasicSalary();
        this.allowances = employee.calculateAllowances();
        this.grossSalary = employee.calculateGrossSalary();

        this.incomeTax = TaxCalculator.calculateMonthlyIncomeTax(grossSalary);
        this.providentFund = TaxCalculator.calculateProvidentFund(basicSalary);
        this.professionalTax = TaxCalculator.getProfessionalTax();
        this.totalDeductions = incomeTax + providentFund + professionalTax;

        this.netSalary = grossSalary - totalDeductions;
    }

    public Employee getEmployee() { return employee; }
    public String getPayPeriod() { return payPeriod; }
    public double getGrossSalary() { return grossSalary; }
    public double getTotalDeductions() { return totalDeductions; }
    public double getNetSalary() { return netSalary; }

    /** Prints a formatted payslip directly to the console. */
    public void printSlip() {
        System.out.print(toReportString());
    }

    /** Same content as printSlip(), returned as a String (used for console and file export). */
    public String toReportString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
        String line = "=".repeat(58);
        String thin = "-".repeat(58);
        StringBuilder sb = new StringBuilder();

        sb.append(line).append(System.lineSeparator());
        sb.append(String.format("%37s%n", "SALARY SLIP"));
        sb.append(line).append(System.lineSeparator());
        sb.append(String.format("Slip ID       : %s%n", slipId));
        sb.append(String.format("Pay Period    : %s%n", payPeriod));
        sb.append(String.format("Generated On  : %s%n", generatedOn.format(fmt)));
        sb.append(thin).append(System.lineSeparator());
        sb.append(String.format("Employee ID   : %d%n", employee.getEmployeeId()));
        sb.append(String.format("Name          : %s%n", employee.getName()));
        sb.append(String.format("Department    : %s%n", employee.getDepartment()));
        sb.append(String.format("Designation   : %s%n", employee.getDesignation()));
        sb.append(String.format("Employee Type : %s%n", employee.getEmployeeType()));
        sb.append(thin).append(System.lineSeparator());
        sb.append("EARNINGS").append(System.lineSeparator());
        sb.append(String.format("  Basic Salary         : Rs. %,12.2f%n", basicSalary));
        sb.append(String.format("  Allowances           : Rs. %,12.2f%n", allowances));
        sb.append(String.format("  Gross Salary         : Rs. %,12.2f%n", grossSalary));
        sb.append(thin).append(System.lineSeparator());
        sb.append("DEDUCTIONS").append(System.lineSeparator());
        sb.append(String.format("  Income Tax (TDS)     : Rs. %,12.2f%n", incomeTax));
        sb.append(String.format("  Provident Fund (PF)  : Rs. %,12.2f%n", providentFund));
        sb.append(String.format("  Professional Tax     : Rs. %,12.2f%n", professionalTax));
        sb.append(String.format("  Total Deductions     : Rs. %,12.2f%n", totalDeductions));
        sb.append(thin).append(System.lineSeparator());
        sb.append(String.format("NET SALARY (Take Home) : Rs. %,12.2f%n", netSalary));
        sb.append(line).append(System.lineSeparator());
        sb.append(System.lineSeparator());

        return sb.toString();
    }
}
