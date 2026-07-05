import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the Bank — holds all registered accounts and manages
 * lookups and inter-account transfers.
 */
public class Bank {

    // Keyed by User ID (used for login)
    private final Map<String, Account> accountsByUserId;

    // Keyed by Account ID (used for transfers - recipient lookup)
    private final Map<String, Account> accountsByAccountId;

    // Used to auto-generate a unique account ID for every new account created
    private int nextAccountNumber = 1001;

    private final DatabaseManager db;

    public Bank() {
        accountsByUserId = new HashMap<>();
        accountsByAccountId = new HashMap<>();
        db = new DatabaseManager();
        loadAccountsFromDatabase();
    }

    private void loadAccountsFromDatabase() {
        for (Account account : db.loadAllAccounts()) {
            addAccount(account);
        }
    }

    public boolean isEmpty() {
        return accountsByUserId.isEmpty();
    }

    public void addAccount(Account account) {
        accountsByUserId.put(account.getUserId(), account);
        accountsByAccountId.put(account.getAccountId(), account);

        // Keep the counter ahead of any manually pre-loaded demo account IDs
        String idNumberPart = account.getAccountId().replaceAll("[^0-9]", "");
        if (!idNumberPart.isEmpty()) {
            int idNumber = Integer.parseInt(idNumberPart);
            if (idNumber >= nextAccountNumber) {
                nextAccountNumber = idNumber + 1;
            }
        }
    }

    // Use this (instead of addAccount) whenever a BRAND NEW account is created,
    // so it also gets saved permanently to the database
    public void persistNewAccount(Account account) {
        addAccount(account);
        db.insertAccount(account);
    }

    // Call this right after any deposit/withdraw/transfer action on an account
    // to save its latest transaction + updated balance to the database
    public void persistTransaction(Account account) {
        List<Transaction> history = account.getTransactions();
        if (history.isEmpty()) {
            return;
        }
        Transaction latest = history.get(history.size() - 1);
        db.insertTransaction(account.getAccountId(), latest);
        db.updateBalance(account.getAccountId(), account.getBalance());
    }

    public Account findByUserId(String userId) {
        return accountsByUserId.get(userId);
    }

    public Account findByAccountId(String accountId) {
        return accountsByAccountId.get(accountId);
    }

    public boolean isUserIdTaken(String userId) {
        return accountsByUserId.containsKey(userId);
    }

    public String generateAccountId() {
        return "ACC" + (nextAccountNumber++);
    }

    /**
     * Transfers money from one account to another.
     * Returns a result message describing the outcome.
     */
    public String transfer(Account sender, String recipientAccountId, double amount) {
        if (recipientAccountId.equals(sender.getAccountId())) {
            return "ERROR: You cannot transfer money to your own account.";
        }

        Account recipient = findByAccountId(recipientAccountId);
        if (recipient == null) {
            return "ERROR: Recipient account not found.";
        }

        boolean success = sender.deductForTransfer(amount, recipientAccountId);
        if (!success) {
            return "INSUFFICIENT_FUNDS";
        }

        recipient.receiveTransfer(amount, sender.getAccountId());

        // Save both sides of the transfer to the database
        persistTransaction(sender);
        persistTransaction(recipient);

        return "SUCCESS";
    }
}
