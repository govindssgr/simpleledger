package ledger;

import ledger.dto.Balance;

public class LedgerEntity {
    private long principal;
    private int years;
    private float interestRate;
    // Not keeping amountPaid as double
    // https://stackoverflow.com/questions/3730019/why-not-use-double-or-float-to-represent-currency
    // Since this problem demands ceiling whole number, storing the value as it is.
    // But, if we need to store in precision of 2 digits,we'll have to store value * 100
    private final long amountToBePaid;
    private final long emi;
    private final PaymentTree paymentTree;

    public LedgerEntity(long principal, int years, float interestRate) {
        this.principal = principal;
        this.years = years;
        this.interestRate = interestRate;
        // The final emi can also be ceiled. Hence, storing amountToBePaid as ceiled value
        amountToBePaid = principal + (long) Math.ceil(principal * years * (double) interestRate / 100);

        int numberOfInstallments = (years * 12);

        if (amountToBePaid % numberOfInstallments == 0L) {
            emi = amountToBePaid / numberOfInstallments;
        } else {
            emi = amountToBePaid / numberOfInstallments + 1;
        }
        paymentTree = new PaymentTree(numberOfInstallments, amountToBePaid);
    }

    public void addPayment(long amount, int emiNumber) {
        paymentTree.add(emiNumber, amount);
    }

    public Balance balance(int emiNumber) {
        long amountPaid = paymentTree.amountPaid(emiNumber);
        long balance = amountToBePaid - amountPaid;

        final int pendingEmis;
        if (balance % emi == 0L) {
            pendingEmis = (int) (balance / emi);
        } else {
            pendingEmis = (int) (balance / emi) + 1;
        }
        return new Balance(amountPaid, pendingEmis);
    }
}
