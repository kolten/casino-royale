
public class Timer
{
	private long startTime;
	
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
	
	public void start()
	{
		startTime = System.currentTimeMillis();
	}
	
	public long getTimeMs()
	{
		return System.currentTimeMillis() - startTime;
	}
	
}
