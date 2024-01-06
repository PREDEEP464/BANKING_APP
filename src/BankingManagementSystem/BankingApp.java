package BankingManagementSystem;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.Scanner;

public class BankingApp extends Application {
    private static final String mongoDbUrl = "mongodb://localhost:27017";
    private static final String databaseName = "banking_system";

    private static final MongoClient mongoClient = MongoClients.create(mongoDbUrl);
    private static final MongoDatabase database = mongoClient.getDatabase(databaseName);

    private static final Scanner scanner = new Scanner(System.in);
    private Stage dashboardStage;

    public static void main(String[] args) {
        try{
        launch(args);
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            mongoClient.close();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("ICON.png"));
            primaryStage.getIcons().add(icon);
            User user = new User(database, scanner);
            Accounts accounts = new Accounts(database, scanner);
            AccountManager accountManager = new AccountManager(database, scanner);

            GridPane grid = new GridPane();
            grid.setAlignment(javafx.geometry.Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));

            Label welcomeLabel = new Label("WELCOME TO BANKING SYSTEM");
            welcomeLabel.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-padding: 10px;");
            grid.add(welcomeLabel, 0, 0);

            Button registerButton = new Button("Register");
            Button loginButton = new Button("Login");
            Button exitButton = new Button("Exit");

            registerButton.setOnAction(e -> handleRegister(user));
            loginButton.setOnAction(e -> handleLogin(user, accounts, accountManager));
            exitButton.setOnAction(e -> {
                System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                System.out.println("Exiting System!");
                primaryStage.close();
            });

            grid.add(registerButton, 0, 1);
            grid.add(loginButton, 0, 2);
            grid.add(exitButton, 0, 3);

            GridPane.setHalignment(registerButton, javafx.geometry.HPos.CENTER);
            GridPane.setHalignment(loginButton, javafx.geometry.HPos.CENTER);
            GridPane.setHalignment(exitButton, javafx.geometry.HPos.CENTER);

            Scene scene = new Scene(grid, 400, 300);

            primaryStage.setTitle("Banking System");
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    private void handleRegister(User user) {
        GridPane registerGrid = new GridPane();
        registerGrid.setAlignment(javafx.geometry.Pos.CENTER);
        registerGrid.setHgap(10);
        registerGrid.setVgap(10);
        registerGrid.setPadding(new Insets(25, 25, 25, 25));

        Label nameLabel = new Label("Full Name:");
        TextField nameTextField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button registerButton = new Button("Register");

        registerGrid.add(nameLabel, 0, 0);
        registerGrid.add(nameTextField, 1, 0);
        registerGrid.add(emailLabel, 0, 1);
        registerGrid.add(emailTextField, 1, 1);
        registerGrid.add(passwordLabel, 0, 2);
        registerGrid.add(passwordField, 1, 2);
        registerGrid.add(registerButton, 1, 3);

        Scene registerScene = new Scene(registerGrid, 300, 250);

        Stage registerStage = new Stage();
        registerStage.setTitle("Register");
        registerStage.setScene(registerScene);
        registerStage.show();

        registerButton.setOnAction(e -> {
            String fullName = nameTextField.getText();
            String email = emailTextField.getText();
            String password = passwordField.getText();

            if (validateRegisterInput(fullName, email, password)) {
                user.register(fullName, email, password);

                registerStage.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Successful");
                alert.setHeaderText(null);
                alert.setContentText("Registration successful! You can now log in.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Registration Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid input. Please check your information and try again.");
                alert.showAndWait();
            }
        });
    }

    private void handleLogin(User user, Accounts accounts, AccountManager accountManager) {
        GridPane loginGrid = new GridPane();
        loginGrid.setAlignment(javafx.geometry.Pos.CENTER);
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(25, 25, 25, 25));

        Label emailLabel = new Label("Email:");
        TextField emailTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginGrid.add(emailLabel, 0, 0);
        loginGrid.add(emailTextField, 1, 0);
        loginGrid.add(passwordLabel, 0, 1);
        loginGrid.add(passwordField, 1, 1);
        loginGrid.add(loginButton, 1, 2);

        Scene loginScene = new Scene(loginGrid, 300, 200);

        Stage loginStage = new Stage();
        loginStage.setTitle("Login");
        loginStage.setScene(loginScene);

        loginStage.show();


