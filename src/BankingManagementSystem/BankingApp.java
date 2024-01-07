package BankingManagementSystem;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;

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

    Image backgroundImage = new Image(getClass().getResourceAsStream("New.jpg"));
    BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);


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
                grid.setVgap(40);
                grid.setPadding(new Insets(25, 25, 25, 25));

                Image backgroundImage = new Image(getClass().getResourceAsStream("New.jpg"));
                BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
                grid.setBackground(new Background(background));

                grid.setAlignment(Pos.CENTER); 

                ColumnConstraints columnConstraints = new ColumnConstraints();
                columnConstraints.setHalignment(HPos.CENTER);
                grid.getColumnConstraints().addAll(columnConstraints, columnConstraints, columnConstraints);

                Text welcomeText = new Text("WELCOME TO BANKING SYSTEM");
                welcomeText.setFill(javafx.scene.paint.Color.WHITE);
                welcomeText.setFont(javafx.scene.text.Font.font("Frutiger", FontWeight.BOLD, 48));
                welcomeText.setStyle("-fx-stroke: black; -fx-stroke-width: 2px;");
                grid.add(welcomeText, 0, 0, 3, 1);

                HBox buttonRow = new HBox(40); 
                buttonRow.setAlignment(javafx.geometry.Pos.CENTER);
                Button registerButton = new Button("Register");
                Button loginButton = new Button("Login");
                Button exitButton = new Button("Exit");
                buttonRow.getChildren().addAll(registerButton, loginButton, exitButton);

                double buttonWidth = 250.0;
                registerButton.setPrefWidth(buttonWidth);
                loginButton.setPrefWidth(buttonWidth);
                exitButton.setPrefWidth(buttonWidth);

                registerButton.setStyle("-fx-background-color: #4CAF50; -fx-font-weight: bold; -fx-text-fill: white;-fx-font-size: 26px;");
                loginButton.setStyle("-fx-background-color: #2196F3; -fx-font-weight: bold; -fx-text-fill: white;-fx-font-size: 26px;");
                exitButton.setStyle("-fx-background-color: #f44336; -fx-font-weight: bold; -fx-text-fill: white;-fx-font-size: 26px;");
                registerButton.setOnAction(e -> handleRegister(user));
                loginButton.setOnAction(e -> handleLogin(user, accounts, accountManager));
                exitButton.setOnAction(e -> {
                System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                System.out.println("Exiting System!");
                primaryStage.close();
            });
            VBox buttonList = new VBox(20);
            buttonList.setAlignment(javafx.geometry.Pos.CENTER);
            buttonList.getChildren().addAll(registerButton, loginButton, exitButton);

            grid.add(buttonList, 0, 1, 3, 1);
            Scene scene = new Scene(grid, 1280, 730);

            primaryStage.setTitle("Banking System");
            primaryStage.setScene(scene);

        
            registerButton.setOnMouseEntered((MouseEvent e) -> {
                registerButton.setEffect(new Glow());
            });
            loginButton.setOnMouseEntered((MouseEvent e) -> {
                loginButton.setEffect(new Glow());
            });
            exitButton.setOnMouseEntered((MouseEvent e) -> {
                exitButton.setEffect(new Glow());
            });

            registerButton.setOnMouseExited((MouseEvent e) -> {
                registerButton.setEffect(null);
            });
            loginButton.setOnMouseExited((MouseEvent e) -> {
                loginButton.setEffect(null);
            });
            exitButton.setOnMouseExited((MouseEvent e) -> {
                exitButton.setEffect(null);
            });

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    private void handleRegister(User user) {
        GridPane registerGrid = new GridPane();
        registerGrid.setAlignment(javafx.geometry.Pos.CENTER);
        registerGrid.setHgap(10);
        registerGrid.setVgap(35); 
        registerGrid.setPadding(new Insets(25, 25, 25, 25));
    
        Label nameLabel = new Label("Full Name:");
        nameLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        TextField nameTextField = new TextField();
        nameTextField.setStyle("-fx-font-size: 19px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;"); 
    
        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        TextField emailTextField = new TextField();
        emailTextField.setStyle("-fx-font-size: 19px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");
    
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");
    
        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
        registerButton.setOnMouseEntered((MouseEvent event) -> registerButton.setEffect(new Glow()));
        registerButton.setOnMouseExited((MouseEvent event) -> registerButton.setEffect(null));

        registerGrid.add(nameLabel, 0, 0);
        registerGrid.add(nameTextField, 1, 0);
        registerGrid.add(emailLabel, 0, 1);
        registerGrid.add(emailTextField, 1, 1);
        registerGrid.add(passwordLabel, 0, 2);
        registerGrid.add(passwordField, 1, 2);
        registerGrid.add(registerButton, 1, 3);
    
        Image backgroundImage = new Image(getClass().getResourceAsStream("New.jpg"));
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        registerGrid.setBackground(new Background(background));
    
        Scene registerScene = new Scene(registerGrid, 1280, 730);
    
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
                alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
                alert.getDialogPane().setPrefWidth(600);
                alert.getDialogPane().setPrefHeight(200);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Registration Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid input. Please check your information and try again.");
                alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
                alert.getDialogPane().setPrefWidth(600);
                alert.getDialogPane().setPrefHeight(200);
                alert.showAndWait();
            }
        });
    }
    

    private void handleLogin(User user, Accounts accounts, AccountManager accountManager) {
        GridPane loginGrid = new GridPane();
        loginGrid.setAlignment(javafx.geometry.Pos.CENTER);
        loginGrid.setHgap(20);
        loginGrid.setVgap(40);
        loginGrid.setPadding(new Insets(25, 25, 25, 25));

        loginGrid.setBackground(new Background(background));

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
        TextField emailTextField = new TextField();
        emailTextField.setStyle("-fx-font-size: 19px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
        loginButton.setOnMouseEntered((MouseEvent event) -> loginButton.setEffect(new Glow()));
        loginButton.setOnMouseExited((MouseEvent event) -> loginButton.setEffect(null));

        loginGrid.add(emailLabel, 0, 0);
        loginGrid.add(emailTextField, 1, 0);
        loginGrid.add(passwordLabel, 0, 1);
        loginGrid.add(passwordField, 1, 1);
        loginGrid.add(loginButton, 1, 2);

        Scene loginScene = new Scene(loginGrid, 1280, 730);

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

        alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
        alert.getDialogPane().setPrefWidth(600);
        alert.getDialogPane().setPrefHeight(200);
        alert.showAndWait();

        loginStage.close();

        Stage dashboardStage = new Stage();
        dashboardStage.setTitle("User Dashboard");

        Button openAccountButton = new Button("Open Account");
        openAccountButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
        openAccountButton.setOnMouseEntered((MouseEvent event) -> openAccountButton.setEffect(new Glow()));
        openAccountButton.setOnMouseExited((MouseEvent event) -> openAccountButton.setEffect(null));
        
        Button checkBalanceButton = new Button("Check Balance");
        checkBalanceButton.setStyle("-fx-background-color: #8A2BE2; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
        checkBalanceButton.setOnMouseEntered((MouseEvent event) -> checkBalanceButton.setEffect(new Glow()));
        checkBalanceButton.setOnMouseExited((MouseEvent event) -> checkBalanceButton.setEffect(null));
        
        Button creditButton = new Button("Credit Money");
        creditButton.setStyle("-fx-background-color: #006400; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
        creditButton.setOnMouseEntered((MouseEvent event) -> creditButton.setEffect(new Glow()));
        creditButton.setOnMouseExited((MouseEvent event) -> creditButton.setEffect(null));
        
        Button debitButton = new Button("Debit Money");
        debitButton.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
        debitButton.setOnMouseEntered((MouseEvent event) -> debitButton.setEffect(new Glow()));
        debitButton.setOnMouseExited((MouseEvent event) -> debitButton.setEffect(null));
        
        Button transferButton = new Button("Transfer");
        transferButton.setStyle("-fx-background-color: #FFA500; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
        transferButton.setOnMouseEntered((MouseEvent event) -> transferButton.setEffect(new Glow()));
        transferButton.setOnMouseExited((MouseEvent event) -> transferButton.setEffect(null));
        
        Button backButton = new Button("Logout");
        backButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
        backButton.setOnMouseEntered((MouseEvent event) -> backButton.setEffect(new Glow()));
        backButton.setOnMouseExited((MouseEvent event) -> backButton.setEffect(null));

        VBox vbox = new VBox(50);
        vbox.getChildren().addAll(openAccountButton, checkBalanceButton, creditButton, debitButton, transferButton, backButton);
        vbox.setAlignment(Pos.CENTER);

        BackgroundImage dashboardBackground = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        vbox.setBackground(new Background(dashboardBackground));

        Scene dashboardScene = new Scene(vbox, 1280, 730);
        dashboardStage.setScene(dashboardScene);
        dashboardStage.show();
        

        openAccountButton.setOnAction(event -> {
            Stage openaccStage = new Stage();
            openaccStage.setTitle("Open Account");
            GridPane OAGrid = new GridPane();
            OAGrid.setAlignment(javafx.geometry.Pos.CENTER);
            OAGrid.setHgap(20);
            OAGrid.setVgap(50);
            OAGrid.setPadding(new Insets(25, 25, 25, 25));
            OAGrid.setBackground(new Background(background));

            Label accName = new Label("Name:");
            TextField accNameField = new TextField();
            Label amount = new Label("Amount:");
            TextField amountField = new TextField();
            Label pin = new Label("Pin");
            PasswordField pinField= new PasswordField();
            Button sumbitB = new Button("Submit");

    
            accName.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
            accNameField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");
            
            amount.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
            amountField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");

            pin.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
            pinField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");

            sumbitB.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
            sumbitB.setOnMouseEntered(mouseEvent -> sumbitB.setEffect(new Glow()));
            sumbitB.setOnMouseExited(mouseEvent -> sumbitB.setEffect(null));

            
            OAGrid.add(accName, 0, 0);  
            OAGrid.add(accNameField, 1, 0);
            OAGrid.add(amount, 0, 1);
            OAGrid.add(amountField, 1, 1);
            OAGrid.add(pin,0,2);
            OAGrid.add(pinField,1,2);
            OAGrid.add(sumbitB,1,3);

            GridPane.setHalignment(sumbitB, HPos.CENTER);

            Scene openaccScene = new Scene(OAGrid,1280,730);
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
                alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
                alert1.getDialogPane().setPrefWidth(600);
                alert1.getDialogPane().setPrefHeight(200);
                alert1.showAndWait();
                openaccStage.close();
                
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
                alert1.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
                alert1.getDialogPane().setPrefWidth(600);
                alert1.getDialogPane().setPrefHeight(200);
                alert1.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Account Balance");
                alert1.setHeaderText(null);
                alert1.setContentText("Invalid Account Number");
                alert1.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
                alert1.getDialogPane().setPrefWidth(600);
                alert1.getDialogPane().setPrefHeight(200);
                alert1.showAndWait();
            }
        });

        creditButton.setOnAction(event -> {
            Stage openaccStage = new Stage();
            openaccStage.setTitle("Credit Money");
            GridPane OAGrid = new GridPane();
            OAGrid.setAlignment(javafx.geometry.Pos.CENTER);
            OAGrid.setHgap(10);
            OAGrid.setVgap(40);
            OAGrid.setPadding(new Insets(25, 25, 25, 25));
            OAGrid.setBackground(new Background(background));

            Label amount = new Label("Amount:");
            TextField amountField = new TextField();
            Label pin = new Label("Pin");
            PasswordField pinField= new PasswordField();
            Button sumbitB = new Button("Submit");

            amount.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
            amountField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");

            pin.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
            pinField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");

            sumbitB.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
            sumbitB.setOnMouseEntered(mouseEvent -> sumbitB.setEffect(new Glow()));
            sumbitB.setOnMouseExited(mouseEvent -> sumbitB.setEffect(null));


            OAGrid.add(amount, 0, 0);  
            OAGrid.add(amountField, 1, 0);
            OAGrid.add(pin, 0, 1);
            OAGrid.add(pinField, 1, 1);
            OAGrid.add(sumbitB, 0, 2, 2, 1); 

            GridPane.setHalignment(sumbitB, HPos.CENTER);

            Scene openaccScene = new Scene(OAGrid,1280,730);
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
                    alert1.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
                    alert1.getDialogPane().setPrefWidth(600);
                    alert1.getDialogPane().setPrefHeight(200);
                    alert1.showAndWait();
                    openaccStage.close();
                }
                if(newb==0){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Credit Failure");
                    alert1.setHeaderText(null);
                    alert1.setContentText(amount1+" cannot be credited");
                    alert1.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
                    alert1.getDialogPane().setPrefWidth(600);
                    alert1.getDialogPane().setPrefHeight(200);
                    alert1.showAndWait();
                }
                
            });
        });
        

        debitButton.setOnAction(event -> {
            Stage openaccStage = new Stage();
            openaccStage.setTitle("Debit Money");
            GridPane OAGrid = new GridPane();
            OAGrid.setAlignment(javafx.geometry.Pos.CENTER);
            OAGrid.setHgap(10);
            OAGrid.setVgap(40);
            OAGrid.setPadding(new Insets(25, 25, 25, 25));
            OAGrid.setBackground(new Background(background));

            Label amount = new Label("Amount:");
            TextField amountField = new TextField();
            Label pin = new Label("Pin");
            PasswordField pinField= new PasswordField();
            Button sumbitB = new Button("Submit");

            amount.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
            amountField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");

            pin.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
            pinField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");

            sumbitB.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
            sumbitB.setOnMouseEntered(mouseEvent -> sumbitB.setEffect(new Glow()));
            sumbitB.setOnMouseExited(mouseEvent -> sumbitB.setEffect(null));


            OAGrid.add(amount, 0, 0);  
            OAGrid.add(amountField, 1, 0);
            OAGrid.add(pin, 0, 1);
            OAGrid.add(pinField, 1, 1);
            OAGrid.add(sumbitB, 0, 2, 2, 1); 

            GridPane.setHalignment(sumbitB, HPos.CENTER);

            Scene openaccScene = new Scene(OAGrid,1280,730);
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
                    alert1.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
                    alert1.getDialogPane().setPrefWidth(600);
                    alert1.getDialogPane().setPrefHeight(200);
                    alert1.showAndWait();
                    openaccStage.close();
                }
                if(newb==0){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Debit Failure");
                    alert1.setHeaderText(null);
                    alert1.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
                    alert1.setContentText("Insufficient Balance");
                    alert1.getDialogPane().setPrefWidth(600);
                    alert1.getDialogPane().setPrefHeight(200);
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
            OAGrid.setVgap(30);
            OAGrid.setPadding(new Insets(25, 25, 25, 25));
            OAGrid.setBackground(new Background(background));

            Label amount = new Label("Amount:");
            TextField amountField = new TextField();
            Label name = new Label("Sender Email:");
            TextField nameField= new TextField();
            Label pin = new Label("Pin");
            PasswordField pinField= new PasswordField();
            Button sumbitB = new Button("Submit");

            amount.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
            amountField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");

            name.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
            nameField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");

            pin.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");
            pinField.setStyle("-fx-font-size: 18px; -fx-pref-height: 40; -fx-border-color: blue; -fx-border-width: 2px;");

            sumbitB.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-pref-width: 200px; -fx-pref-height: 60px;"); 
            sumbitB.setOnMouseEntered(mouseEvent -> sumbitB.setEffect(new Glow()));
            sumbitB.setOnMouseExited(mouseEvent -> sumbitB.setEffect(null));

            OAGrid.add(amount, 0, 0);  
            OAGrid.add(amountField, 1, 0);
            OAGrid.add(name,0,1);
            OAGrid.add(nameField,1,1);
            OAGrid.add(pin, 0, 2);
            OAGrid.add(pinField, 1, 2);
            OAGrid.add(sumbitB,1,3);

            GridPane.setHalignment(sumbitB, HPos.CENTER);

            Scene openaccScene = new Scene(OAGrid,1280,730);
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
                    alert1.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
                    alert1.getDialogPane().setPrefWidth(600);
                    alert1.getDialogPane().setPrefHeight(200);
                    openaccStage.setScene(openaccScene);
                    alert1.showAndWait();
                    openaccStage.close();
                }
                if(a==0){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Transaction Failure");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Insufficient Balance");
                    alert1.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
                    alert1.getDialogPane().setPrefWidth(600);
                    alert1.getDialogPane().setPrefHeight(200);
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
        alert.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Frutiger'; -fx-alignment: center;");
        alert.getDialogPane().setPrefWidth(600);
        alert.getDialogPane().setPrefHeight(200);
        alert.showAndWait();
    }
});
    }

    private boolean validateRegisterInput(String fullName, String email, String password) {
        return !fullName.isEmpty() && !email.isEmpty() && !password.isEmpty();
    }
}
