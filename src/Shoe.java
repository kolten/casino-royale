import java.util.ArrayList;
import java.lang.Math;
import CR.card;

/* Adapted from source code provided by math.hws.edu/javanotes/ and author David J. Eck.
Licensed under Creative Commons Attribution Noncommercial Sharealike 3.0.
+ Modified to use ArrayList instead.
+ removed Joker functionality
+ card changed to follow CR.card's functionality
+ Six decks of 52 cards are instantiated.
+ getCardsLeft() changed to getCardsUsed()
+ added variables size and MIN_CARDS_USED
*/
public class Shoe {
	private int cardsUsed;		//Index of where to draw the card and how many have cards are left
	private ArrayList<card> deck;	//ArrayList of all 312 cards used
	private final int MIN_CARDS_USED = 104;	//Constant to track the minimum amount of cards used before shuffle is needed.

	public static final int size = 312;

	public Shoe(){
		//Set global variables
		cardsUsed = 0;
		deck = new ArrayList<card>();

		//Populate deck with 52 unique cards 6 times.
		int i, j, k;
		char base, suite = '1';
		for(i = 0; i < 4; i++){
			base = '1';
			switch(suite){
				case 'C': suite = 'H'; break;
				case 'H': suite = 'D'; break;
				case 'D': suite = 'S'; break;
				default: suite = 'C'; break;
			}
			for(j = 0; j < 13; j++){
				switch(base){
					case '9': base = 'T'; break;
					case 'T': base = 'J'; break;
					case 'J': base = 'Q'; break;
					case 'Q': base = 'K'; break;
					case 'K': base = 'A'; break;
					default: base++; break;
				}
				for(k = 0; k < 6; k++){
					deck.add(new card(suite, base, true));
				}
			}
		}
	}

	public void shuffle(){
		int i;
		for (i = deck.size()-1; i > 0; i-- ) {
			int rand = (int)(Math.random()*(i+1));	//Chooses a random number inclusively between 0 and i.
			card temp = deck.get(i);		
			deck.set(i, deck.get(rand));		//Swaps cards at indices i and rand.
			deck.set(rand, temp);			
		}
		cardsUsed = 0;
	}
	
	//Using this function requires a try-catch block.
	public card drawCard(boolean isVisible){
		if(cardsUsed >= MIN_CARDS_USED)
			throw new IllegalStateException("Need to shuffle.");
		card toSend = deck.get(cardsUsed);
		toSend.visible = isVisible;
		cardsUsed++;
		return toSend;
	}
	
	public int getCardsUsed(){
		return cardsUsed;
	}
}
