

public class Bank {
	
	private float credits;
	
	public Bank()
	{
		setCredits(0);
	}
	
	public Bank(float c)
	{
		setCredits(c);
	}
	
	public void addCredits(float c)
	{
		credits += c;
	}
	
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
