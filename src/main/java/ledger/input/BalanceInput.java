package ledger.input;

public class BalanceInput {
    private String bankName;
    private String borrowerName;
    private int emiNumber;

    public BalanceInput(String bankName, String borrowerName, int emiNumber) {
        this.bankName = bankName;
        this.borrowerName = borrowerName;
        this.emiNumber = emiNumber;
    }

    public int getEmiNumber() {
        return emiNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }
}
