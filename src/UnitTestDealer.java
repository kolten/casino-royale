import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class UnitTestDealer {

	final int Start_at_table = 0;
    Dealer dealer = new Dealer();

    @Test
    public void sampleDealerTest() {

        System.out.println("Inside sampleDealerTest()");    
        assertEquals(Start_at_table, dealer.getNumberAtTable());
           
    }
}