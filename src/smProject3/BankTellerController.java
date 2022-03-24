package smProject3;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This class is responsible for handling all the functions of the Bank Teller GUI.
 * @author Aaron Browne, Harshkumar Patel
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
	@FXML private javafx.scene.control.Button quitButton;

	@FXML private TextArea output;

	/**
	 * Initializes the vales and sets up the program.
	 */
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
		amount.setDisable(true);
		disableHandlers();
		database = new AccountDatabase();
	}

	/**
	 * Enables and disables certain fields based on what state the GUI is in.
	 */
	public void disableHandlers()
	{
		type.getSelectionModel().selectedItemProperty().addListener((property, oldValue, newValue) -> {
			if(action.getValue().equals("Open"))
			{
				if(newValue.equals("College Checking") || newValue.equals("Checking"))
				{
					loyal.setDisable(true);
					if(newValue.equals("Checking"))
					{
						campus.setDisable(true);
					}
					if(newValue.equals("College Checking"))
					{
						campus.setDisable(false);
					}
				} else
				{
					campus.setDisable(true);
					if(newValue.equals("Savings")) loyal.setDisable(false);
					else loyal.setDisable(true);
				}
			}
		});

		action.getSelectionModel().selectedItemProperty().addListener((property, oldValue, newValue) -> {
			if(newValue.equals("Open"))
			{
				init.setDisable(false);
			} else
			{
				init.setDisable(true);
			}

			if(newValue.equals("Deposit") || newValue.equals("Withdraw"))
			{
				amount.setDisable(false);
			} else
			{
				amount.setDisable(true);
			}
		});
	}

	/**
	 * This is a event handler for 'submit' button, based on options chosen by the user it calls on specific methods to do so.
	 */
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

	/**
	 * This is the even handler for 'quit' button and it closes the UI window
	 */
	public void quit()
	{
		Stage stage = (Stage) quitButton.getScene().getWindow();
	    stage.close();
	}


	/**
	 * Creates a Profile object with the information provided.
	 * @param open True if you're opening a file and false if not.
	 * @return The resulting profile or null if the data in the input string would result in an invalid profile.
	 */
	private Profile createProfile(boolean open)
	{
	    String name = holder.getText();

	    if(name.equals("")) {
	    	if (open) output.appendText("\nMissing data for opening an account.");
	    	else      output.appendText("\nMissing data for closing an account.");
	    	return null;
	    }

	    String firstNLast[] = name.trim().split("\\s+");
	    if(firstNLast.length != 2)
	    {
	    	output.appendText("Please input a first and last name.");
	    	return null;
	    }

	    String dateOfBirth = dob.getText();
	    if(dateOfBirth.equals("")) {
	    	if (open) output.appendText("Missing data for opening an account.");
	    	else      output.appendText("Missing data for closing an account.");
	    	return null;
	    }

	    Date birth = new Date(dateOfBirth);

	    if(!birth.isValid())
	    {
	    	output.appendText("Date of birth invalid.");
	    	return null;
	    }

	    Profile p = new Profile(firstNLast[0], firstNLast[1], dateOfBirth);
		return p;
	}

	/**
	 * Creates an Account object with the info provided.
	 * @param type The type of account in string form.
	 * @param profile The holder of the account as a Profile object.
	 * @param init The initial deposit.
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
			    	output.appendText("Minimum of $2500 to"
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
	    	output.appendText("Missing data for opening an account.");
	    	return;
	    }

    	try
	    {
	    	initVal = Double.parseDouble(initString);
	    } catch(NumberFormatException e)
	    {
	    	output.appendText("Not a valid amount.");
	    	return;
	    }

    	if(initVal <= 0)
	    {
	    	output.appendText("Initial deposit cannot be 0 or negative.");
	    	return;
	    }

    	Account account = createAccount(typeString, profile, initVal);
	    if(account == null) return;

	    if(!database.open(account))
	    {
	    	if(!database.reopen(account))
	    	{
	    		output.appendText("\n"+profile +
	    				" same account(type) is in the database.");
	    	} else 
	    	{
	    		output.appendText("\nAccount reopened.");
	    	}
	    } else
	    {
	    	output.appendText("\nAccount opened.");
	    }
	}

	/**
	 * Closes an account with the information in the given string.
	 * Prints an error message if the command is invalid or the account cannot be closed.
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
	    	output.appendText("Account cannot be closed because"
	    			+ "it does not exist.");
	    	return;
	    }

	    if(!database.close(closeIt))
	    {
	    	output.appendText("\nAccount is closed already.");
	    } else
	    {
	    	output.appendText("\nAccount closed.");
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
			if(deposit) output.appendText("\nDeposit - "
					+ "amount cannot be 0 or negative.");
			else        output.appendText("\nWithdraw - "
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
			output.appendText(String.format("%s %s is not in the database.\n",holder, a.getType()));
			return false;
		}

		if(deposit) database.deposit(a);
		else
		{
			if(!database.withdraw(a))
			{
				output.appendText("\nWithdraw - insufficient fund.");
				return false;
			}
		}

		return true;
	}

	/**
	 * Deposits money into an account based on the information in the input string.
	 * Prints an error message to the console if the input is invalid.
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
	    	output.appendText("\nInvalid Command!");
	    	return;
	    }

	    try
	    {
	    	depositAmount = Double.parseDouble(amountString);
	    } catch(NumberFormatException e)
	    {
	    	output.appendText("\nNot a valid amount.");
	    	return;
	    }

	    if (processTransaction(depositAmount, profile, typeString, true))
	    	output.appendText("\nDeposit - balance updated.");
	}

	/**
	 * Withdraws money from an account based on the information in the input string.
	 * Prints an error message to the console if the input is invalid.
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
	    	output.appendText("\nInvalid Command!");
	    	return;
	    }

	    try
	    {
	    	withdrawAmount = Double.parseDouble(amountString);
	    } catch (NumberFormatException e)
	    {
	    	output.appendText("\nNot a valid amount.");
	    	return;
	    }

	    if(processTransaction(withdrawAmount, profile, typeString, false))
	    	output.appendText("\nWithdraw - balance updated.");
	}

	/**
	 * Outputs info about all the accounts to the text area.
	 */
	public void print()
	{
		output.appendText("\n"+database.print());
	}

	/**
	 * Outputs info about all the accounts sorted by account type to the text area.
	 */
	public void printByAccountType()
	{
		output.appendText("\n"+database.printByAccountType());
	}

	/**
	 * Outputs info about all the accounts with the monthly fee and interest to the text area.
	 */
	public void printFeeAndInterest()
	{
		output.appendText("\n"+database.printFeeAndInterest());
	}

	/**
	 * Outputs info about all the accounts with updated balances to the text area.
	 */
	public void update()
	{
		output.appendText("\n"+database.update());
	}
}
