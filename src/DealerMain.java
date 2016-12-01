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
	
	public void run(String partition, String pubTopic, String subTopic)
	{	
		Dealer dealer;
		DealerSub sub;
		DealerPub pub;
		Timer timer;

		int buffer = 200;
		//int pubBuffer = 5000;		Hardcoded publish time buffer?
		
		/** Condition counters. **/
		int gameCount = 0;
		int kcount = 0;			//Kick counter for unresponsive players
		int jcount = 0;			//Join counter if all wagered and less than 6 players

		boolean noReply = true;
		boolean stillDealing = true;
		
		dealer = new Dealer();
		sub = new DealerSub(partition, subTopic); // Sub needs to have the same topic name as the dealer pub
		pub = new DealerPub(partition, pubTopic); // Vice versa
		timer = new Timer();
		ArrayList<bjPlayer> playerMessages = new ArrayList<bjPlayer>();
		
		int i;

		/* Generic PubSub loop.
		pub.write(dealer.getMsg());
		
		System.out.println("State: Writing to be noticed.");
		
		timer.start();
		
		while(timer.getTimeMs() < 4400){	//Read for joining, etc.
			playerMessages = sub.read(dealer.getUuid(), dealer.getActivePlayers(), dealer.getTarget_uuid());
			System.out.println("State: Reading, now");
			if(playerMessages != null && !playerMessages.isEmpty()){
				for(i = 0; i < playerMessages.size(); i++){
					switch(playerMessages.get(i).action.value()){
						case CR.bjp_action._joining: dealer.join(playerMessages.get(i)); break;
						default: System.out.println("Everything is broken."); break;
					}
				}
			}
			if(playerMessages != null){
				playerMessages.clear();
			}
			Timer.wait(buffer);
		}
		 * */

		noReply = false;
		dealer.shuffle();
		while(gameCount < 1){
			if(dealer.getCardsLeftInDeck() < 250){
				dealer.shuffle();
			}
			while(dealer.stillWagering() || (dealer.isFullTable() && !dealer.stillWagering() && jcount < 2) || (dealer.sameAction(bjd_action.shuffling)) ){
				while(dealer.getActivePlayers() == 0) {		//Loop for empty table
					pub.write(dealer.getMsg());
					System.out.println("Lonely: Single and ready to mingle.");
					timer.start();
					while(timer.getTimeMs() < 4400){	//Read loop, with buffer
						playerMessages = sub.read(dealer.getUuid());	//Read only joining messages
						System.out.println("Searching for Players in your area.");
						if(playerMessages != null && !playerMessages.isEmpty()){
							System.out.println("Someone is possibly joining");
							for(i = 0; i < playerMessages.size() && i < MAX_PLAYERS.value; i++){
								if(playerMessages.get(i).action.value() == CR.bjp_action._joining){
									dealer.join(playerMessages.get(i));
									System.out.println("Someone is joing! o_o");
								}
							}
						}
						if(playerMessages != null){
							playerMessages.clear();
						}
						Timer.wait(buffer); 
					}
					dealer.waiting();
				}	//Breaks from loop if any players have joined.
				
				pub.write(dealer.getMsg());
				System.out.println("I need a wager, and I need it now!");
				timer.start();

				dealer.nextSeat(noReply);
				noReply = true;
				
				while(timer.getTimeMs() < 4400){	//Read loop for wagering, exiting, or joining messages.
					playerMessages = sub.read(dealer.getUuid(), dealer.getActivePlayers(), dealer.getTarget_uuid());
					
					if(playerMessages != null && !playerMessages.isEmpty()){
						System.out.println("Wager/Exit/Join messages have been read. :D");
						for(i = 0; i < playerMessages.size(); i++){
							switch(playerMessages.get(i).action.value()){
								case CR.bjp_action._joining: dealer.join(playerMessages.get(i)); break;
								case CR.bjp_action._exiting:
									if(noReply){
										dealer.kickPlayer(playerMessages.get(i).uuid);
										noReply = false;
									}
									System.out.println("God damn it.");
									break;
								case CR.bjp_action._wagering:
									if(noReply){
										dealer.setWagertoPlayer(playerMessages.get(i));
										System.out.println("It works!");
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
					System.out.println("kCount received");
					kcount++;
				}
				if(kcount >= 2 && dealer.stillWagering()){
					System.out.println("Booom, get out the way.");
					dealer.kickPlayer(dealer.getTarget_uuid());
					//noReply = false;
					kcount = 0;
				}
				if(!dealer.stillWagering() && dealer.getActivePlayers() < MAX_PLAYERS.value){
					System.out.printf("Jcount received: %d\n", jcount);
					System.out.println("Still wagering? " +dealer.stillWagering());
					jcount++;
				}
				
				/*
				if(!dealer.startGame()){
					jcount = 0;
					while(jcount < 4){
						BankerMagic
					}
				}*/
			}	//Breaks if all have wagered with full table or join counter has reached 2.

				//Special condition break only if table is full and all have wagered.
			while(dealer.isFullTable() && !dealer.stillWagering())
			{
				pub.write(dealer.getMsg());
				
				System.out.println("Special break for good players.");
				
				timer.start();
				
				while(timer.getTimeMs() < 4400){	//Read for joining, etc.
					playerMessages = sub.read(dealer.getUuid());
					System.out.println("I don't need your messages. >:(");
					if(playerMessages != null && !playerMessages.isEmpty()){
						for(i = 0; i < playerMessages.size(); i++){
							switch(playerMessages.get(i).action.value()){
								case CR.bjp_action._joining: 
									System.out.println("Why are you still joining, dingus.");
									dealer.join(playerMessages.get(i));
									break;
								default: System.out.println("Scary things are happening"); break;
							}
						}
					}
					if(playerMessages != null){
						playerMessages.clear();
					}
					Timer.wait(buffer);
				}
			}
			
			dealer.startGame();
			dealer.dealingInitial();
			pub.write(dealer.getMsg());
			
			System.out.println("First time dealing, be gentle.");
			
			timer.start();
			
			while(timer.getTimeMs() < 4400){	//Read for joining, etc.
				playerMessages = sub.read(dealer.getUuid());
				System.out.println("Reading for posterity's sake. I don't care what you send.");
				if(playerMessages != null && !playerMessages.isEmpty()){
					for(i = 0; i < playerMessages.size(); i++){
						switch(playerMessages.get(i).action.value()){
							case CR.bjp_action._joining: dealer.join(playerMessages.get(i)); break;
							default: System.out.println("I really want some crackers."); break;
						}
					}
				}
				if(playerMessages != null){
					playerMessages.clear();
				}
				Timer.wait(buffer);
			}
			
			noReply = true;
			while(stillDealing){
				dealer.nextSeat(noReply);
				noReply = true;
				
				pub.write(dealer.getMsg());
				System.out.println("Listen here " + dealer.getTarget_uuid() + ", I need an answer.");
				timer.start();
				
				while(timer.getTimeMs() < 4400){	//Reading for dealing with your problems.
					playerMessages = sub.read(dealer.getUuid(), dealer.getActivePlayers(), dealer.getTarget_uuid());
					
					if(playerMessages != null && !playerMessages.isEmpty()){
						System.out.println("Let's hope that player " + dealer.getTarget_uuid() + " had replied!");
						for(i = 0; i < playerMessages.size(); i++){
							switch(playerMessages.get(i).action.value()){
								case CR.bjp_action._joining: dealer.join(playerMessages.get(i)); break;
								case CR.bjp_action._requesting_a_card:
									if(noReply){
										System.out.println("I'll gladly give you a card.");
										dealer.giving_Card(playerMessages.get(i).uuid);
										noReply = false;
									}
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
			}	//Breaks if all have wagered with full table or join counter has reached 2.

			dealer.dealSelf();
			
			pub.write(dealer.getMsg());
			
			System.out.println("My hand might be smoking!");
			
			timer.start();
			
			while(timer.getTimeMs() < 4400){	//Read for joining
				playerMessages = sub.read(dealer.getUuid());
				System.out.println("The fact I have to do this speaks volumes of your abilites to join.");
				if(playerMessages != null && !playerMessages.isEmpty()){
					for(i = 0; i < playerMessages.size(); i++){
						switch(playerMessages.get(i).action.value()){
							case CR.bjp_action._joining: dealer.join(playerMessages.get(i)); break;
							default: System.out.println("Set the noose."); break;
						}
					}
				}
				if(playerMessages != null){
					playerMessages.clear();
				}
				Timer.wait(buffer);
			}
			
			dealer.collecting();
			
			pub.write(dealer.getMsg());
			
			System.out.println("Congratulations, time to soup some people!");
			
			timer.start();
			
			while(timer.getTimeMs() < 4400){	//Read for joining
				playerMessages = sub.read(dealer.getUuid());
				System.out.println("Yep, people are still joining this late in the game.");
				if(playerMessages != null && !playerMessages.isEmpty()){
					for(i = 0; i < playerMessages.size(); i++){
						switch(playerMessages.get(i).action.value()){
							case CR.bjp_action._joining: dealer.join(playerMessages.get(i)); break;
							default: System.out.println("I see the light, and it's orange."); break;
						}
					}
				}
				if(playerMessages != null){
					playerMessages.clear();
				}
				Timer.wait(buffer);
			}
			
			dealer.resetPayouts();
			
			dealer.paying();
			
			pub.write(dealer.getMsg());
			
			System.out.println("Paying people to cover my hide.");
			
			timer.start();
			
			while(timer.getTimeMs() < 4400){	//Read for joining
				playerMessages = sub.read(dealer.getUuid());
				System.out.println("Reading joining messages has now become my only job.");
				if(playerMessages != null && !playerMessages.isEmpty()){
					for(i = 0; i < playerMessages.size(); i++){
						switch(playerMessages.get(i).action.value()){
							case CR.bjp_action._joining: dealer.join(playerMessages.get(i)); break;
							default: System.out.println("Cut my life into pieces."); break;
						}
					}
				}
				if(playerMessages != null){
					playerMessages.clear();
				}
				Timer.wait(buffer);
			}
			
			dealer.endGame();
			
			System.out.println("I'm logic's end.");
			gameCount++;
		}
		System.out.println("I'm an end, I am legion.");
		
		sub.close();
		pub.close();
	}
}



