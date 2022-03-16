package smProject3;

/**
 * Contains information about a single profile.
 * Keeps track of the first and last name, and the birth date.
 * @author Aaron Browne, Harshkumar Patel
 */
public class Profile
{
	private String fname;
	private String lname;
	private Date dob;

    /**
	 * Creates an instance of the Profile class given patient first name, last name and date of birth (as a string).
	 * @param fname String first name.
	 * @param lname String last name.
	 * @param date Date of birth.
	 */
	public Profile(String fname, String lname, String dob)
	{
		this.fname = fname;
		this.lname = lname;
		this.dob = new Date(dob);
	}

	/**
	 * Determines if two profiles are for the same person.
	 * @param p The profile to compare against.
	 * @return true if the accounts have the same name and birth date, false if not.
	 */
	public boolean isEquals(Profile p) 
	{
		return (fname.toLowerCase().equals(p.fname.toLowerCase())
				&& lname.toLowerCase().equals(p.lname.toLowerCase())
				&& dob.equals(p.dob));
	}

	/**
	 * Returns a string representation of the profile.
	 * @return A string representation of the profile.
	 */
	@Override
	public String toString()
	{
		return fname + " " + lname + " " + dob.toString();
	}
} 
