import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class UnitTestMain {
    public static void main(String[] args) {

        // test the UnitTestDealer class
        Result dealerTest = JUnitCore.runClasses(UnitTestDealer.class);
            
        for (Failure failure : dealerTest.getFailures()) {
            System.out.println(failure.toString());
        }
            
        System.out.println(dealerTest.wasSuccessful());
		// test the UnitTestBank class
		Result bankTest = JUnitCore.runClasses(UnitTestBank.class);
		
		for (Failure failure : bankTest.getFailures())	{
			System.out.println(failure.toString());
		}
		System.out.println(bankTest.wasSuccessful());
		// test the UnitTestTimer class
		Result timerTest = JUnitCore.runClasses(UnitTestTimer.class);
		
		for (Failure failure : timerTest.getFailures())	{
			System.out.println(failure.toString());
		}
		System.out.println(timerTest.wasSuccessful());
    }
}