import CR.card;

public class Hand {
	private card cards[];
	private int cardsInHand;
	private int cardHandValue[];
	private int totalHandValue;
	
	public Hand()
	{
		cardsInHand = 0;
		cards = new card[21];
		cardHandValue = new int[21];
	}
	
	public static boolean isValidCard(card testCard)
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
	
	public void setCard(card toSet)
	{
		if(isValidCard(toSet))
		{
			cards[cardsInHand] = toSet;
			switch(cards[cardsInHand].base_value){
						case '2': cardHandValue[cardsInHand] = 2; break;
						case '3': cardHandValue[cardsInHand] = 3; break;
						case '4': cardHandValue[cardsInHand] = 4; break;
						case '5': cardHandValue[cardsInHand] = 5; break;
						case '6': cardHandValue[cardsInHand] = 6; break;
						case '7': cardHandValue[cardsInHand] = 7; break;
						case '8': cardHandValue[cardsInHand] = 8; break;
						case '9': cardHandValue[cardsInHand] = 9; break;
						case 'T': cardHandValue[cardsInHand] = 10; break;
						case 'J': cardHandValue[cardsInHand] = 10; break;
						case 'Q': cardHandValue[cardsInHand] = 10; break;
						case 'K': cardHandValue[cardsInHand] = 10; break;
						case 'A':
							if(totalHandValue <= 10)
							{
								knownHandValue[cardsInHand] = 11;
								totalHandValue += 11;
								hasAce++;
							}
							else if(totalHandValue > 10)
							{
								cardHandValue[cardsInHand] = 1;
								totalHandValue += 1;
							}
							break;
						default: System.out.println("Major error, please fix."); break;
					}
			cardsInHand++;
			
		}
		else System.out.println("Given bad card, request another!");
	}
	
	public int handValue()
	{
		int i;
		if(cardsInHand == 0)
			return totalHandValue;
		else if(totalHandValue <= 21)
		{
			return totalHandValue;
		}
		else
		{
			for(i = cardsInHand - 1; i >= 0; i--)
			{
				
			}
		}
	}
}
