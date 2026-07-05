import java.util.List;
import java.util.Scanner;

/**
 * Handles the ATM's user-facing flow: login, main menu, and each transaction type.
 */
public class ATM {

    private static final int MAX_LOGIN_ATTEMPTS = 3;

    private final Bank bank;
    private final Scanner scanner;
    private Account currentAccount;

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printWelcomeBanner();

        boolean exitProgram = false;

        while (!exitProgram) {
            System.out.println("\n1. Login");
            System.out.println("2. Create New Account");
            System.out.println("3. Exit");
            System.out.print("Choose an option (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (login()) {
                        runMainMenu();
                    } else {
                        System.out.println("\n❌ Too many incorrect attempts. Access denied.");
                        System.out.println("Please contact your bank branch for assistance.");
                    }
                    break;
                case "2":
                    createNewAccount();
                    break;
                case "3":
                    exitProgram = true;
                    System.out.println("\nThank you for visiting. Goodbye!\n");
                    break;
                default:
                    System.out.println("⚠ Invalid option. Please choose between 1 and 3.");
            }
        }

        scanner.close();
    }

    private void printWelcomeBanner() {
        System.out.println("==================================================");
        System.out.println("        WELCOME TO OIBSIP JAVA ATM SIMULATOR");
        System.out.println("==================================================");
    }

    private void createNewAccount() {
        System.out.println("\n------------- CREATE NEW ACCOUNT -------------");

        System.out.print("Enter your full name: ");
        String name = scanner.nextLine().trim();

        String userId;
        while (true) {
            System.out.print("Choose a User ID: ");
            userId = scanner.nextLine().trim();

            if (userId.isEmpty()) {
                System.out.println("⚠ User ID cannot be empty.");
            } else if (bank.isUserIdTaken(userId)) {
                System.out.println("⚠ This User ID is already taken. Try another.");
            } else {
                break;
            }
        }

        String pin;
        while (true) {
            System.out.print("Set a 4-digit PIN: ");
            pin = scanner.nextLine().trim();

            if (pin.length() == 4 && pin.chars().allMatch(Character::isDigit)) {
                break;
            }
            System.out.println("⚠ PIN must be exactly 4 digits.");
        }

        System.out.print("Enter initial deposit amount (Rs., 0 allowed): ");
        double initialDeposit = readNonNegativeAmount();

        String newAccountId = bank.generateAccountId();
        Account newAccount = new Account(newAccountId, userId, pin, name, initialDeposit);
        bank.persistNewAccount(newAccount);

        System.out.println("\n✅ Account created successfully!");
        System.out.println("Your Account ID: " + newAccountId);
        System.out.println("Please remember your User ID and PIN to log in.");
    }

    private boolean login() {
        for (int attempt = 1; attempt <= MAX_LOGIN_ATTEMPTS; attempt++) {
            System.out.print("\nEnter User ID: ");
            String userId = scanner.nextLine().trim();

            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine().trim();

            Account account = bank.findByUserId(userId);

            if (account != null && account.authenticate(pin)) {
                currentAccount = account;
                System.out.println("\n✅ Login successful! Welcome, " + account.getHolderName() + ".");
                return true;
            } else {
                int remaining = MAX_LOGIN_ATTEMPTS - attempt;
                System.out.println("❌ Incorrect User ID or PIN.");
                if (remaining > 0) {
                    System.out.println("Attempts remaining: " + remaining);
                }
            }
        }
        return false;
    }

    private void runMainMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n---------------- MAIN MENU ----------------");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Check Balance");
            System.out.println("6. Quit");
            System.out.println("--------------------------------------------");
            System.out.print("Choose an option (1-6): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showTransactionHistory();
                    break;
                case "2":
                    handleWithdraw();
                    break;
                case "3":
                    handleDeposit();
                    break;
                case "4":
                    handleTransfer();
                    break;
                case "5":
                    showBalance();
                    break;
                case "6":
                    running = false;
                    printGoodbye();
                    break;
                default:
                    System.out.println("⚠ Invalid option. Please choose between 1 and 6.");
            }
        }
    }

    private void showBalance() {
        System.out.println("\n---------------- ACCOUNT BALANCE ----------------");
        System.out.println("Account Holder : " + currentAccount.getHolderName());
        System.out.println("Account ID     : " + currentAccount.getAccountId());
        System.out.printf("Current Balance: Rs. %.2f%n", currentAccount.getBalance());
        System.out.println("--------------------------------------------------");
    }

    private void showTransactionHistory() {
        List<Transaction> history = currentAccount.getTransactions();
        System.out.println("\n------------- TRANSACTION HISTORY -----------");

        if (history.isEmpty()) {
            System.out.println("No transactions yet in this session.");
        } else {
            for (Transaction t : history) {
                System.out.println(t);
            }
        }
        System.out.println("----------------------------------------------");
    }

    private void handleWithdraw() {
        System.out.print("\nEnter amount to withdraw: Rs. ");
        double amount = readAmount();
        if (amount <= 0) return;

        boolean success = currentAccount.withdraw(amount);
        if (success) {
            bank.persistTransaction(currentAccount);
            System.out.printf("✅ Withdrawal successful. New Balance: Rs. %.2f%n", currentAccount.getBalance());
        } else {
            System.out.println("❌ Insufficient Funds.");
        }
    }

    private void handleDeposit() {
        System.out.print("\nEnter amount to deposit: Rs. ");
        double amount = readAmount();
        if (amount <= 0) return;

        currentAccount.deposit(amount);
        bank.persistTransaction(currentAccount);
        System.out.printf("✅ Deposit successful. New Balance: Rs. %.2f%n", currentAccount.getBalance());
    }

    private void handleTransfer() {
        System.out.print("\nEnter recipient Account ID: ");
        String recipientId = scanner.nextLine().trim();

        System.out.print("Enter amount to transfer: Rs. ");
        double amount = readAmount();
        if (amount <= 0) return;

        String result = bank.transfer(currentAccount, recipientId, amount);

        switch (result) {
            case "SUCCESS":
                System.out.printf("✅ Transfer successful. New Balance: Rs. %.2f%n", currentAccount.getBalance());
                break;
            case "INSUFFICIENT_FUNDS":
                System.out.println("❌ Insufficient Funds.");
                break;
            default:
                System.out.println(result); // Prints specific error message (e.g. recipient not found)
        }
    }

    private double readNonNegativeAmount() {
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            if (amount < 0) {
                System.out.println("⚠ Amount cannot be negative. Setting initial balance to 0.");
                return 0;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("⚠ Invalid amount entered. Setting initial balance to 0.");
            return 0;
        }
    }

    private double readAmount() {
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            if (amount <= 0) {
                System.out.println("⚠ Amount must be greater than zero.");
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("⚠ Invalid amount entered.");
            return -1;
        }
    }

    private void printGoodbye() {
        System.out.println("\n==================================================");
        System.out.println("   Thank you for using OIBSIP Java ATM Simulator");
        System.out.println("                 Goodbye, " + currentAccount.getHolderName() + "!");
        System.out.println("==================================================\n");
    }
}