import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class UnitTestBank {

    final float Dealer_Start = 500f;
	Bank bank = new Bank(Dealer_Start);
	Dealer dealer = new Dealer();

    @Test
    public void testGetCredits() {
        System.out.println("Inside testGetCredits()");    
        assertEquals(Dealer_Start, bank.getCredits(), 0.125f);
    }
    
    @Test
    public void testDealerBankCredits() {
        System.out.println("Inside testGetCredits()");    
        assertEquals(Dealer_Start, dealer.getBank().getCredits(), 0.125f);
    }
}