
import DDS.*; // opensplice stuff
import CR.*; // idl stuff
import java.util.ArrayList;

public class DealerMain

{
	Dealer dealer;
	DealerSub sub;
	DealerPub pub;
	Timer timer;

	int gameCount = 0;
	int kcount = 0;			//Kick counter for unresponsive players
	int jcount = 0;			//Join counter if all wagered and less than 6 players

	boolean notReadFromPlayer = true;
	boolean stillWagering = true;
	boolean allWagered = true;

	public static void main(String[] args) {
		DealerMain main = new DealerMain();
		main.run(args[0], args[1], args[2]);
	}
	
	public void run(String partition, String pubTopic, String subtopic)
	{
		dealer = new Dealer();
		sub = new DealerSub(partition, subtopic); // Sub needs to have the same topic name as the dealer pub
		pub = new DealerPub(partition, pubTopic); // Vice versa
		timer = new Timer();

		boolean exiting = false;
		boolean allWagered = false; 

		// while( !exiting ){
		// 	dealer.shuffle();
		// 	pub.write(dealer.getMsg());

		// 	dealer.waiting(); // Dealer action set to waiting

		// 	while(dealer.getActivePlayers() == 0 || !allWagered){
		// 		// Joining method / waiting
		// 		ArrayList<bjPlayer> playerMessages = sub.read(dealer.getUuid());
		// 		for(bjPlayer temp : playerMessages){
		// 			dealer.join(temp);

		// 			// take wager from player
		// 			dealer.getWagerFromPlayer(temp);
		// 			allWagered = true;
		// 		}
		// 		// Wait 5 seconds
		// 		timer.wait(5000);
		// 	}

		while(gameCount < 5){
			dealer.shuffle();
			while((stillWagering) || (dealer.getActivePlayers() < 6) && allWagered && jcount < 2){
				//pub.write(dealer.getMsg());
				//timer.start();

				while(dealer.getActivePlayers() == 0) {
					pub.write(dealer.getMsg());
					System.out.printf("writing to pub");
					timer.start();
					while(timer.getTimeMs() < 4500){
						ArrayList<bjPlayer> playerMessages = sub.read(dealer.getUuid());
						System.out.printf("reading from sub");
						if(playerMessages != null){
							for(bjPlayer temp : playerMessages){
	
								dealer.join(temp);
							}
						}
						timer.wait(200); 
					}
				}
				//Breaks from loop if any players have joined.
				pub.write(dealer.getMsg());
				timer.start();
			}
		}
	}
}



