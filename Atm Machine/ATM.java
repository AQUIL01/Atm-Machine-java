import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

// User Class for storing user details
class User implements Serializable {
    String username;
    String password;
    double balance;
    double savings;
    java.util.List<String> transactions;

    User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        this.savings = 0.0;
        this.transactions = new java.util.ArrayList<>();
    }
}

public class ATM {
    private static JFrame frame;
    private static JTextField usernameField, pinField;
    private static JLabel accountLabel, savingsLabel;
    private static Map<String, User> users = new HashMap<>();
    private static String currentUser;

    public static void main(String[] args) {
        loadData();
        createLoginScreen();
    }

    // Login Screen
    private static void createLoginScreen() {
        frame = new JFrame("ATM Login");
        frame.setSize(660, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setBounds(200, 120, 80, 25);
        frame.add(nameLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(300, 120, 165, 25);
        frame.add(usernameField);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        pinLabel.setBounds(200, 160, 80, 25);
        frame.add(pinLabel);

        pinField = new JPasswordField(20);
        pinField.setBounds(300, 160, 165, 25);
        frame.add(pinField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(200, 220, 80, 25);
        frame.add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(300, 220, 120, 25);
        frame.add(registerButton);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

        frame.setVisible(true);
    }

    // Login Logic
    private static void login() {
        String username = usernameField.getText();
        String password = pinField.getText();

        if (users.containsKey(username) && users.get(username).password.equals(password)) {
            currentUser = username;
            createMainScreen();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Registration Logic
    private static void register() {
        String username = usernameField.getText();
        String password = pinField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Username and password cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
        } else if (users.containsKey(username)) {
            JOptionPane.showMessageDialog(frame, "User already exists", "Registration Error", JOptionPane.ERROR_MESSAGE);
        } else {
            users.put(username, new User(username, password));
            saveData();
            JOptionPane.showMessageDialog(frame, "User registered successfully", "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Main Screen after Login
    private static void createMainScreen() {
        frame.getContentPane().removeAll();
        frame.repaint();

        JLabel welcomeLabel = new JLabel("Welcome " + currentUser);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        welcomeLabel.setBounds(20, 20, 300, 25);
        frame.add(welcomeLabel);

        // Account Balance Labels
        accountLabel = new JLabel("₹" + users.get(currentUser).balance);
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        accountLabel.setBounds(20, 80, 200, 25);
        frame.add(accountLabel);

        savingsLabel = new JLabel("₹" + users.get(currentUser).savings);
        savingsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        savingsLabel.setBounds(20, 120, 200, 25);
        frame.add(savingsLabel);

        // Buttons
        JButton cashButton = new JButton("Get Cash");
        cashButton.setBounds(200, 100, 150, 60);
        frame.add(cashButton);

        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(400, 100, 150, 60);
        frame.add(depositButton);

        JButton transactionsButton = new JButton("Transactions");
        transactionsButton.setBounds(200, 180, 150, 60);
        frame.add(transactionsButton);

        JButton quickCashButton = new JButton("Quick Cash ₹500");
        quickCashButton.setBounds(400, 180, 150, 60);
        frame.add(quickCashButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(300, 260, 100, 60);
        frame.add(logoutButton);

        // Button Actions
        cashButton.addActionListener(e -> getCash());
        depositButton.addActionListener(e -> deposit());
        transactionsButton.addActionListener(e -> showTransactions());
        quickCashButton.addActionListener(e -> quickCash());
        logoutButton.addActionListener(e -> logout());

        frame.setVisible(true);
    }

    // Cash Withdrawal
    private static void getCash() {
        User user = users.get(currentUser);

        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount > user.balance) {
                    JOptionPane.showMessageDialog(frame, "Insufficient balance", "Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    user.balance -= amount;
                    user.transactions.add("Withdrew: ₹" + amount);
                    updateBalance();
                    saveData();
                    JOptionPane.showMessageDialog(frame, "₹" + amount + " withdrawn successfully!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Deposit Amount
    private static void deposit() {
        User user = users.get(currentUser);

        String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                user.balance += amount;
                user.transactions.add("Deposited: ₹" + amount);
                updateBalance();
                saveData();
                JOptionPane.showMessageDialog(frame, "₹" + amount + " deposited successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Quick Cash Withdrawal
    private static void quickCash() {
        User user = users.get(currentUser);
        double amount = 500.00;
        if (amount > user.balance) {
            JOptionPane.showMessageDialog(frame, "Insufficient balance", "Error", JOptionPane.WARNING_MESSAGE);
        } else {
            user.balance -= amount;
            user.transactions.add("Quick Cash: ₹" + amount);
            updateBalance();
            saveData();
            JOptionPane.showMessageDialog(frame, "₹" + amount + " withdrawn successfully!");
        }
    }

    // Show Transaction History
    private static void showTransactions() {
        User user = users.get(currentUser);
        JTextArea textArea = new JTextArea(20, 30);
        for (String transaction : user.transactions) {
            textArea.append(transaction + "\n");
        }
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
    }

    // Update Balance Labels
    private static void updateBalance() {
        accountLabel.setText("₹" + users.get(currentUser).balance);
        savingsLabel.setText("₹" + users.get(currentUser).savings);
    }

    // Logout and Return to Login Screen
    private static void logout() {
        currentUser = null;
        frame.dispose();
        createLoginScreen();
    }

    // Save and Load User Data
    private static void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            out.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("users.dat"))) {
            users =  (Map<String, User>) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found, starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}