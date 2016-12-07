import CR.*;
import java.lang.Math;

public class Player {

	public static final int MIN_BET = 1;
	public static final int MAX_BET = 5;

	int uuid;
	int seqno;
	float credits;
	int wager;
	int dealer_id;
	bjp_action action;
	
	int seatNumber;

	String typeOfPlayer;

	bjPlayer msg;

	Hand hand;
	Bank bank;

	/**
	* Default constructor that initializes member variables with random uuid.
	*/
	public Player(){
		this((int)(Math.random()*8096)); // Constructor chaining
	}

	/**
	* Constructor that initializes member variables with a defined uuid.
	* @param uuid The Player's uuid.
	*/
	public Player(int uuid){
		this(uuid, 0);
	}

	/**
	* Constructor that initializes member variables with a defined uuid and dealer_id.
	* @param uuid The Player uuid.
	*/
	public Player(int uuid, int dealer_id){
		this.uuid = uuid;
		seqno = 1;
		credits = 100.0f;
		wager = 0;
		this.dealer_id = dealer_id;
		action = CR.bjp_action.joining;
		
		seatNumber = 0;

		typeOfPlayer = "Mr. Conservative";
		
		msg = new bjPlayer();
		
		hand = new Hand();
		bank = new Bank();
	}

	public float getCredits(){
		return credits;
	}

	public float setCredits(float credits){
		this.credits = credits;
		return credits;
	}

	public int getSeqno(){
		return seqno;
	}

	public int setSeqno(int seqno){
		this.seqno = seqno;
		return seqno;
	}

	public String getTypeOfPlayer(){
		return typeOfPlayer;
	}

	public String setTypeOfPlayer(String player){
		this.typeOfPlayer = player;
		return typeOfPlayer;
	}

	public int getUuid(){
		return uuid;
	}

	public int setUuid(int uuid){
		this.uuid = uuid;
		return uuid;
	}

	public int getWager(){
		return wager;
	}

	public int setWager(int wager){
		this.wager = wager;
		return wager;
	}

	public int getDealerID(){
		return dealer_id;
	}

	// Sorry T.

	public int setSeatNumber(int seatNumber){
		this.seatNumber = seatNumber;
		return seatNumber;
	}

	public int getSeatNumber(){
		System.out.printf("[Player] Seat number is: %d\n", seatNumber);
		return seatNumber;
	}

	/* Core functions */
	
	/**
	* Creates the player's current message to be passed to the OpenSplice publisher
	* @return Publisher object with Player's game information
	*/
	public bjPlayer getMsg(){
		bjPlayer temp = new bjPlayer(uuid, seqno, credits, wager, dealer_id, action);
		seqno++;
		return 	temp;
	}

	/**
	* Handling for the initial part of the Dealer executable's dealing sequence
	* @param dealer The Dealer executable's message passed into the Player factory
	*/
	public void initDeal(bjDealer dealer){
		if(dealer != null){
			hand.addCard(dealer.players[getSeatNumber()].cards[0]);
			hand.addCard(dealer.players[getSeatNumber()].cards[1]);
		}
	}

	/**
	* Handling for the repeated part of the Dealer executable's dealing sequence
	* @param dealer The Dealer executable's message passed into the Player factory
	*/
	public void singleDeal(bjDealer dealer){
		if(dealer != null){
			hand.addCard(dealer.players[getSeatNumber()].cards[hand.getNumberOfCards()]);
		}
	}

	/**
	* This runs the calculations for the player's hand value by accessing the local Hand object
	* @return The value of the player's blackjack hand in points
	*/
	public int getCurrentHandValue(){
		return hand.getHandValue();
	}

	/**
	* Handling for joining any detected dealer on the Opensplice subscriber
	* @param dealer The Dealer executable's message passed into the Player factory
	*/
	public void joinGame(bjDealer dealer){
		dealer_id = dealer.uuid;
		action = bjp_action.joining;
	}

	/**
	* Handling for leaving a connected dealer
	* @param dealer The Dealer executable's message passed into the Player factory
	*/
	public void exitGame(bjDealer dealer){
		action = bjp_action.exiting;
	}

	/**
	* Notifies the Player factory to set the player action to requesting_a_card
	*/
	public void requestCard(){
		// Hit
		action = bjp_action.requesting_a_card;
		
	}

	/**
	* Notifies the Player factory to set the player action to stay
	*/
	public void stay(){
		// stay
		action = bjp_action.none;
	}

// Old code that was never filled out. Handled/patched in PlayerMain.
/*
	public void loss(bjDealer dealer){

	}

	public void win(bjDealer dealer){
		
	}
*/

	/**
	* Assigns a random value to the Player factory for wagering between 1-5 credits.
	* If funds are low, 1 credit.
	* @param dealer The Dealer executable's message passed into the Player factory
	*/
	public void placeWager(bjDealer dealer){
		float currentCredits = getCredits();
		if(currentCredits <= 5 || currentCredits == 100){
			wager = 1;
			action = bjp_action.wagering;
		} else if(currentCredits > 5) {
			wager = (int)(4 * Math.random()) + 1; // wager random value between 1 and 5
			action = bjp_action.wagering;
		}
	}

	/**
	* Empties the Player factory's hand information to prepare for a new round.
	*/
	public void endGame(){
		hand.emptyHand();
	}
}
