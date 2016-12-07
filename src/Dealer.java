/** Dealer.java
		A Factory object that holds all the data required for manipulating and producing
	bjDealer objects.
 **/
import CR.bjDealer;
import CR.player_status;
import CR.bjd_action;

import CR.bjPlayer;
import CR.bjp_action;

import CR.MAX_PLAYERS;
import CR.card;

public class Dealer {

	/** Private variable declarations **/
	private int uuid;
	private int seqno;
	private int active_players;
	private player_status[] players;
	private bjd_action action;
	private Hand hand;					//Holds dealer hand and card logic.
	private int target_uuid;

	private Shoe deck;		//Holds all 6 decks and logic for decks
	private Bank bank;		//Holds credits and logic
	private int atTable;	//Number of players in game
	private int targetSeat;	//Seat number of the targeted player.

	/** Constructor that initializes all private variables and sets uuid to parameter */
	public Dealer(){
		uuid = (int)(Math.random()*8096);
		seqno = 1;
		active_players = 0;
		players = new player_status[MAX_PLAYERS.value];
		hand = new Hand();
		for(int i = 0; i < MAX_PLAYERS.value; i++){
			Hand temp = new Hand();
			players[i] = new player_status(0, 0, 0f, temp.getHand());
		}
		action  = CR.bjd_action.shuffling;
		target_uuid = 0;

		deck = new Shoe();
		bank = new Bank(500f);
		atTable = 0;
		targetSeat = 0;

		shuffle();
	}

	/** Constructor that initializes all private variables and sets uuid to parameter
	 * @param uuid to set Dealer factory to */
	public Dealer(int uuid){
		this.uuid = 1;
		seqno = 1;
		active_players = 0;
		players = new player_status[MAX_PLAYERS.value];
		hand = new Hand();
		for(int i = 0; i < MAX_PLAYERS.value; i++){
			Hand temp = new Hand();
			players[i] = new player_status(0, 0, 0f, temp.getHand());
		}
		action  = CR.bjd_action.shuffling;
		target_uuid = 0;

		deck = new Shoe();
		bank = new Bank(500f);
		atTable = 0;
		targetSeat = 0;

		shuffle();
	}

	/** Creates the player's current message to be passed to the OpenSplice publisher
	 * @return bjDealer message that contains all the significant values of DealerFactory **/
	public bjDealer getMsg(){
		System.out.println("[Dealer " + getUuid() + "] Current credits in bank: " + bank.getCredits());
		bjDealer temp = new bjDealer(uuid, seqno, active_players, players, action, hand.getHand(), target_uuid);
		seqno++;
		return 	temp;
	}

	/**============== Boolean Methods ===================**/

	/** Condition check if the table is full, or not.
	 * @return true for full table, else false */
	public boolean isFullTable(){
		// Returns true if seats are full, false if not.
		if(getActivePlayers() < MAX_PLAYERS.value){
			return false;
		}
		else {
			return true;
		}
	}

	/** Checks all the wager of all wagers of players at table, 
	 *  @return true if any wagers from the list of active players are equal to 0. **/	
	public boolean stillWagering() {
		int i;
		for(i = 0; i < getActivePlayers(); i++){
			if(players[i].wager == 0){
				return true;
			}
		}
		System.out.println("[Dealer " + getUuid() + "] Not all players have made a wager.");
		return false;
	}

	/** Checks how many credits the dealer should have before starting a game.
	 * @return  true if there are sufficient credits, else false **/
	public boolean checkCredits(){
		// Returns true if Dealer has sufficient credits, else false
		if(bank.getCredits() >= getActivePlayers() * 20){
			return true;
		}
		return false;
	}

	/** Checks if the given dealer state is the same as the current action.
	 * @param value within the bjd_action
	 * @return true if action.value is same as the given value, else false. **/
	public boolean sameAction(bjd_action action){
		if(getActionValue() == action.value()){
			return true;
		}
		return false;
	}

	/**========== Sequencing Methods =============**/

