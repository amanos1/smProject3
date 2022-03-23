package smProject3;

/**
 * Subclass of Checking that contains information about
 * a College Checking account.
 * Additionally keeps track of which Rutgers campus a student is from.
 * @author Aaron Browne, Harshkumar Patel
 */
public class CollegeChecking extends Checking
{
	private int campus;

	private static final int NEW_BRUNSWICK = 0;
	private static final int NEWARK = 1;
	private static final int CAMDEN = 2;
	private static final double YEARLY_INTEREST = 0.0025;

	/**
	 * Creates an instance of the CollegeChecking class.
	 * @param holder A Profile representing the owner of the account.
	 * @param init The initial deposit.
	 * @param campusCode The campus code for the account. 0 if New Brunswick, 1 if Newark, and 2 if Camden.
	 */
	public CollegeChecking(Profile holder, double init, int campusCode)
	{
		this.holder = holder;
		this.balance = init;
		this.campus = campusCode;
	}

	/**
	 * Creates an instance of the CollegeChecking class.
	 * @param holder A Profile representing the owner of the account.
	 * @param init The initial deposit.
	 * @param campusCode The campus code for the account. 0 if New Brunswick, 1 if Newark, and 2 if Camden.
	 */
	public CollegeChecking(Profile holder, double init, String campus)
	{
		this.holder = holder;
		this.balance = init;
		switch(campus)
		{
		case "New Brunswick":
			this.campus = NEW_BRUNSWICK;
			break;
		case "Camden":
			this.campus = CAMDEN;
			break;
		case "Newark":
			this.campus = NEWARK;
			break;
		}
	}

	/**
	 * The College Checking account has no monthly fee, so this function will return 0.
	 * @return The monthly fee.
	 */
	@Override
	public double fee()
	{
		return 0;
	}

	/**
	 * Returns the monthly interest.
	 * @return The monthly interest.
	 */
	@Override
	public double monthlyInterest()
	{
		return balance * (YEARLY_INTEREST / Month.TOTAL_MONTHS);
	}

	/**
	 * Returns a string containing the type of account.
	 * For use with the Account.toString method.
	 * @return a string containing the type of account
	 */
	@Override
	public String getType()
	{
		return "College Checking";
	}

	/**
	 * Returns the name of the campus the account is registered to.
	 * @return The name of the campus the account is registered to.
	 */
	public String getCampus()
	{
		switch(campus)
		{
			case NEW_BRUNSWICK: return "NEW_BRUNSWICK";
			case CAMDEN: return "CAMDEN";
			case NEWARK: return "NEWARK";
			default: return "";
		}
	}

	/**
	 * Returns the campus code.
	 * @return The campus code.
	 */
	public int getCampusCode()
	{
		return campus;
	}

	/**
	 * Reopens an account and initializes it with the information in the given Account.
	 * @param acc The account to copy information from.
	 */
	@Override
	public void unclose(Account acc)
	{
		super.unclose(acc);

		CollegeChecking cc = (CollegeChecking) acc;
		campus = cc.getCampusCode();
	}

	/**
	 * Returns a string representation of the Account
	 * @return a string representation of the Account
	 */
	@Override
	public String toString()
	{
		String acc = super.toString();
		acc += "::" + getCampus();
		return acc;
	}
}
