package smProject3;

import java.util.StringTokenizer;

/**
 * 
 * I JUST COPIED AND PASTED A BUNCH OF FUNCTIONS FROM BANKTELLER FROM PROJECT 2, WE HAVE TO CHANGE THEM SO THAT THEY ACTUALLY WORK
 *
 */
public class BankTellerController
{
	private AccountDatabase database;
	private static final int MONEY_MARKET_MIN = 2500;

	public void buttonPressed()
	{
	}

	public void quit()
	{
	}

	/**
	 * Creates a Profile object with the information provided.
	 * @param input The input string containing information about the Profile.
	 * @param st The StringTokenizer we use to parse the input string. 
	 * @param open Boolean for whether the Profile is being used for opening or closing an account.
	 * @return The resulting profile or null if the data in the input string would result in an invalid profile.
	 */
	private Profile createProfile(String input, StringTokenizer st, boolean open)
	{
	    if(!st.hasMoreTokens()) {
	    	if (open) System.out.println("Missing data for opening an account.");
	    	else      System.out.println("Missing data for closing an account.");
	    	return null;
	    }
	    String fname = st.nextToken();

	    if(!st.hasMoreTokens()) {
	    	if (open) System.out.println("Missing data for opening an account.");
	    	else      System.out.println("Missing data for closing an account.");
	    	return null;
	    }
	    String lname = st.nextToken();

	    if(!st.hasMoreTokens()) {
	    	if (open) System.out.println("Missing data for opening an account.");
	    	else      System.out.println("Missing data for closing an account.");
	    	return null;
	    }

	    String dob = st.nextToken();
	    Date birth = new Date(dob);

	    if(!birth.isValid())
	    {
	    	System.out.println("Date of birth invalid.");
	    	return null;
	    }

	    Profile p = new Profile(fname, lname, dob);
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
	private Account createAccount(String input, String type,
			Profile profile, StringTokenizer st, double init)
	{
	    Account a;

    	switch(type)
	    {
		    case "C":
			    a = new Checking(profile, init);
		    	break;

		    case "CC":
			    if(!st.hasMoreTokens())
			    {
			    	System.out.println("Missing data for"
			    			+ "opening an account.");
			    	return null;
			    }

			    int campus;
			    try
			    {
			    	campus = Integer.parseInt(st.nextToken());
			    } catch(NumberFormatException e)
			    {
			    	System.out.println("Not a valid amount.");
			    	return null;
			    }

			    if(campus > 2 || campus < 0)
			    {
			    	System.out.println("Invalid campus code.");
			    	return null;
			    }

			    a = new CollegeChecking(profile, init, campus);
		    	break;

		    case "S":
			    if(!st.hasMoreTokens())
			    {
			    	System.out.println("Missing data for opening an account.");
			    	return null;
			    }

			    String loyal = st.nextToken();
		    	a = new Savings(profile, init, loyal.equals("1"));
		    	break;

		    case "MM":
		    	if (init < MONEY_MARKET_MIN)
		    	{
			    	System.out.println("Minimum of $2500 to"
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
	private void open(String com)
	{
		com = com.replaceAll("\\s+", " ");
	    StringTokenizer st = new StringTokenizer(com, " ");
	    st.nextToken();
	    if(!st.hasMoreTokens())
	    {
	    	System.out.println("Missing data for opening an account.");
	    	return;
	    }

	    String type = st.nextToken();

	    Profile profile = createProfile(com, st, true);
	    if(profile == null) return;

    	if(!st.hasMoreTokens())
	    {
	    	System.out.println("Missing data for opening an account.");
	    	return;
	    }

	    double init;
	    try
	    {
	    	init = Double.parseDouble(st.nextToken());
	    } catch(NumberFormatException e)
	    {
	    	System.out.println("Not a valid amount.");
	    	return;
	    }

    	if(init <= 0)
	    {
	    	System.out.println("Initial deposit cannot be 0 or negative.");
	    	return;
	    }

    	Account account = createAccount(com, type, profile, st, init);
	    if(account == null) return;

	    if(!database.open(account))
	    {
	    	if(!database.reopen(account))
	    	{
	    		System.out.println(profile +
	    				" same account(type) is in the database.");
	    	} else 
	    	{
	    		System.out.println("Account reopened.");
	    	}
	    } else
	    {
	    	System.out.println("Account opened.");
	    }
	}

	/**
	 * Closes an account with the information in the given string.
	 * Prints an error message if the command is invalid or the account cannot be closed.
	 * @param com The input string containing information about the account to be closed.
	 */
	private void close(String com)
	{
		com = com.replaceAll("\\s+", " ");
	    StringTokenizer st = new StringTokenizer(com, " ");
	    st.nextToken();
	    if(!st.hasMoreTokens())
	    {
	    	System.out.println("Invalid Command!");
	    	return;
	    }

	    String type = st.nextToken();

	    Profile profile = createProfile(com, st, false);
	    if(profile == null) return;

	    Account closeIt;
	    if(type.equals("C")) closeIt = new Checking(profile, 1);
	    else if(type.equals("CC")) closeIt = 
	    		new CollegeChecking(profile, 1, 0);
	    else if(type.equals("S")) closeIt = new Savings(profile, 1, true);
	    else closeIt = new MoneyMarket(profile, MONEY_MARKET_MIN);

	    if(!database.isThere(closeIt))
	    {
	    	System.out.println("Account cannot be closed because"
	    			+ "it does not exist.");
	    	return;
	    }

	    if(!database.close(closeIt))
	    {
	    	System.out.println("Account is closed already.");
	    } else
	    {
	    	System.out.println("Account closed.");
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
			if(deposit) System.out.println("Deposit - "
					+ "amount cannot be 0 or negative.");
			else        System.out.println("Withdraw - "
					+ "amount cannot be 0 or negative.");
			return false;
		}

		Account a;
		switch(type)
		{
			case "C":
				a = new Checking(holder, amount);
				break;
			case "CC":
				a = new CollegeChecking(holder, amount, 0);
				break;
			case "S":
				a = new Savings(holder, amount, true);
				break;
			case "MM":
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
				System.out.println("Withdraw - insufficient fund.");
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
	private void deposit(String com)
	{
		com = com.replaceAll("\\s+", " ");
		StringTokenizer st = new StringTokenizer(com, " ");
	    st.nextToken();
	    if(!st.hasMoreTokens())
	    {
	    	System.out.println("Invalid Command!");
	    	return;
	    }

	    String type = st.nextToken();

	    Profile profile = createProfile(com, st, true);
	    if(profile == null) return;
	    if(!st.hasMoreTokens())
	    {
	    	System.out.println("Invalid Command!");
	    	return;
	    }

    	double depositAmount;
	    try
	    {
	    	depositAmount = Double.parseDouble(st.nextToken());
	    } catch(NumberFormatException e)
	    {
	    	System.out.println("Not a valid amount.");
	    	return;
	    }

	    if (processTransaction(depositAmount, profile, type, true))
	    	System.out.println("Deposit - balance updated.");
	}

	/**
	 * Withdraws money from an account based on the information in the input string.
	 * Prints an error message to the console if the input is invalid.
	 * @param com The input string containing information about the withdrawal.
	 */
	private void withdraw(String com)
	{
		com = com.replaceAll("\\s+", " ");
		StringTokenizer st = new StringTokenizer(com, " ");
	    st.nextToken();
	    if(!st.hasMoreTokens())
	    {
	    	System.out.println("Invalid Command!");
	    	return;
	    }

	    String type = st.nextToken();

	    Profile profile = createProfile(com, st, true);
	    if(profile == null) return;
	    if(!st.hasMoreTokens())
	    {
	    	System.out.println("Invalid Command!");
	    	return;
	    }

	    double withdrawAmount;
	    try
	    {
	    	withdrawAmount = Double.parseDouble(st.nextToken());
	    } catch (NumberFormatException e)
	    {
	    	System.out.println("Not a valid amount.");
	    	return;
	    }

	    if(processTransaction(withdrawAmount, profile, type, false))
	    	System.out.println("Withdraw - balance updated.");
	}
}
