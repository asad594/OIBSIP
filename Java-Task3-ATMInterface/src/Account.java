import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single bank account belonging to a user.
 * Holds balance, credentials, and a running list of transactions.
 */
public class Account {

    private final String accountId;
    private final String userId;
    private final String pin;
    private final String holderName;
    private double balance;
    private final List<Transaction> transactions;

    public Account(String accountId, String userId, String pin, String holderName, double initialBalance) {
        this.accountId = accountId;
        this.userId = userId;
        this.pin = pin;
        this.holderName = holderName;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    public boolean authenticate(String enteredPin) {
        return this.pin.equals(enteredPin);
    }

    public String getPin() {
        return pin;
    }

    // Used only when reloading a transaction that was already saved in the database (avoids re-logging it)
    public void addExistingTransaction(Transaction t) {
        transactions.add(t);
    }

    public String getAccountId() {
        return accountId;
    }

    public String getUserId() {
        return userId;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction("Deposit", amount, balance, "Cash deposited"));
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        transactions.add(new Transaction("Withdraw", amount, balance, "Cash withdrawn"));
        return true;
    }

    // Used internally by Bank when this account is the SENDER in a transfer
    public boolean deductForTransfer(double amount, String recipientAccountId) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        transactions.add(new Transaction("Transfer Out", amount, balance,
                "To Account: " + recipientAccountId));
        return true;
    }

    // Used internally by Bank when this account is the RECEIVER in a transfer
    public void receiveTransfer(double amount, String senderAccountId) {
        balance += amount;
        transactions.add(new Transaction("Transfer In", amount, balance,
                "From Account: " + senderAccountId));
    }
}
