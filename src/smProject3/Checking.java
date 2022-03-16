package smProject3;

/**
 * Subclass of Account that contains information about a Checking account.
 * @author Aaron Browne, Harshkumar Patel
 */
public class Checking extends Account
{
	private static final double YEARLY_INTEREST = 0.001;
	private static final double FEE = 25;
	private static final int FEE_WAIVE = 1000;

	/**
	 * This constructor with no arguments will never be run, but I need it to run the program without an error.
	 */
	public Checking() { }

	/**
	 * Creates an instance of the Checking class when given just the holder.
	 * @param holder The holder of the account. Should be Profile class.
	 */
	public Checking(Profile holder, double init)
	{
		this.holder = holder;
		this.balance = init;
	}

	/**
	 * Returns the monthly interest.
	 * @return The monthly interest.
	 */
	public double monthlyInterest()
	{
		return balance * (YEARLY_INTEREST / Month.TOTAL_MONTHS);
	}

	/**
	 * Returns the holder of the account.
	 * @return The holder of the account as a Profile class.
	 */
	public Profile getHolder()
	{
		return holder;
	}

	/**
	 * Returns the monthly fee.
	 * Returns 0 if the fee is waived.
	 * @return The monthly fee.
	 */
	public double fee()
	{
		if(balance >= FEE_WAIVE) return 0;
		return FEE;
	}

	/**
	 * Returns a string containing the type of account.
	 * For use with the AccountDatabase.print method.
	 * @return A string containing the type of account
	 */
	public String getType()
	{
		return "Checking";
	}
}
