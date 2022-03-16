package smProject3;

/**
 * Class that stores all the Accounts in the database and manipulates it.
 * @author Aaron Browne, Harshkumar Patel
 */
public class AccountDatabase 
{
	private Account [] accounts;
	private int numAcct;

	private static final int GROW_AMOUNT = 4;

	/**
	 * Initializes the account database.
	 */
	public AccountDatabase() 
	{
		accounts = new Account[GROW_AMOUNT];
		numAcct = 0;
	}

	/**
	 * Searches for an account in the database.
	 * @param account The account to search for.
	 * @return Index of an account with the same holder & type in the accounts array, -1 if account does not exist.
	 */
	private int find(Account account)
	{
		for(int i = 0; i < numAcct; i++) 
		{
			if(accounts[i].equals(account)) return i;
		}
		return -1;
	}

	/**
	 * Increases the length of the accounts Array by four when the array is filled.
	 */
	private void grow() 
	{
		Account [] temp = new Account[accounts.length + GROW_AMOUNT];

		for(int i = 0; i < accounts.length; i++)
		{
			temp[i] = accounts[i];
		}

		accounts = temp;
	}

	/**
	 * Determines whether there is already a checking account with the same patient in the database.
	 * @param account The account we are checking if there is a duplicate of.
	 * @return true if the account already exists in the database, false if not.
	 */
	private boolean checkingConflict(Checking account)
	{
		if(account.getType().equals("Checking"))
		{
			CollegeChecking cc = new CollegeChecking(account.getHolder(), 1, 0);
			return !(find(cc) == -1);
		} else
		{
			Checking c = new Checking(account.getHolder(), 1);
			return !(find(c) == -1);
		}
	}

	/**
	 * Reopens a closed account.
	 * @param account An account with the same type and holder as the one to be reopened.
	 * @return false if the account is not in the database or if it's already open, true otherwise.
	 */
	public boolean reopen(Account account)
	{
		int index = find(account);
		if (index < 0) return false;
		if(!accounts[index].isClosed()) return false;

		accounts[index].unclose(account);
		return true;
	}

	/**
	 * Adds an account to the database if one with the same holder and type does not already exist.
	 * @param account The account to add to the database.
	 * @return true if the account was successfully added to the database, false if not.
	 */
	public boolean open(Account account)
	{
		if(numAcct == accounts.length)
		{
			grow();
		}

		if(!(find(account) == -1)) return false;

		if(account.getType().equals("Checking") || account.getType().equals("College Checking"))
		{
			if(checkingConflict((Checking) account)) return false;
		}

		accounts[numAcct++] = account;
		return true;
		
	}

	/**
	 * Closes an account, if it exists in the database.
	 * @param account The account to close.
	 * @return true if the close was successful, false if the account is already closed.
	 */
	public boolean close(Account account)
	{
		int accountIndex = find(account);

		Account close = accounts[accountIndex];
		if(close.isClosed()) return false;

		close.close();
		return true;
	}

	/**
	 * Determines whether or not an account is in the database.
	 * @param account The account to search for.
	 * @return true if an Account with the same holder & type in database, false if not.
	 */
	public boolean isThere(Account account)
	{
		return find(account) >= 0;
	}

	/**
	 * Adds money into an account.
	 * @param account The account to deposit into, with account.balance being the amount.
	 */
	public void deposit(Account account)
	{
		Account original = accounts[find(account)];
		original.deposit(account.balance);
	}

	/**
	 * Takes money from an account, if it has enough money.
	 * @param account The account to withdraw from, with account.balance being the amount.
	 * @return false if insufficient funds, true otherwise.
	 */
	public boolean withdraw(Account account)
	{
		Account original = accounts[find(account)];
		if(account.getBalance() > original.getBalance()) return false;
		original.withdraw(account.balance);
		return true;
	}

	/**
	 * Prints info about all the accounts in the system to the console.
	 * Prints in whatever order they may be in.
	 */
	public void print()
	{
		if(numAcct == 0)
		{
			System.out.println("Account Database is empty!");
			return;
		}

		System.out.println("\n*list of accounts in the database*");

		for(int i = 0; i < numAcct; i++)
		{
			Account a = accounts[i];
			System.out.println(a.toString());
		}

		System.out.println("*end of list.*\n");
	}

	/**
	 * Prints the same info as the print() method plus the monthly interest and fee for each account in the system.
	 * Prints in whatever order they may be in.
	 */
	public void printFeeAndInterest() 
	{
		if(numAcct == 0)
		{
			System.out.println("Account Database is empty!");
			return;
		}

		System.out.println("\n*list of accounts with fee and monthly interest");

		for(int i = 0; i < numAcct; i++)
		{
			Account a = accounts[i];
			System.out.printf("%s::fee $%.2f::monthly interest $%.2f\n", a, a.fee(), a.monthlyInterest());
		}

		System.out.println("*end of list.\n");
	}

	/**
	 * Sorts the accounts in the system by account type and prints them to the console.
	 * If two accounts are the same type, it does not matter which order they are in.
	 */
	public void printByAccountType() 
	{
		if(numAcct == 0)
		{
			System.out.println("Account Database is empty!");
			return;
		}

		for(int i = 0; i < numAcct; i++)
		{
			for(int j = i + 1; j < numAcct; j++)
			{
				if(accounts[i].getType().compareTo(accounts[j].getType()) > 0)
				{
					swap(i, j);
				}
			}
		}

		System.out.println("\n*list of accounts by account type.");

		for(int i = 0; i < numAcct; i++)
		{
			Account a = accounts[i];
			System.out.println(a.toString());
		}

		System.out.println("*end of list.\n");
	}

	/**
	 * Updates each of the account based on what they would be the next month and prints them to the console.
	 */
	public void update() 
	{
		if(numAcct == 0)
		{
			System.out.println("Account Database is empty!");
			return;
		}

		System.out.println("\n*list of accounts with updated balance");

		for(int i = 0; i < numAcct; i++) 
		{
			Account acc = accounts[i];
			acc.deposit(acc.monthlyInterest());
			acc.deductFees();
			Account a = accounts[i];
			System.out.println(a.toString());
		}

		System.out.println("*end of list.\n");
	}

	/**
	 * Swaps elements a and b in the accounts array.
	 * For use with the printByAccountType() method.
	 * @param a First element to be swapped.
	 * @param b Second element to be swapped.
	 */
	private void swap(int a, int b)
	{
		Account temp = accounts[a];
		accounts[a] = accounts[b];
		accounts[b] = temp;
	}

}
