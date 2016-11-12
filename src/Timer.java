
public class Timer
{
	static long startTime;
	
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
	
	public static void start()
	{
		startTime = System.currentTimeMillis();
	}
	
	public static long getTimeMs()
	{
		return System.currentTimeMillis() - startTime;
	}
	
}
