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
    }
}