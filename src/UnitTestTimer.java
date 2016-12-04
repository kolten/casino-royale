import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class UnitTestTimer {

    final int Wait_Time = 1000; // The timer should wait 1000 ms precisely

    @Test
    public void timerTest(){
        Timer timer = new Timer();

        timer.start(); // start timer (stopwatch)
        timer.wait(Wait_Time); // puts thread to sleep for 1000 ms
        final long Test_time_elapsed = timer.getTimeMs(); // get current time on stopwatch

        System.out.println("timerTest(): 1000 ms wait within 2 ms of error?");

        assertEquals(Wait_Time, Test_time_elapsed, 2); // +- 2 ms of margin of error
    }
}