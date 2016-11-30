/*
	Dealer.java
	[Insert Class Description here]
*/


import DDS.*;
import CR.*;

public class Dealer {

	float credits; // TODO: make this a float?
	int uuid;
	int seqno;
	int target_uuid;
	int activePlayers;
	int atTable; // Number of players in game;
	int targetSeat;

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
		setTargetSeat(0);
	}

	public Dealer(int uuid){
		msg = new bjDealer();
		credits = 500.0f;
		deck = new Shoe();
		shuffle();
		players = new player_status[6];
		setTargetSeat(0);
	}

	// Keeping? Reply:
	public boolean acceptPlayer(){
		// Returns true if there are empty seats, false if not
		
		return true;
	}

	// Keeping? Reply:
	public void askPlayer(){
		// Asks player for 'hit' or 'stay'
	}

	/*
		This function uses the getCredits and getActivePlayers getter(s)
		to check how many credits the dealer should have before starting
		a game. If the dealer has less than the require amount, this function
		will return false, signaling the need for restocking of credits.
		Function: checkCredits
		@param  none
		@return  true/false
	*/
	public boolean checkCredits(){
		// Returns true if Dealer has sufficient credits, else false
		if(getCredits() >= getActivePlayers() * 20){
			return true;
		}
		return false;
	}

	// Keeping? Reply:
	public boolean checkSeats(){
		// Returns true if seats are full, false if not, 6 seats at table
		return true;
	}

	public float getCredits(){
		// Getter for credits
		return credits;
	}

	// Keeping? Reply:
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

	/*
		This function iterates through the current players at a table
		and sets the current players state to wagering.
		Function: getWagerFromPlayer
		@param  msg
		@return none
	*/
	public void getWagerFromPlayer(bjPlayer msg){
		// Takes 
		if(msg.action.value() == bjp_action._wagering){
			int i;
			for(i = 0; i < 6; i++){
				if(msg.uuid == players[i].uuid){
					players[i].wager = msg.wager;
				}
			}
		}
	}

	/*
		This function sets the current state of the dealer
		to dealing.
		Function: giveCards
		@param  none
		@return null
	*/
	public Hand giveCards(){
		//
		action = bjd_action.dealing;
		return null; // Do we still want this???????
	}

	/*
		Function: 
		@param  
		@return 
	*/
	public void kickPlayer(int uuid){
		
	}
	/*

		Function: 
		@param  
		@return 
	*/
	public float payPlayer(int uuid){ 
		// 
		return 0;
	}

	/*
		Function: 
		
		@param  
		@return 
	*/
	public void playHand(){

	}


	/*
		Function: 
		
		@param  
		@return 
	*/
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

	/*
		Function: 
		
		@param  
		@return 
	*/
	public void shuffle(){
		msg.action = action.shuffling;
		deck.shuffle(); // Shuffle deck function in Shoe class
	}

	/*
		Function: 
		
		@param  
		@return 
	*/
	public boolean startGame(){
		if(checkCredits()){
			setNumberAtTable(getActivePlayers());
			setTarget_uuid(0);
			return true;	
		}
		action = bjd_action.waiting;
		// TODO: Begin restocking the dealer to 500 credits, need to sleep for 30 seconds
		credits = 500f;
		return false;

	}

	/*
		Function: 
		
		@param  
		@return 
	*/
	public void waiting(){
		action = bjd_action.waiting;
	}

	public int setActivePlayers(int activePlayers){
		this.activePlayers = activePlayers;
		return activePlayers;
	}

	public int getActivePlayers(){
		return activePlayers;
	}

	/*
		Function: 
		
		@param  
		@return 
	*/
	public void join(bjPlayer msg){
		if(getActivePlayers() < 6){
			// Assuming players do not leave
			player_status player = new player_status(msg.uuid, msg.wager, 0f, new card[21]);
			players[getActivePlayers()] = player;
			setActivePlayers(getActivePlayers() + 1);
		}
	}

	// These are the most recently added functions

	public int setNumberAtTable(int count){
		this.atTable = count;
		return count;
	}

	/*
		Function: 
		
		@param  
		@return 
	*/
	public bjDealer getMsg(){
		return new bjDealer(getUuid(), getSeqno(), getActivePlayers(), players, new card[21], getTarget_uuid());
	}

	/*
		getNumberAtTable
		@param none 
		@return atTable
	*/
	public int getNumberAtTable(){
		return atTable;
	}

	/*
		Function: setNumberAtTable
		
		@param notRead 
		@return none
	*/
	public void nextSeat(boolean notRead){
		if((getNumberAtTable() == 0) && (getActivePlayers() > getTargetSeat()) && (!notRead)){
			
		}
	}

	/*
		Function: stillWagering
		
		@param none 
		@return true/false
	*/	
	public boolean stillWagering(){
		int i;
		for(i = 0; i < getActivePlayers(); i++){
			if(players[i].wager == 0){
				return false;
			}
		}
		return true;
	}

	/*
		Function: 
		
		@param  
		@return 
	*/
	public boolean allWagered(){
		return !stillWagering();
	}

	/*
		Function: 
		
		@param  
		@return 
	*/
	public int getTargetSeat(){
		return targetSeat;
	}

	/*
		Function: 
		
		@param  
		@return 
	*/
	public int setTargetSeat(int targetSeat){
		this.targetSeat = targetSeat;
		return targetSeat;
	}

	public int getAction(){
		return action.value();
	}
	
	public int getCardsLeftInDeck(){
		return 312 - deck.getCardsUsed();
	}
}
