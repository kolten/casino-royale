import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class UnitTestTimer {

    final int Wait_Time = 5000;
	Timer timer = new Timer();
	//timer.start();
	//Timer.wait(Wait_Time);
	final long Test_time_elapsed = timer.getTimeMs();

    @Test
    public void sampleTimerTest() {

        System.out.println("Inside sampleTimerTest()");    
        assertEquals(System.currentTimeMillis(), Test_time_elapsed, 2);
           
    }
}