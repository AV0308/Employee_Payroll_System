/**
 * Centralises all statutory-deduction logic: progressive income-tax slabs,
 * Provident Fund (PF) and Professional Tax. Kept as a stateless utility class
 * (private constructor, static methods only) because tax rules are policy,
 * not per-employee data.
 *
 * NOTE: The slab values below are a simplified illustrative model loosely
 * based on typical Indian income-tax slabs, meant for demonstrating the
 * calculation logic -- adjust the constants to match your actual local
 * tax rules before using this in a real payroll system.
 */
public final class TaxCalculator {

    private static final double SLAB1_LIMIT = 250_000;   // 0%   up to this
    private static final double SLAB2_LIMIT = 500_000;   // 5%   in this band
    private static final double SLAB3_LIMIT = 1_000_000; // 20%  in this band, 30% above

    private static final double SLAB2_RATE = 0.05;
    private static final double SLAB3_RATE = 0.20;
    private static final double SLAB4_RATE = 0.30;
    private static final double CESS_RATE = 0.04; // health & education cess on the tax itself

    private static final double PF_PERCENT = 0.12;              // employee PF contribution
    private static final double PROFESSIONAL_TAX_MONTHLY = 200.0;

    private TaxCalculator() {
        // utility class: prevent instantiation
    }

    /** Computes progressive slab-based tax (plus cess) on an annual income figure. */
    public static double calculateAnnualIncomeTax(double annualIncome) {
        double tax;
        if (annualIncome <= SLAB1_LIMIT) {
            tax = 0;
        } else if (annualIncome <= SLAB2_LIMIT) {
            tax = (annualIncome - SLAB1_LIMIT) * SLAB2_RATE;
        } else if (annualIncome <= SLAB3_LIMIT) {
            tax = (SLAB2_LIMIT - SLAB1_LIMIT) * SLAB2_RATE
                    + (annualIncome - SLAB2_LIMIT) * SLAB3_RATE;
        } else {
            tax = (SLAB2_LIMIT - SLAB1_LIMIT) * SLAB2_RATE
                    + (SLAB3_LIMIT - SLAB2_LIMIT) * SLAB3_RATE
                    + (annualIncome - SLAB3_LIMIT) * SLAB4_RATE;
        }
        tax += tax * CESS_RATE;
        return tax;
    }

    /** Projects the monthly gross salary to an annual figure, then divides the annual tax by 12. */
    public static double calculateMonthlyIncomeTax(double monthlyGrossSalary) {
        double projectedAnnualIncome = monthlyGrossSalary * 12;
        return calculateAnnualIncomeTax(projectedAnnualIncome) / 12;
    }

    public static double calculateProvidentFund(double basicSalary) {
        return basicSalary * PF_PERCENT;
    }

    public static double getProfessionalTax() {
        return PROFESSIONAL_TAX_MONTHLY;
    }
}
