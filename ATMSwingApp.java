import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ATMSwingApp extends JFrame {
    JLabel jlab;
    JButton crAcc;
    JButton usAcc;
    JButton close;
    Map<Integer, Account> accounts; // Use a map to store accounts with account number as key
    Account currentAccount;

    Font custom = new Font("Arial", Font.PLAIN, 20);
    private static final String FILE_NAME = "accounts.txt";
    ATMSwingApp() {
        accounts = new HashMap<>();
        currentAccount = null;
        loadAccountsFromFile();
        JFrame frame = new JFrame("ATMSwingApp");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Layouts
        frame.setLayout(new BorderLayout());

        // Main panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Panel for buttons on the left-bottom
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new BoxLayout(buttonPanel2, BoxLayout.Y_AXIS));

        // Buttons
        crAcc = new JButton("Create an account");
        usAcc = new JButton("Use your account");
        close = new JButton("Close");
        if(accounts.isEmpty()) usAcc.setEnabled(false);

        // Label
        jlab = new JLabel("Choose option", SwingConstants.CENTER);

        // Action Listeners
        crAcc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createAccountWindow();
            }
        });

        usAcc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (accounts.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No accounts available. Please create an account first.");
                } else {
                    // Show dialog for account operations
                    useAccountWindow();
                }
            }
        });

        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // Adding components to the frame
        frame.add(jlab, BorderLayout.NORTH);
        buttonPanel.add(crAcc);
        buttonPanel.add(usAcc);
        buttonPanel2.add(close);
        JPanel leftBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftBottomPanel.add(buttonPanel2);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(leftBottomPanel, BorderLayout.SOUTH);
    }

    public void createAccountWindow() {
        JFrame crAccFrame = new JFrame("Create Account");
        crAccFrame.setSize(300, 200);
        crAccFrame.setLayout(new BorderLayout());
        crAccFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close this new window
        crAccFrame.setVisible(true);

        JLabel newLabel = new JLabel("Start creating account!", SwingConstants.CENTER);
        crAccFrame.add(newLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel accNumberLabel = new JLabel("Account Number: ");
        JTextField accNumberField = new JTextField(10);
        JLabel pinLabel = new JLabel("PIN: ");
        JTextField pinField = new JTextField(10);

        panel.add(accNumberLabel);
        panel.add(accNumberField);
        panel.add(pinLabel);
        panel.add(pinField);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int accNumber = Integer.parseInt(accNumberField.getText());
                    int pin = Integer.parseInt(pinField.getText());

                    if (accounts.containsKey(accNumber)) {
                        JOptionPane.showMessageDialog(crAccFrame, "Account already exists.");
                    } else {
                        Account newAccount = new Account();
                        newAccount.setCustomerNumber(accNumber);
                        newAccount.setPinNumber(pin);
                        accounts.put(accNumber, newAccount);
                        saveAccountsToFile();
                        JOptionPane.showMessageDialog(crAccFrame, "Account created successfully!");
                        usAcc.setEnabled(true); // Enable the "Use your account" button
                        crAccFrame.dispose(); // Close the new window
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(crAccFrame, "Invalid input. Please enter valid numbers.");
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                crAccFrame.dispose(); // Close the new window
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createButton);
        buttonPanel.add(backButton);

        crAccFrame.add(panel, BorderLayout.CENTER);
        crAccFrame.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void useAccountWindow() {
        JFrame useAccFrame = new JFrame("Use Account");
        useAccFrame.setSize(300, 200);
        useAccFrame.setLayout(new BorderLayout());
        useAccFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        useAccFrame.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        JLabel accNumberLabel = new JLabel("Enter Account Number:");
        JTextField accNumberField = new JTextField(10);

        JLabel pinLabel = new JLabel("Enter PIN:");
        JTextField pinField = new JTextField(10);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int accNumber = Integer.parseInt(accNumberField.getText());
                    int pin = Integer.parseInt(pinField.getText());

                    if (accounts.containsKey(accNumber) && accounts.get(accNumber).getPinNumber() == pin) {
                        currentAccount = accounts.get(accNumber);
                        useAccOptionsWindow();
                        useAccFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(useAccFrame, "Invalid account number or PIN.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(useAccFrame, "Invalid input. Please enter valid numbers.");
                }
            }
        });

        panel.add(accNumberLabel);
        panel.add(accNumberField);
        panel.add(pinLabel);
        panel.add(pinField);
        panel.add(loginButton);

        useAccFrame.add(panel, BorderLayout.CENTER);
    }

    public void useAccOptionsWindow() {
        JFrame optionsFrame = new JFrame("Account Options");
        optionsFrame.setSize(300, 200);
        optionsFrame.setLayout(new BorderLayout());
        optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        optionsFrame.setVisible(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JButton checkBalance = new JButton("Check Balance");
        JButton withdraw = new JButton("Withdraw");
        JButton deposit = new JButton("Deposit");
        JButton backButton = new JButton("Back");
        JButton delete = new JButton("Delete");

        checkBalance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(optionsFrame,
                        "Checking Balance: " + currentAccount.getCheckingBalance() + "\n" +
                                "Saving Balance: " + currentAccount.getSavingBalance());
            }
        });

        withdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String amountStr = JOptionPane.showInputDialog(optionsFrame, "Enter amount to withdraw:");
                try {
                    double amount = Double.parseDouble(amountStr);
                    if (currentAccount.getCheckingBalance() >= amount) {
                        currentAccount.calcCheckingWithdraw(amount);
                        JOptionPane.showMessageDialog(optionsFrame, "Withdrawal successful!");
                        saveAccountsToFile();
                    } else {
                        JOptionPane.showMessageDialog(optionsFrame, "Insufficient funds.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(optionsFrame, "Invalid input.");
                }
            }
        });

        deposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame depositFrame = new JFrame("Deposit");
                depositFrame.setSize(200, 150);
                depositFrame.setLayout(new BorderLayout());
                depositFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                depositFrame.setVisible(true);

                JLabel chooseAccount = new JLabel("Choose account",SwingConstants.CENTER);
                chooseAccount.setFont(custom);
                JPanel buttonPanel = new JPanel();
                JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                JButton checking = new JButton("Checking");
                JButton savings = new JButton("Savings");
                JButton back = backButton(depositFrame);

                checking.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String amountStr = JOptionPane.showInputDialog(depositFrame, "Enter amount to deposit:");
                        if (amountStr != null) { // Check if the input is not null
                            try {
                                double amount = Double.parseDouble(amountStr);
                                currentAccount.calcCheckingDeposit(amount);
                                JOptionPane.showMessageDialog(depositFrame, "Deposit successful!");
                                saveAccountsToFile();
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(depositFrame, "Invalid input. Please enter a valid number.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(depositFrame, "Deposit cancelled.");
                        }
                    }
                });

                savings.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String amountStr = JOptionPane.showInputDialog(depositFrame, "Enter amount to deposit:");
                        if (amountStr != null) { // Check if the input is not null
                            try {
                                double amount = Double.parseDouble(amountStr);
                                currentAccount.calcSavingDeposit(amount);
                                JOptionPane.showMessageDialog(depositFrame, "Deposit successful!");
                                saveAccountsToFile();
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(depositFrame, "Invalid input. Please enter a valid number.");
                            }
                        } else {
                            JOptionPane.showMessageDialog(depositFrame, "Deposit cancelled.");
                        }
                    }
                });



                buttonPanel.add(checking);
                buttonPanel.add(savings);
                leftPanel.add(back);
                depositFrame.add(chooseAccount, BorderLayout.NORTH);
                depositFrame.add(buttonPanel, BorderLayout.CENTER);
                depositFrame.add(leftPanel, BorderLayout.SOUTH);
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                optionsFrame.dispose();
            }
        });

        delete.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (currentAccount == null) {
                        JOptionPane.showMessageDialog(null, "No account is currently selected.");
                        return;
                    }
                JFrame deleteFrame = new JFrame("Delete Account");
                deleteFrame.setSize(300, 200);
                deleteFrame.setLayout(new BorderLayout());
                deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                deleteFrame.setVisible(true);

                JLabel confirm = new JLabel("Are you sure you want to delete your account?", SwingConstants.CENTER);
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new GridLayout(2, 1));
                JButton yes =  new JButton("Yes");
                JButton no =  new JButton("No");

                    yes.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // Delete the account
                            accounts.remove(currentAccount.getCustomerNumber());
                            JOptionPane.showMessageDialog(deleteFrame, "Account deleted successfully");
                            deleteFrame.dispose();
                            currentAccount = null; // Clear the current account
                            saveAccountsToFile();
                            if(accounts.isEmpty()){
                                usAcc.setEnabled(false); // Disable the "Use your account" button

                            }
                            optionsFrame.dispose();
                        }
                    });

                    no.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(deleteFrame, "Account has NOT been deleted");
                            deleteFrame.dispose();
                        }
                    });
                buttonPanel.add(yes);
                buttonPanel.add(no);
                deleteFrame.add(buttonPanel, BorderLayout.CENTER);
                deleteFrame.add(confirm, BorderLayout.NORTH);

            }
        });

        panel.add(checkBalance);
        panel.add(withdraw);
        panel.add(deposit);
        panel.add(delete);
        panel.add(backButton);


        optionsFrame.add(panel, BorderLayout.CENTER);
    }

    private JButton backButton(JFrame frame) {
         JButton button = new JButton("Back");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        return button;
    }

    private void saveAccountsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<Integer, Account> entry : accounts.entrySet()) {
                Account account = entry.getValue();
                writer.write(account.getCustomerNumber() + "," + account.getPinNumber() + "," +
                        account.getCheckingBalance() + "," + account.getSavingBalance());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAccountsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return; // File does not exist, so no accounts to load
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int accNumber = Integer.parseInt(parts[0]);
                    int pin = Integer.parseInt(parts[1]);
                    double checkingBalance = Double.parseDouble(parts[2]);
                    double savingBalance = Double.parseDouble(parts[3]);

                    Account account = new Account();
                    account.setCustomerNumber(accNumber);
                    account.setPinNumber(pin);
                    account.calcCheckingDeposit(checkingBalance); // Initialize balances
                    account.calcSavingDeposit(savingBalance);

                    accounts.put(accNumber, account);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ATMSwingApp();
            }
        });
    }
}




