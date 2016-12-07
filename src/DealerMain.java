import CR.*; // idl stuff

import java.util.ArrayList;

public class DealerMain {

	static final int shortBuffer = 200;		//Hard-coded read buffer.
	static final int longBuffer = 5000;	//Hard-coded publish time buffer.
	
	public static void main(String[] args) {
		DealerMain main = new DealerMain();
		if(args != null){
			main.run(args[0], args[1], args[2],shortBuffer,longBuffer);
		}
		else {
			main.run("Casino Royale", "bjDealer", "bjPlayer",shortBuffer,longBuffer);
		}
	}

	private Dealer dealer = new Dealer();
	private DealerSub sub;
	private DealerPub pub;
	private Timer timer;
	private Timer systimer;
	private ArrayList<bjPlayer> playerMessages;

	/** Runs the entirety of the Casino Royale Blackjack game.
	 * @param partion name (Participant) to use of the OpenSplice Domain.
	 * @param pubTopic name to use for publisher to use. Should be same as subTopic for PlayerMain.
	 * @param subTopic name to use for subscriber to use. Should be same as pubTopic for PlayerMain. */
	public void run(String partition, String pubTopic, String subTopic, int buffer, int pubBuffer)
	{
		// final int buffer = 200;		//Hard-coded read buffer.
		// final int pubBuffer = 5000;	//Hard-coded publish time buffer.

		/** Condition counters. **/
		int gameCount = 0;				//Number of games played.
		int kcount = 0;					//Kick counter for unresponsive players
		int jcount = 0;					//Join counter if all wagered and less than 6 players

		boolean noReply = false;		//Boolean for tracking replies
		boolean stillDealing = true;	//Boolean to check if all players had stayed hand.

		/** Factory and PubSub instantiation **/
		sub = new DealerSub(partition, subTopic);	// Sub needs to have the same topic name as the dealer pub
		pub = new DealerPub(partition, pubTopic);	// Vice versa
		timer = new Timer();
		systimer = new Timer();
		
		timer.start();
		systimer.start();
		while(gameCount < 2){
			dealer.shuffle();	//Will only shuffle when there a predetermined amount of cards are used.
				//Waiting loop for reading Joining/Wagering/Exiting messages from the player.
			while(dealer.stillWagering() || (!dealer.isFullTable() && !dealer.stillWagering() && jcount < 2) || dealer.sameAction(bjd_action.shuffling)){
					//Empty table and shuffling loop that will repeat until at least one player has joined.
				while(dealer.getActivePlayers() == 0 || dealer.sameAction(bjd_action.shuffling)) {

					System.out.println("[Dealer " + dealer.getUuid() + "] Table is being set, please wait.");

					pubSubAction(buffer, pubBuffer, bjd_action.shuffling);

					dealer.waiting();
				}	//Breaks from loop if any players have joined.

				dealer.nextSeat(noReply);	//Targets the next player.
				noReply = true;

				System.out.println("[Dealer " + dealer.getUuid() + "] A player is required for wagers.");

				noReply = pubSubAction(buffer, pubBuffer, bjd_action.waiting);
				
				if(!noReply){
					jcount = 0;
					kcount = 0;
				}
				else if(noReply && dealer.stillWagering()){		//Kick timer starts here iff there are players still wagering.
					kcount++;
					System.out.println("[Dealer " + dealer.getUuid() + "] Player " + dealer.getTarget_uuid() + "will be kicked in " + (2 - kcount) + " messages.");
				}
				else if(!dealer.stillWagering() && dealer.getActivePlayers() < MAX_PLAYERS.value){
					jcount++;
					System.out.println((2-jcount) + " messages until game starts.");
				}
				if(kcount >= 2 && dealer.stillWagering()){		//Kick timer resets after player has been kicked.
					System.out.println("[Dealer " + dealer.getUuid() + "] Player " + dealer.getTarget_uuid() + " has been kicked from table.");
					//dealer.kickPlayer(dealer.getTarget_uuid());
					//noReply = false;
					kcount = 0;
				}
					//Should there be less than the MAX_PLAYERS at the table and all have wagered, a joining counter will start.
						//This counter will allow for 10s for players to join.
				if(!dealer.stillWagering() && (dealer.isFullTable() || jcount >= 2) && !dealer.startGame()){
						//Start game will check credits and will only enter this conditional if dealer has insufficient credits.
						//The waiting phase will repeat 4 times (or 30s).
					System.out.println("[Dealer " + dealer.getUuid() + "] Restocking credits; Please wait for 30s until restocking has finished.");
					jcount = 0;
					while(jcount < 4){
						pubSubAction(buffer, pubBuffer, bjd_action.shuffling);
						jcount++;
					}
					dealer.restockCredits();
					if(dealer.stillWagering()){	//If any players have joined during this phase, the join counter is reset.
						noReply = true;
						jcount = 0;
					}
					else {
						dealer.startGame();
					}
				}
			}	//Breaks if all have wagered with full table or join counter has reached 2.

				//Special conditional waiting phase if table is full and all have wagered. Does nothing.
			if(dealer.isFullTable() && !dealer.stillWagering()){
				System.out.println("[Dealer " + dealer.getUuid() + "] All players have wagered. Dealing will start soon.");

				pubSubAction(buffer, pubBuffer, bjd_action.shuffling);
			}

			dealer.startGame();

			dealer.dealingInitial();		//First card of initial dealing phase.

			System.out.printf("\n\nEvery player receives 2 card.\n\n");

			pubSubAction(buffer, pubBuffer, bjd_action.shuffling);	//Read only joining messages, since initial dealing does not require a response.
			
			stillDealing = true;
			noReply = true;
			while(stillDealing){		//Second dealing phase that repeats until all player have stayed their hand.
				dealer.nextSeat(noReply);
				noReply = true;			//No Reply or None elicits that they are staying hand.
										//Seats only change when a hand is stayed.

				System.out.println("[Dealer " + dealer.getUuid() + "] Player " + dealer.getTarget_uuid() + ": Hit or stay?");

				noReply = pubSubAction(buffer, pubBuffer, bjd_action.dealing);
				
				if(dealer.getTarget_uuid() == 0){
					dealer.resetSeating();
					stillDealing = false;
				}
			}	//When all players at table has been targeted at some point

			dealer.dealSelf();

			System.out.println("[Dealer " + dealer.getUuid() + "] Finished dealing. Preparing to divy up the payouts.");

			pubSubAction(buffer, pubBuffer, bjd_action.shuffling);

			dealer.collecting();

			System.out.println("[Dealer " + dealer.getUuid() + "] Collecting from players.");

			pubSubAction(buffer, pubBuffer, bjd_action.collecting);

			dealer.resetPayouts();

			dealer.paying();

			System.out.println("[Dealer " + dealer.getUuid() + "] Paying players.");

			pubSubAction(buffer, pubBuffer, bjd_action.paying);

			dealer.endGame();

			System.out.println("[Dealer " + dealer.getUuid() + "] The round has ended.");
			gameCount++;
			noReply = false;
			stillDealing = true;
			kcount = 0;
			jcount = 0;
		}

		System.out.println("[Dealer " + dealer.getUuid() + "] Dealer Exiting, Leaving table");
		System.out.println("[Dealer " + dealer.getUuid() + "] Dealer left with " + dealer.getBank().getCredits() + " credits in Bank");
		Timer.wait(buffer);
		
		sub.close();
		pub.close();
	}
	
