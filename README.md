# Employee Payroll System (Java)

A console-based payroll system built with core + advanced Java OOP concepts.

## How to Compile & Run

```bash
javac *.java
java Main
```

Requires JDK 17+ (uses `String.repeat()`, arrow-style `switch`, and pattern
matching for `instanceof`).

## Project Structure

| File | Purpose |
|---|---|
| `Employee.java` | Abstract base class — shared fields, encapsulated getters/setters, abstract salary methods |
| `FullTimeEmployee.java` | Salaried staff: basic pay + HRA/DA/medical/conveyance allowances |
| `Manager.java` | Extends `FullTimeEmployee` — adds bonus + per-head leadership allowance |
| `PartTimeEmployee.java` | Hourly pay with 1.5x overtime beyond 160 hrs/month |
| `Department.java` / `EmployeeStatus.java` | Enums for fixed-value fields |
| `Payable.java` | Interface: anything that can `generatePayslip()` |
| `InvalidSalaryException.java` | Custom checked exception for bad financial input |
| `TaxCalculator.java` | Static utility: progressive income-tax slabs, PF, professional tax |
| `SalarySlip.java` | Immutable computed payslip for one employee/one pay period |
| `PayrollManager.java` | Stores all employee records; runs payroll; save/load via serialization; export to text file |
| `Main.java` | Console menu tying everything together |

## OOP Concepts Demonstrated

- **Abstraction** — `Employee` declares `calculateGrossSalary()`, `calculateAllowances()`,
  `getEmployeeType()` as abstract; each subtype fills in its own logic.
- **Inheritance** — `Employee` → `FullTimeEmployee` → `Manager` (multi-level),
  and `Employee` → `PartTimeEmployee`.
- **Polymorphism** — `PayrollManager` and `SalarySlip` operate on `Employee`
  references, but the correct overridden salary logic always runs
  (e.g. `Collections.sort()` uses each employee's own `calculateGrossSalary()`
  via `compareTo()`).
- **Encapsulation** — all fields are `private`/`protected`; access only via
  getters/setters, with setters validating input.
- **Interfaces** — `Payable` (`generatePayslip`) and `Comparable<Employee>`.
- **Enums** — `Department`, `EmployeeStatus`.
- **Custom exceptions** — `InvalidSalaryException` is thrown/caught around
  every place financial data is set.
- **Collections & data processing** — `ArrayList`, `Collections.sort`,
  `removeIf`, stream-free filtering by name/ID.
- **File I/O / persistence** — records saved/loaded with `ObjectOutputStream`
  / `ObjectInputStream`; payroll reports exported as a plain `.txt` file.

## Salary & Tax Model (simplified, for demonstration)

- **Full-Time**: Basic + HRA (20%) + DA (15%) + fixed Medical (₹1,250) + fixed
  Conveyance (₹1,600).
- **Manager**: Full-Time allowances + special bonus + ₹500/team member.
- **Part-Time**: hourly rate × hours, with 1.5× overtime multiplier beyond
  160 hours/month.
- **Deductions**: progressive income-tax slabs (0% / 5% / 20% / 30%) plus a
  4% cess on the tax, 12% Provident Fund on basic salary, and a flat monthly
  professional tax (₹200).

These numbers are illustrative — adjust the constants in `TaxCalculator`,
`FullTimeEmployee`, `Manager`, and `PartTimeEmployee` to match real
company/tax-jurisdiction rules.

## Using the Menu

1. **Add Employee** — choose Full-Time / Manager / Part-Time and enter details.
2. **View All Employees** — lists records sorted by gross salary.
3. **Generate Payslip for One Employee** — enter an ID and pay period.
4. **Generate Payroll for All Employees** — prints every payslip.
5. **View Payroll Summary** — one-line-per-employee table with totals.
6. **Remove Employee** — delete a record by ID.
7. **Save Records to File** — persists all records to `employee_records.dat`.
8. **Load Records from File** — restores records from that file.
9. **Export Monthly Payroll Report** — writes all payslips to a `.txt` file.
0. **Exit**
