package ledger.input;

public class LoanInput {
    private String bankName;
    private String borrowerName;
    private long principal;
    private int years;
    private float interestRate;

    public LoanInput(String bankName, String borrowerName, long principal, int years, float interestRate) {
        this.bankName = bankName;
        this.borrowerName = borrowerName;
        this.interestRate = interestRate;
        this.principal = principal;
        this.years = years;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public int getYears() {
        return years;
    }

    public long getPrincipal() {
        return principal;
    }
}
