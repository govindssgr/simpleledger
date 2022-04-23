package ledger.dto;

public class Balance {
    private final long amount;
    private final int numberOfEmis;

    public Balance(long amount, int numberOfEmis) {
        this.amount = amount;
        this.numberOfEmis = numberOfEmis;
    }

    public long getAmount() {
        return amount;
    }

    public int getNumberOfEmis() {
        return numberOfEmis;
    }
}