    loginButton.setOnAction(e -> {
    String email = emailTextField.getText();
    String password = passwordField.getText();

    String loggedInUser = user.login(email, password);

    if (loggedInUser != null) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Success");
        alert.setHeaderText(null);
        alert.setContentText("Welcome, " + loggedInUser + "!");
        alert.showAndWait();

        loginStage.close();

        Stage dashboardStage = new Stage();
        dashboardStage.setTitle("User Dashboard");

        Button openAccountButton = new Button("Open Account");
        Button checkBalanceButton = new Button("Check Balance");
        Button creditButton = new Button("Credit Money");
        Button debitButton = new Button("Debit Money");
        Button transferButton = new Button("Transfer Money");
        Button backButton = new Button("Back");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(openAccountButton, checkBalanceButton, creditButton, debitButton, transferButton, backButton);
        vbox.setAlignment(Pos.CENTER);

        Scene dashboardScene = new Scene(vbox, 400, 300);
        dashboardStage.setScene(dashboardScene);
        dashboardStage.show();

        openAccountButton.setOnAction(event -> {
            Stage openaccStage = new Stage();
            openaccStage.setTitle("Open Account");
            GridPane OAGrid = new GridPane();
            OAGrid.setAlignment(javafx.geometry.Pos.CENTER);
            OAGrid.setHgap(10);
            OAGrid.setVgap(10);
            OAGrid.setPadding(new Insets(25, 25, 25, 25));

            Label accName = new Label("Name:");
            TextField accNameField = new TextField();
            Label amount = new Label("Amount:");
            TextField amountField = new TextField();
            Label pin = new Label("pin");
            PasswordField pinField= new PasswordField();
            Button sumbitB = new Button("submit");

            OAGrid.add(accName, 0, 0);  
            OAGrid.add(accNameField, 1, 0);
            OAGrid.add(amount, 0, 1);
            OAGrid.add(amountField, 1, 1);
            OAGrid.add(pin,0,2);
            OAGrid.add(pinField,1,2);
            OAGrid.add(sumbitB,1,3);

            GridPane.setHalignment(sumbitB, HPos.CENTER);

            Scene openaccScene = new Scene(OAGrid,400,300);
            openaccStage.setScene(openaccScene);
            openaccStage.show();


            sumbitB.setOnAction(event2 -> {
                String accName1 = accNameField.getText();
                long amount1 = Long.parseLong(amountField.getText());
                String pin1 = pinField.getText();
                
                long accNo= accounts.openAccount(email,accName1,amount1,pin1);
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Account creation Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Welcome, " + accNo + "!");
                alert1.showAndWait();
                
            });
        });

        checkBalanceButton.setOnAction(event -> {
            long accountNumber = accounts.getAccountNumber(email);
            double balance = accountManager.getBalance(accountNumber);
            if (balance >= 0) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Account Balance");
                alert1.setHeaderText(null);
                alert1.setContentText("Balance:" + balance);
                alert1.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Account Balance");
                alert1.setHeaderText(null);
                alert1.setContentText("Invalid Account Number");
                alert1.showAndWait();
            }
        });

        creditButton.setOnAction(event -> {
            Stage openaccStage = new Stage();
            openaccStage.setTitle("Credit Money");
            GridPane OAGrid = new GridPane();
            OAGrid.setAlignment(javafx.geometry.Pos.CENTER);
            OAGrid.setHgap(10);
            OAGrid.setVgap(10);
            OAGrid.setPadding(new Insets(25, 25, 25, 25));

            Label amount = new Label("Amount:");
            TextField amountField = new TextField();
            Label pin = new Label("pin");
            PasswordField pinField= new PasswordField();
            Button sumbitB = new Button("submit");

            OAGrid.add(amount, 0, 0);  
            OAGrid.add(amountField, 1, 0);
            OAGrid.add(pin, 0, 1);
            OAGrid.add(pinField, 1, 1);
            OAGrid.add(sumbitB,0,2);

            GridPane.setHalignment(sumbitB, HPos.CENTER);

            Scene openaccScene = new Scene(OAGrid,400,300);
            openaccStage.setScene(openaccScene);
            openaccStage.show();


            sumbitB.setOnAction(event2 -> {
                long amount1 = Long.parseLong(amountField.getText());
                String pin1 = pinField.getText();
                long accountNumber= accounts.getAccountNumber(email);
                long newb=accountManager.creditMoney(accountNumber,amount1,pin1);
                if(newb!=0){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Credit Success");
                    alert1.setHeaderText(null);
                    alert1.setContentText("New Balance:" + newb);
                    alert1.showAndWait();
                    openaccStage.close();
                }
                if(newb==0){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Credit Failure");
                    alert1.setHeaderText(null);
                    alert1.setContentText(amount1+" cannot be credited");
                    alert1.showAndWait();
                }
                
            });
        });
        

        debitButton.setOnAction(event -> {
            Stage openaccStage = new Stage();
            openaccStage.setTitle("Credit Money");
            GridPane OAGrid = new GridPane();
            OAGrid.setAlignment(javafx.geometry.Pos.CENTER);
            OAGrid.setHgap(10);
            OAGrid.setVgap(10);
            OAGrid.setPadding(new Insets(25, 25, 25, 25));

            Label amount = new Label("Amount:");
            TextField amountField = new TextField();
            Label pin = new Label("pin");
            PasswordField pinField= new PasswordField();
            Button sumbitB = new Button("submit");

            OAGrid.add(amount, 0, 0);  
            OAGrid.add(amountField, 1, 0);
            OAGrid.add(pin, 0, 1);
            OAGrid.add(pinField, 1, 1);
            OAGrid.add(sumbitB,0,2);

            GridPane.setHalignment(sumbitB, HPos.CENTER);

            Scene openaccScene = new Scene(OAGrid,400,300);
            openaccStage.setScene(openaccScene);
            openaccStage.show();


            sumbitB.setOnAction(event2 -> {
                long accountNumber = accounts.getAccountNumber(email);
                long amount1 = Long.parseLong(amountField.getText());
                String pin1 = pinField.getText();
                long newb=accountManager.debitMoney(accountNumber,amount1,pin1);
                if(newb!=0){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Debit Success");
                    alert1.setHeaderText(null);
                    alert1.setContentText("New Balance:" + newb);
                    alert1.showAndWait();
                    openaccStage.close();
                }
                if(newb==0){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Debit Failure");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Insufficient Balance");
                    alert1.showAndWait();
                }
                
            });
        });

