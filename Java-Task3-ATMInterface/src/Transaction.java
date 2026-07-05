import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single banking transaction (Deposit, Withdraw, Transfer, etc.)
 * Every transaction is logged with a timestamp and the balance after it occurred.
 */
public class Transaction {

    private final String type;
    private final double amount;
    private final double balanceAfter;
    private final String details;
    private final LocalDateTime timestamp;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Transaction(String type, double amount, double balanceAfter, String details) {
        this(type, amount, balanceAfter, details, LocalDateTime.now());
    }

    // Used when reconstructing a transaction loaded from the database (keeps original timestamp)
    public Transaction(String type, double amount, double balanceAfter, String details, LocalDateTime timestamp) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.details = details;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-10s  Amount: Rs. %-10.2f  Balance After: Rs. %-10.2f  %s",
                timestamp.format(FORMATTER), type, amount, balanceAfter, details);
    }
}
