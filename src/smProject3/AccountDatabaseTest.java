package smProject3;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class used to test the open and close methods in the AccountDatabase class.
 * @author Aaron Browne, Harshkumar Patel
 */
public class AccountDatabaseTest
{

	/**
	 * Performs a series of tests on the open function.
	 */
	@org.junit.Test
	public void open()
	{
		AccountDatabase database = new AccountDatabase();
		Profile p = new Profile("Whatever", "Lastname", "2/24/2014");

		//test case 1, return true if account successfully added
		Checking c = new Checking(p, 500);
		assertTrue(database.open(c));

		//test case 2, return false if same account type & holder is in the database
		assertFalse(database.open(c));

		//test case 3, college checking and checking are considered conflicting types
		CollegeChecking cc = new CollegeChecking(p, 500, 1);
		assertFalse(database.open(cc));

		//test case 4, return false if same account type & holder is in the database
		Savings s = new Savings(p, 500, true);
		assertTrue(database.open(s));
		assertFalse(database.open(s));

		//test case 5, does not add to database if the same account exists and is closed
		database.close(c);
		assertFalse(database.open(c));
	}

	/**
	 * Performs a series of tests on the close function.
	 */
	@org.junit.Test
	public void close()
	{
		AccountDatabase database = new AccountDatabase();
		Profile p = new Profile("Whatever", "Lastname", "2/24/2014");

		//NOTE: The AccountDatabase.close() function does not handle the exception if the account is not the accounts array

		//test case 1, returns true if the account is closed successfully
		Savings s = new Savings(p, 500, true);
		database.open(s);
		assertTrue(database.close(s));

		//test case 2, returns false if the account is already closed
		assertFalse(database.close(s));
	}
}
