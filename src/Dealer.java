
import DDS.*;
import CR.*;

public class Dealer {

	long credits; // TODO: make this a float?
	long uuid;
	long seqno;
	long target_uuid;

	boolean isHuman;

	player_status player;

	public Dealer(){

	}

	public static void main(String[] args) {
		
		System.out.println("Placeholder print to screen - Dealer");
		
	}

	public Boolean acceptPlayer(){
		// Returns true if there are empty seats, false if not
		
		return true;
	}

	public void askPlayer(){
		// Asks player for 'hit' or 'stay'
	}

	public Boolean checkCredits(){
		// Returns true if Dealer has sufficient credits
		return true;
	}

	public Boolean checkSeats(){
		// Returns true if seats are full, false if not, 6 seats at table
		return true;
	}

	public long getCredits(){
		// Getter for credits
		return credits;
	}

	public Boolean getIsHuman(){
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

	public Card giveCards(){
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

	public Card sendCardToPlayer(Card c){
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

	public void shuffleDeck(){

	}

	public boolean startGame(){
		return true;
	}

	// This is a default function in Java's Object class
	// public void wait(){
	//
	// }
	
	private void sendTestData(){
	
	}
	
}
