import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


public class OptionMenu extends Account{
    Scanner menuInput = new Scanner(System.in);
    DecimalFormat moneyFormat = new DecimalFormat("'$'###,##0.00");

    HashMap<Integer, Integer> data = new HashMap<Integer,Integer>();
    HashSet<Account> accounts=new HashSet<>();


    public void getLogin() throws IOException{
        int x = 1;
        System.out.println("Welcome to the ATM Project!");
        do{
            try {

                if(data.isEmpty()){
                  accounts.add(createAccount());
                }else {
                    System.out.println("Enter a valid account number");
                    int cn = menuInput.nextInt();
                    System.out.println("Enter a PIN number");
                    int pn = menuInput.nextInt();
                    if(data.containsKey(cn) && data.get(cn) == pn){
                        Account loggedInAccount = getAccountByNumberAndPin(cn, pn);

                        if (loggedInAccount != null) {
                            System.out.println("Login successful!");
                            getAccountType(); // Proceed with account options
                        } else {
                            System.out.println("Account not found!");
                        }
                    }else
                        System.out.println("Invalid Customer Number or Pin Number!");
                 }
            }catch (Exception e){
                System.out.println("\n"+ "Invalid Character(s). Only Numbers." + "\n");
                x=2;
            }

        }while(x==1);
    }
    public void getAccountType() {
        System.out.println("Select the Account you Want to Access: ");
        System.out.println(" Type 1 - Checking Account");
        System.out.println(" Type 2 - Saving Account");
        System.out.println(" Type 3 - Exit");

        int selection = menuInput.nextInt();

        switch(selection){
            case 1:
                getChecking();
                break;
            case 2:
                getSaving();
                break;
            case 3:
                System.out.println("Thank you for using the ATM Project!");
                break;
            default:
                System.out.println("\n" + "Invalid Selection. Please try again.");
                getAccountType();
        }
    }
    public void getChecking() {
        System.out.println("Checking Account: ");
        System.out.println(" Type 1 - View Balance");
        System.out.println(" Type 2 - Withdraw Funds");
        System.out.println(" Type 3 - Deposit Funds");
        System.out.println(" Type 4 - Exit");
        System.out.println("Choice: ");

        int selection = menuInput.nextInt();

        switch(selection){
            case 1:
                System.out.println("Checking Account Balance: " + moneyFormat.format(getCheckingBalance()));
                getAccountType();
                break;
            case 2:
                getCheckingWithdrawInput();
                getAccountType();
                break;
            case 3:
                getCheckingDepositInput();
                getAccountType();
                break;
            case 4:
                System.out.println("Thank you for using the ATM Project!");
                break;

            default:
                System.out.println("\n" + "Invalid Selection. Please try again.");
                getChecking();
        }
    }
    public void getSaving() {
        System.out.println("Saving Account: ");
        System.out.println("Checking Account: ");
        System.out.println(" Type 1 - View Balance");
        System.out.println(" Type 2 - Withdraw Funds");
        System.out.println(" Type 3 - Deposit Funds");
        System.out.println(" Type 4 - Exit");
        System.out.println("Choice: ");

        int selection = menuInput.nextInt();

        switch(selection){
            case 1:
                System.out.println("Checking Account Balance: " + moneyFormat.format(getSavingBalance()));
                getAccountType();
                break;
            case 2:
                getSavingWithdrawInput();
                getAccountType();
                break;
            case 3:
                getSavingDepositInput();
                getAccountType();
                break;
            case 4:
                System.out.println("Thank you for using the ATM Project!");
                break;

            default:
                System.out.println("\n" + "Invalid Selection. Please try again.");
                getChecking();
        }
    }
    public Account createAccount(){
        System.out.println("Creating Account: ");
        Account account = new Account();
        System.out.println("There are no accounts open. Please enter a 6-digit account number: ");
        int accNumber = menuInput.nextInt();
        System.out.println("Please enter a 6-digit PIN number: ");
        int pin = menuInput.nextInt();
        data.put(accNumber, pin);
        account.setCustomerNumber(accNumber);
        account.setPinNumber(pin);
        System.out.println("You have successfully created an account. ");
        return account;
    }
    public Account getAccountByNumberAndPin(int accountNumber, int pin) {
        for (Account account : accounts) {
            if (account.getCustomerNumber() == accountNumber && account.getPinNumber() == pin) {
                return account; // Return the matching account
            }
        }
        return null; // Return null if no match is found
    }

}
