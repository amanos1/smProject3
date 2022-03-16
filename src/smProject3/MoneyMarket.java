package smProject3;

/**
 * Subclass of Savings that contains information about a Money Market account.
 * Additionally keeps track of the amount of withdrawals total.
 * @author Aaron Browne, Harshkumar Patel
 */
public class MoneyMarket extends Savings
{
	private int withdrawls;

	private static final double YEARLY_INTEREST = 0.008;
	private static final double LOYAL_INTEREST = 0.0095;
	private static final double FEE = 10;
	private static final int FEE_WAIVE = 2500;
	private static final int TOTAL_ALLOWED_WITHDRAWALS = 3;

	/**
	 * Creates an instance of the MoneyMarket class when given the holder and initial deposit.
	 * @param holder The holder of the account. Should be Profile class.
	 * @param init The initial deposit.
	 */
	public MoneyMarket(Profile holder, double init)
	{
		this.holder = holder;
		this.balance = init;
		this.loyal = true;
		this.withdrawls = 0;
	}

	/**
	 * Removes money from an account.
	 * Sets to account to longer be loyal if it drops below $2,500.
	 * @param amount The amount to remove.
	 */
	@Override
	public void withdraw(double amount)
	{
		super.withdraw(amount);
		if(balance < FEE_WAIVE) loyal = false;
		withdrawls++;
	}

	/**
	 * Reopens an account and initializes it with the information in the given Account.
	 * @param acc The account to copy information from.
	 */
	@Override
	public void unclose(Account acc)
	{
		super.unclose(acc);
		loyal = true;
	}

	/**
	 * Returns the monthly interest.
	 * @return The monthly interest.
	 */
	@Override
	public double monthlyInterest()
	{
		if(loyal) return balance * (LOYAL_INTEREST / Month.TOTAL_MONTHS);
		return           balance * (YEARLY_INTEREST / Month.TOTAL_MONTHS);
	}

	/**
	 * Returns the monthly fee.
	 * Returns 0 if the fee is waived.
	 * @return The monthly fee.
	 */
	@Override
	public double fee()
	{
		if(balance >= FEE_WAIVE
				&& withdrawls <= TOTAL_ALLOWED_WITHDRAWALS) return 0;
		return FEE;
	}

	/**
	 * Returns a string containing the type of account.
	 * For use with the AccountDatabase.print method.
	 * @return A string containing the type of account
	 */
	@Override
	public String getType()
	{
		return "Money Market Savings";
	}

	/**
	 * Returns a string representation of the Account
	 * @return a string representation of the Account
	 */
	@Override
	public String toString()
	{
		String acc = super.toString();
		acc += "::withdrawl: " + withdrawls;
		return acc;
	}
}
