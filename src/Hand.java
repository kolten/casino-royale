import CR.card;

public class Hand {
	private card cards[];		//Array of cards that represent hands.
	private int cardsInHand;	//Number of cards that are in hand
	private int totalHandValue;	//Total hand value
	private int hasAce;		//Number of aces in hand that have a value of 11.
	
	/* Constructor for hand that sets all starter values to 0.*/
	public Hand()
	{
		cardsInHand = 0;
		cards = new card[21];
		totalHandValue = 0;
		hasAce = 0;
	}
	
	/* Checks if a valid poker card, based off of suite */
	public static boolean isValidCard(card testCard)
	{
		if(testCard != null)
		{
			if(testCard.suite != 'C' || testCard.suite != 'H' || testCard.suite != 'D' || testCard.suite != 'S')
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

	/* Prints card from parameter, regardless if visible or not.*/
	public static void printCard(card obj)
	{
		System.out.println("           suite :" + obj.suite);
		System.out.println("      base_value :" + obj.base_value);
		System.out.println("         visible :" + obj.visible);
	}
	
	/* Adds card to hand if space is available, it is a valid poker card. Ajusts total hand value according to card's base value.*/
	public void addCard(card toSet)
	{
		if(isValidCard(toSet) && cardsInHand < 21)
		{
			cards[cardsInHand] = new card(toSet.suite, toSet.base_value, toSet.visible);
			if(toSet.visible)
			{
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
		}
		else System.out.println("Given bad card, request another!");
	}
	
	/* Calculates and gets totalHandValue.*/
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


	/* Returns cards in hand.*/
	public int getNumberOfCards(){
		return cardsInHand;
	}

	/* Non useful setters and getters
	private void setHandValue(int totalHandValue)
	{
		this.totalHandValue = totalHandValue;
	}

	private void setCardsInHand(int cardsInHand)
	{
		this.cardsInHand = cardsInHand;
	}
	
	private void setHasAce(int hasAce)
	{
		this.hasAce = hasAce;
	}
	*/

	/* Empties hand by zeroing all variables.*/
	public void emptyHand()
	{
		totalHandValeu = 0;
		hasAce = 0;
		cardsInHand = 0;
		for(int i = 0; i < 21; i++)
		{
			cards[i] = null;
		}
	}
}
