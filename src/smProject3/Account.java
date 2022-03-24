package smProject3;
import java.text.DecimalFormat;

/**
 * Abstract class that contains information about a single account.
 * @author Aaron Browne, Harshkumar Patel
 */
public abstract class Account {
	protected Profile holder;
	protected boolean closed;
	protected double balance;

	/**
	 * This constructor with no arguments will never be run, but I need it to run the program without an error.
	 */
	public Account() { }

	/**
	 * Creates an instance of the Account class when given the holder and an initial deposit.
	 * For use with the checking class.
	 * @param holder The holder of the account. Should be Profile class.
	 * @param init The initial deposit.
	 */
	public Account(Profile holder, int init)
	{
		this.holder = holder;
		this.balance = init;
	}

	/**
	 * Determines if two accounts are equal to each other.
	 * @return true if the accounts have the same holder and type, false if not.
	 */
	@Override
	public boolean equals(Object obj)
	{
		Account newAccount = (Account) obj;
		return this.holder.isEquals(newAccount.holder)
				&& getType().equals(newAccount.getType());
	}

	/**
	 * Returns a string representation of the Account
	 * @return a string representation of the Account
	 */
	@Override
	public String toString()
	{
		DecimalFormat df = new DecimalFormat("###,##0.00");
		String acc = String.format("%s::%s::Balance $%s",
				getType(), holder, df.format(balance));
		if(closed) acc += "::CLOSED";
		return acc;
	}

	/**
	 * Takes out an amount of money from an account.
	 * @param amount Amount of money to take out.
	 */
	public void withdraw(double amount)
	{
		balance -= amount;
	}

	/**
	 * Adds an amount of money to an account.
	 * @param amount Amount of money to add.
	 */
	public void deposit(double amount)
	{
		balance += amount;
	}

	/**
	 * Returns the balance.
	 * @return The balance.
	 */
	public double getBalance()
	{
		return balance;
	}

	/**
	 * Returns whether or not the account is closed.
	 * @return true if account is closed, false if not.
	 */
	public boolean isClosed()
	{
		return closed;
	}

	/**
	 * Sets an account to closed and resets the balance to zero.
	 */
	public void close()
	{
		closed = true;
		balance = 0;
	}

	/**
	 * Reopens an account and initializes it with the information in the given Account.
	 * @param acc The account to copy information from.
	 */
	public void unclose(Account acc)
	{
		closed = false;
		balance = acc.getBalance();
	}
	
	/**
	 * Deducts fees from balance if it is not waived.
	 */
	public void deductFees() 
	{
		if(balance > fee()) balance -= fee();
		else balance = 0;
	}

	/**
	 * Returns the monthly interest.
	 * @return The monthly interest.
	 */
	public abstract double monthlyInterest();

	/**
	 * Returns the monthly fee.
	 * @return The monthly fee.
	 */
	public abstract double fee();

	/**
	 * Returns the account type (class name).
	 * @return The account type as a string.
	 */
	public abstract String getType();
}
