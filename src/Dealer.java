import src.Card;
import src.PlayerAction;
import src.Shoe;
import src.Snooper;
import src.Player;
import src.DealerAction;

public class Dealer {

	long credits;
	long uuid;
	long seqno;
	long target_uuid;

	Boolean isHuman;

	Player_Status player;

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
		return getSeqno;
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
	}

	public void kickPlayer(long uuid){
		//
	}

	public long payPlayer(long uuid){
		// 
	}

	public void playHand(){

	}

	public Card sendCardToPlayer(long uuid){

	}

	public long setCredit(long credits){
		// Setter for Dealer
	}

	public Boolean setIsHuman(Boolean isHuman){
		// Setter
	}

	public long setSeqno(long seqno){
		// Setter
	}

	public long setTarget_uuid(long target_uuid){
		// Setter
		
	}

	public long setUuid(long uuid){

	}

	public void shuffleDeck(){

	}

	public Boolean startGame(){

	}

	public void wait(){

	}
	
	private void sendTestData(){
	
	}
	
}
