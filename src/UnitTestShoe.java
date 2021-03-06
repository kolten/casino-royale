import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertTrue;
import CR.card;
import java.util.ArrayList;

public class UnitTestShoe {

	final int SIZE = 312;
	Shoe shoe;

    @Test
    public void testShoeSize() {
        System.out.println("Inside testShoeSize()");
        shoe = new Shoe(false);
        card drawnCard;
        int i, cardCount = 1;
        for(i = 0; i < 313; i++){
        	System.out.print("#" + (i+1) + ":");
        	drawnCard = shoe.drawCard(true);
        	if(!drawnCard.equals(null) && drawnCard != null){
        		cardCount++;
        	}
        }
        assertTrue(SIZE == cardCount);
    }
    
    @Test
    public void testDeckContents(){
        System.out.println("Inside testDeckContents()");
    	shoe = new Shoe(false);
    	ArrayList<card> uniqueCards = new ArrayList<card>();
        card drawnCard;
        boolean validCard = true;
    	int i = 0;
    	for(i = 0; i < 312; i++){
        	System.out.print("#" + (i+1) + ":");
    		drawnCard = shoe.drawCard(true);
    		if(uniqueCards.contains(drawnCard)){
    			uniqueCards.add(drawnCard);
    		}
    	}
    	for(i = 0; i < uniqueCards.size() && validCard; i++){
        	System.out.print("#" + (i+1) + ":");
    		drawnCard = uniqueCards.get(i);
    		Hand.printCard(drawnCard);
    		switch(drawnCard.suite){
			case 'C':
			case 'H':
			case 'D':
			case 'S': break;
			default: validCard = false; break;
			}
    		switch(drawnCard.base_value){
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case 'T':
			case 'J':
			case 'Q':
			case 'K':
			case 'A': break;
			default: validCard = false; break;
			}
    		System.out.println("isValid? " + validCard);
    	}
    	assertTrue(validCard);
    }
}