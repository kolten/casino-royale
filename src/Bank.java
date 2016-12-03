public class Bank {
	
	private float credits;
	
	/**
	* Default constructor that initializes with 0 credits
	*/
	public Bank()
	{
		setCredits(0);
	}
	
	/**
	* Default constructor that initializes with a defined number of credits
	* @param c Number of credits to start with
	*/
	public Bank(float c)
	{
		setCredits(c);
	}
	
	/**
	* Increases credits by the amount specified.
	 * @param c Number of credits to add
	 */
	public void addCredits(float c)
	{
		credits += c;
	}
	
	/**
	* Decreases credits by the amount specified.
	 * @param c Number of credits to remove
	 */
	public void subtractCredits(float c)
	{
		credits -= c;
	}
	
	public float getCredits()
	{
		return credits;
	}
	
	public void setCredits(float c)
	{
		credits = c;
	}
}
