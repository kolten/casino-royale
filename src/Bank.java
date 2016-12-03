public class Bank {
	
	private float credits;
	
	public Bank()
	{
		setCredits(0);
	}
	
	/**
	 * @param c
	 */
	public Bank(float c)
	{
		setCredits(c);
	}
	
	/**
	 * @param c
	 */
	public void addCredits(float c)
	{
		credits += c;
	}
	
	/**
	 * @param c
	 */
	public void subtractCredits(float c)
	{
		credits -= c;
	}
	
	/**
	 * @return
	 */
	public float getCredits()
	{
		return credits;
	}
	
	/**
	 * @param c
	 */
	public void setCredits(float c)
	{
		credits = c;
	}
}
