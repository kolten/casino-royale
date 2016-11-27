
import DDS.*;
import CR.*;

public class Dealer {

	float credits; // TODO: make this a float?
	int uuid;
	int seqno;
	int target_uuid;
	int activePlayers;

	boolean isHuman;
	bjd_action action;
	Shoe deck;
	Hand hand;
	Bank bank;

	bjDealer msg;
	

	player_status[] players;

	public Dealer(){
		msg = new bjDealer();
		credits = 500.0f;
		deck = new Shoe();
		shuffle();
		players = new player_status[6];
	}

	public Dealer(int uuid){
		msg = new bjDealer();
		credits = 500.0f;
		deck = new Shoe();
		shuffle();
		players = new player_status[6];
	}

	public boolean acceptPlayer(){
		// Returns true if there are empty seats, false if not
		
		return true;
	}

	public void askPlayer(){
		// Asks player for 'hit' or 'stay'
	}

	public boolean checkCredits(){
		// Returns true if Dealer has sufficient credits, else false
		if(getCredits() >= 20){
			return true;
		}
		return false;
	}

	public boolean checkSeats(){
		// Returns true if seats are full, false if not, 6 seats at table
		return true;
	}

	public float getCredits(){
		// Getter for credits
		return credits;
	}

	public boolean getIsHuman(){
		// Getter for isHuman
		return isHuman;
	}

	public int getSeqno(){
		// Getter for Sequence Number
		return seqno;
	}

	public int getTarget_uuid(){
		// Getter for Target UUID
		return target_uuid;
	}

	public int getUuid(){
		// Getter for uuid
		return uuid;
	} 

	public void getWageFromPlayer(int uuid){
		// Takes 
	}

	public Hand giveCards(){
		//
		action = bjd_action.dealing;
		return null;
	}

	public void kickPlayer(int uuid){
		//
		
	}

	public float payPlayer(int uuid){
		// 
		return 0;
	}

	public void playHand(){

	}



	public Hand sendCardToPlayer(Hand c){
		return c;

	}

	public float setCredit(float credits){
		// Setter for Dealer
		this.credits = credits;
		return credits;
	}

	public boolean setIsHuman(boolean isHuman){
		// Setter
		this.isHuman = isHuman;
		return isHuman;
	}

	public int setSeqno(int seqno){
		// Setter
		this.seqno = seqno;
		return seqno;
	}

	public int setTarget_uuid(int target_uuid){
		// Setter
		this.target_uuid = target_uuid;
		return target_uuid;
	}

	public int setUuid(int uuid){
		this.uuid = uuid;
		return uuid;
	}

	public void shuffle(){
		action = action.shuffling;
		deck.shuffle(); // Shuffle deck function in Shoe class
	}


	public boolean startGame(){
		if(checkCredits() && checkSeats()){
			return true;	
		}
		action = action.waiting;
		// TODO: Begin restocking the dealer to 500 credits, need to sleep for 30 seconds
		credits = 500f;
		return false;

	}

	// This is a default function in Java's Object class
	// public void wait(){
	//
	// }

	public int setActivePlayers(int activePlayers){
		this.activePlayers = activePlayers;
		return activePlayers;
	}

	public int getActivePlayers(){
		return activePlayers;
	}


	public bjDealer getMsg(){
		return msg;
	}
	
	private void sendTestData(){
	
	}

	public void join(bjPlayer player){
		
	}

	
}
