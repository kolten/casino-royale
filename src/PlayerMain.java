
import CR.*; // idl stuff


public class PlayerMain{

	Player player;
	PlayerSub sub;
	PlayerPub pub;
	int seatCount;
	Timer timer;

	public static void main(String[] args) {
		PlayerMain main = new PlayerMain();
		if(args != null){
			main.run(args[0], args[1], args[2]);
		}
		else {
			main.run("Casino Royale", "bjPlayer", "bjDealer");
		}
	}
	
	public void run(String partition, String pubTopic, String subtopic)
	{
		player = new Player();
		sub = new PlayerSub(partition, subtopic); // Sub needs to have the same topic name as the dealer pub
		pub = new PlayerPub(partition, pubTopic); // Vice versa
		timer = new Timer();

		boolean exiting = false;
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
			// Joining
			while( notSeated ){
				temp = sub.read();
				if(temp != null){
					//seatCount = seatCount + 1;

					if(temp.active_players < 6){
						player.joinGame(temp);
						pub.write(player.getMsg());
						//notSeated = false;
						//timer.start();   ??? maybe ???
						timer.wait(5000); // Wait 5 seconds
						temp = sub.read();
						if(temp != null){
							int i = 0;
							for(i = 0; i < temp.active_players; i++){
								if(player.getUuid() == temp.players[i].uuid){
										notSeated = false;
										player.setSeatNumber(i);
										System.out.println("I'm a real boy");
								} 
							}
						}
					}

				} else {
					// No dealer found
				}
			}
			System.out.println("Oh boy, I'm going to wager again");
			// Wagering
			while(wagering){
				temp = sub.read(player.getDealerID());
				System.out.println("I need a dealer in my life.");
				if((temp != null) && (temp.target_uuid == player.getUuid()) && (temp.action.value() == bjd_action._waiting)){
					player.placeWager(temp);
					pub.write(player.getMsg());
					wagering = false;
					System.out.println("I'm suprised I made it this far.");
				}
			}
			System.out.println("I'm actually in game!");
			
			// Initial playing round?
			while(playingInitial){
				temp = sub.read(player.getDealerID());
				if((temp != null) && (temp.action.value() == bjd_action._dealing)){
					
					player.initDeal(temp);
					playingInitial = false;
				}
			}

			// Let's play a game.

			while(playing){
				temp = sub.read(player.getDealerID());
				if((temp != null) && (temp.target_uuid == player.getUuid()) && (temp.action.value() == bjd_action._dealing)){
					if(not_initial){
						player.singleDeal(temp);
					}

					not_initial = true;

					if(player.getCurrentHandValue() >= 17){
							player.stay();
							playing = false;

						}

						else if(player.getCurrentHandValue() <= 16){
								player.requestCard();
						}
						pub.write(player.getMsg());
				}
			}

			// Take the L 
			while ( losing ){
				temp = sub.read(player.getDealerID());
				if((temp != null) && (temp.target_uuid == player.getUuid()) && (temp.action.value() == bjd_action._collecting)){
					// TODO: set up bank or subtract from credits
					// For now,
					float curCredits = player.getCredits() - player.getWager();
					player.setCredits(curCredits);
					losing = false;
				}
			}

			// You're a winner, Harry.
			while(winning){
				temp = sub.read(player.getDealerID());
				if((temp != null) && (temp.target_uuid == player.getUuid()) && (temp.action.value()==bjd_action._paying)){
					// TODO: set up bank or add to credits
					// For now,
					float curCredits = player.getCredits() + player.getWager();
					player.setCredits(curCredits);
					playingInitial = false;
				}
			}
			// TODO: check if there are no players sitting and exit?
			exiting = false;
			wagering = true;
			playingInitial = true;
			playing = true;
			losing = true;
			winning = true;
			not_initial = false;
		}
	}
	
}