	/** Changes state to shuffling, and shuffles deck. **/
	public void shuffle(){
		if(deck.getCardsUsed() > 104){
			action = bjd_action.shuffling;
			deck.shuffle(); // Shuffle deck function in Shoe class
		}
	}

	/** Sets current action to waiting. */
	public void waiting(){
		action = bjd_action.waiting;
	}

	/** Adds player to the table, if player is not already at the table.
	 * @param message from player to parse information to player_status array */
	public void join(bjPlayer msg){
		if(getActivePlayers() < MAX_PLAYERS.value){
			int i;
			boolean notAtTable = true;
			for(i = 0; i < getActivePlayers(); i++)
			{
				if(players[i].uuid == msg.uuid){
					System.out.println("[Dealer " + getUuid() + "] You're already at the table, user " + msg.uuid);
					notAtTable = false;
				}
			}
			if(notAtTable && msg.credits > msg.wager){
				Hand temp = new Hand();
				player_status player = new player_status(msg.uuid, msg.wager, 0f, temp.getHand());
				players[getActivePlayers()] = player;
				setActivePlayers(getActivePlayers() + 1);
			}
			else if(notAtTable){
				System.out.println("[Dealer " + getUuid() + "] Player " + msg.uuid + " does not have enough credits to play.");
			}
		}
		else {
			System.out.println("[Dealer " + getUuid() + "] Table's full. Stop bothering.");
		}
	}

	/** Iterates through the current players at a table and
	 * sets the current players state to wagering.
	 * @param bjPlayer message with nonzero wager */
	public void setWagertoPlayer(bjPlayer msg){
		if(msg.action.value() == bjp_action._wagering){
			int i;
			for(i = 0; i < MAX_PLAYERS.value; i++){
				if(msg.uuid == players[i].uuid){
					System.out.println("[Dealer " + getUuid() + "] Setting wager");
					players[i].wager = msg.wager;
					i = MAX_PLAYERS.value;
				}
			}
		}
	}

	/** Removes players with matching uuid from table. 
	 * @param uuid of the player to kick */
	public void exiting(int uuid){
		int i;
		for(i = 0; i < getActivePlayers(); i++){
			if(players[i].uuid == uuid){
				Hand temp = new Hand();
				setActivePlayers(getActivePlayers() - 1);
				players[i] = new player_status(0, 0, 0f, temp.getHand());
				for(;i < getActivePlayers()-1; i++){
					player_status temp2 = players[i];
					players[i] = players[i+1];
					players[i] = temp2;
				}
			}
		}
		
	}
	
	/** Kicks players with matching uuid from table. 
	 * @param uuid of the player to kick */
	public void kickPlayer(int uuid){
		int i;
		for(i = 0; i < getActivePlayers(); i++){
			if(players[i].uuid == uuid){
				Hand temp = new Hand();
				setActivePlayers(getActivePlayers() - 1);
				players[i] = new player_status(0, 0, 0f, temp.getHand());
				for(;i < getActivePlayers()-1; i++){
					player_status temp2 = players[i];
					players[i] = players[i+1];
					players[i] = temp2;
				}
			}
		}
	}

	/** Finalizes players at table, and resets target_uuid and target_seat
	 * @return true if there is sufficient credits, else false*/
	public boolean startGame(){
		if(checkCredits()){
			setNumberAtTable(getActivePlayers());
			resetSeating();
			System.out.println("[Dealer " + getUuid() + "] The BlackJack game has started. Will soon commence dealing.");
			return true;	
		}
		System.out.println("startGame() returns... False! :( )");
		return false;
	}

	/** Restock credit amount stored in the bank to 500. **/
	public void restockCredits(){
		bank.setCredits(500f);
	}

