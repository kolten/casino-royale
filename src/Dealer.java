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


	boolean isHuman;
	bjd_action action;
	Shoe deck;
	Hand hand;

	Player_Status player;

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

	public Hand giveCards(){
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

<<<<<<< Updated upstream
	public Card sendCardToPlayer(long uuid){

=======
	public Hand sendCardToPlayer(Hand c){
		return c;
>>>>>>> Stashed changes
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

	public void shuffle(){
		action = new bjd_action(0); // Set the action to shuffling
		deck.shuffle(); // Shuffle deck function in Shoe class
	}

<<<<<<< Updated upstream
	public Boolean startGame(){

=======
	public boolean startGame(){
		if(checkCredits() && checkSeats()){
			return true;	
		}
		action = new bjd_action(1); // Set the action to waiting
		// TODO: Begin restocking the dealer to 500 credits, need to sleep for 30 seconds
		credits = 500;
		return false;
>>>>>>> Stashed changes
	}

	public void wait(){

	}
	
	private void sendTestData(){
	
	}
	
}
