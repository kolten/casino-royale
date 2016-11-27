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

	// Sets player ID and the dealer ID it joined it, not sure if this is correct.
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

	/* Core functions */

	public bjPlayer getMsg(bjPlayer msg){
		return msg;
	}

	public int getCurrentHandValue(Hand h){
		return h.handValue();
	}

	public void joinGame(bjDealer dealer){
		
	}

	public void exitGame(bjDealer dealer){
		
	}

	public void requestCard(bjDealer dealer){
		// Hit
	}

	public void stay(bjDealer dealer){
		// stay

	}

	public void loss(bjDealer dealer){

	}

	public void win(bjDealer dealer){
		
	}

	public void placeWager(bjDealer dealer){
		float currentCredits = getCredits();
		if(currentCredits <= 5){
			wager = 1;
			credits = credits - 1;
			this.setCredits(credits);
			msg.wager = wager;
		} else if(currentCredits > 5) {
			//http://stackoverflow.com/questions/363681/generating-random-integers-in-a-specific-range
			wager = rand.nextInt((MAX_BET - MIN_BET) + 1 ) + MIN_BET;
			credits = credits - wager;
			this.setCredits(credits);
			msg.wager = wager;
		}
	}

}
