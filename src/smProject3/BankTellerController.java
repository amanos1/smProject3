package smProject3;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * 
 * I JUST COPIED AND PASTED A BUNCH OF FUNCTIONS FROM BANKTELLER FROM PROJECT 2, WE HAVE TO CHANGE THEM SO THAT THEY ACTUALLY WORK
 * TODO: CHECK IF FIRST AAAAND LAST NAME ARE GOOD
 *
 */
public class BankTellerController implements Initializable
{
	private AccountDatabase database;
	private static final int MONEY_MARKET_MIN = 2500;

	@FXML private TextField holder;
	@FXML private TextField dob;
	@FXML private TextField init;
	@FXML private TextField amount;

	@FXML private ChoiceBox<String> action;
	@FXML private ChoiceBox<String> type;
	@FXML private ChoiceBox<String> loyal;
	@FXML private ChoiceBox<String> campus;

	@FXML private TextArea output;

	@Override
	public void initialize(URL url, ResourceBundle bundle)
	{
		action.getItems().addAll("Open", "Close", "Deposit",
				"Withdraw");
		action.setValue("Open");
		type.getItems().addAll("Checking", "College Checking",
				"Savings", "Money Market");
		type.setValue("Checking");
		campus.getItems().addAll("New Brunswick", "Camden", "Newark");
		campus.setValue("New Brunswick");
		loyal.getItems().addAll("Yes","No");
		loyal.setValue("Yes");
		loyal.setDisable(true);
		campus.setDisable(true);
		
		type.getSelectionModel().selectedItemProperty().addListener((property, oldValue, newValue) -> {
			if(newValue.equals("College Checking") || newValue.equals("Checking")) {
				loyal.setDisable(true);
				if(newValue.equals("College Checking")) {
					campus.setDisable(false);
				}
			} else {
				campus.setDisable(true);
				loyal.setDisable(false);
			}
		});

		database = new AccountDatabase();
	}

	public void buttonPressed()
	{
		
		String actionString = action.getValue();
             if(actionString.equals("Open")) open();
        else if(actionString.equals("Close")) close();
        else if(actionString.equals("Deposit")) deposit();
        else if(actionString.equals("Withdraw")) withdraw();
        holder.clear();
        dob.clear();
        init.clear();
        amount.clear();
}

	public void quit()
	{
	}

	public void actionChanged()
	{
		campus.setDisable(true);
	}

	public void typeChanged()
	{
		loyal.setDisable(false);
	}

	/**
	 * Creates a Profile object with the information provided.
	 * @param input The input string containing information about the Profile.
	 * @param st The StringTokenizer we use to parse the input string. 
	 * @param open Boolean for whether the Profile is being used for opening or closing an account.
	 * @return The resulting profile or null if the data in the input string would result in an invalid profile.
	 */
	private Profile createProfile(boolean open)
	{
	    String name = holder.getText();

	    if(name.equals("")) {
	    	if (open) output.setText("Missing data for opening an account.");
	    	else      output.setText("Missing data for closing an account.");
	    	return null;
	    }
	    String firstNLast[] = name.split(" ");

	    String dateOfBirth = dob.getText();
	    if(dateOfBirth.equals("")) {
	    	if (open) output.setText("Missing data for opening an account.");
	    	else      output.setText("Missing data for closing an account.");
	    	return null;
	    }

	    Date birth = new Date(dateOfBirth);

	    if(!birth.isValid())
	    {
	    	output.setText("Date of birth invalid.");
	    	return null;
	    }

	    Profile p = new Profile(firstNLast[0], firstNLast[1], dateOfBirth);
		return p;
	}

	/**
	 * Creates an Account object with the info provided.
	 * @param input The string containing information about the account to open.
	 * @param type The type of account in string form.
	 * @param profile The holder of the account as a Profile object.
	 * @param st The StringTokenizer we use to parse the input string.
	 * @return The resulting account or null if the data in the input string would result in an invalid account.
	 */
	private Account createAccount(String type, Profile profile,
			double init)
	{
	    Account a;

    	switch(type)
	    {
		    case "Checking":
			    a = new Checking(profile, init);
			    
		    	break;

		    case "College Checking":
			    String campusString = campus.getValue();
			    
			    a = new CollegeChecking(profile, init, campusString);
		    	break;

		    case "Savings":
			    String loyalString = loyal.getValue();
		    	a = new Savings(profile, init, loyalString.equals("Yes"));
		    	
		    	break;

		    case "Money Market":
		    	if (init < MONEY_MARKET_MIN)
		    	{
			    	output.setText("Minimum of $2500 to"
			    			+ "open a MoneyMarket account.");
			    	return null;
		    	}

			    a = new MoneyMarket(profile, init);
			   
		    	break;

		    default:
	    		a = null;
	    }
	    return a;
	}

