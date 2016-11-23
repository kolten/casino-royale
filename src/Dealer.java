
import DDS.*;
import CR.*;

public class Dealer {

	long credits; // TODO: make this a float?
	long uuid;
	long seqno;
	long target_uuid;

	boolean isHuman;
	bjd_action action;
	Shoe deck;
	Hand hand;


	player_status player;

	public Dealer(){
		credits = 500.0;
		deck = new Shoe();
		shuffle();
	}

	public Dealer(long uuid){
		credits = 500.0;
		deck = new Shoe();
		shuffle();
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
			return true
		}
		return false;
	}

	public boolean checkSeats(){
		// Returns true if seats are full, false if not, 6 seats at table
		return true;
	}

	public long getCredits(){
		// Getter for credits
		return credits;
	}

	public boolean getIsHuman(){
		// Getter for isHuman
		return isHuman;
	}

	public long getSeqno(){
		// Getter for Sequence Number
		return seqno;
	}

	public long getTarget_uudi(){
		// Getter for Target UUID
		return target_uuid;
	}

	public long getUuid(){
		// Getter for uuid
		return uuid;
	} 

	public void getWageFromPlayer(long uuid){
		// Takes 
	}

	public Hand giveCards(){
		//
		return null;
	}

	public void kickPlayer(long uuid){
		//
		
	}

	public long payPlayer(long uuid){
		// 
		return 0;
	}

	public void playHand(){

	}



	public Hand sendCardToPlayer(Hand c){
		return c;

	}

	public long setCredit(long credits){
		// Setter for Dealer
		this.credits = credits;
		return credits;
	}

	public boolean setIsHuman(boolean isHuman){
		// Setter
		this.isHuman = isHuman;
		return isHuman;
	}

	public long setSeqno(long seqno){
		// Setter
		this.seqno = seqno;
		return seqno;
	}

	public long setTarget_uuid(long target_uuid){
		// Setter
		this.target_uuid = target_uuid;
		return target_uuid;
	}

	public long setUuid(long uuid){
		this.uuid = uuid;
		return uuid;
	}

	public void shuffle(){
		action = new bjd_action(0); // Set the action to shuffling
		deck.shuffle(); // Shuffle deck function in Shoe class
	}


	public boolean startGame(){
		if(checkCredits() && checkSeats()){
			return true;	
		}
		action = new bjd_action(1); // Set the action to waiting
		// TODO: Begin restocking the dealer to 500 credits, need to sleep for 30 seconds
		credits = 500;
		return false;

	}

	// This is a default function in Java's Object class
	// public void wait(){
	//
	// }
	
	private void sendTestData(){
	
	}
	
}
