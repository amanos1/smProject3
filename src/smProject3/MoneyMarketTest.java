package smProject3;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class used to test the monthlyInterest method in the MoneyMarket class.
 * @author Harshkumar Patel
 */
public class MoneyMarketTest
{

	/**
	 * Performs a series of tests on the monthlyInterest function.
	 */
	@org.junit.Test
	void monthlyInterest() {
		MoneyMarket mm;
		Profile h = new Profile("Harsh", "Patel","8/18/2001");
		mm = new MoneyMarket(h,3000);

		//Loyal account with 0.95% Annual Interest Rate
		assertTrue(mm.monthlyInterest() == 2.38);
		
		//withdrawing 1000 will make the balance drop below 2500 which will make the accout NOT loyal
		mm.withdraw(1000);
		//Calculated monthly interest using 0.8% as annual interest rate
		assertTrue(mm.monthlyInterest() == 1.33);

	}

}
