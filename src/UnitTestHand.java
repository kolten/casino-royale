import org.junit.Test;
import static org.junit.Assert.assertEquals;
import CR.card;

public class UnitTestHand {

	final int HAND_START = 0;
	final boolean BLACKJACK = true;
	final boolean NOT_BLACKJACK = false;
	Hand hand = new Hand();

	@Test
	public void testInitialHandValue() {
		System.out.println("Inside initial testHandValue()");    
		assertEquals(HAND_START, hand.getHandValue());
		hand.emptyHand();
		assertEquals(HAND_START, hand.getHandValue());
	}
	
	@Test
	public void testHandValueTwo(){
		System.out.println("Inside only 2 card testHandValue()");
		hand.emptyHand();
		hand.addCard(new card('C', '2', true));
		assertEquals(2, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('H', '2', true));
		assertEquals(2, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('D', '2', true));
		assertEquals(2, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('S', '2', true));
		assertEquals(2, hand.getHandValue());
		hand.emptyHand();
	}
	
	@Test
	public void testHandValueThree(){
		System.out.println("Inside only 3 card testHandValue()");
		hand.emptyHand();
		hand.addCard(new card('C', '3', true));
		assertEquals(3, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('H', '3', true));
		assertEquals(3, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('D', '3', true));
		assertEquals(3, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('S', '3', true));
		assertEquals(3, hand.getHandValue());
		hand.emptyHand();
	}
	
	@Test
	public void testHandValueFour(){
		System.out.println("Inside only 4 card testHandValue()");
		hand.emptyHand();
		hand.addCard(new card('C', '4', true));
		assertEquals(4, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('H', '4', true));
		assertEquals(4, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('D', '4', true));
		assertEquals(4, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('S', '4', true));
		assertEquals(4, hand.getHandValue());
		hand.emptyHand();
	}

	@Test
	public void testHandValueFive(){
		System.out.println("Inside only 5 card testHandValue()");
		hand.emptyHand();
		hand.addCard(new card('C', '5', true));
		assertEquals(5, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('H', '5', true));
		assertEquals(5, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('D', '5', true));
		assertEquals(5, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('S', '5', true));
		assertEquals(5, hand.getHandValue());
		hand.emptyHand();
	}

	@Test
	public void testHandValueSix(){
		System.out.println("Inside only 6 card testHandValue()");
		hand.emptyHand();
		hand.addCard(new card('C', '6', true));
		assertEquals(6, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('H', '6', true));
		assertEquals(6, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('D', '6', true));
		assertEquals(6, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('S', '6', true));
		assertEquals(6, hand.getHandValue());
		hand.emptyHand();
	}

	@Test
	public void testHandValueSeven(){
		System.out.println("Inside only 7 card testHandValue()");
		hand.emptyHand();
		hand.addCard(new card('C', '7', true));
		assertEquals(7, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('H', '7', true));
		assertEquals(7, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('D', '7', true));
		assertEquals(7, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('S', '7', true));
		assertEquals(7, hand.getHandValue());
		hand.emptyHand();
	}

	@Test
	public void testHandValueEight(){
		System.out.println("Inside only 8 card testHandValue()");
		hand.emptyHand();
		hand.addCard(new card('C', '8', true));
		assertEquals(8, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('H', '8', true));
		assertEquals(8, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('D', '8', true));
		assertEquals(8, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('S', '8', true));
		assertEquals(8, hand.getHandValue());
		hand.emptyHand();
	}

	@Test
	public void testHandValueNine(){
		System.out.println("Inside only 9 card testHandValue()");
		hand.emptyHand();
		hand.addCard(new card('C', '9', true));
		assertEquals(9, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('H', '9', true));
		assertEquals(9, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('D', '9', true));
		assertEquals(9, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('S', '9', true));
		assertEquals(9, hand.getHandValue());
		hand.emptyHand();
	}

	@Test
	public void testHandValueTen(){
		System.out.println("Inside only cards with value 10 testHandValue()"); 
		hand.emptyHand();
		int i, j;
		char base, suite = '1';
		for(i = 0; i < 4; i++){
			base = '9';
			switch(suite){
			case 'C': suite = 'H'; break;
			case 'H': suite = 'D'; break;
			case 'D': suite = 'S'; break;
			default: suite = 'C'; break;
			}
			for(j = 0; j < 4; j++){
				switch(base){
				case '9': base = 'T'; break;
				case 'T': base = 'J'; break;
				case 'J': base = 'Q'; break;
				case 'Q': base = 'K'; break;
				}
				hand.emptyHand();
				hand.addCard(new card(suite, base, true));
				assertEquals(10, hand.getHandValue());
				hand.emptyHand();
			}
		}
	}

	@Test
	public void testHandValueEleven(){
		System.out.println("Inside only 11 Ace card testHandValue()");    
		assertEquals(HAND_START, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('C', 'A', true));
		assertEquals(11, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('H', 'A', true));
		assertEquals(11, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('D', 'A', true));
		assertEquals(11, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('S', 'A', true));
		assertEquals(11, hand.getHandValue());
		hand.emptyHand();
	}

	@Test
	public void testHandValueAce(){
		System.out.println("Inside conditional Ace card testHandValue()");
		hand.emptyHand();
		hand.addCard(new card('C', 'A', true));
		hand.addCard(new card('C', 'A', true));
		assertEquals(12, hand.getHandValue());
		hand.emptyHand();
		hand.addCard(new card('C', '2', true));
		hand.addCard(new card('C', 'A', true));
		hand.addCard(new card('C', 'K', true));
		assertEquals(13, hand.getHandValue());
		hand.emptyHand();
	}
	
	@Test
	public void testBlackJack(){
		System.out.println("Inside testBlackJack()");
		int i;
		char suite = '1';
		for(i = 0; i < 4; i++){
			switch(suite){
			case 'C': suite = 'H'; break;
			case 'H': suite = 'D'; break;
			case 'D': suite = 'S'; break;
			default: suite = 'C'; break;
			}
			hand.emptyHand();
			hand.addCard(new card(suite, 'K', true));
			hand.addCard(new card(suite, 'A', true));
			assertEquals(BLACKJACK, Hand.blackJack(hand.getHand()));
			hand.addCard(new card(suite, '2', true));
			assertEquals(NOT_BLACKJACK, Hand.blackJack(hand.getHand()));
			hand.emptyHand();
		}
	}
}