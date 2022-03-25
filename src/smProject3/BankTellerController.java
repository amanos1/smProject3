package smProject3;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
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

	@FXML private RadioButton actionOpen;
	@FXML private RadioButton actionClose;
	@FXML private RadioButton actionDeposit;
	@FXML private RadioButton actionWithdraw;

	@FXML private RadioButton typeChecking;
	@FXML private RadioButton typeCollegeChecking;
	@FXML private RadioButton typeSavings;
	@FXML private RadioButton typeMoneyMarket;

	@FXML private RadioButton loyalYes;
	@FXML private RadioButton loyalNo;

	@FXML private RadioButton campusN;
	@FXML private RadioButton campusNB;
	@FXML private RadioButton campusC;
	@FXML private javafx.scene.control.Button quitButton;

	@FXML private TextArea output;

	private final ToggleGroup action = new ToggleGroup();
	private final ToggleGroup type = new ToggleGroup();
	private final ToggleGroup loyal = new ToggleGroup();
	private final ToggleGroup campus = new ToggleGroup();

	/**
	 * Initializes the vales and sets up the program.
	 */
	@Override
	public void initialize(URL url, ResourceBundle bundle)
	{
		actionOpen.setToggleGroup(action);
		actionOpen.setSelected(true);
		actionClose.setToggleGroup(action);
		actionDeposit.setToggleGroup(action);
		actionWithdraw.setToggleGroup(action);

		typeChecking.setToggleGroup(type);
		typeChecking.setSelected(true);
		typeChecking.setUserData("Checking");
		typeCollegeChecking.setToggleGroup(type);
		typeCollegeChecking.setUserData("College Checking");
		typeSavings.setToggleGroup(type);
		typeSavings.setUserData("Savings");
		typeMoneyMarket.setToggleGroup(type);
		typeMoneyMarket.setUserData("Money Market");

		loyalYes.setToggleGroup(loyal);
		loyalYes.setSelected(true);
		loyalYes.setUserData("Loyal");
		loyalNo.setToggleGroup(loyal);
		loyalNo.setUserData("Not Loyal");
		disableLoyal(true);

		campusNB.setToggleGroup(campus);
		campusNB.setSelected(true);
		campusNB.setUserData("New Brunswick");
		campusN.setToggleGroup(campus);
		campusN.setUserData("Newark");
		campusC.setToggleGroup(campus);
		campusC.setUserData("Camden");
		disableCampus(true);

		amount.setDisable(true);
		disableHandlers();
		database = new AccountDatabase();
	}

	/**
	 * Sets all three of the campus radio buttons to the same disabled value.
	 * @param doIt The disabled value.
	 */
	private void disableCampus(boolean doIt)
	{
		campusNB.setDisable(doIt);
		campusN.setDisable(doIt);
		campusC.setDisable(doIt);
	}

	/**
	 * Sets both of the loyal radio buttons to the same disabled value.
	 * @param doIt The disabled value.
	 */
	private void disableLoyal(boolean doIt)
	{
		loyalYes.setDisable(doIt);
		loyalNo.setDisable(doIt);
	}

	/**
	 * Enables and disables certain fields based on what state the GUI is in.
	 */
	public void disableHandlers()
	{
		type.selectedToggleProperty().addListener((property, oldValue, newValue) -> {
			if(action.getSelectedToggle() == actionOpen)
			{
				if(newValue == typeCollegeChecking)
				{
					disableCampus(false);
					disableLoyal(true);
				} else if (newValue == typeSavings)
				{
					disableLoyal(false);
					disableCampus(true);
				} else
				{
					disableLoyal(true);
					disableCampus(true);
				}
			} else
			{
				disableLoyal(true);
				disableCampus(true);
			}
		});

		action.selectedToggleProperty().addListener((property, oldValue, newValue) -> {
			if(newValue == actionOpen)
			{
				init.setDisable(false);
				amount.setDisable(true);
				if(type.getSelectedToggle() == typeSavings) disableLoyal(false);
				else disableLoyal(true);
				if(type.getSelectedToggle() == typeCollegeChecking) disableCampus(false);
				else disableCampus(true);
			} else if(newValue == actionDeposit || newValue == actionWithdraw)
			{
				amount.setDisable(false);
				init.setDisable(true);
				disableLoyal(true);
				disableCampus(true);
			} else
			{
				init.setDisable(true);
				amount.setDisable(true);
				disableLoyal(true);
				disableCampus(true);
			}
		});
	}

	/**
	 * This is a event handler for 'submit' button, based on options chosen by the user it calls on specific methods to do so.
	 */
	public void buttonPressed()
	{
		Toggle actionString = action.getSelectedToggle();
             if(actionString == actionOpen) open();
        else if(actionString == actionClose) close();
        else if(actionString == actionDeposit) deposit();
        else if(actionString == actionWithdraw) withdraw();
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
	    	output.appendText("\nPlease input a first and last name.");
	    	return null;
	    }

	    String dateOfBirth = dob.getText();
	    if(dateOfBirth.equals("")) {
	    	if (open) output.appendText("\nMissing data for opening an account.");
	    	else      output.appendText("\nMissing data for closing an account.");
	    	return null;
	    }

	    Date birth = new Date(dateOfBirth);

	    if(!birth.isValid())
	    {
	    	output.appendText("\nDate of birth invalid.");
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
			    String campusString = campus.getSelectedToggle().getUserData().toString();
			    
			    a = new CollegeChecking(profile, init, campusString);
		    	break;

		    case "Savings":
			    String loyalString = loyal.getSelectedToggle().getUserData().toString();
		    	a = new Savings(profile, init, loyalString.equals("Yes"));
		    	
		    	break;

		    case "Money Market":
		    	if (init < MONEY_MARKET_MIN)
		    	{
			    	output.appendText("\nMinimum of $2500 to"
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
	    String typeString = type.getSelectedToggle().getUserData().toString();

	    Profile profile = createProfile(true);
	    if(profile == null) return;


	    double initVal;
	    String initString = init.getText();
    	if(initString.equals(""))
	    {
	    	output.appendText("\nMissing data for opening an account.");
	    	return;
	    }

    	try
	    {
	    	initVal = Double.parseDouble(initString);
	    } catch(NumberFormatException e)
	    {
	    	output.appendText("\nNot a valid amount.");
	    	return;
	    }

    	if(initVal <= 0)
	    {
	    	output.appendText("\nInitial deposit cannot be 0 or negative.");
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
	    String typeString = type.getSelectedToggle().getUserData().toString();

	    Profile profile = createProfile(false);
	    if(profile == null) return;

	    Account closeIt;
	    if(typeString.equals("Checking")) closeIt = new Checking(profile, 1);
	    else if(typeString.equals("College Checking")) closeIt = 
	    		new CollegeChecking(profile, 1, 0);
	    else if(typeString.equals("Savings")) closeIt = new Savings(profile, 1, true);
	    else closeIt = new MoneyMarket(profile, MONEY_MARKET_MIN);

	    if(!database.isThere(closeIt))
	    {
	    	output.appendText("\nAccount cannot be closed because"
	    			+ " it does not exist.");
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
			output.appendText("\n"+String.format("%s %s is not in the database.",holder, a.getType()));
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
	    String typeString = type.getSelectedToggle().getUserData().toString();

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
	    String typeString = type.getSelectedToggle().getUserData().toString();

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
