import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all communication with the SQL Server database:
 * saving accounts, updating balances, logging transactions, and loading
 * everything back when the program restarts.
 */
public class DatabaseManager {

    // SQL Server Authentication -> username/password based login.
    // Change "localhost" if your SQL Server instance has a different name (e.g. localhost\SQLEXPRESS)
    private static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=ATM_DB;user=atm_user;password=Atm@12345;encrypt=true;trustServerCertificate=true;";

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public void insertAccount(Account account) {
        String sql = "INSERT INTO Accounts (AccountId, UserId, Pin, HolderName, Balance) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, account.getAccountId());
            ps.setString(2, account.getUserId());
            ps.setString(3, account.getPin());
            ps.setString(4, account.getHolderName());
            ps.setDouble(5, account.getBalance());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("⚠ Could not save account to database: " + e.getMessage());
        }
    }

    public void updateBalance(String accountId, double newBalance) {
        String sql = "UPDATE Accounts SET Balance = ? WHERE AccountId = ?";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setString(2, accountId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("⚠ Could not update balance in database: " + e.getMessage());
        }
    }

    public void insertTransaction(String accountId, Transaction t) {
        String sql = "INSERT INTO Transactions (AccountId, Type, Amount, BalanceAfter, Details, Timestamp) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountId);
            ps.setString(2, t.getType());
            ps.setDouble(3, t.getAmount());
            ps.setDouble(4, t.getBalanceAfter());
            ps.setString(5, t.getDetails());
            ps.setTimestamp(6, Timestamp.valueOf(t.getTimestamp()));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("⚠ Could not save transaction to database: " + e.getMessage());
        }
    }

    public List<Account> loadAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT AccountId, UserId, Pin, HolderName, Balance FROM Accounts";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Account account = new Account(
                        rs.getString("AccountId"),
                        rs.getString("UserId"),
                        rs.getString("Pin"),
                        rs.getString("HolderName"),
                        rs.getDouble("Balance")
                );

                for (Transaction t : loadTransactionsForAccount(account.getAccountId())) {
                    account.addExistingTransaction(t);
                }

                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println("⚠ Could not load accounts from database: " + e.getMessage());
        }
        return accounts;
    }

    public List<Transaction> loadTransactionsForAccount(String accountId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT Type, Amount, BalanceAfter, Details, Timestamp FROM Transactions WHERE AccountId = ? ORDER BY Id ASC";

        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction(
                            rs.getString("Type"),
                            rs.getDouble("Amount"),
                            rs.getDouble("BalanceAfter"),
                            rs.getString("Details"),
                            rs.getTimestamp("Timestamp").toLocalDateTime()
                    );
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠ Could not load transactions from database: " + e.getMessage());
        }
        return list;
    }
}