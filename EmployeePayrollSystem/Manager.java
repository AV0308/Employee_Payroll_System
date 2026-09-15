/**
 * A Manager is a FullTimeEmployee that additionally earns a special
 * discretionary bonus and a per-head leadership allowance for their team.
 * Demonstrates multi-level inheritance (Employee -> FullTimeEmployee -> Manager)
 * and overriding a method that itself calls super's implementation.
 */
public class Manager extends FullTimeEmployee {

    private static final double LEADERSHIP_ALLOWANCE_PER_MEMBER = 500.0;

    private double specialBonus;
    private int teamSize;

    public Manager(String name, Department department, String designation, double basicSalary,
                   double specialBonus, int teamSize) throws InvalidSalaryException {
        super(name, department, designation, basicSalary);
        this.specialBonus = specialBonus;
        this.teamSize = teamSize;
    }

    @Override
    public double calculateAllowances() {
        double leadershipAllowance = teamSize * LEADERSHIP_ALLOWANCE_PER_MEMBER;
        return super.calculateAllowances() + specialBonus + leadershipAllowance;
    }

    @Override
    public String getEmployeeType() {
        return "Manager";
    }

    public double getSpecialBonus() { return specialBonus; }
    public void setSpecialBonus(double specialBonus) { this.specialBonus = specialBonus; }

    public int getTeamSize() { return teamSize; }
    public void setTeamSize(int teamSize) { this.teamSize = teamSize; }
}
