package ledger.input;

public class PaymentInput {
    private final String bankName;
    private final String borrowerName;
    private final long amount;
    private final int emiNumber;

    public PaymentInput(String bankName, String borrowerName, long amount, int emiNumber) {
        this.bankName = bankName;
        this.borrowerName = borrowerName;
        this.amount = amount;
        this.emiNumber = emiNumber;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public String getBankName() {
        return bankName;
    }

    public int getEmiNumber() {
        return emiNumber;
    }

    public long getAmount() {
        return amount;
    }
}
