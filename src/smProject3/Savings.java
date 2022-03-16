package smProject3;

/**
 * Subclass of Account that contains information about a Savings account.
 * Additionally keeps track of whether the account is loyal or not.
 * @author Aaron Browne, Harshkumar Patel
 */
public class Savings extends Account
{
	protected boolean loyal;

	private static final double INTEREST = 0.003;
	private static final double LOYAL_INTEREST = 0.0045;
	private static final double FEE = 6;
	private static final int FEE_WAIVE = 300;

	/**
	 * This constructor with no arguments will never be run, but I need it to run the program without an error.
	 */
	public Savings() { }

	/**
	 * Creates an instance of the Savings class when given the holder and an initial deposit.
	 * @param holder The holder of the account. Should be Profile class.
	 * @param init the initial deposit.
	 */
	public Savings(Profile holder, double init, boolean loyal)
	{
		this.holder = holder;
		this.balance = init;
		this.loyal =  loyal;
	}

	/**
	 * Returns the monthly interest.
	 * @return The monthly interest.
	 */
	public double monthlyInterest()
	{
		if(loyal) 
			return balance * (LOYAL_INTEREST / Month.TOTAL_MONTHS);
		return     balance * (INTEREST / Month.TOTAL_MONTHS);
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
	 * Does the same as the general Account's close method, but also sets the account to be not loyal.
	 */
	@Override
	public void close()
	{
		super.close();
		loyal = false;
	}

	/**
	 * Returns a string containing the type of account.
	 * For use with the AccountDatabase.print method.
	 * @return A string containing the type of account
	 */
	public String getType()
	{
		return "Savings";
	}

	/**
	 * Returns a string representation of the Account
	 * @return a string representation of the Account
	 */
	@Override
	public String toString()
	{
		String acc = super.toString();
		if(loyal) acc += "::Loyal";
		return acc;
	}
}
