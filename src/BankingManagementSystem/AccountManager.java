package BankingManagementSystem;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.Scanner;

public class AccountManager {
    private MongoDatabase database;
    private Scanner scanner;

    public AccountManager(MongoDatabase database, Scanner scanner) {
        this.database = database;
        this.scanner = scanner;
    }

    public boolean accountExists(String email) {
        try {
            MongoCollection<Document> usersCollection = database.getCollection("User");

            Document query = new Document("email", email);
            long count = usersCollection.count(query);

            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Exception while checking account existence: " + e.getMessage());
            return false; // Return false to indicate failure
        }
    }

    public double getBalance(long accountNumber) {
        try {
            MongoCollection<Document> accountsCollection = database.getCollection("accounts");

            Document query = new Document("account_number", accountNumber);
            Document account = accountsCollection.find(query).first();

            if (account != null) {
                double balance = account.getDouble("balance");
                System.out.println("Your Account Balance: Rs." + balance);
                return balance;
            } else {
                System.out.println("Account not found!");
                return -1; // Return a negative value to indicate failure
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Return a negative value to indicate failure
        }
    }


    public void creditMoney(long accountNumber) {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String securityPin = scanner.nextLine();

        try {
            MongoCollection<Document> accountsCollection = database.getCollection("accounts");

            // Check if account exists and security pin is valid
            Document query = new Document("account_number", accountNumber)
                    .append("security_pin", securityPin);
            Document account = accountsCollection.find(query).first();

            if (account != null) {
                // Update the balance
                double newBalance = account.getDouble("balance") + amount;
                accountsCollection.updateOne(query, new Document("$set", new Document("balance", newBalance)));

                System.out.println("Rs." + amount + " credited Successfully");
            } else {
                System.out.println("Invalid Security Pin!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void debitMoney(long accountNumber) {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String securityPin = scanner.nextLine();

        try {
            MongoCollection<Document> accountsCollection = database.getCollection("accounts");

            // Check if account exists and security pin is valid
            Document query = new Document("account_number", accountNumber)
                    .append("security_pin", securityPin);
            Document account = accountsCollection.find(query).first();

            if (account != null) {
                double currentBalance = account.getDouble("balance");
                if (amount <= currentBalance) {
                    // Update the balance
                    double newBalance = currentBalance - amount;
                    accountsCollection.updateOne(query, new Document("$set", new Document("balance", newBalance)));

                    System.out.println("Rs." + amount + " debited Successfully");
                } else {
                    System.out.println("Insufficient Balance!");
                }
            } else {
                System.out.println("Invalid Pin!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void transferMoney(long senderAccountNumber) {
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiverAccountNumber = scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String securityPin = scanner.nextLine();

        try {
            MongoCollection<Document> accountsCollection = database.getCollection("accounts");

            // Check if sender account exists and security pin is valid
            Document senderQuery = new Document("account_number", senderAccountNumber)
                    .append("security_pin", securityPin);
            Document senderAccount = accountsCollection.find(senderQuery).first();

            if (senderAccount != null) {
                double senderBalance = senderAccount.getDouble("balance");
                if (amount <= senderBalance) {
                    // Update sender's balance
                    double newSenderBalance = senderBalance - amount;
                    accountsCollection.updateOne(senderQuery, new Document("$set", new Document("balance", newSenderBalance)));

                    // Update receiver's balance
                    Document receiverQuery = new Document("account_number", receiverAccountNumber);
                    Document receiverAccount = accountsCollection.find(receiverQuery).first();

                    if (receiverAccount != null) {
                        double receiverBalance = receiverAccount.getDouble("balance");
                        double newReceiverBalance = receiverBalance + amount;
                        accountsCollection.updateOne(receiverQuery, new Document("$set", new Document("balance", newReceiverBalance)));

                        System.out.println("Transaction Successful!");
                        System.out.println("Rs." + amount + " Transferred Successfully");
                    } else {
                        System.out.println("Receiver Account not found!");
                    }
                } else {
                    System.out.println("Insufficient Balance!");
                }
            }
        } catch (MongoException e) {
            e.printStackTrace();  // Handle the exception appropriately
        }
    }
}