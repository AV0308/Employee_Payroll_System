/**
 * Contract for anything that can produce a salary slip for a given pay period.
 * Kept as a separate interface (rather than baking it into Employee) so other
 * kinds of payees -- e.g. a future Contractor or Vendor class -- could
 * implement it too, without being forced into the Employee hierarchy.
 */
public interface Payable {
    SalarySlip generatePayslip(String payPeriod);
}