        transferButton.setOnAction(event -> {
            Stage openaccStage = new Stage();
            openaccStage.setTitle("Transfer Money");
            GridPane OAGrid = new GridPane();
            OAGrid.setAlignment(javafx.geometry.Pos.CENTER);
            OAGrid.setHgap(10);
            OAGrid.setVgap(10);
            OAGrid.setPadding(new Insets(25, 25, 25, 25));

            Label amount = new Label("Amount:");
            TextField amountField = new TextField();
            Label name = new Label("Sender Email:");
            TextField nameField= new TextField();
            Label pin = new Label("pin");
            PasswordField pinField= new PasswordField();
            Button sumbitB = new Button("submit");

            OAGrid.add(amount, 0, 0);  
            OAGrid.add(amountField, 1, 0);
            OAGrid.add(name,0,1);
            OAGrid.add(nameField,1,1);
            OAGrid.add(pin, 0, 2);
            OAGrid.add(pinField, 1, 2);
            OAGrid.add(sumbitB,1,3);

            GridPane.setHalignment(sumbitB, HPos.CENTER);

            Scene openaccScene = new Scene(OAGrid,400,300);
            openaccStage.setScene(openaccScene);
            openaccStage.show();


            sumbitB.setOnAction(event2 -> {
                long senderAccountNumber= accounts.getAccountNumber(email);
                String name2=nameField.getText();
                long name1= accounts.getAccountNumber(name2);
                long amount1 = Long.parseLong(amountField.getText());
                String pin1 = pinField.getText();
                long a=accountManager.transferMoney(senderAccountNumber,name1,amount1,pin1);
                if(a!=0){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Transfer Success");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Rs."+amount1+" Successfully Transferred");
                    alert1.showAndWait();
                    openaccStage.close();
                }
                if(a==0){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Transaction Failure");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Insufficient Balance");
                    alert1.showAndWait();
                }
            });
        });

        backButton.setOnAction(event -> {
            dashboardStage.close();
        });

    } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText("Invalid email or password. Please try again.");
        alert.showAndWait();
    }
});
    }

    private boolean validateRegisterInput(String fullName, String email, String password) {
        return !fullName.isEmpty() && !email.isEmpty() && !password.isEmpty();
    }
}