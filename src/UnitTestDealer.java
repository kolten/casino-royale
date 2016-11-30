import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class UnitTestDealer {

    final int test_uuid = 42;
    Dealer dealer = new Dealer(test_uuid);

    @Test
    public void sampleDealerTest() {

        System.out.println("Inside sampleDealerTest()");    
        assertEquals(test_uuid, dealer.getUuid());  
           
    }
}