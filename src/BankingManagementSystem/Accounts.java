package BankingManagementSystem;

    import com.mongodb.client.MongoCollection;
    import com.mongodb.client.MongoCursor;
    import com.mongodb.client.MongoDatabase;
    import com.mongodb.client.model.Filters;
    import org.bson.Document;

    import java.util.Scanner;

    public class Accounts {
        private MongoDatabase database;
        private Scanner scanner;

        public Accounts(MongoDatabase database, Scanner scanner) {
            this.database = database;
            this.scanner = scanner;
        }

        public long openAccount(String email,String fullName,long balance,String securityPin) {
            if (!accountExist(email)) {

                try {
                    long accountNumber = generateAccountNumber();

                    MongoCollection<Document> accountsCollection = database.getCollection("accounts");

                    // Create a new document for account creation
                    Document accountDocument = new Document("account_number", accountNumber)
                            .append("full_name", fullName)
                            .append("email", email)
                            .append("balance", balance)
                            .append("security_pin", securityPin);

                    accountsCollection.insertOne(accountDocument);

                    return accountNumber;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            throw new RuntimeException("Account Already Exists");
        }
        public long getAccountNumber(String email) {
            try {
                MongoCollection<Document> accountsCollection = database.getCollection("accounts");

                Document query = new Document("email", email);
                Document accountDocument = accountsCollection.find(query).first();

                if (accountDocument != null) {
                    return accountDocument.getLong("account_number");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw new RuntimeException("Account Number Doesn't Exist!");
        }

        private long generateAccountNumber() {
            try {
                MongoCollection<Document> accountsCollection = database.getCollection("accounts");

                Document sort = new Document("account_number", -1);
                Document latestAccount = accountsCollection.find().sort(sort).limit(1).first();

                if (latestAccount != null) {
                    return latestAccount.getLong("account_number") + 1;
                } else {
                    return 10000100;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 10000100;
        }

        public boolean accountExist(String email) {
            try {
                MongoCollection<Document> usersCollection = database.getCollection("Users");

                Document query = new Document("email", email);
                long count = usersCollection.count(query);

                return count > 0;
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Exception while checking account existence: " + e.getMessage());
                return false; 
            }
        }

        public void fetchUserAccounts(String userEmail) {
            MongoCollection<Document> accountsCollection = database.getCollection("accounts");

            MongoCursor<Document> cursor = accountsCollection.find(Filters.eq("email", userEmail)).iterator();

            System.out.println("Existing Accounts for User: " + userEmail);
            while (cursor.hasNext()) {
                Document account = cursor.next();
                System.out.println("Account Number: " + account.getLong("account_number"));
            }
        }

    }