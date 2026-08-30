import java.util.*;

class Transaction {
    private final String type;
    private final double amount;
    private final String timestamp;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @Override
    public String toString() {
        return String.format("[%s] %-10s : Rs.%.2f", timestamp, type, amount);
    }
}

class BankAccount {
    private final String accountNumber;
    private final String accountHolder;
    private double balance;
    private final List<Transaction> transactionHistory;

    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        addTransaction("INITIAL DEP", initialBalance);
    }

    public String getAccountHolder() { return accountHolder; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction("DEPOSIT", amount);
            System.out.printf("\u2705 Success: Rs.%.2f deposited successfully.\n", amount);
        } else {
            System.out.println("[X] Error: Invalid deposit amount.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("[X] Error: Invalid withdrawal amount.");
            return false;
        }
        if (amount > balance) {
            System.out.println("[X] Error: Insufficient funds inside the account.");
            return false;
        }
        balance -= amount;
        addTransaction("WITHDRAWAL", amount);
        System.out.printf("\u2705 Success: Rs.%.2f withdrawn successfully.\n", amount);
        return true;
    }

    public void displayHistory() {
        System.out.println("\n======= TRANSACTION HISTORY =======");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction t : transactionHistory) {
                System.out.println(t);
            }
        }
        System.out.println("===================================");
    }

    private void addTransaction(String type, double amount) {
        transactionHistory.add(new Transaction(type, amount));
    }
}

public class ProfessionalATM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=========================================");
        System.out.println("       WELCOME TO APEX DIGITAL BANK      ");
        System.out.println("=========================================");
        System.out.print("Please enter your Full Name to log in: ");
        String holderName= scanner.nextLine();

         System.out.print("Please enter your Bank Account Number: ");
         String accountNumber= scanner.nextLine();


        System.out.print("Set your opening Account Balance : ");
        double initialBalance = getValidDouble(scanner);
        scanner.nextLine();
        BankAccount userAccount = new BankAccount(accountNumber, holderName, initialBalance);
         System.out.println("\n----------------------------");
         System.out.println("Account Configured Successfully!");
         System.out.println("Account Holder: " + userAccount.getAccountHolder());
         System.out.println("---------------------------------");

        System.out.println("Hello, " + userAccount.getAccountHolder() + "!");

        int choice;
        do {
            System.out.println("\n::: ATM MAIN MENU :::");
            System.out.println("1. Check Account Balance");
            System.out.println("2. Deposit Funds");
            System.out.println("3. Withdraw Cash");
            System.out.println("4. Mini Statement (History)");
            System.out.println("5. Exit System");
            System.out.print("Please enter your choice (1-5): ");
            
            while (!scanner.hasNextInt()) {
                System.out.print("[X] Invalid input. Choose a number between 1-5: ");
                scanner.next();
            }
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> System.out.printf("➡ Current Available Balance: Rs.%.2f\n", userAccount.getBalance());
                case 2 -> {
                    System.out.print("Enter amount to deposit: Rs.");
                    double depAmt = getValidDouble(scanner);
                    userAccount.deposit(depAmt);
                }
                case 3 -> {
                    System.out.print("Enter amount to withdraw: Rs.");
                    double witAmt = getValidDouble(scanner);
                    userAccount.withdraw(witAmt);
                }
                case 4 -> userAccount.displayHistory();
                case 5 -> System.out.println("\nThank you for banking with Apex Digital Bank. Have a nice day!");
                default -> System.out.println("[X] Invalid choice. Please try again.");
            }
        } while (choice != 5);
        scanner.close();
    }

    private static double getValidDouble(Scanner scanner) {
        while (!scanner.hasNextDouble()) {
            System.out.print( "[X] Invalid amount format. Enter again: Rs");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}