	/**	Sets action to dealing, and gives cards to all players in game.**/
	public void dealingInitial(){
		int i;
		action = bjd_action.dealing;
		System.out.println("[Dealer " + getUuid() + "] Giving myself some cards.");
		card temp = deck.drawCard(false);
		hand.addCard(new card(temp.suite, temp.base_value, temp.visible));
		temp = null;
		temp = deck.drawCard(true);
		hand.addCard(new card(temp.suite, temp.base_value, temp.visible));
		for(i = 0; i < getNumberAtTable(); i++){
			System.out.println("[Dealer " + getUuid() + "] Giving player " + i + "  some cards.");
			temp = null;
			temp = deck.drawCard(true);
			players[i].cards[0] = new card(temp.suite, temp.base_value, temp.visible);
			temp = null;
			temp = deck.drawCard(true);
			players[i].cards[1] = new card(temp.suite, temp.base_value, temp.visible);
		}
		if(hand.getHandValue() == 21){
			hand.flipCard();
		}
	}

	/** Gives a card to the player with the uuid given
	 * @param uuid of the intended player
	 * @returns true if a card was given, false if a card **/
	public boolean giving_Card(int uuid){
		int i, j;
		for(i = 0; i < getNumberAtTable(); i++){
			if(players[i].uuid == uuid){
				if(Hand.calculateHandValue(players[i].cards) <= 21){
					for(j = 2; j < 21; j++){
						if(!Hand.isValidCard(players[i].cards[j])){
							card temp = deck.drawCard(true);
							players[i].cards[j] = new card(temp.suite, temp.base_value, temp.visible);
							return true;
						}
					}
				}
				else return false;
			}
		}
		return false;
	}

	/** Finishes logic of dealing own hand. **/
	public void dealSelf(){
		int i;

		hand.flipCard();
		card dealer_hand[] = hand.getHand();
		System.out.println("I reveal my trap card!");
		for(i = 0; i < hand.getNumberOfCards(); i++){
			Hand.printCard(dealer_hand[i]);
		}

		System.out.printf("\n\nMy next trick revolves around %d\n", hand.getHandValue());
		while(hand.getHandValue() < 17){
			card temp = deck.drawCard(true);
			hand.addCard(new card(temp.suite, temp.base_value, temp.visible));
			System.out.println("Maybe I'll be better next time with " + hand.getHandValue());
		}

		// System.out.println("I received the luck of the gods. May the Casino Gods have mercy on thy soul.");
		System.out.println("[Dealer " + getUuid() + "] Dealt cards to self.");
	}

	/** Finds and calculates payouts for the loser's of the game.**/
	public void collecting(){
		int i, player_hand_value;
		action = bjd_action.collecting;
		if(hand.getHandValue() <= 21){
			for(i = 0; i < getNumberAtTable(); i++){
				player_hand_value = Hand.calculateHandValue(players[i].cards);
				//Shouldn't calculate loss for players that bust, but for testing later.
				if(player_hand_value > 21 || player_hand_value < hand.getHandValue()){
					players[i].payout = (float)(players[i].wager);
					players[i].wager = 0;
					bank.addCredits(players[i].payout);
				}
			}
		}
		else
		{
			System.out.println("[Dealer " + getUuid() + "] Dealer busted, but I tried to collect, for no reason.");
		}
	}

	/** Finds and calculates payouts for the winner's of the game.**/
	public void paying(){
		int i, player_hand_value;
		action = bjd_action.paying;
		for(i = 0; i < getNumberAtTable(); i++){
			player_hand_value = Hand.calculateHandValue(players[i].cards);
			if(hand.getHandValue() > 21 || (player_hand_value > hand.getHandValue() && player_hand_value <= 21)){
				players[i].payout = (float)(players[i].wager);
				if(Hand.blackJack(players[i].cards)){
					players[i].payout = 1.5f * players[i].payout;
					System.out.println("[Dealer " + getUuid() + "] Blackjack!");
				}
				players[i].wager = 0;
				bank.subtractCredits(players[i].payout);
			}
		}
	}

	/** Payouts and set back to 0. **/
	public void resetPayouts(){
		int i;
		for(i = 0; i < getNumberAtTable(); i++){
			players[i].payout = 0f;
		}
	}

	/** Zeros all values **/
	public void endGame(){
		resetSeating();
		int i;
		for(i = 0;  i < getNumberAtTable(); i++){
			Hand temp1 = new Hand();
			players[i].wager = 0;
			players[i].payout = 0f;
			players[i].cards = temp1.getHand();
		}
		hand.emptyHand();

		setNumberAtTable(0);
		waiting();
	}

