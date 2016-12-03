import CR.*; // idl stuff
import java.util.ArrayList;

public class DealerMain {

	public static void main(String[] args) {
		DealerMain main = new DealerMain();
		if(args != null){
			main.run(args[0], args[1], args[2]);
		}
		else {
			main.run("Casino Royale", "bjDealer", "bjPlayer");
		}
	}

	private Dealer dealer;
	private DealerSub sub;
	private DealerPub pub;
	private Timer timer;
	private ArrayList<bjPlayer> playerMessages;

	public void run(String partition, String pubTopic, String subTopic)
	{
		final int buffer = 200;		//Hardcoded read buffer. // Make sure this matches DealerMain buffer
		final int pubBuffer = 4400;	//Hardcoded publish time buffer.
		
		/** Condition counters. **/
		int gameCount = 0;				//Number of games played.
		int kcount = 0;					//Kick counter for unresponsive players
		int jcount = 0;					//Join counter if all wagered and less than 6 players

		boolean noReply = false;		//Boolean for tracking replies
		boolean stillDealing = true;	//Boolean to check if all players had stayed hand.
		
		/** Factory and PubSub instantiation **/
		dealer = new Dealer();						//Dealer factory to produce bjDealer objects.
		sub = new DealerSub(partition, subTopic);	// Sub needs to have the same topic name as the dealer pub
		pub = new DealerPub(partition, pubTopic);	// Vice versa
		timer = new Timer();						//Timer to keep publish time.
		playerMessages = new ArrayList<bjPlayer>();

		int i;

		while(gameCount < 1){
			dealer.shuffle();
			
			while(dealer.stillWagering() || (dealer.isFullTable() && !dealer.stillWagering() && jcount < 2) || dealer.sameAction(bjd_action.shuffling)){
					//Empty table loop.
				while(dealer.getActivePlayers() == 0 || dealer.sameAction(bjd_action.shuffling)) {

					System.out.println("Table is being set, please wait.");
					
					joinPubSub(buffer, pubBuffer);

					dealer.waiting();
				}	//Breaks from loop if any players have joined.
				

				dealer.nextSeat(noReply);
				noReply = true;

				System.out.println("A player is required for wagers.");
				
				pub.write(dealer.getMsg());
				timer.start();
					//Read loop for wagering, exiting, or joining messages.
				while(timer.getTimeMs() < pubBuffer){
					playerMessages = sub.read(dealer.getUuid(), dealer.getActivePlayers(), dealer.getTarget_uuid());
					
					if(playerMessages != null && !playerMessages.isEmpty()){
						System.out.println(playerMessages.size() + " messages received at " + timer.getTimeMs() + " after publishing.");
						for(i = 0; i < playerMessages.size(); i++){
							System.out.println("~~~~~~~~|Message " + i + "|~~~~~~~~");
							switch(playerMessages.get(i).action.value()){
								case CR.bjp_action._joining:
									jcount = 0;
									dealer.join(playerMessages.get(i));
									break;
								case CR.bjp_action._exiting:
									if(noReply){
										dealer.kickPlayer(playerMessages.get(i).uuid);
										kcount = 0;
										noReply = false;
									}
									break;
								case CR.bjp_action._wagering:
									if(noReply){
										dealer.setWagertoPlayer(playerMessages.get(i));
										System.out.println("Wager has been received.");
										kcount = 0;
										noReply = false;
									}
									break;
								default: System.out.println("Why do bad things happen to bad code?"); break;
							}
						}
					}
					if(playerMessages != null){
						playerMessages.clear();
					}
					Timer.wait(buffer);
				}
				
				if(noReply && dealer.stillWagering()){
					kcount++;
					System.out.println("Player " + dealer.getTarget_uuid() + "will be kicked in " + (2 - kcount) + " messages.");
				}
				if(kcount >= 2 && dealer.stillWagering()){
					System.out.println("Player " + dealer.getTarget_uuid() + " has been kicked from table.");
					dealer.kickPlayer(dealer.getTarget_uuid());
					//noReply = false;
					kcount = 0;
				}
				if(!dealer.stillWagering() && dealer.getActivePlayers() < MAX_PLAYERS.value){
					jcount++;
					System.out.println((2-jcount) + " messages until game starts.");
				}
				
				System.out.println(timer.getTimeMs() + " ms after publishing.");
				
				if(!dealer.startGame()){
					System.out.println("Restocking credits; Please wait for 30s until restocking has finished.");
					jcount = 0;
					while(jcount < 4){
						joinPubSub(buffer, pubBuffer);
						jcount++;
					}
					dealer.restockCredits();
				}
			}	//Breaks if all have wagered with full table or join counter has reached 2.

				//Special condition break only if table is full and all have wagered.
			if(dealer.isFullTable() && !dealer.stillWagering()){
				System.out.println("All players have wagered. Dealing will start soon.");
				
				joinPubSub(buffer, pubBuffer);
			}
			
			dealer.startGame();
			
			dealer.dealingInitial();
			
			System.out.printf("\n\nEvery player receives 2 card.\n\n");
			
			joinPubSub(buffer, pubBuffer);
			
			noReply = true;
			while(stillDealing){
				dealer.nextSeat(noReply);
				noReply = true;

				System.out.println("Player " + dealer.getTarget_uuid() + ": Hit or stay?");
				
				pub.write(dealer.getMsg());
				timer.start();
				
				while(timer.getTimeMs() < pubBuffer){	//Reading for dealing with your problems.
					playerMessages = sub.read(dealer.getUuid(), dealer.getActivePlayers(), dealer.getTarget_uuid());
					if(playerMessages != null && !playerMessages.isEmpty()){
						System.out.println(playerMessages.size() + " messages received at " + timer.getTimeMs() + " after publishing.");
						for(i = 0; i < playerMessages.size(); i++){
							System.out.println("~~~~~~~~|Message " + i + "~~~~~~~~");
							switch(playerMessages.get(i).action.value()){
								case CR.bjp_action._joining: dealer.join(playerMessages.get(i)); break;
								case CR.bjp_action._requesting_a_card:
									if(noReply){
										System.out.println("A card has been requested.");
										noReply = !dealer.giving_Card(playerMessages.get(i).uuid);
									}
									break;
								case CR.bjp_action._none:
									System.out.println("Staying hand.");
									break;
								default: System.out.println("Logic is so twisted, it has it's own stop?"); break;
							}
						}
					}
					if(playerMessages != null){
						playerMessages.clear();
					}
					Timer.wait(buffer);
				}
				if(dealer.getTarget_uuid() == 0){
					dealer.resetSeating();
					stillDealing = false;
				}
				
				System.out.println(timer.getTimeMs() + " ms after publishing.");
			}	//When all players at table has been targeted at some point

			dealer.dealSelf();

			System.out.println("Finished dealing. Preparing to divy up the payouts.");
			
			joinPubSub(buffer, pubBuffer);
			
			dealer.collecting();

			System.out.println("Collecting from players.");
			
			joinPubSub(buffer, pubBuffer);
			
			dealer.resetPayouts();
			
			dealer.paying();

			System.out.println("Paying players.");
			
			joinPubSub(buffer, pubBuffer);
			
			dealer.endGame();
			
			System.out.println("The round has ended.");
			gameCount++;
			noReply = false;
			stillDealing = true;
			kcount = 0;
			jcount = 0;
		}
		
		System.out.println("Exiting, Leaving table");
		timer.wait(buffer);
		sub.close();
		pub.close();
	}

	/** PubSub loop that read only joining messages from the player
	 * @param buffer to affect how often the messages will be read.
	 * @param pubBuffer to affect how long until next publish. **/
	public void joinPubSub(int buffer, int pubBuffer){
		int j;
		
		pub.write(dealer.getMsg());
		timer.start();
		
		while(timer.getTimeMs() < pubBuffer){	//Read for joining
			playerMessages = sub.read(dealer.getUuid());
			if(playerMessages != null && !playerMessages.isEmpty()){
				System.out.println(playerMessages.size() + " messages received at " + timer.getTimeMs() + " ms after publishing.");
				for(j = 0; j < playerMessages.size(); j++){
					System.out.println("~~~~~~~~|Message " + j + "|~~~~~~~~");
					switch(playerMessages.get(j).action.value()){
						case CR.bjp_action._joining:
							System.out.println("Player " + playerMessages.get(j).uuid + " has joined the table.");
							dealer.join(playerMessages.get(j));
							break;
						default: System.out.println("Function has failed."); break;
					}
				}
			}
			if(playerMessages != null){
				playerMessages.clear();
			}
			Timer.wait(buffer);
		}
		
		System.out.println(timer.getTimeMs() + " ms after publishing.");
	}
}