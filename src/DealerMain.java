
import DDS.*; // opensplice stuff
import CR.*; // idl stuff
import java.util.ArrayList;

public class DealerMain

{
	Dealer dealer;
	DealerSub sub;
	DealerPub pub;
	Timer timer;

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

		while( !exiting ){
			dealer.shuffle();
			pub.write(dealer.getMsg());

			while(dealer.getActivePlayers() == 0){
				// Joining method
				ArrayList<bjPlayer> players = new ArrayList<bjPlayer>;
				players.add(sub.read(dealer.getUuid()));
				for(bjPlayer temp : players){
					dealer.join(temp);
					dealer.setActivePlayers(players.size());
				}
			}
		}
	
	}
	
}



