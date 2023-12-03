import java.io.*;
import java.util.Scanner;
import java.awt.Toolkit;

class BankAccount implements Serializable {
    private String phone;
    private String password;
    private float balance;

    public BankAccount(String phone, String password) {
        this.phone = phone;
        this.password = password;
        this.balance = 0.00f;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}

public class BankingSystemApp {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opt;

        do {
            System.out.println("\nChoose an option");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Exit\n");

            System.out.print("Your choice:\t");
            opt = scanner.nextInt();

            switch (opt) {
                case 1:
                    registerAccount();
                    break;
                case 2:
                    loginToAccount();
                    break;
                case 3:
                    System.out.println("Exiting the Banking System. Goodbye!");
                    break;
                default:
                     clearScreen();
                    System.out.println("\nInvalid option");
            }
        } while (opt != 3);
         clearScreen();
        
    }

    private static void registerAccount() {
        clearScreen();
        System.out.print("Enter your phone number:\t");
        String phone = scanner.next();
        System.out.print("Enter your new password:\t");
        String password = scanner.next();

        BankAccount account = new BankAccount(phone, password);
        saveAccount(account);
        clearScreen();
        System.out.println("\nAccount successfully registered");
        
    }

    private static void loginToAccount() {
        clearScreen();
        System.out.print("\nPhone number:\t");
        String phone = scanner.next();
        System.out.print("Password:\t");
        String password = scanner.next();

        BankAccount account = loadAccount(phone);

        if (account != null && password.equals(account.getPassword())) {
            System.out.println("\n\t\tWelcome " + account.getPhone());
            handleServices(account);
        } else {
            clearScreen();
            System.out.println("\nInvalid phone number or password");
        }
    }

    private static void handleServices(BankAccount account) {
        char cont = 'y';

        while (Character.toLowerCase(cont) == 'y') {
            clearScreen();
            System.out.println("Services");
            System.out.println("1. Balance Inquiry");
            System.out.println("2. Cash In");
            System.out.println("3. Cash Withdrawal");
            System.out.println("4. Merchant Payment");
            System.out.println("5. Send Money");
            System.out.println("6. Change Password");
            System.out.println("7. Log Out\n");

            System.out.print("Your choice:\t");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\nYour current balance is Bdt/=" + account.getBalance());
                    break;
                case 2:
                    cashIn(account);
                    break;
                case 3:
                    cashWithdrawal(account);
                    break;
                case 4:
                    makeMerchantPayment(account);
                    break;
                case 5:
                    sendMoney(account);
                    break;
                case 6:
                    changePassword(account);
                    break;
                case 7:
                    clearScreen();
                    System.out.println("Logged out!");
                    return; 
                default:
                  
                    System.out.println("\nInvalid option");
            }

            System.out.print("\nDo you want to continue? [y/n]\t");
            cont = scanner.next().charAt(0);
        } 
        clearScreen();
    }
    private static void cashIn(BankAccount account) {
    System.out.print("\nEnter the amount:\t");
    float amount = scanner.nextFloat();
    account.setBalance(account.getBalance() + amount);
    saveAccount(account);
    System.out.println("\nSuccessfully cashed in Bdt/=" + amount + " and your current balance is Bdt/=" + account.getBalance());
    }

    private static void cashWithdrawal(BankAccount account) {
    System.out.print("\nEnter the amount:\t");
    float amount = scanner.nextFloat();

    if (amount > account.getBalance()) {
        System.out.println("\nInsufficient Balance");
    } else {
        account.setBalance(account.getBalance() - amount);
        saveAccount(account);
        System.out.println("\nYou have withdrawn Bdt/=" + amount + " and your current balance is Bdt/=" + account.getBalance());
    }
    }

    private static void makeMerchantPayment(BankAccount account) {
        System.out.print("\nPlease enter the merchant phone number to transfer the balance:\t");
        String merchantPhone = scanner.next();
        System.out.print("\nPlease enter the amount to transfer:\t");
        float amount = scanner.nextFloat();
        BankAccount merchantAccount = loadAccount(merchantPhone);

        if (merchantAccount != null) {
            if (amount > account.getBalance()) {
                beepAndPrint("\nInsufficient balance");
            } else {
                account.setBalance(account.getBalance() - amount);
                merchantAccount.setBalance(merchantAccount.getBalance() + amount);

                saveAccount(account);
                saveAccount(merchantAccount);

                System.out.println("\nYou have successfully paid Bdt/=" + amount + " to " + merchantPhone +
                        " and your current balance is Bdt/=" + account.getBalance());
            }
        } else {
            beepAndPrint("\nMerchant account not registered");
        }
    }

    private static void sendMoney(BankAccount account) {
        System.out.print("\nPlease enter the phone number to transfer the balance:\t");
        String receiverPhone = scanner.next();
        System.out.print("\nPlease enter the amount to transfer:\t");
        float amount = scanner.nextFloat();
        BankAccount receiverAccount = loadAccount(receiverPhone);

        if (receiverAccount != null) {
            if (amount > account.getBalance()) {
                beepAndPrint("\nInsufficient balance");
            } else {
                account.setBalance(account.getBalance() - amount);
                receiverAccount.setBalance(receiverAccount.getBalance() + amount);

                saveAccount(account);
                saveAccount(receiverAccount);

                System.out.println("\nYou have successfully transferred Bdt/=" + amount + " to " + receiverPhone +
                        " and your current balance is Bdt/=" + account.getBalance());
            }
        } else {
            beepAndPrint("\nReceiver account not registered");
        }
    }

    private static void changePassword(BankAccount account) {
        System.out.print("\nPlease enter your new password:\t");
        String newPassword = scanner.next();
        account.setPassword(newPassword);
        saveAccount(account);
        System.out.println("\nPassword successfully changed");
    }

    private static void beepAndPrint(String message) {
        System.out.println(message);
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void saveAccount(BankAccount account) {
        String filename = account.getPhone() + ".dat";

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            objectOutputStream.writeObject(account);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BankAccount loadAccount(String phone) {
        String filename = phone + ".dat";

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
            return (BankAccount) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}