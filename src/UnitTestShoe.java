import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertTrue;
import CR.card;
import java.util.ArrayList;

public class UnitTestShoe {

	Shoe shoe;

    @Test
    public void testShoeSize() {
        System.out.println("Inside testShoeSize()");
        shoe = new Shoe(false);
        card drawnCard;
        int i, cardCount = 0;
        for(i = 0; i < 313; i++){
        	drawnCard = shoe.drawCard(true);
        	if(!drawnCard.equals(null) && drawnCard != null){
        		cardCount++;
        	}
        }
        assertTrue(Shoe.SIZE == cardCount);
    }
    
    @Test
    public void testDeckContents(){
        System.out.println("Inside testDeckContents()");
    	shoe = new Shoe(false);
    	ArrayList<card> uniqueCards = new ArrayList<card>();
        card drawnCard;
    	int i = 0;
    	for(i = 0; i < 313; i++){
    		drawnCard = shoe.drawCard(true);
    		if(uniqueCards.contains(drawnCard)){
    			uniqueCards.add(drawnCard);
    		}
    	}
    	assertTrue(uniqueCards.size() == 52);
    }
}