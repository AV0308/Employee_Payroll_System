import java.io.*;
import java.util.*;

/**
 * Central data-processing hub of the system: holds the in-memory list of
 * employee records, runs payroll across all of them, and persists records
 * to disk (via Java serialization) so they survive between program runs.
 */
public class PayrollManager {

    private static final String DATA_FILE = "employee_records.dat";
    private final List<Employee> employees;

    public PayrollManager() {
        this.employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        System.out.println("Employee added successfully -> ID: " + employee.getEmployeeId());
    }

    public boolean removeEmployee(int employeeId) {
        return employees.removeIf(e -> e.getEmployeeId() == employeeId);
    }

    public Employee findById(int employeeId) {
        for (Employee e : employees) {
            if (e.getEmployeeId() == employeeId) return e;
        }
        return null;
    }

    public List<Employee> findByName(String name) {
        List<Employee> matches = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getName().equalsIgnoreCase(name)) matches.add(e);
        }
        return matches;
    }

    public List<Employee> getAllEmployees() {
        return Collections.unmodifiableList(employees);
    }

    /** Returns a copy of the roster sorted by gross salary, highest first (uses Employee.compareTo). */
    public List<Employee> getEmployeesSortedByGrossSalary() {
        List<Employee> sorted = new ArrayList<>(employees);
        Collections.sort(sorted);
        return sorted;
    }

    public void generatePayrollForAll(String payPeriod) {
        if (employees.isEmpty()) {
            System.out.println("No employee records found.");
            return;
        }
        for (Employee e : employees) {
            e.generatePayslip(payPeriod).printSlip();
        }
    }

    public double getTotalMonthlyPayrollExpense(String payPeriod) {
        double total = 0;
        for (Employee e : employees) {
            total += e.generatePayslip(payPeriod).getNetSalary();
        }
        return total;
    }

    public void printPayrollSummary(String payPeriod) {
        if (employees.isEmpty()) {
            System.out.println("No employee records found.");
            return;
        }
        String thin = "-".repeat(72);
        System.out.println("=".repeat(72));
        System.out.printf("PAYROLL SUMMARY - %s%n", payPeriod);
        System.out.println("=".repeat(72));
        System.out.printf("%-6s %-18s %-12s %16s %16s%n", "ID", "Name", "Type", "Gross", "Net");
        System.out.println(thin);

        double totalGross = 0, totalNet = 0;
        for (Employee e : employees) {
            SalarySlip slip = e.generatePayslip(payPeriod);
            System.out.printf("%-6d %-18s %-12s %,16.2f %,16.2f%n",
                    e.getEmployeeId(), e.getName(), e.getEmployeeType(),
                    slip.getGrossSalary(), slip.getNetSalary());
            totalGross += slip.getGrossSalary();
            totalNet += slip.getNetSalary();
        }
        System.out.println(thin);
        System.out.printf("%-38s %,16.2f %,16.2f%n", "TOTAL", totalGross, totalNet);
        System.out.println("=".repeat(72));
    }

    // ---------------- Persistence: store employee records on disk ----------------

    public void saveRecords() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(new ArrayList<>(employees));
            System.out.println("Saved " + employees.size() + " record(s) to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Failed to save records: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadRecords() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("No saved record file found (" + DATA_FILE + ").");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Employee> loaded = (List<Employee>) ois.readObject();
            employees.clear();
            employees.addAll(loaded);
            for (Employee e : loaded) {
                Employee.syncIdCounterPastId(e.getEmployeeId());
            }
            System.out.println("Loaded " + loaded.size() + " employee record(s) from " + DATA_FILE);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load records: " + e.getMessage());
        }
    }

    public void exportPayrollReport(String payPeriod, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Employee e : employees) {
                writer.print(e.generatePayslip(payPeriod).toReportString());
            }
            System.out.println("Payroll report exported to " + fileName);
        } catch (IOException e) {
            System.out.println("Failed to export report: " + e.getMessage());
        }
    }

    public int getEmployeeCount() {
        return employees.size();
    }
}
