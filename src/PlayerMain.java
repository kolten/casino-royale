import CR.*; // idl stuff

public class PlayerMain{

	Player player = new Player();
	PlayerSub sub;
	PlayerPub pub;
	int seatCount;
	Timer timer;
	boolean exiting = false;
	static final int pauseBuffer = 6000;
	int gameNumber = 0;

	/**
	* Main function. Creates an instance of PlayerMain and calls a run() function. 
	* The arguments are the OpenSplice partition name, bjPlayer topic name, and
	* bjDealer topic name. If the arguments are empty, default arguments are used.
	* 
	* @param args This takes in arguments when running the executable.
	*/
	public static void main(String[] args) {
		PlayerMain main = new PlayerMain();
		if(args != null){
			main.run(args[0], args[1], args[2], pauseBuffer);
		}
		else {
			main.run("Casino Royale", "bjPlayer", "bjDealer", pauseBuffer);
		}
	}
	
	/**
	* The primary function that drives the program logic.
	* Makes use of a Player factory object and precise timing with DealerMain's logic to run correctly.
	* 
	* @param partition OpenSplice partition name
	* @param pubTopic OpenSplice bjPlayer topic name
	* @param subtopic OpenSplice bjDealer topic name
	*/
	public void run(String partition, String pubTopic, String subtopic, int bufferLong)
	{
		sub = new PlayerSub(partition, subtopic); // Sub needs to have the same topic name as the dealer pub
		pub = new PlayerPub(partition, pubTopic); // Vice versa
		timer = new Timer();

		//final int buffer = 200; 		// Make sure this matches DealerMain buffer
		// final int bufferLong = 6000; 	// Dealer's combined buffer time is ~4600 ms, don't go under that. Don't go over buffer+pubBuffer

		boolean notSeated = true;
		boolean wagering = true;
		boolean playingInitial = true;
		boolean playing = true;
		boolean losing = true;
		boolean winning = true;
		boolean not_initial = false;

		bjDealer temp;
		// Are you ready ???? 🎺🎺🎺
		while ( !exiting ) {
			gameNumber++;
			// Joining
			while( notSeated ){
				temp = null;
				temp = sub.read();
				if(temp != null){
					//seatCount = seatCount + 1;

					if(temp.active_players < 6){
						player.joinGame(temp);
						pub.write(player.getMsg());
						//notSeated = false;
						Timer.wait(bufferLong); // Wait 5 seconds
						temp = sub.read(temp.uuid);
						if(temp != null){
							int i = 0;
							for(i = 0; i < temp.active_players; i++){
								if(player.getUuid() == temp.players[i].uuid){
										notSeated = false;
										player.setSeatNumber(i);
										System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] I have joined a game at seat " + player.getSeatNumber());
								} 
							}
						}
					}

				} else {
					// No dealer found
				}
			}
			System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] I am starting the wagering sequence");

			timer.start();
			// Wagering
			while(wagering){
				temp = null;
				temp = sub.read(player.getDealerID());
				if(temp != null && temp.target_uuid == player.getUuid() && temp.action.value() == bjd_action._waiting){
					player.placeWager(temp);
					pub.write(player.getMsg());
					wagering = false;
					System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] A wager has been made for " + player.getWager() + " credits");
				}
				else if(timer.getTimeMs() > bufferLong*10 || (temp != null && temp.seqno == 0)) // this is a quick fix to go to the end of the loop.
				{
					System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] The dealer has left the table.");
					exiting = true;
					wagering = false;
				}
				// TODO: Clean up this quick fix. This assumes dealers only exit during wagering
			}
			if(exiting)
			{
				continue;
			}
			// see above TODO about the exiting quick fix

			System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] I'm actually in game!");
			
			// Initial playing round - 2 cards, no requests yet
			while(playingInitial){
				temp = null;
				temp = sub.read(player.getDealerID());
				if(temp != null && temp.action.value() == bjd_action._dealing){
					
					player.initDeal(temp);
					playingInitial = false;
				}
			}

			// Let's play a game.

			System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] I am preparing to hit or stay.");
			while(playing){
				temp = null;
				temp = sub.read(player.getDealerID());
				if((temp != null) && (temp.target_uuid == player.getUuid()) && (temp.action.value() == bjd_action._dealing)){
					if(not_initial){
						player.singleDeal(temp);
					}

					not_initial = true;

					if(player.getCurrentHandValue() >= 17){
							player.stay();
							playing = false;
							System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] I am going to stop requesting (Stay).");
						}

						else if(player.getCurrentHandValue() <= 16){
							player.requestCard();
							System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] I am going to request a card (Hit).");
						}
						pub.write(player.getMsg());
						System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] My hand has a value of " + player.getCurrentHandValue());

						// print if we get a blackjack
						if(player.getCurrentHandValue() == 21 && player.getHand().getNumberOfCards() == 2)
							System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] I got a Blackjack!");
				}
			}

			// Take the L 
			System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] Looking for collecting message...");
			while ( losing ){
				temp = sub.read(player.getDealerID());
				if((temp != null) && (temp.action.value() == bjd_action._collecting)){
					System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] Dealer is collecting credits!");
					// TODO: set up bank or subtract from credits
					// For now,
					// float curCredits = player.getCredits() - player.getWager();
					// uses the payout from message rather than locally subtracting what we waged
					float curCredits = player.getCredits() - temp.players[player.getSeatNumber()].payout;
					
					if(curCredits < player.getCredits())
						System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] We lost! We now have " + curCredits + " credits");
					else
						System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] We did not lose any credits.");
						
					
					player.setCredits(curCredits);
					losing = false;
				}
			}

			// You're a winner, Harry.
			System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] Looking for paying message");
			while(winning){
				temp = sub.read(player.getDealerID());
				if((temp != null) && (temp.action.value() == bjd_action._paying)){
					System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] Dealer is paying out credits!");
					// TODO: set up bank or add to credits
					// For now,
					// float curCredits = player.getCredits() + player.getWager();
					// uses the payout from message rather than locally adding what we waged
					float curCredits = player.getCredits() + temp.players[player.getSeatNumber()].payout;
					
					if(curCredits > player.getCredits())
						System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] We won! We now have " + curCredits + " credits");
					else
						System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] We did not win any credits.");
					
					player.setCredits(curCredits);
					winning = false;
				}
			}

			System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] I am removing my cards from my hand.");
			player.endGame();

			System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] The round has ended.");
			// TODO: check if there are no players sitting and exit?
			// exiting = true;
			wagering = true;
			playingInitial = true;
			playing = true;
			losing = true;
			winning = true;
			not_initial = false;
			notSeated = false;
		}
		System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] Exiting, leaving table");
		System.out.println("[Player " + player.getUuid() + " G" + gameNumber + "] We left the table with " + player.getCredits() + " credits.");

		sub.close();
		pub.close();
	}
	
}
