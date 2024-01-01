package BankingManagementSystem;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Scanner;

public class User {
    private MongoDatabase database;
    private Scanner scanner;

    public User(MongoDatabase database, Scanner scanner) {
        this.database = database;
        this.scanner = scanner;
    }

    public void register() {
        scanner.nextLine();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (userExists(email)) {
            System.out.println("User Already Exists for this Email Address!!");
            return;
        }

        try {
            MongoCollection<Document> usersCollection = database.getCollection("users");

            // Create a new document for user registration
            Document userDocument = new Document("full_name", fullName)
                    .append("email", email)
                    .append("password", password);

            // Insert the document into the "users" collection
            usersCollection.insertOne(userDocument);

            System.out.println("Registration Successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String login() {
        scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            MongoCollection<Document> usersCollection = database.getCollection("users");

            // Check if the user exists in the "users" collection
            Document query = new Document("email", email)
                    .append("password", password);
            Document userDocument = usersCollection.find(query).first();

            if (userDocument != null) {
                return email;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean userExists(String email) {
        try {
            MongoCollection<Document> usersCollection = database.getCollection("users");

            // Check if the user exists in the "users" collection
            Document query = new Document("email", email);
            Document userDocument = usersCollection.find(query).first();

            return userDocument != null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
