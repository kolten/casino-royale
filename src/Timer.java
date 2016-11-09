
public class Timer
{
	public static void wait(int milliseconds)
	{
		try
		{
			Thread.sleep(milliseconds);
		}
		catch(InterruptedException ie)
		{
			System.out.println("An error occured using Thread.sleep()");
		}
	}
}
