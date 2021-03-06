import CR.card;

import java.lang.Math;

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
	private int cardsUsed;	//Index of where to draw the card and how many have cards are left
	private card deck[];	//Array of all 312 cards used
	public static final int SIZE = 312;	//Size of deck.

	/** Constructor that initializes the entire deck of SIZE with shuffling **/
	public Shoe(){
		//Set global variables
		deck = new card[SIZE];
		cardsUsed = 0;

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
					deck[cardsUsed]=new card(suite, base, false);
					cardsUsed++;
				}
			}
		}
		cardsUsed = 0;
		shuffle();
		shuffle();
		shuffle();
		shuffle();
	}


	/** Constructor that initializes the entire deck of SIZE with or without shuffling **/
	public Shoe(boolean shuffle){
		//Set global variables
		deck = new card[SIZE];
		cardsUsed = 0;

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
					deck[cardsUsed]=new card(suite, base, false);
					cardsUsed++;
				}
			}
		}
		cardsUsed = 0;
		if(shuffle)
		{
			shuffle();
			shuffle();
			shuffle();
			shuffle();
		}
	}

	/** Shuffles deck by randomly swapping from the top to bottom.**/
	public void shuffle(){
		int i;
		for (i = SIZE-1; i > 0; i-- ) {
			int rand = (int)(Math.random()*(i+1));	//Chooses a random number inclusively between 0 and i.
			card temp = deck[i];
			deck[i] = deck[rand];		//Swaps cards at indices i and rand.
			deck[rand] = temp;
		}
		cardsUsed = 0;
	}

	/** Draws the card at the top of the deck at the index cardsUsed.
	 * @param isVisible boolean to set the card.
	 * @return  clone of card object at index cardsUsed from deck.*/
	public card drawCard(boolean isVisible){
		if(cardsUsed < 312){
			card toSend = new card(deck[cardsUsed].suite, deck[cardsUsed].base_value, isVisible);
			cardsUsed++;
			System.out.println("Card coming your way!");
			Hand.printCard(toSend);
			return toSend;
		}
		return null;
	}

	/**============ Getters && Setters ===================**/
	/**
	 * @return the cardsUsed
	 */
	public int getCardsUsed() {
		return cardsUsed;
	}

	/**
	 * @param cardsUsed the cardsUsed to set
	 */
	public void setCardsUsed(int cardsUsed) {
		this.cardsUsed = cardsUsed;
	}

	/**
	 * @return the deck
	 */
	public card[] getDeck() {
		return deck;
	}

	/**
	 * @param deck the deck to set
	 */
	public void setDeck(card[] deck) {
		this.deck = deck;
	}

	public void stackDeck()
	{
		// create a deck with aces instead of 2, 3, 4 for more blackjacks
		//Set global variables
		deck = new card[SIZE];
		cardsUsed = 0;

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
					if(base=='2'||base=='3'||base=='4'||base=='5')
						deck[cardsUsed]=new card(suite, 'A', false);
					else if(base=='6'||base=='7'||base=='8')
						deck[cardsUsed]=new card(suite, 'K', false);
					else
						deck[cardsUsed]=new card(suite, base, false);
					cardsUsed++;
				}
			}
		}
		cardsUsed = 0;
	}


}