	/**
	 * Opens a new account with the information in the given string.
	 * Prints error message if the command is invalid or the account conflicts with another one.
	 * @param com The input string containing information about the account to be created.
	 */
	private void open()
	{
	    String typeString = type.getValue();

	    Profile profile = createProfile(true);
	    if(profile == null) return;


	    double initVal;
	    String initString = init.getText();
    	if(initString.equals(""))
	    {
	    	output.setText("Missing data for opening an account.");
	    	return;
	    }

    	try
	    {
	    	initVal = Double.parseDouble(initString);
	    } catch(NumberFormatException e)
	    {
	    	output.setText("Not a valid amount.");
	    	return;
	    }

    	if(initVal <= 0)
	    {
	    	output.setText("Initial deposit cannot be 0 or negative.");
	    	return;
	    }

    	Account account = createAccount(typeString, profile, initVal);
	    if(account == null) return;

	    if(!database.open(account))
	    {
	    	if(!database.reopen(account))
	    	{
	    		output.setText(profile +
	    				" same account(type) is in the database.");
	    	} else 
	    	{
	    		output.setText("Account reopened.");
	    	}
	    } else
	    {
	    	output.setText("Account opened.");
	    }
	}

	/**
	 * Closes an account with the information in the given string.
	 * Prints an error message if the command is invalid or the account cannot be closed.
	 * @param com The input string containing information about the account to be closed.
	 */
	private void close()
	{
	    String typeString = type.getValue();

	    Profile profile = createProfile(false);
	    if(profile == null) return;

	    Account closeIt;
	    if(typeString.equals("Checking")) closeIt = new Checking(profile, 1);
	    else if(typeString.equals("College CHecking")) closeIt = 
	    		new CollegeChecking(profile, 1, 0);
	    else if(typeString.equals("Savings")) closeIt = new Savings(profile, 1, true);
	    else closeIt = new MoneyMarket(profile, MONEY_MARKET_MIN);

	    if(!database.isThere(closeIt))
	    {
	    	output.setText("Account cannot be closed because"
	    			+ "it does not exist.");
	    	return;
	    }

	    if(!database.close(closeIt))
	    {
	    	output.setText("Account is closed already.");
	    } else
	    {
	    	output.setText("Account closed.");
	    }
	}

	/**
	 * Makes sure the transaction is valid and either deposits or withdraws from an account.
	 * @param amount The amount to deposit/withdraw.
	 * @param holder The holder of the account as a Profile class.
	 * @param type The type of account as a String.
	 * @param deposit true if transaction is a deposit and false if it is a withdrawal.
	 * @return true if the deposit/withdrawal was successful, false if not.
	 */
	private boolean processTransaction(double amount, Profile holder,
			String type, boolean deposit)
	{
		if(amount <= 0)
		{
			if(deposit) output.setText("Deposit - "
					+ "amount cannot be 0 or negative.");
			else        output.setText("Withdraw - "
					+ "amount cannot be 0 or negative.");
			return false;
		}

		Account a;
		switch(type)
		{
			case "Checking":
				a = new Checking(holder, amount);
				break;
			case "College Checking":
				a = new CollegeChecking(holder, amount, 0);
				break;
			case "Savings":
				a = new Savings(holder, amount, true);
				break;
			case "Money Market":
				a = new MoneyMarket(holder, amount);
				break;
			default:
				return false;
		}

		if(!database.isThere(a))
		{
			System.out.printf("%s %s is not in the database.\n",
					holder, a.getType());
			return false;
		}

		if(deposit) database.deposit(a);
		else
		{
			if(!database.withdraw(a))
			{
				output.setText("Withdraw - insufficient fund.");
				return false;
			}
		}

		return true;
	}

	/**
	 * Deposits money into an account based on the information in the input string.
	 * Prints an error message to the console if the input is invalid.
	 * @param com The input string containing information about the deposit.
	 */
	private void deposit()
	{
	    String typeString = type.getValue();

	    Profile profile = createProfile(true);
	    if(profile == null) return;

    	double depositAmount;
    	String amountString = amount.getText();
	    if(amountString.equals(""))
	    {
	    	output.setText("Invalid Command!");
	    	return;
	    }

	    try
	    {
	    	depositAmount = Double.parseDouble(amountString);
	    } catch(NumberFormatException e)
	    {
	    	output.setText("Not a valid amount.");
	    	return;
	    }

	    if (processTransaction(depositAmount, profile, typeString, true))
	    	output.setText("Deposit - balance updated.");
	}

	/**
	 * Withdraws money from an account based on the information in the input string.
	 * Prints an error message to the console if the input is invalid.
	 * @param com The input string containing information about the withdrawal.
	 */
	private void withdraw()
	{
	    String typeString = type.getValue();

	    Profile profile = createProfile(true);
	    if(profile == null) return;

	    double withdrawAmount;
	    String amountString = amount.getText();
	    if(amountString.equals(""))
	    {
	    	output.setText("Invalid Command!");
	    	return;
	    }

	    try
	    {
	    	withdrawAmount = Double.parseDouble(amountString);
	    } catch (NumberFormatException e)
	    {
	    	output.setText("Not a valid amount.");
	    	return;
	    }

	    if(processTransaction(withdrawAmount, profile, typeString, false))
	    	output.setText("Withdraw - balance updated.");
	}

	public void print()
	{
		output.setText(database.print());
	}

	public void printByAccountType()
	{
		output.setText(database.printByAccountType());
	}

	public void printFeeAndInterest()
	{
		output.setText(database.printFeeAndInterest());
	}

	public void update()
	{
		output.setText(database.update());
	}
}
