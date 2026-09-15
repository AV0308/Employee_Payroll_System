import java.util.List;
import java.util.Scanner;

/**
 * Console entry point for the Employee Payroll System.
 * Wires together Employee subclasses, TaxCalculator, SalarySlip and
 * PayrollManager behind a simple text menu.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final PayrollManager payrollManager = new PayrollManager();

    public static void main(String[] args) {
        loadSampleEmployees();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addEmployeeFlow();
                case 2 -> viewAllEmployees();
                case 3 -> generateSinglePayslip();
                case 4 -> generateAllPayslips();
                case 5 -> viewPayrollSummary();
                case 6 -> removeEmployeeFlow();
                case 7 -> payrollManager.saveRecords();
                case 8 -> payrollManager.loadRecords();
                case 9 -> exportReportFlow();
                case 0 -> {
                    running = false;
                    System.out.println("Exiting Payroll System. Goodbye!");
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n===== EMPLOYEE PAYROLL SYSTEM =====");
        System.out.println("1. Add Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. Generate Payslip for One Employee");
        System.out.println("4. Generate Payroll for All Employees");
        System.out.println("5. View Payroll Summary");
        System.out.println("6. Remove Employee");
        System.out.println("7. Save Records to File");
        System.out.println("8. Load Records from File");
        System.out.println("9. Export Monthly Payroll Report to Text File");
        System.out.println("0. Exit");
    }

    /** Seeds the system with a few sample records so the menu has data to show immediately. */
    private static void loadSampleEmployees() {
        try {
            payrollManager.addEmployee(new FullTimeEmployee(
                    "Aditi Sharma", Department.INFORMATION_TECHNOLOGY, "Software Engineer", 65000));
            payrollManager.addEmployee(new Manager(
                    "Rohan Verma", Department.SALES, "Sales Manager", 90000, 8000, 6));
            payrollManager.addEmployee(new PartTimeEmployee(
                    "Meera Iyer", Department.MARKETING, "Content Writer", 350, 180));
            payrollManager.addEmployee(new FullTimeEmployee(
                    "Karan Singh", Department.FINANCE, "Accountant", 48000));
        } catch (InvalidSalaryException e) {
            System.out.println("Error loading sample data: " + e.getMessage());
        }
    }

    private static void addEmployeeFlow() {
        System.out.println("\nSelect Employee Type:");
        System.out.println("1. Full-Time Employee");
        System.out.println("2. Manager");
        System.out.println("3. Part-Time Employee");
        int type = readInt("Enter choice: ");

        System.out.print("Name: ");
        String name = scanner.nextLine();
        Department department = readDepartment();
        System.out.print("Designation: ");
        String designation = scanner.nextLine();

        try {
            switch (type) {
                case 1 -> {
                    double basic = readDouble("Basic Salary: ");
                    payrollManager.addEmployee(new FullTimeEmployee(name, department, designation, basic));
                }
                case 2 -> {
                    double basic = readDouble("Basic Salary: ");
                    double bonus = readDouble("Special Bonus: ");
                    int teamSize = readInt("Team Size: ");
                    payrollManager.addEmployee(new Manager(name, department, designation, basic, bonus, teamSize));
                }
                case 3 -> {
                    double rate = readDouble("Hourly Rate: ");
                    double hours = readDouble("Hours Worked This Month: ");
                    payrollManager.addEmployee(new PartTimeEmployee(name, department, designation, rate, hours));
                }
                default -> System.out.println("Invalid employee type.");
            }
        } catch (InvalidSalaryException e) {
            System.out.println("Could not add employee: " + e.getMessage());
        }
    }

    private static void viewAllEmployees() {
        List<Employee> all = payrollManager.getAllEmployees();
        if (all.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        System.out.println("\n--- Employee Records (sorted by gross salary, highest first) ---");
        for (Employee e : payrollManager.getEmployeesSortedByGrossSalary()) {
            System.out.println(e);
        }
    }

    private static void generateSinglePayslip() {
        int id = readInt("Enter Employee ID: ");
        Employee e = payrollManager.findById(id);
        if (e == null) {
            System.out.println("Employee not found.");
            return;
        }
        String month = readMonth();
        e.generatePayslip(month).printSlip();
    }

    private static void generateAllPayslips() {
        String month = readMonth();
        payrollManager.generatePayrollForAll(month);
    }

    private static void viewPayrollSummary() {
        String month = readMonth();
        payrollManager.printPayrollSummary(month);
    }

    private static void removeEmployeeFlow() {
        int id = readInt("Enter Employee ID to remove: ");
        boolean removed = payrollManager.removeEmployee(id);
        System.out.println(removed ? "Employee removed." : "Employee ID not found.");
    }

    private static void exportReportFlow() {
        String month = readMonth();
        System.out.print("Enter output file name (e.g. payroll_report.txt): ");
        String fileName = scanner.nextLine();
        payrollManager.exportPayrollReport(month, fileName);
    }

    // ---------------- Input helpers ----------------

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Double.parseDouble(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readMonth() {
        System.out.print("Enter pay period (e.g. September 2026): ");
        return scanner.nextLine();
    }

    private static Department readDepartment() {
        Department[] values = Department.values();
        System.out.println("Select Department:");
        for (int i = 0; i < values.length; i++) {
            System.out.printf("%d. %s%n", i + 1, values[i]);
        }
        int choice = readInt("Enter choice: ");
        if (choice < 1 || choice > values.length) {
            System.out.println("Invalid choice, defaulting to OPERATIONS.");
            return Department.OPERATIONS;
        }
        return values[choice - 1];
    }
}
