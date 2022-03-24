package smProject3;

import java.util.Calendar;

/**
 * Contains information about a single date.
 * This includes the month, day of month, and year.
 * Can determine if the date is a valid calendar date.
 * This is the same Date class from project 1.
 * @author Aaron Browne, Harshkumar Patel
 */
public class Date implements Comparable<Date>
{
	private int year;
	private int month;
	private int day;

	final static int MIN_YEAR = 1900;
	final static int MIN_MONTH = 1;
	final static int TOTAL_FEB_DAYS = 28;
	final static int TOTAL_FEB_LEAP_DAYS = 29;
	final static int TOTAL_AJSN_DAYS = 30; //AJSN = April, June, September, and November
	final static int TOTAL_MONTH_DAYS = 31;

	/**
	 * When given no arguments, the constructor creates an instance of date with the current date,
	 * as provided by Java's Calendar library.
	 */
	public Date() 
	{
		Calendar calendar = Calendar.getInstance();
		this.month  = calendar.get(Calendar.MONTH);
		this.year = calendar.get(Calendar.YEAR);
		this.day = calendar.get(Calendar.DATE);
	}

	/**
	 * When given a string, this constructor parses the string and creates an instance of date with it.
	 * @param date The date string in mm/dd/yyyy format.
	 */
    public Date(String date)
    {
    	String[] dateElements = date.split("/");
    	try
    	{
	        month = Integer.parseInt(dateElements[0]);
	        day = Integer.parseInt(dateElements[1]);
	        year = Integer.parseInt(dateElements[2]);
    	} catch(NumberFormatException e)
		{
			this.month  = 0;
			this.year = 0;
			this.day = 0;
		}
    }

    /**
     * Creates a clone of the date provided.
     * @param d The date to clone.
     */
	public Date(Date d)
	{
		this.month = d.getMonth();
		this.day = d.getDay();
		this.year = d.getYear();
	}

	/**
	 * Creates an instance of Date with the month, day and year provided.
	 * @param m The month.
	 * @param d The day of the month.
	 * @param y The year.
	 */
	public Date(int m, int d, int y) 
	{
		this.month = m;
		this.day = d;
		this.year = y;
	}

	/**
	 * Returns the day of the month
	 * @return The day of the month
	 */
	public int getDay()
	{
		int d = this.day;
		return d;
	}

	/**
	 * Returns the month.
	 * @return The month.
	 */
	public int getMonth() 
	{
		int m = this.month;
		return m;
	}

	/**
	 * Returns the year.
	 * @return The year.
	 */
	public int getYear() 
	{
		int y = this.year;
		return y;
	}

	/**
	 * Changes the day of the month.
	 * @param d The new day of the month.
	 */
	public void setDay(int d) 
	{
		this.day = d;
	}

	/**
	 *  Changes the month.
	 * @param m The new month.
	 */
	public void setMonth(int m) 
	{
	    this.month = m;
	}

	/**
	 * Changes the year.
	 * @param y The new year.
	 */
	public void setYear(int y) 
	{
		this.year = y;
	}

	/**
	 * Determines whether or not the date is a valid calendar date.
	 * Does not allow years before 1900 or after the current one.
	 * Takes into account the amount of days of different months and leap years.
	 * @return true if the date is valid, false if not.
	 */
	public boolean isValid()
	{
		boolean leapYear = isLeapYear(year);
		Calendar today = Calendar.getInstance();

		if(year < MIN_YEAR || year > today.get(Calendar.YEAR)) return false;

		if(month > Month.TOTAL_MONTHS || month < MIN_MONTH) return false;

		if(day < 1) return false;
		if(month == Month.FEB)
		{
			if(leapYear)
			{
				if(day > TOTAL_FEB_LEAP_DAYS) return false;
			} else
			{
				if(day > TOTAL_FEB_DAYS) return false;
			}
		} else if(month == Month.APR || month  == Month.JUN || month == Month.SEP || month == Month.NOV) //MONTHS WITH 30 DAYS
		{
			if(day > TOTAL_AJSN_DAYS) return false;
		} else  //MONTHS WITH 31 DAYS
		{
			if(this.day > TOTAL_MONTH_DAYS) return false;
		}

		return true;
	}

	/**
	 * Determines whether or not the given year is a leap year.
	 * @param y The year to analyze.
	 * @return true if the year is a leap year, false if not.
	 */
	private boolean isLeapYear(int y) 
	{
		final int QUADRENNIAL = 4;
		final int CENTENNIAL = 100;
	    final int QUATERCENTENNIAL = 400;
	    if(y % QUADRENNIAL == 0) 
	    {
	    	if(y % CENTENNIAL == 0) 
	    	{
	    		if(y % QUATERCENTENNIAL == 0) 
	    		{
	    			return true;
	    		}else 
	    		{
	    			return false;
	    		}
	    	}else 
	    	{
	    		return true;
	    	}
	    }else 
	    {
	    	return false;
	    }
	}

	/**
	 * Determine whether two dates are equal.
	 * @param date The date to compare against.
	 * @return true if the dates are equal, false if not.
	 */
	public boolean equals(Date date)
	{
		return compareTo(date) == 0;
	}

	/**
	 * Compares two dates.
	 * @param date The date to compare the current one to.
	 * @return 1 if the current date occurs after the given one, -1 if the current date occurs before, and 0 if the the dates are the same.
	 */
	@Override
	public int compareTo(Date date)
	{
		if(year > date.getYear()) return 1;
		else if(year < date.getYear()) return -1;
		if(month > date.getMonth()) return 1;
		else if(month < date.getMonth()) return -1;
		if(day > date.getDay()) return 1;
		else if(day < date.getDay()) return -1;
		return 0;
	}

	/**
	 * Converts the date into a string
	 * @return A string representation of the date in mm/dd/yyyy format.
	 */
	public String toString()
	{
		return month + "/" + day + "/" + year;
	}
}
