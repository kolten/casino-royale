import CR.*; // idl stuff
import java.util.ArrayList;

public class DealerMain {
	
	Dealer dealer;
	DealerSub sub;
	DealerPub pub;
	Timer timer;

	int gameCount = 0;
	int kcount = 0;			//Kick counter for unresponsive players
	int jcount = 0;			//Join counter if all wagered and less than 6 players
	int buffer = 200;

	boolean notReadFromPlayer = true;
	boolean stillWagering = true;
	boolean allWagered = true;
	boolean exiting = false;

	public static void main(String[] args) {
		DealerMain main = new DealerMain();
		if(args != null){
			main.run(args[0], args[1], args[2]);
		}
		else {
			main.run("Casino Royale", "bjDealer", "bjPlayer");
		}
	}
	
	public void run(String partition, String pubTopic, String subtopic)
	{
		dealer = new Dealer();
		sub = new DealerSub(partition, subtopic); // Sub needs to have the same topic name as the dealer pub
		pub = new DealerPub(partition, pubTopic); // Vice versa
		timer = new Timer();
		ArrayList<bjPlayer> playerMessages = new ArrayList<bjPlayer>();
		
		int i, j;

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
						case CR.bjp_action._joining: dealer.join(); break;
						default: System.out.println("Everything is broken."); break;
					}
				}
				timer.wati(buffer);
			}
		}
		if(playerMessages != null){
			playerMessages.clear();
		}
		 * */

		notReadFromPlayer = false;
		dealer.shuffle();
		while(gameCount < 5){
			if(dealer.getCardsLeftInDeck() < 250){
				dealer.shuffle();
			}
			while((stillWagering) || ((dealer.getActivePlayers() < 6) && allWagered && jcount < 2) || (dealer.getAction() == bjd_action._shuffling) ){
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
						timer.wait(buffer); 
					}
					if(playerMessages != null){
						playerMessages.clear();
					}
					dealer.waiting();
				}	//Breaks from loop if any players have joined.
				
				pub.write(dealer.getMsg());
				System.out.println("I need a wager, and I need it now!");
				timer.start();

				dealer.nextSeat(notReadFromPlayer);
				notReadFromPlayer = true;
				
				while(timer.getTimeMs() < 4400){	//Read loop for wagering, exiting, or joining messages.
					playerMessages = sub.read(dealer.getUuid(), dealer.getActivePlayers(), dealer.getTarget_uuid());
					
					if(playerMessages != null && !playerMessages.isEmpty()){
						System.out.println("Wager/Exit/Join messages have been read. :D");
						for(i = 0; i < playerMessages.size(); i++){
							switch(playerMessages.get(i).action.value()){
								case CR.bjp_action._joining: dealer.join(playerMessages.get(i)); break;
								case CR.bjp_action._exiting:
									if(notReadFromPlayer){
										dealer.kickPlayer(playerMessages.get(i).uuid);
										notReadFromPlayer = false;
									}
									System.out.println("God damn it.");
									break;
								case CR.bjp_action._wagering:
									if(notReadFromPlayer){
										dealer.getWagerFromPlayer(playerMessages.get(i));
										System.out.println("It works!");
										kcount = 0;
										notReadFromPlayer = false;
									}
									break;
								default: System.out.println("Why do bad things happen to bad code?"); break;
							}
						}
					}
					timer.wait(buffer);
				}
				if(playerMessages != null){
					playerMessages.clear();
				}
				
				if(notReadFromPlayer){
					System.out.println("kCount received");
					kcount++;
				}
				if(kcount >= 2 && dealer.stillWagering()){
					System.out.println("Booom, get out the way.");
					dealer.kickPlayer(dealer.getTarget_uuid());
					notReadFromPlayer = false;
					kcount = 0;
				}
				if(dealer.allWagered() && dealer.getActivePlayers() < MAX_PLAYERS.value){
					System.out.println("Jcount received");
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
			
			dealer.setTargetSeat(0);
			System.out.println("I'm logic's end.");
			gameCount++;
		}
		System.out.println("I'm an end, I am legion.");
		
		sub.close();
		pub.close();
	}
}