	/** Sets target seat and uuid to 0.**/
	public void resetSeating(){
		setTarget_uuid(0);
		setTargetSeat(0);
	}

	/** Targets the player at the next seat, dependent on sequence and whether a reply was given.
	 * In the wager phase and a reply was given, the next player will be targeted.
	 * In the dealing phase and none/no reply, the next player will be targetd.
	 * When the last player is targeted, the target_uuid will be set to 0.
	 * @param boolean representing if a reply was received **/
	public void nextSeat(boolean noReply){
		if(getNumberAtTable() == 0 && !noReply){
			if(getActivePlayers() > getTargetSeat()){
				System.out.println("[Dealer " + getUuid() + "] I'm moving on from you.");
				setTargetSeat(getTargetSeat()+1);
				setTarget_uuid(players[targetSeat-1].uuid);
			}
			else if(getActivePlayers() == getTargetSeat()){
				System.out.println("[Dealer " + getUuid() + "] End of table reached.");
				setTarget_uuid(0);
			}
		}
		else if(getNumberAtTable() > 0 && noReply){
			if(getNumberAtTable() > getTargetSeat()){
				System.out.println("[Dealer " + getUuid() + "] Targeting next player for response.");
				setTargetSeat(getTargetSeat()+1);
				setTarget_uuid(players[getTargetSeat()-1].uuid);
			}
			else if(getNumberAtTable() == getTargetSeat()){
				System.out.println("[Dealer " + getUuid() + "] Dealing phase should be done... Probably.");
				setTarget_uuid(0);
			}
		}

	}

	/**============ Getters && Setters ===================**/

	/**
	 * @return the uuid
	 */
	public int getUuid() {
		return uuid;
	}

	/**
	 * @return the seqno
	 */
	public int getSeqno() {
		return seqno;
	}

	/**
	 * @param seqno the seqno to set
	 */
	public void setSeqno(int seqno) {
		this.seqno = seqno;
	}

	/**
	 * @return the active_players
	 */
	public int getActivePlayers() {
		return active_players;
	}

	/**
	 * @param active_players the active_players to set
	 */
	public void setActivePlayers(int active_players) {
		this.active_players = active_players;
	}

	/**
	 * @return the players
	 */
	public player_status[] getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(player_status[] players) {
		this.players = players;
	}

	/**
	 * @return the action
	 */
	public int getActionValue() {
		return action.value();
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(bjd_action action) {
		this.action = action;
	}

	/**
	 * @return the hand
	 */
	public Hand getHand() {
		return hand;
	}

	/**
	 * @param hand the hand to set
	 */
	public void setHand(Hand hand) {
		this.hand = hand;
	}

	/**
	 * @return the target_uuid
	 */
	public int getTarget_uuid() {
		return target_uuid;
	}

	/**
	 * @param target_uuid the target_uuid to set
	 */
	public void setTarget_uuid(int target_uuid) {
		this.target_uuid = target_uuid;
	}

	/**
	 * @return the deck
	 */
	public Shoe getDeck() {
		return deck;
	}

	/**
	 * @param deck the deck to set
	 */
	public void setDeck(Shoe deck) {
		this.deck = deck;
	}

	/**
	 * @return the bank
	 */
	public Bank getBank() {
		return bank;
	}

	/**
	 * @param bank the bank to set
	 */
	public void setBank(Bank bank) {
		this.bank = bank;
	}

	/**
	 * @return the atTable
	 */
	public int getNumberAtTable() {
		return atTable;
	}

	/**
	 * @param atTable the atTable to set
	 */
	public void setNumberAtTable(int atTable) {
		this.atTable = atTable;
	}

	/**
	 * @return the targetSeat
	 */
	public int getTargetSeat() {
		return targetSeat;
	}

	/**
	 * @param targetSeat the targetSeat to set
	 */
	public void setTargetSeat(int targetSeat) {
		this.targetSeat = targetSeat;
	}
}
