
public class Timer
{
	private long startTime;
	
	/**
	* Calls Thread.sleep() for a number of milliseconds
	* @param milliseconds Number of milliseconds to pause the program.
	*/
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
	
	/**
	* Starts a timer by saving the system clock time in milliseconds.
	*/
	public void start()
	{
		startTime = System.currentTimeMillis();
	}
	
	/**
	* Looks at the time passed since Timer.start() was called.
	* @return Milliseconds that have passed since Timer.start() was called.
	*/
	public long getTimeMs()
	{
		return System.currentTimeMillis() - startTime;
	}
	
}
