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
            return false; 
            
        }
    }

    public long getBalance(long accountNumber) {
        try {
            MongoCollection<Document> accountsCollection = database.getCollection("accounts");

            Document query = new Document("account_number", accountNumber);
            Document account = accountsCollection.find(query).first();

            if (account != null) {
                long balance = account.getLong("balance");
                System.out.println("Your Account Balance: Rs." + balance);
                return balance;
            } else {
                System.out.println("Account not found!");
                return -1; 
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; 
        }
    }


    public long creditMoney(long accountNumber,long amount,String securityPin) {

        try {
            MongoCollection<Document> accountsCollection = database.getCollection("accounts");

            Document query = new Document("account_number", accountNumber)
                    .append("security_pin", securityPin);
            Document account = accountsCollection.find(query).first();

            if (account != null) {
                long newBalance = account.getLong("balance") + amount;
                accountsCollection.updateOne(query, new Document("$set", new Document("balance", newBalance)));

                return newBalance;
            } else {
                System.out.println("Invalid Security Pin!");
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long debitMoney(long accountNumber,long amount,String securityPin) {

        try {
            MongoCollection<Document> accountsCollection = database.getCollection("accounts");

            Document query = new Document("account_number", accountNumber)
                    .append("security_pin", securityPin);
            Document account = accountsCollection.find(query).first();

            if (account != null) {
                long currentBalance = account.getLong("balance");
                if (amount <= currentBalance) {
                    // Update the balance
                    long newBalance = currentBalance - amount;
                    accountsCollection.updateOne(query, new Document("$set", new Document("balance", newBalance)));

                    System.out.println("Rs." + amount + " debited Successfully");
                    return newBalance;
                } else {
                    System.out.println("Insufficient Balance!");
                    return 0;
                }
            } else {
                System.out.println("Invalid Pin!");
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public long transferMoney(long senderAccountNumber,long receiverAccountNumber,long amount,String securityPin) {

        try {
            MongoCollection<Document> accountsCollection = database.getCollection("accounts");

            Document senderQuery = new Document("account_number", senderAccountNumber)
                    .append("security_pin", securityPin);
            Document senderAccount = accountsCollection.find(senderQuery).first();

            if (senderAccount != null) {
                long senderBalance = senderAccount.getLong("balance");
                if (amount <= senderBalance) {
                    long newSenderBalance = senderBalance - amount;
                    accountsCollection.updateOne(senderQuery, new Document("$set", new Document("balance", newSenderBalance)));

                    Document receiverQuery = new Document("account_number", receiverAccountNumber);
                    Document receiverAccount = accountsCollection.find(receiverQuery).first();

                    if (receiverAccount != null) {
                        long receiverBalance = receiverAccount.getLong("balance");
                        long newReceiverBalance = receiverBalance + amount;
                        accountsCollection.updateOne(receiverQuery, new Document("$set", new Document("balance", newReceiverBalance)));

                        System.out.println("Transaction Successful!");
                        System.out.println("Rs." + amount + " Transferred Successfully");
                        return 1;
                    } else {
                        System.out.println("Receiver Account not found!");
                        return 0;
                    }
                } else {
                    System.out.println("Insufficient Balance!");
                    return 0;
                }
            }
        } catch (MongoException e) {
            e.printStackTrace(); 
            return 0; 
        }
        return 0;
    }
}