/**
 * Entry point for the ATM Interface simulation.
 * Sets up the Bank with demo accounts, then launches the ATM.
 *
 * Demo Login Credentials:
 *   User ID: asad594   | PIN: 1234   | Account ID: ACC1001
 *   User ID: sara_khan | PIN: 5678   | Account ID: ACC1002
 */
public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();

        // Only seed demo accounts the very first time (i.e. database is empty).
        // On every later run, accounts are loaded back from SQL Server automatically.
        if (bank.isEmpty()) {
            bank.persistNewAccount(new Account("ACC1001", "asad594", "1234", "Muhammad Asad", 5000.00));
            bank.persistNewAccount(new Account("ACC1002", "sara_khan", "5678", "Sara Khan", 3000.00));
        }

        ATM atm = new ATM(bank);
        atm.start();
    }
}
