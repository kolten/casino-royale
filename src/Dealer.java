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
	private final int uuid;
	private int seqno;
	private int active_players;
	private player_status[] players;
	private bjd_action action;
	private Hand hand;					//Holds dealer hand and card logic.
	private int target_uuid;

	//boolean isHuman; Will not do human interaction

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
		
		//isHuman = false;

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
		
		//isHuman = false;

		deck = new Shoe();
		bank = new Bank(500f);
		atTable = 0;
		targetSeat = 0;
		
		shuffle();
	}
	
	/** @return bjDealer message that contains all the significant values of DealerFactory**/
	public bjDealer getMsg(){
		bjDealer temp = new bjDealer(uuid, seqno, active_players, players, action, hand.getHand(), target_uuid);
		nextSeqno();
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
		System.out.println("debug statement stillWagering()");
		return false;
	}
	
	/** Checks how many credits the dealer should have before starting a game.
	 * @return  true if there are sufficient credits, else false **/
	public boolean checkCredits(){
		// Returns true if Dealer has sufficient credits, else false
		if(creditsInBank() >= getActivePlayers() * 20){
			return true;
		}
		return false;
	}

	/** Checks if the given dealer state is the same as the current action.
	 * @param value within the bjd_action
	 * @return true if action.value is same as the given value, else false. **/
	public boolean sameAction(bjd_action action){
		if(this.action.value() == action.value()){
			return true;
		}
		return false;
	}

	/**========== Sequencing Methods =============**/
	
	/** Changes state to shuffling, and shuffles deck. **/
	public void shuffle(){
		action = bjd_action.shuffling;
		deck.shuffle(); // Shuffle deck function in Shoe class
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
					System.out.println("You're already at the table, you jackwagon.");
					notAtTable = false;
				}
			}
			if(notAtTable){
				Hand temp = new Hand();
				player_status player = new player_status(msg.uuid, msg.wager, 0f, temp.getHand());
				players[getActivePlayers()] = player;
				setActivePlayers(getActivePlayers() + 1);
			}
		}
		else {
			System.out.println("Table's full. Stop bothering.");
		}
	}

	/** Sets current action to waiting. */
	public void waiting(){
		action = bjd_action.waiting;
	}

	/** Iterates through the current players at a table and
	 * sets the current players state to wagering.
	 * @param  msg
	 * @return none */
	public void setWagertoPlayer(bjPlayer msg){
		if(msg.action.value() == bjp_action._wagering){
			int i;
			for(i = 0; i < MAX_PLAYERS.value; i++){
				if(msg.uuid == players[i].uuid){
					System.out.println("Setting wager");
					players[i].wager = msg.wager;
					i = MAX_PLAYERS.value;
				}
			}
		}
	}

	/** Kicks players with matching uuid from table. 
	 * @param  */
	public void kickPlayer(int uuid){
		/*//Not doing this yet. But eventaully.
		int i;
		for(i = 0; i < getActivePlayers(); i++){
			if(players[i].uuid == uuid){
				Hand temp = new Hand();
				setActivePlayers(getActivePlayers() - 1);
				players[i] = new player_status(0, 0, 0f, temp.getHand());
			}
		}*/
		
	}

	/** Finalizes players at table, and resets target_uuid and target_seat
	 * @return true if there is sufficient credits, else false*/
	public boolean startGame(){
		if(checkCredits()){
			setNumberAtTable(getActivePlayers());
			resetSeating();
			System.out.println("startGame() returns... True! :)");
			return true;	
		}
		System.out.println("startGame() returns... False! :( )");
		return false;
	}

	/**	Sets action to dealing, and gives cards to all players in game.**/
	public void dealingInitial(){
		int i;
		action = bjd_action.dealing;
		System.out.println("Giving myself some cards.");
		card temp = deck.drawCard(false);
		hand.addCard(new card(temp.suite, temp.base_value, temp.visible));
		temp = null;
		temp = deck.drawCard(true);
		hand.addCard(new card(temp.suite, temp.base_value, temp.visible));
		for(i = 0; i < getNumberAtTable(); i++){
			System.out.println("Giving player " + i + "  some cards.");
			temp = null;
			temp = deck.drawCard(true);
			players[i].cards[0] = new card(temp.suite, temp.base_value, temp.visible);
			temp = null;
			temp = deck.drawCard(true);
			players[i].cards[1] = new card(temp.suite, temp.base_value, temp.visible);
		}
	}
	
	/** Gives a card to the player with the uuid given
	 * @param uuid of the intended player **/
	public void giving_Card(int uuid){
		int i, j;
		for(i = 0; i < getNumberAtTable(); i++){
			if(players[i].uuid == uuid){
				for(j = 3; j < 21; j++){
					if(!Hand.isValidCard(players[i].cards[j])){
						card temp = deck.drawCard(true);
						players[i].cards[j] = new card(temp.suite, temp.base_value, temp.visible);
						return;
					}
				}
			}
		}
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
		
		System.out.println("I received the luck of the gods. May the Casino Gods have mercy on thy soul.");
	}
	
	/** Finds and calculates payouts for the loser's of the game.**/
	public void collecting(){
		int i, player_hand_value;
		action = bjd_action.collecting;
		if(hand.getHandValue() <= 21){
			for(i = 0; i < getNumberAtTable(); i++){
				player_hand_value = Hand.calculateHandValue(players[i].cards);
					//Shouldn't calculate loss for players that bust, but for testing later.
				if(player_hand_value > 21 || player_hand_value <= hand.getHandValue()){
						players[i].payout = (float)(players[i].wager);
				}
			}
		}
		else
		{
			System.out.println("Dealer busted, but I tried to collect, for no reason.");
		}
	}
	
	/** Finds and calculates payouts for the winner's of the game.**/
	public void paying(){
		int i, player_hand_value;
		action = bjd_action.paying;
		for(i = 0; i < getNumberAtTable(); i++){
			player_hand_value = Hand.calculateHandValue(players[i].cards);
			if(hand.getHandValue() > 21 || player_hand_value >= hand.getHandValue() && player_hand_value <= 21){
				players[i].payout = (float)(players[i].wager);
				if(Hand.blackJack(players[i].cards)){
					players[i].payout = 1.5f * players[i].payout;
				}
			}
		}
	}
	
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
			Hand temp = new Hand();
			players[i].wager = 0;
			players[i].payout = 0f;
			players[i].cards = temp.getHand();
		}
		setNumberAtTable(0);
	}
	
	public void resetSeating(){
		setTarget_uuid(0);
		setTargetSeat(0);
	}

	/** Targets the player at the next seat, should that player exist and a reply was given.
	 * @param boolean representing if a reply was received **/
	public void nextSeat(boolean noReply){
		if((getNumberAtTable() == 0) && (getActivePlayers() > getTargetSeat()) && (!noReply)){
			System.out.println("I'm moving on from you.");
			targetSeat++;
			setTarget_uuid(players[targetSeat-1].uuid);
		}
		else if((getNumberAtTable() == 0) && (getActivePlayers() == getTargetSeat()) && (!noReply)){
			System.out.println("End of table reached.");
			setTarget_uuid(0);
		}
		else if(getNumberAtTable() > 0 && getNumberAtTable() > getTargetSeat() && (!noReply)){
			System.out.println("Targeting next player for response.");
			setTarget_uuid(players[getTargetSeat()-1].uuid);
		}
		else if(getNumberAtTable() > 0 && getNumberAtTable() == getTargetSeat() && (!noReply)){
			System.out.println("Dealing phase should be done... Probably.");
			setTarget_uuid(0);
		}
	}

	public void nextSeqno() {
		seqno++;
	}

	/**=========== Deck Methods ========**/
	
	public int getCardsLeftInDeck(){
		return Shoe.size - deck.getCardsUsed();
	}
	
	/**=========== Bank Methods =========**/
	
	/**=========== Bank Methods ========**/
	
	/** @return the total credits in bank **/
	public float creditsInBank() {
		return bank.getCredits();
	}

	public void restockCredits(){
		bank.setCredits(500f);
	}
	
	/** @param bank the bank to set **/
	public void addCredits(float credits) {
		bank.addCredits(credits);
	}
	
	public void subtractCredits(float credits)
	{
		bank.subtractCredits(credits);
	}
	
	/**=============== Getters && Setters ==============**/
	
	/**============ Getters && Setters ===================**/

	/** @return the uuid **/
	public int getUuid() {
		return uuid;
	}

	/** @return the seqno **/
	public int getSeqno() {
		return seqno;
	}

	/** @return the activePlayers **/
	public int getActivePlayers() {
		return active_players;
	}

	/** @param activePlayers the activePlayers to set **/
	public void setActivePlayers(int activePlayers) {
		this.active_players = activePlayers;
	}

	/** @return the action **/
	public int getActionValue() {
		return action.value();
	}

	/** @param action the action to set **/
	public void setActionValue(int value) {
		if(value < 5)
		{
			action = bjd_action.from_int(value);
		}
	}

	/** @return the target_uuid **/
	public int getTarget_uuid() {
		return target_uuid;
	}

	/** @param target_uuid the target_uuid to set **/
	public void setTarget_uuid(int target_uuid) {
		this.target_uuid = target_uuid;
	}

	/** @return the atTable **/
	public int getNumberAtTable() {
		return atTable;
	}

	/** @param atTable the number of players in game **/
	public void setNumberAtTable(int atTable) {
		this.atTable = atTable;
	}

	/** @return the targetSeat **/
	public int getTargetSeat() {
		return targetSeat;
	}

	/** @param targetSeat the seat to set the target_uuid to **/
	public void setTargetSeat(int targetSeat) {
		this.targetSeat = targetSeat;
	}
}
