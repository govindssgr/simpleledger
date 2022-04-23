package ledger;

import ledger.dto.Balance;
import ledger.input.BalanceInput;
import ledger.input.LoanInput;
import ledger.input.PaymentInput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The Challenge https://codu.ai/coding-problem/the%20ledger%20co
 * <p>
 * Assumptions:
 * 1) The question doesn't clearly mention if one borrower is allowed to take multiple loans from the same bank
 * From the examples given if seems it shouldn't be allowed. Both parallel and sequential loans are not supported
 * But the same borrower can take loans from different banks.
 * 2) Assuming borrower's name as identifier of the borrower and no two borrower of the same name exist
 * 3) Name of the bank not the borrower include SPACE
 */
public class LedgerApplication {

    public static void main(String[] args) throws IOException {

        String fileName = args[0];

        Map<String, LedgerEntity> ledgerEntityMap = new HashMap<>();

        List<LoanInput> loanInputs = new LinkedList<>();
        List<PaymentInput> paymentInputs = new LinkedList<>();
        List<BalanceInput> balanceInputs = new LinkedList<>();

        readInputs(fileName, loanInputs, paymentInputs, balanceInputs);
        createEntries(ledgerEntityMap, loanInputs);
        addPayments(ledgerEntityMap, paymentInputs);
        printBalance(ledgerEntityMap, balanceInputs);
    }

    private static void printBalance(Map<String, LedgerEntity> ledgerEntityMap, List<BalanceInput> balanceInputs) {
        for (BalanceInput input : balanceInputs) {
            String key = key(input.getBankName(), input.getBorrowerName());
            if (!ledgerEntityMap.containsKey(key)) {
                throw new RuntimeException(String.format("Processing BALANCE. Loan doesn't exist for <bank, borrower> pair <%1$s, %2$s>",
                        input.getBankName(), input.getBorrowerName()));
            }
            Balance balance = ledgerEntityMap.get(key).balance(input.getEmiNumber());
            System.out.printf("%1$s %2$s %3$s %4$s%n",
                    input.getBankName(),
                    input.getBorrowerName(),
                    balance.getAmount(),
                    balance.getNumberOfEmis());
        }
    }

    private static void addPayments(Map<String, LedgerEntity> ledgerEntityMap, List<PaymentInput> paymentInputs) {
        for (PaymentInput input : paymentInputs) {
            String key = key(input.getBankName(), input.getBorrowerName());
            if (!ledgerEntityMap.containsKey(key)) {
                throw new RuntimeException(String.format("Processing PAYMENT. Loan doesn't exist for <bank, borrower> pair <%1$s, %2$s>",
                        input.getBankName(), input.getBorrowerName()));
            }
            ledgerEntityMap.get(key).addPayment(input.getAmount(), input.getEmiNumber());
        }
    }

    private static void createEntries(Map<String, LedgerEntity> ledgerEntityMap, List<LoanInput> loanInputs) {
        for (LoanInput input : loanInputs) {
            String key = key(input.getBankName(), input.getBorrowerName());
            if (ledgerEntityMap.containsKey(key)) {
                throw new RuntimeException(String.format("Processing LOAN. Loan already exist for <bank, borrower> pair <%1$s, %2$s>",
                        input.getBankName(), input.getBorrowerName()));
            }
            ledgerEntityMap.put(key, new LedgerEntity(input.getPrincipal(), input.getYears(), input.getInterestRate()));
        }
    }

    private static String key(String bankName, String borrowerName) {
        return String.join("_", bankName, borrowerName);
    }

    private static void readInputs(String fileName, List<LoanInput> loanInputs,
                                   List<PaymentInput> paymentInputs, List<BalanceInput> balanceInputs) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.lines().forEach(line -> readLine(line, loanInputs, paymentInputs, balanceInputs));
        }
    }

    private static void readLine(String line, List<LoanInput> loanInputs, List<PaymentInput> paymentInputs, List<BalanceInput> balanceInputs) {
        String[] words = line.split(" ");
        switch (words[0]) {
            case "LOAN":
                loanInputs.add(new LoanInput(words[1], words[2], Long.parseLong(words[3]),
                        Integer.parseInt(words[4]), Float.parseFloat(words[5])));
                break;
            case "PAYMENT":
                paymentInputs.add(new PaymentInput(words[1], words[2], Long.parseLong(words[3]), Integer.parseInt(words[4])));
                break;
            case "BALANCE":
                balanceInputs.add(new BalanceInput(words[1], words[2], Integer.parseInt(words[3])));
                break;
        }
        // Not expecting other than LOAN, PAYMENT (or) BALANCE as first word.
    }

}
