import CR.*;
import DDS.*;
import java.util.Random;

public class Player {

	public static final int MIN_BET = 1;
	public static final int MAX_BET = 5;

	float credits;
	int dealer_id;
	int seqno;
	int uuid;
	int wager;
	int seatNumber;

	String typeOfPlayer;

	bjp_action action;
	bjPlayer msg;

	Hand hand;
	Bank bank;

	Random rand;

	public Player(){
		msg = new bjPlayer();
		hand = new Hand();
		credits = 100.0f;
		typeOfPlayer = "Mr. Conservative";
		action = bjp_action.joining;
	}

	public Player(int uuid){
		msg = new bjPlayer();
		this.uuid = uuid;
		this.dealer_id = dealer_id;
		hand = new Hand();
		credits = 100.0f;
		typeOfPlayer = "Mr. Conservative";
		action = bjp_action.joining;
	}

	public Player(int uuid, int dealer_id){
		msg = new bjPlayer();
		this.uuid = uuid;
		this.dealer_id = dealer_id;
		hand = new Hand();
		credits = 100.0f;
		typeOfPlayer = "Mr. Conservative";
		action = bjp_action.joining;
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
		return seatNumber;
	}

	/* Core functions */

	public bjPlayer getMsg(){
		bjPlayer temp = new bjPlayer(uuid, seqno, credits, wager, dealer_id, action);
		if(temp != null){
			return 	temp;
		}
		else{
			System.out.println("This function is broken.");
			return new bjPlayer();
		}
	}

	public void initDeal(bjDealer dealer){
		if(dealer != null){
			hand.addCard(dealer.players[getSeatNumber()].cards[0]);
			hand.addCard(dealer.players[getSeatNumber()].cards[1]);
		}
	}

	public void singleDeal(bjDealer dealer){
		if(dealer != null){
			hand.addCard(dealer.players[getSeatNumber()].cards[hand.getNumberOfCards()]);
		}
	}

	public int getCurrentHandValue(){
		return hand.getHandValue();
	}

	public void joinGame(bjDealer dealer){
		dealer_id = dealer.uuid;
		action = bjp_action.joining;
	}

	public void exitGame(bjDealer dealer){
		action = bjp_action.exiting;
	}

	public void requestCard(){
		// Hit
		action = bjp_action.requesting_a_card;
		
	}

	public void stay(){
		// stay
		action = bjp_action.none;
	}

	public void loss(bjDealer dealer){

	}

	public void win(bjDealer dealer){
		
	}

	public void placeWager(bjDealer dealer){
		float currentCredits = getCredits();
		if(currentCredits <= 5){
			wager = 1;
			//credits = credits - 1;
			this.setCredits(credits);
			action = bjp_action.wagering;
			wager = wager;
		} else if(currentCredits > 5) {
			//http://stackoverflow.com/questions/363681/generating-random-integers-in-a-specific-range
			wager = rand.nextInt((MAX_BET - MIN_BET) + 1 ) + MIN_BET;
			//credits = credits - 1;
			this.setCredits(credits);
			action = bjp_action.wagering;
			wager = wager;
		}
	}

}
