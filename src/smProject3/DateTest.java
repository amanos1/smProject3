package smProject3;

import static org.junit.jupiter.api.Assertions.*;

class DateTest {

	@org.junit.Test
	public void isValid() {
		Date d;

		d = new Date("5/22/1883");
		assertFalse(d.isValid());

		d = new Date("16/3/1984");
		assertFalse(d.isValid());

		d = new Date("0/15/2002");
		assertFalse(d.isValid());

		d = new Date("2/29/1993");
		assertFalse(d.isValid());
		
		d = new Date("2/-1/1993");
		assertFalse(d.isValid());
		
		d = new Date("2/31/1984");
		assertFalse(d.isValid());
		
		d = new Date("6/31/2000");
		assertFalse(d.isValid());
		
		d = new Date("6/0/2000");
		assertFalse(d.isValid());
		
		d = new Date("12/500/1994");
		assertFalse(d.isValid());
		
		d = new Date("12/0/1994");
		assertFalse(d.isValid());
		
		d = new Date("3/3/2019");
		assertTrue(d.isValid());
	}

}
