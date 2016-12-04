import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class UnitTestBank {

    final float Dealer_Start = 500f;
	Bank bank = new Bank(Dealer_Start);

    @Test
    public void sampleTimerTest() {

        System.out.println("Inside sampleBankTest()");    
        assertEquals(Dealer_Start, bank.getCredits(), 0.125f);
           
    }
}