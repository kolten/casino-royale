import CR.card;

public class Hand {
	/** Global variables **/
	private card cards[];		//Array of cards that represent hands.
	private int cardsInHand;	//Number of cards that are in hand
	private int totalHandValue;	//Total hand value
	private int hasAce;		//Number of aces in hand that have a value of 11.
	
	/** Constructor for hand that sets all starter values to 0. **/
	public Hand()
	{
		cards = new card[21];
		emptyHand();			//Initializes and sets variables to 0.
	}

	/** Empties hand by zeroing all variables, and places "null" cards into every slot of card array. **/
	public void emptyHand()
	{
		totalHandValue = 0;
		hasAce = 0;
		cardsInHand = 0;
		for(int i = 0; i < 21; i++)
		{
			cards[i] = new card('\0', '\0', true);
		}
		//cards = Hand.EMPTY_HAND;
	}
	
	/** Adds card to hand if space is available and is a valid card
	 * and adjusts total hand value accordingly.
	 * @param card object to add to hand. **/
	public void addCard(card toSet)
	{
		if(isValidCard(toSet) && cardsInHand < 21)
		{
			cards[cardsInHand] = new card(toSet.suite, toSet.base_value, toSet.visible);
			switch(cards[cardsInHand].base_value)
			{
				case '2': totalHandValue += 2; break;
				case '3': totalHandValue += 3; break;
				case '4': totalHandValue += 4; break;
				case '5': totalHandValue += 5; break;
				case '6': totalHandValue += 6; break;
				case '7': totalHandValue += 7; break;
				case '8': totalHandValue += 8; break;
				case '9': totalHandValue += 9; break;
				case 'T': totalHandValue += 10; break;
				case 'J': totalHandValue += 10; break;
				case 'Q': totalHandValue += 10; break;
				case 'K': totalHandValue += 10; break;
				case 'A':
					if(totalHandValue <= 10)
					{
						totalHandValue += 11;
						hasAce++;
					}
					else if(totalHandValue > 10)
					{
						totalHandValue += 1;
					}
					break;
				default: System.out.println("Major error, please fix."); break;
			}
			cardsInHand++;
		}
		else{
			printCard(toSet);
			System.out.println("Given bad card, request another!");
		}
	}

	/** Flips all invisible cards to visible. **/
	public void flipCard()
	{
		int i;
		for(i = 0; i < getNumberOfCards(); i++)
		{
			cards[i].visible = true;
		}
	}
	
	/**============= Getters && Setters ==============**/

	/** @return the number of cards in hand.**/
	public int getNumberOfCards()
	{
		return cardsInHand;
	}
	
	/** @return returns card array that represents hand **/
	public card[] getHand()
	{
		int i;
		card temp[] = new card[21];
		for(i = 0; i < 21; i++){
			temp[i] = new card(cards[i].suite, cards[i].base_value, cards[i].visible);
		}
		return temp;
	}
	
	/** Calculates and gets totalHandValue. 
	 * @return hand value according to blackjack rules.**/
	public int getHandValue()
	{
		if(totalHandValue > 21 && hasAce == 1)	//If there is an ace in hand and current hand value is a bust, it will change the ace value from 11 to 1.
		{
			totalHandValue -= 10;
			hasAce--;
		}
		else if(hasAce > 1)	//Blackjack rules and all possible card configurations will not allow this to happen. Major Error otherwise.
		{
			System.out.println("Hand is borked.");
		}
		return totalHandValue;
	}

	/**=================== Static methods ==============**/
	
	/** Checks if there are only two valid cards in the hand that have a value of 21.
	 * @param card array representing hand to check.
	 * @return true if the value was an Ace and a ten point card. **/
	public static boolean blackJack(card hand[])
	{
		if(hand != null && hand.length != 21)
		{
			if(isValidCard(hand[1]) && isValidCard(hand[2]) && !isValidCard(hand[3]))
			{
				switch(hand[1].base_value)
				{
					case 'A':
						if(hand[2].base_value == 'T' || hand[2].base_value == 'J' || hand[2].base_value == 'Q' || hand[2].base_value == 'K')
						{
							return true;
						}
						return false;
					case 'T':
					case 'J':
					case 'Q':
					case 'K':
						if(hand[2].base_value == 'A')
						{
							return true;
						}
					default: return false;
				}
			}
		}
		return false;
	}
	
	/** Checks if a valid poker card, based off of suite 
	 * @param card object to test if it is a valid poker card. 
	 * @return true if it is a valid poker card, and false if invalid. **/
	public static boolean isValidCard(card testCard)
	{
		if(testCard != null)
		{
			if(testCard.suite != 'C' && testCard.suite != 'H' && testCard.suite != 'D' && testCard.suite != 'S')
			{
				return false;
			}
			else if(testCard.base_value < '1' || (testCard.base_value > '9' && testCard.base_value != 'T' && testCard.base_value != 'J' && testCard.base_value != 'Q' && testCard.base_value != 'K' && testCard.base_value != 'A'))
			{
				return false;
			}
			else return true;
		}
		else return false;
	}

	/** Prints card from parameter, regardless if visible or not. 
	 * @param card object to print it's values.**/
	public static void printCard(card obj)
	{
		System.out.println("           suite :" + obj.suite);
		System.out.println("      base_value :" + obj.base_value);
		System.out.println("         visible :" + obj.visible);
	}
	
	/** Calculate hand based off of the given card array
	 * @param card array to calculate hand value
	 * @return total hand value **/
	public static int calculateHandValue(card hand[])
	{
		int i, total, aceNum = 0;
		total = 0;
		for(i = 0; i < hand.length; i++)
		{
			if(isValidCard(hand[i]))
			{
				if(hand[i].visible)
				{
					switch(hand[i].base_value)
					{
						case '2': total += 2; break;
						case '3': total += 3; break;
						case '4': total += 4; break;
						case '5': total += 5; break;
						case '6': total += 6; break;
						case '7': total += 7; break;
						case '8': total += 8; break;
						case '9': total += 9; break;
						case 'T': total += 10; break;
						case 'J': total += 10; break;
						case 'Q': total += 10; break;
						case 'K': total += 10; break;
						case 'A':
							if(total <= 10)
							{
								total += 11;
								aceNum++;
							}
							else if(total > 10)
							{
								total += 1;
							}
							break;
						default: System.out.println("Computer has failed at calculating. Everything is wrong."); break;
					}
				}
				else System.out.println("Invisible entity lurks in your hand. Consider exorcism.");
			}
		}
		if(total > 21 && aceNum == 1)
		{
			total -= 10;
			aceNum--;
		}
		else if(aceNum > 1)
		{
			System.out.println("Smoking aces are ruining the game.");
		}
		return total;
	}
}