	/** PubSub loop that will read joining messages and specific responses from player based off of the current dealer action.
	 * Waiting and Dealing are only times when specific responses are needed, and thus are the only ones checked.
	 * Shuffling will be used as the default action for reading joining messages only.
	 * @param buffer to affect how often the messages will be read.
	 * @param pubBuffer to affect how long until next publish.
	 * @param action to show parse what responses are necessary from the player
	 * @return boolean if a reply was read or not.**/
	public boolean pubSubAction(int buffer, int pubBuffer, bjd_action action){
		boolean noReply = true;
		int j;

		pub.write(dealer.getMsg());
		System.out.println("[Dealer " + dealer.getUuid() + "] Sent message at " + systimer.getTimeMs() + " ms from System Time.");
		System.out.println("[Dealer " + dealer.getUuid() + "] Local timer at " + timer.getTimeMs() + " ms after publishing.");
		
		timer.start();

		while(timer.getTimeMs() < pubBuffer - buffer){	//Read for joining
			if(action.value() == bjd_action._dealing){
				playerMessages = sub.read(dealer.getUuid(), dealer.getNumberAtTable(), dealer.getTarget_uuid());
			}
			else if(action.value() == bjd_action._waiting){
				playerMessages = sub.read(dealer.getUuid(), dealer.getActivePlayers(), dealer.getTarget_uuid());
			}
			else playerMessages = sub.read(dealer.getUuid());
			if(playerMessages != null && !playerMessages.isEmpty()){
				System.out.println(playerMessages.size() + " messages received at " + timer.getTimeMs() + " ms after publishing.");
				for(j = 0; j < playerMessages.size(); j++){
					System.out.println("[Dealer " + dealer.getUuid() + "] ~~~~~~~~|Message " + j + "|~~~~~~~~");
					switch(playerMessages.get(j).action.value()){
					case CR.bjp_action._joining:
						System.out.println("[Dealer " + dealer.getUuid() + "] Player " + playerMessages.get(j).uuid + " has joined the table.");
						dealer.join(playerMessages.get(j));
						break;
					case CR.bjp_action._wagering:
						if(noReply && action.value() == bjd_action._waiting){
							dealer.setWagertoPlayer(playerMessages.get(j));
							System.out.println("[Dealer " + dealer.getUuid() + "] Player " + playerMessages.get(j).uuid + " has chosen to wager " + playerMessages.get(j).wager + " credits.");
							noReply = false;
						}
						break;
					case CR.bjp_action._exiting:
						if(noReply && action.value() == bjd_action._waiting){
							System.out.println("[Dealer " + dealer.getUuid() + "] Player " + playerMessages.get(j).uuid + " has chosen to leave.");
							dealer.exiting(playerMessages.get(j).uuid);;
							noReply = false;
						}
						break;
					case CR.bjp_action._requesting_a_card:
						if(noReply && action.value() == bjd_action._dealing){
							System.out.println("[Dealer " + dealer.getUuid() + "] A card has been requested.");
							noReply = !dealer.giving_Card(playerMessages.get(j).uuid);
						}
						break;
					case CR.bjp_action._none:	//None response is interpreted as the no response.
						if(noReply && action.value() == bjd_action._dealing){
							System.out.println("[Dealer " + dealer.getUuid() + "] Player " + playerMessages.get(j).uuid + " has chosen to stay hand.");
							break;
						}
					default: System.out.println("[Dealer " + dealer.getUuid() + "] Function has failed."); break;
					}
				}
			}
			if(playerMessages != null){
				playerMessages.clear();
			}
			Timer.wait(buffer);
		}

		while(timer.getTimeMs() < pubBuffer - 10);
		
		System.out.println(timer.getTimeMs() + " ms after publishing.");
		
		return noReply;
	}

	
	/**
	 * @return the dealer
	 */
	public Dealer getDealer() {
		return dealer;
	}

	/**
	 * @param dealer the dealer to set
	 */
	public void setDealer(Dealer dealer) {
		this.dealer = dealer;
	}

	/**
	 * @return the playerMessages
	 */
	public ArrayList<bjPlayer> getPlayerMessages() {
		return playerMessages;
	}

	/**
	 * @param playerMessages the playerMessages to set
	 */
	public void setPlayerMessages(ArrayList<bjPlayer> playerMessages) {
		this.playerMessages = playerMessages;
	}
}